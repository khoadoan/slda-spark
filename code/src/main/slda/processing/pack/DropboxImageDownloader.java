package slda.processing.pack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.TaskInputOutputContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import slda.hadoop.SimpleCounter;
import cern.colt.Arrays;

import com.dropbox.core.DbxEntry;

import edu.umd.cloud9.io.pair.PairOfIntString;

public class DropboxImageDownloader extends Configured implements Tool {
	private static final Logger LOG = Logger.getLogger(DropboxImageDownloader.class);
	private static final String INPUT = "input";
	private static final String OUTPUT = "output";
	private static final String NUM_REDUCERS = "reducers";
	private static final String DROPBOX_AUTH = "auth";
	
	public static void main(String[] args) throws Exception {
	  ToolRunner.run(new DropboxImageDownloader(), args);
	}
	public int run(String[] args) throws Exception {
	    Options options = new Options();

	    options.addOption(OptionBuilder.withArgName("path").hasArg()
	        .withDescription("input path").create(INPUT));
	    options.addOption(OptionBuilder.withArgName("path").hasArg()
	        .withDescription("output path").create(OUTPUT));
	    options.addOption(OptionBuilder.withArgName("num").hasArg()
	        .withDescription("number of reducers").create(NUM_REDUCERS));
	    options.addOption(OptionBuilder.withArgName("auth").hasArg()
		        .withDescription("dropbox auth file").create(DROPBOX_AUTH));
	    
	    CommandLine cmdline;
	    CommandLineParser parser = new GnuParser();

	    try {
	      cmdline = parser.parse(options, args);
	    } catch (ParseException exp) {
	      System.err.println("Error parsing command line: " + exp.getMessage());
	      return -1;
	    }

	    if (!cmdline.hasOption(INPUT) || !cmdline.hasOption(OUTPUT)) {
	      System.out.println("args: " + Arrays.toString(args));
	      HelpFormatter formatter = new HelpFormatter();
	      formatter.setWidth(120);
	      formatter.printHelp(this.getClass().getName(), options);
	      ToolRunner.printGenericCommandUsage(System.out);
	      return -1;
	    }

	    String inputPath = cmdline.getOptionValue(INPUT);
	    String outputPath = cmdline.getOptionValue(OUTPUT);
	    int reduceTasks = cmdline.hasOption(NUM_REDUCERS) ?
	        Integer.parseInt(cmdline.getOptionValue(NUM_REDUCERS)) : 10;
	    String dropboxAccessToken = cmdline.getOptionValue(DROPBOX_AUTH);

	    LOG.info("Tool: " + DropboxImageDownloader.class.getSimpleName());
	    LOG.info(" - input path: " + inputPath);
	    LOG.info(" - output path: " + outputPath);
	    LOG.info(" - number of reducers: " + reduceTasks);
	    LOG.info(" - dropbox auth: " + dropboxAccessToken);

	    Job job = new Job(getConf());
	    Configuration conf = job.getConfiguration();
	    
	    job.setJobName(DropboxImageDownloader.class.getSimpleName());
	    job.setJarByClass(DropboxImageDownloader.class);

	    job.setNumReduceTasks(reduceTasks);

	    FileInputFormat.setInputPaths(job, new Path(inputPath));
	    FileOutputFormat.setOutputPath(job, new Path(outputPath));
	    
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    	    
	    job.setMapOutputKeyClass(IntWritable.class);
	    job.setMapOutputValueClass(Text.class);
	    job.setOutputKeyClass(PairOfIntString.class);
	    job.setOutputValueClass(BytesWritable.class);

	    job.setMapperClass(MyMapper.class);
	    job.setReducerClass(MyReducer.class);
	    job.setPartitionerClass(MyPartitioner.class);

	    //DistributedCache.addCacheFile(new URI(dropboxAccessToken), conf);
	    conf.setStrings(DROPBOX_AUTH, dropboxAccessToken);
	    
	    FileOutputFormat.setCompressOutput(job, true);
	    FileOutputFormat.setOutputCompressorClass(job, BZip2Codec.class);
	    
	    // Delete the output directory if it exists already.
	    Path outputDir = new Path(outputPath);
	    FileSystem.get(conf).delete(outputDir, true);

	    long startTime = System.currentTimeMillis();
	    job.waitForCompletion(true);
	    LOG.info("Job Finished in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");

	    return 0;
	  }
	
	public static class MyMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
		private final IntWritable KEY = new IntWritable();
		private final Text VALUE = new Text();
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] indexPathPair = value.toString().split(",");
			KEY.set(Integer.parseInt(indexPathPair[0]));
			VALUE.set(indexPathPair[1]);
			context.write(KEY, VALUE);
		}
	}
	
	public static class MyPartitioner extends Partitioner<IntWritable, Text> {
		@Override
		public int getPartition(IntWritable key, Text value, int numReducers) {
			//Round-robin distribution
			return key.get() % numReducers;
		}
	}
	
	public static class MyReducer extends Reducer<IntWritable, Text, PairOfIntString, BytesWritable> {
		private DropboxService dbx;
		
		private final PairOfIntString KEY = new PairOfIntString();
		private final BytesWritable VALUE = new BytesWritable();
		
		private static enum Count { DOWNLOADED };
		private SimpleCounter<TaskInputOutputContext<?,?,?,?>, Enum<?>> counter;
		
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			Configuration conf = context.getConfiguration();
			dbx = new DropboxService(conf.get(DROPBOX_AUTH));
			counter = new SimpleCounter<TaskInputOutputContext<?,?,?,?>, Enum<?>>(context, "DROPBOX", 50, Count.DOWNLOADED);
		}
		@Override
		protected void reduce(IntWritable key, Iterable<Text> values,
				Context context)
				throws IOException, InterruptedException {
			Iterator<Text> iter = values.iterator();
			
			while(iter.hasNext()) {
				String boxFolder = iter.next().toString();
				List<DbxEntry> entries = dbx.list(boxFolder);
				for(DbxEntry entry: entries) {
					if(entry.isFile()) {
						DbxEntry.File file = entry.asFile();
						ByteArrayOutputStream s = new ByteArrayOutputStream((int)file.numBytes);
						dbx.download(s, file.path);
						byte[] content = s.toByteArray();
						VALUE.set(content, 0, content.length);
						KEY.set(key.get(), file.name);
						context.write(KEY, VALUE);
						counter.increment(Count.DOWNLOADED);
					}
				}
			}
		}
		
		@Override
		protected void cleanup(
				org.apache.hadoop.mapreduce.Reducer.Context context)
				throws IOException, InterruptedException {
			counter.push();
		}
	}
}
