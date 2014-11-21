package lda.hadoop.inference;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import lda.hadoop.inference.VariationalInference.ParameterCounter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import com.google.common.base.Preconditions;

import edu.umd.cloud9.io.map.HMapIFW;
import edu.umd.cloud9.io.pair.PairOfIntFloat;
import edu.umd.cloud9.io.pair.PairOfInts;
import edu.umd.cloud9.math.LogMath;
import edu.umd.cloud9.util.map.HMapIV;

public class TermReducer extends 
    Reducer<PairOfInts, DoubleWritable, IntWritable, DoubleWritable> {
  boolean truncateBeta = false;
  // double truncationThreshold = Math.log(0.001);
  int truncationSize = 10000;
  TreeMap<Double, Integer> treeMap = new TreeMap<Double, Integer>();
  Iterator<Entry<Double, Integer>> itr = null;

  private static HMapIV<Set<Integer>> lambdaMap = null;

  private static boolean learning = Settings.LEARNING_MODE;

  private int topicIndex = 0;
  private double normalizeFactor = 0;

  private MultipleOutputs multipleOutputs;
  //TODO: private OutputCollector<PairOfIntFloat, HMapIFW> outputBeta;

  private IntWritable intWritable = new IntWritable();
  private DoubleWritable doubleWritable = new DoubleWritable();

  private PairOfIntFloat outputKey = new PairOfIntFloat();
  private HMapIFW outputValue = new HMapIFW();

  @Override
  public void setup(Context context) {
	Configuration conf = context.getConfiguration();
    multipleOutputs = new MultipleOutputs(context);

    learning = conf.getBoolean(Settings.PROPERTY_PREFIX + "model.train", Settings.LEARNING_MODE);

    truncateBeta = conf.getBoolean(Settings.PROPERTY_PREFIX + "model.truncate.beta", false);

    boolean informedPrior = conf.getBoolean(Settings.PROPERTY_PREFIX + "model.informed.prior",
        false);

    Path[] inputFiles;
    SequenceFile.Reader sequenceFileReader = null;
    try {
      inputFiles = DistributedCache.getLocalCacheFiles(conf);

      if (inputFiles != null) {
        for (Path path : inputFiles) {
          try {
        	  sequenceFileReader = new SequenceFile.Reader(FileSystem.getLocal(conf), path, conf);
            if (path.getName().startsWith(Settings.ALPHA)) {
            	continue;
            } else if (path.getName().startsWith(Settings.BETA)) {
              continue;
            } else if (path.getName().startsWith(InformedPrior.ETA)) {
              Preconditions.checkArgument(lambdaMap == null,
                  "Lambda matrix was initialized already...");
              lambdaMap = InformedPrior.importEta(sequenceFileReader);
            } else {
              throw new IllegalArgumentException("Unexpected file in distributed cache: "
                  + path.getName());
            }
          } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
          } catch (IOException ioe) {
            ioe.printStackTrace();
          } finally {
            IOUtils.closeStream(sequenceFileReader);
          }
        }
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    Preconditions.checkArgument(informedPrior == (lambdaMap != null),
        "Fail to initialize informed prior...");
    
    // System.out.println("======================================================================");
    // System.out.println("Available processors (cores): "
    // + Runtime.getRuntime().availableProcessors());
    // long maxMemory = Runtime.getRuntime().maxMemory();
    // System.out.println("Maximum memory (bytes): "
    // + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
    // System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
    // System.out.println("Total memory (bytes): " + Runtime.getRuntime().totalMemory());
    // System.out.println("======================================================================");
  }

  @Override
  public void reduce(PairOfInts key, Iterable<DoubleWritable> values,
      Context context) throws IOException, InterruptedException {
	Iterator<DoubleWritable> value = values.iterator();
    if (key.getLeftElement() == 0) {
      double sum = value.next().get();
      while (value.hasNext()) {
        sum += value.next().get();
      }

      Preconditions.checkArgument(key.getRightElement() > 0,
          "Unexpected sequence order for alpha sufficient statistics: " + key.toString());

      intWritable.set(key.getRightElement());
      doubleWritable.set(sum);
      context.write(intWritable, doubleWritable);

      return;
    }

    // I would be very surprised to get here...
    Preconditions.checkArgument(learning, "Invalid key from Mapper");
    context.getCounter(ParameterCounter.TOTAL_TERM).increment(1);

    double phiValue = value.next().get();
    while (value.hasNext()) {
      phiValue = LogMath.add(phiValue, value.next().get());
    }

    if (lambdaMap != null) {
      phiValue = LogMath.add(
          InformedPrior.getEta(key.getRightElement(), lambdaMap.get(topicIndex)), phiValue);
    }

    if (topicIndex != key.getLeftElement()) {
      if (topicIndex == 0) {
    	//TODO: khoa - check if this means: first call in the mapper or something else?  
    	//outputBeta = multipleOutputs.getCollector(Settings.BETA, Settings.BETA, reporter);    	  
      } else {
        outputKey.set(topicIndex, (float) normalizeFactor);

        if (truncateBeta) {
          itr = treeMap.entrySet().iterator();
          Entry<Double, Integer> temp = null;
          outputValue.clear();
          while (itr.hasNext()) {
            temp = itr.next();
            outputValue.put(temp.getValue(), temp.getKey().floatValue());
          }
        }

        //TODO: outputBeta.collect(outputKey, outputValue);
        multipleOutputs.write(Settings.BETA, outputKey, outputValue);
      }

      topicIndex = key.getLeftElement();
      normalizeFactor = phiValue;
      if (truncateBeta) {
        treeMap.clear();
        treeMap.put(phiValue, key.getRightElement());
      } else {
        outputValue.clear();
        outputValue.put(key.getRightElement(), (float) phiValue);
      }
    } else {
      if (truncateBeta) {
        if (treeMap.size() >= truncationSize) {
          if (treeMap.firstKey() < phiValue) {
            normalizeFactor = Math.log(Math.exp(normalizeFactor) - Math.exp(treeMap.firstKey()));
            treeMap.remove(treeMap.firstKey());

            treeMap.put(phiValue, key.getRightElement());
            normalizeFactor = LogMath.add(normalizeFactor, phiValue);
          }
        } else {
          treeMap.put(phiValue, key.getRightElement());
          normalizeFactor = LogMath.add(normalizeFactor, phiValue);
        }
      } else {
        normalizeFactor = LogMath.add(normalizeFactor, phiValue);
        outputValue.put(key.getRightElement(), (float) phiValue);
      }
    }
  }

  @Override
  public void cleanup(Context context) throws IOException, InterruptedException {
    if (truncateBeta) {
      if (!treeMap.isEmpty()) {
        outputKey.set(topicIndex, (float) normalizeFactor);
        itr = treeMap.entrySet().iterator();
        Entry<Double, Integer> temp = null;
        outputValue.clear();
        while (itr.hasNext()) {
          temp = itr.next();
          outputValue.put(temp.getValue(), temp.getKey().floatValue());
        }
        //TODO: outputBeta.collect(outputKey, outputValue);
        multipleOutputs.write(Settings.BETA, outputKey, outputValue);
      }
    } else {
      if (!outputValue.isEmpty()) {
        outputKey.set(topicIndex, (float) normalizeFactor);
        //TODO: outputBeta.collect(outputKey, outputValue);
        multipleOutputs.write(Settings.BETA, outputKey, outputValue);
      }
    }
    multipleOutputs.close();
  }
}