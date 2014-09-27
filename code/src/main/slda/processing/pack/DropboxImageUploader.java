package slda.processing.pack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
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
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.TaskInputOutputContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import slda.hadoop.SimpleCounter;
import cern.colt.Arrays;

import com.dropbox.core.DbxEntry;

import edu.umd.cloud9.io.pair.PairOfIntString;

public class DropboxImageUploader extends Configured implements Tool {
	private static final Logger LOG = Logger.getLogger(DropboxImageUploader.class);
	private static final String INPUT = "input";
	private static final String OUTPUT = "output";
	private static final String NUM_REDUCERS = "reducers";
	private static final String DROPBOX_AUTH = "auth";
	
	public static void main(String[] args) throws Exception {
	  ToolRunner.run(new DropboxImageUploader(), args);
	}
	public int run(String[] args) throws Exception {
	    Options options = new Options();

	    options.addOption(OptionBuilder.withArgName("path").hasArg()
	        .withDescription("input path").create(INPUT));
	    options.addOption(OptionBuilder.withArgName("path").hasArg()
	        .withDescription("output path").create(OUTPUT));
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
	    int reduceTasks = 0; //Map-only job
	    String dropboxAuthPath = cmdline.getOptionValue(DROPBOX_AUTH);

	    LOG.info("Tool: " + DropboxImageUploader.class.getSimpleName());
	    LOG.info(" - input path: " + inputPath);
	    LOG.info(" - output path: " + outputPath);
	    LOG.info(" - dropbox auth: " + dropboxAuthPath);

	    Job job = new Job(getConf());
	    Configuration conf = job.getConfiguration();
	    
	    job.setJobName(DropboxImageUploader.class.getSimpleName());
	    job.setJarByClass(DropboxImageUploader.class);

	    job.setNumReduceTasks(reduceTasks);

	    FileInputFormat.setInputPaths(job, new Path(inputPath));
	    FileOutputFormat.setOutputPath(job, new Path(outputPath));
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setMapOutputKeyClass(NullWritable.class);
	    job.setMapOutputValueClass(NullWritable.class);

	    job.setMapperClass(MyMapper.class);

	    DistributedCache.addCacheFile(new URI(dropboxAuthPath), conf);
	    
	    conf.set(OUTPUT, outputPath);
	    
	    // Delete the output directory if it exists already.
	    Path outputDir = new Path(outputPath);
	    FileSystem.get(conf).delete(outputDir, true);

	    long startTime = System.currentTimeMillis();
	    job.waitForCompletion(true);
	    LOG.info("Job Finished in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");

	    return 0;
	  }
	
	public static class MyMapper extends Mapper<PairOfIntString, BytesWritable, NullWritable, NullWritable> {
		private DropboxService dbx;
		
		private final IntWritable KEY = new IntWritable();
		private final Text VALUE = new Text();
		private static enum Count { UPLOADED };
		private SimpleCounter<TaskInputOutputContext<?,?,?,?>, Enum<?>> counter;
		private String boxPath;
		@Override
		protected void setup(Context context)
				throws IOException, InterruptedException {
			Configuration conf = context.getConfiguration();
			for(Path path: DistributedCache.getLocalCacheFiles(conf)) {
				if(path.getName().contains("dropbox")) {
					//Get the authentication file and construct the Dropbox Service
					dbx = new DropboxService(new File(path.toString()));
				}
			}
			boxPath = conf.get(OUTPUT);
			counter = new SimpleCounter<TaskInputOutputContext<?,?,?,?>, Enum<?>>(context, "MAPPER", Count.UPLOADED);
		}
		
		@Override
		protected void map(PairOfIntString key, BytesWritable value, Context context)
				throws IOException, InterruptedException {
			dbx.upload(String.format("%s/%d/%s", boxPath, key.getLeftElement(), key.getRightElement()), value.get());
			counter.increment(Count.UPLOADED);
		}
		
		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			counter.push();
		}
	}
}
