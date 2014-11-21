/*
 * Cloud9: A MapReduce Library for Hadoop
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package lda.hadoop.inference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;

import cern.colt.Arrays;
import edu.umd.cloud9.io.array.ArrayListOfDoublesWritable;
import edu.umd.cloud9.io.map.HMapIVW;
import edu.umd.cloud9.io.map.HMapSIW;
import edu.umd.cloud9.io.map.HashMapWritable;
import edu.umd.cloud9.io.pair.PairOfInts;

/**
 * Annotate documents with output from LDA
 *
 * @author Khoa Doan
 */
public class AnnotateDocuments extends Configured implements Tool {
  private static final Logger LOG = Logger.getLogger(AnnotateDocuments.class);

	public static final String TERM = "term";
	public static final String TITLE = "title";

  // Mapper: emits (token, 1) for every word occurrence.
  private static class MyMapper extends Mapper<IntWritable, HMapIVW<ArrayListOfDoublesWritable>, Text, HMapSIW> {

    // Reuse objects to save overhead of object creation.
	private static Map<Integer, String> termIndex = null;
	private static Map<Integer, String> titleIndex = null;
	
	private static Text title = new Text();
	private static HMapSIW labels = new HMapSIW();
    private static double labelCutoff;
    
    @Override
    public void setup(Context context) throws InterruptedException{
    	SequenceFile.Reader sequenceFileReader = null;
		try {
			Configuration conf = context.getConfiguration();
			Path[] inputFiles = DistributedCache.getLocalCacheFiles(conf);
			
			labelCutoff = Math.log(conf.getFloat(PCUTOFF, 0.9f));

			// Read term and title index
			if (inputFiles != null) {
				for (Path path : inputFiles) {
					try {
						sequenceFileReader = new SequenceFile.Reader(
								FileSystem.getLocal(conf), path, conf);

						if (path.getName().startsWith(TERM)) {
							Preconditions
									.checkArgument(termIndex == null,
											"Term index was initialized already...");
							termIndex = AnnotateDocuments
									.importParameter(sequenceFileReader);
						} else if (path.getName().startsWith(TITLE)) {
							Preconditions
									.checkArgument(titleIndex == null,
											"Title index was initialized already...");
							titleIndex = AnnotateDocuments
									.importParameter(sequenceFileReader);
						} else {
							throw new IllegalArgumentException(
									"Unexpected file in distributed cache: "
											+ path.getName());
						}
					} catch (IllegalArgumentException iae) {
						iae.printStackTrace();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			IOUtils.closeStream(sequenceFileReader);
		}
    }
    
    @Override
    public void map(IntWritable key, HMapIVW<ArrayListOfDoublesWritable> value, Context context)
        throws IOException, InterruptedException {
    	title.set(titleIndex.get(key.get()));
    	labels.clear();
    	
    	for(Integer termId: value.keySet()){
    		//Determine the label
    		ArrayListOfDoublesWritable phi = value.get(termId);
    		for(int i=0; i<phi.size(); i++){
    			//Label the term only when its assignment prob to a topic is greater than the cutoff
    			if(phi.get(i) >= labelCutoff){
    				labels.put(termIndex.get(termId), i+1);
    				break;
    			} else {
    				//No label
    				labels.put(termIndex.get(termId), 0);
    			}
    		}
    	}
    	context.write(title, labels);
    }
  }

  /**
   * Creates an instance of this tool.
   */
  public AnnotateDocuments() {}

  private static final String INPUT = "input";
  private static final String OUTPUT = "output";
  private static final String NUM_REDUCERS = "numReducers";
  private static final String PCUTOFF = "probCutoff";
  private static final String INDEX = "index";
  /**
   * Runs this tool.
   */
  @SuppressWarnings({ "static-access" })
  public int run(String[] args) throws Exception {
    Options options = new Options();

    options.addOption(OptionBuilder.withArgName("path").hasArg()
        .withDescription("input path").create(INPUT));
    options.addOption(OptionBuilder.withArgName("path").hasArg()
        .withDescription("output path").create(OUTPUT));
    options.addOption(OptionBuilder.withArgName("num").hasArg()
        .withDescription("number of reducers").create(NUM_REDUCERS));
    options.addOption(OptionBuilder.withArgName(PCUTOFF).hasArg()
            .withDescription("probability of topic assignment").create(PCUTOFF));
    options.addOption(OptionBuilder.withArgName(INDEX).hasArg()
            .withDescription("path to data directory containing term and title indices").create(INDEX));
    
    CommandLine cmdline;
    CommandLineParser parser = new GnuParser();

    try {
      cmdline = parser.parse(options, args);
    } catch (ParseException exp) {
      System.err.println("Error parsing command line: " + exp.getMessage());
      return -1;
    }

    if (!cmdline.hasOption(INPUT) || !cmdline.hasOption(OUTPUT) || !cmdline.hasOption(INDEX)) {
      System.out.println("args: " + Arrays.toString(args));
      HelpFormatter formatter = new HelpFormatter();
      formatter.setWidth(120);
      formatter.printHelp(this.getClass().getName(), options);
      ToolRunner.printGenericCommandUsage(System.out);
      return -1;
    }

    String indexPath = cmdline.getOptionValue(INDEX);
    String inputPath = cmdline.getOptionValue(INPUT);
    String outputPath = cmdline.getOptionValue(OUTPUT);
    int reduceTasks = cmdline.hasOption(NUM_REDUCERS) ?
        Integer.parseInt(cmdline.getOptionValue(NUM_REDUCERS)) : 1;
    
    float cutoff = 0.9f;
    if(cmdline.hasOption(PCUTOFF)){
    	cutoff = Float.parseFloat(cmdline.getOptionValue(PCUTOFF));
    }
    LOG.info("Tool: " + AnnotateDocuments.class.getSimpleName());
    LOG.info(" - indices path: " + indexPath);
    LOG.info(" - input path: " + inputPath);
    LOG.info(" - output path: " + outputPath);
    LOG.info(" - number of reducers: " + reduceTasks);
    LOG.info(" - log(probCutoff): " + Math.log(cutoff));

    Configuration conf = getConf();
    FileSystem fs = FileSystem.get(conf);
    
    Job job = Job.getInstance(conf);
    job.setJobName(AnnotateDocuments.class.getSimpleName());
    job.setJarByClass(AnnotateDocuments.class);

    String termIndex = indexPath + Path.SEPARATOR + TERM;
    String titleIndex = indexPath + Path.SEPARATOR + TITLE;
    
    Path termIndexPath = new Path(termIndex);
	Path titleIndexPath = new Path(titleIndex);
    
	Preconditions.checkArgument(fs.exists(termIndexPath),
			"Missing term index files... " + termIndexPath);
	DistributedCache.addCacheFile(termIndexPath.toUri(),
			job.getConfiguration());
	Preconditions.checkArgument(fs.exists(titleIndexPath),
			"Missing title index files... " + titleIndexPath);
	DistributedCache.addCacheFile(titleIndexPath.toUri(),
			job.getConfiguration());
	
    job.setNumReduceTasks(reduceTasks);
	conf.setFloat(PCUTOFF, cutoff);
    
    job.setInputFormatClass(SequenceFileInputFormat.class);
    job.setOutputFormatClass(SequenceFileOutputFormat.class);
    FileInputFormat.setInputPaths(job, new Path(inputPath));
    FileOutputFormat.setOutputPath(job, new Path(outputPath));
    
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(HMapSIW.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(HMapSIW.class);
    
    job.setMapperClass(MyMapper.class);

    // Delete the output directory if it exists already.
    Path outputDir = new Path(outputPath);
    FileSystem.get(conf).delete(outputDir, true);

    long startTime = System.currentTimeMillis();
    job.waitForCompletion(true);
    LOG.info("Job Finished in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");

    return 0;
  }
  
  public static Map<Integer, String> importParameter(
			SequenceFile.Reader sequenceFileReader) throws IOException {
		Map<Integer, String> hashMap = new HashMap<Integer, String>();

		IntWritable intWritable = new IntWritable();
		Text text = new Text();
		while (sequenceFileReader.next(intWritable, text)) {
			hashMap.put(intWritable.get(), text.toString());
		}

		return hashMap;
	}

  /**
   * Dispatches command-line arguments to the tool via the {@code ToolRunner}.
   */
  public static void main(String[] args) throws Exception {
    ToolRunner.run(new AnnotateDocuments(), args);
  }
}