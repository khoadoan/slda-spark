package slda.hadoop.inference;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Reducer;


import edu.umd.cloud9.io.pair.PairOfInts;
import edu.umd.cloud9.math.LogMath;

public class TermCombiner extends 
    Reducer<PairOfInts, DoubleWritable, PairOfInts, DoubleWritable> {
  private DoubleWritable outputValue = new DoubleWritable();

  @Override
  public void reduce(PairOfInts key, Iterable<DoubleWritable> values,
      Context context) throws IOException, InterruptedException {
    Iterator<DoubleWritable> value = values.iterator();
	double sum = value.next().get();
    if (key.getLeftElement() <= 0) {
      // this is not a phi value
      while (value.hasNext()) {
        sum += value.next().get();
      }
    } else {
      // this is a phi value
      while (value.hasNext()) {
        sum = LogMath.add(sum, value.next().get());
      }
    }
    outputValue.set(sum);
    context.write(key, outputValue);
  }
}