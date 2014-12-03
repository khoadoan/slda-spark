package slda.hadoop.inference;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Partitioner;


import edu.umd.cloud9.io.pair.PairOfInts;

public class TermPartitioner extends Partitioner<PairOfInts, DoubleWritable> {
  
  @Override
  public int getPartition(PairOfInts key, DoubleWritable value, int numReduceTasks) {
    return (key.getLeftElement() & Integer.MAX_VALUE) % numReduceTasks;
  }

//  public void configure(JobConf conf) {
//  }
}