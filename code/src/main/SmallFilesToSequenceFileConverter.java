import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import cern.colt.Arrays;

public class SmallFilesToSequenceFileConverter { //extends Configured implements Tool {
//  private static final Logger LOG = Logger.getLogger(SmallFilesToSequenceFileConverter.class);
//
//  static class SequenceFileMapper extends 
//    Mapper<NullWritable, BytesWritable, Text, BytesWritable> {
//    
//    private Text filenameKey;
//
//    @Override
//    protected void setup(Context context) throws IOException, InterruptedException {
//  
//      InputSplit split = context.getInputSplit();
//      Path path = ((FileSplit) split).getPath();
//      filenameKey = new Text(path.toString());
//    }
//
//    @Override
//    protected void map(NullWritable key, BytesWritable value, Context context)
//      throws IOException, InterruptedException {
//      context.write(filenameKey, value);
//    }
//
//  }
//
//  /**
//   * Creates an instance of this tool.
//   */
//  public SmallFilesToSequenceFileConverter() {}
//
//  private static final String INPUT = "input";
//  private static final String OUTPUT = "output";
//  private static final String NUM_REDUCERS = "numReducers";
//
//  /**
//   * Runs this tool.
//   */
//  @SuppressWarnings({ "static-access" })
//  public int run(String[] args) throws Exception {
//    Options options = new Options();
//
//    options.addOption(OptionBuilder.withArgName("path").hasArg()
//        .withDescription("input path").create(INPUT));
//    options.addOption(OptionBuilder.withArgName("path").hasArg()
//        .withDescription("output path").create(OUTPUT));
//    options.addOption(OptionBuilder.withArgName("num").hasArg()
//        .withDescription("number of reducers").create(NUM_REDUCERS));
//
//    CommandLine cmdline;
//    CommandLineParser parser = new GnuParser();
//
//    try {
//      cmdline = parser.parse(options, args);
//    } catch (ParseException exp) {
//      System.err.println("Error parsing command line: " + exp.getMessage());
//      return -1;
//    }
//
//    if (!cmdline.hasOption(INPUT) || !cmdline.hasOption(OUTPUT)) {
//      System.out.println("args: " + Arrays.toString(args));
//      HelpFormatter formatter = new HelpFormatter();
//      formatter.setWidth(120);
//      formatter.printHelp(this.getClass().getName(), options);
//      ToolRunner.printGenericCommandUsage(System.out);
//      return -1;
//    }
//
//    String inputPath = cmdline.getOptionValue(INPUT);
//    String outputPath = cmdline.getOptionValue(OUTPUT);
//    int reduceTasks = cmdline.hasOption(NUM_REDUCERS) ?
//        Integer.parseInt(cmdline.getOptionValue(NUM_REDUCERS)) : 1;
//
//    LOG.info("Tool: " + SmallFilesToSequenceFileConverter.class.getSimpleName());
//    LOG.info(" - input path: " + inputPath);
//    LOG.info(" - output path: " + outputPath);
//    LOG.info(" - number of reducers: " + reduceTasks);
//
//    Configuration conf = getConf();
//    Job job = Job.getInstance(conf);
//    job.setJobName(SmallFilesToSequenceFileConverter.class.getSimpleName());
//    job.setJarByClass(SmallFilesToSequenceFileConverter.class);
//
//    //System.out.println("Get here_1");
//    job.setNumReduceTasks(reduceTasks);
//
//    FileInputFormat.setInputPaths(job, new Path(inputPath));
//    FileOutputFormat.setOutputPath(job, new Path(outputPath));
//    
//    job.setInputFormatClass(WholeFileInputFormat.class);
//    job.setOutputFormatClass(SequenceFileOutputFormat.class);
//    
//    SequenceFileOutputFormat.setCompressOutput(job, true);
//    SequenceFileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
//    
//    //System.out.println("Get here_2");
//    
//    job.setOutputKeyClass(Text.class);
//    job.setOutputValueClass(BytesWritable.class);
//
//    job.setMapperClass(SequenceFileMapper.class);
//    
//    //System.out.println("Get here_3");
//    
//    // Delete the output directory if it exists already.
//    Path outputDir = new Path(outputPath);
//    FileSystem.get(conf).delete(outputDir, true);
//    
//    System.out.println("Get here_4");
//
//    long startTime = System.currentTimeMillis();
//    job.waitForCompletion(true);
//    LOG.info("Job Finished in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
//
//    return 0;
//  }
//
//  /**
//   * Dispatches command-line arguments to the tool via the {@code ToolRunner}.
//   */
//  public static void main(String[] args) throws Exception {
//    ToolRunner.run(new SmallFilesToSequenceFileConverter(), args);
//  }
}