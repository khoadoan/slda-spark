package slda.hadoop;

import java.io.FileInputStream;
import java.net.URI;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

/**
 * @author Khoa
 * A simple Hadoop Job Wrapper that coordinates various setup for a typical Hadoop job.
 * To use the simple Job, create a configuration file that declares the job setup, with information
 * such as the mapper's class, reducer's class, input, output...
 * To run a Hadoop job with the created configuration file, use the following command:
 * 		`etc/hadoop-cluster.sh com.verve.hadoop.job.SimpleJob -j "path to configuration file"` 
 */
public class SimpleJob extends Configured implements Tool{
	private static final Logger LOG = Logger.getLogger(SimpleJob.class);
	protected static final  String OPTIONS_STRING = "j:";
	protected static final String JOB_OPTION = "j";
	
	protected SimpleOptions opts = null;
	public SimpleJob() {
		opts = new SimpleOptions(OPTIONS_STRING);
	}
	
	@Override
	public int run(String[] args) throws Exception {
		opts.parse(args);

		Job job = Job.getInstance(getConf());
		Configuration conf = job.getConfiguration();

		LOG.info("Reading Job options at " + opts.getOption(JOB_OPTION));
		
		Properties props = new Properties();
		String configFilename = opts.getOption(JOB_OPTION);
		if(configFilename.startsWith("s3://") || configFilename.startsWith("s3n://") || configFilename.startsWith("hdfs://")) {
			props.load(HadoopUtils.readFromHDFS(conf, configFilename));
		} else {
			props.load(new FileInputStream(configFilename));
		}
		
	    LOG.info("\t-input: " + props.getProperty("input"));
	    LOG.info("\t-output: " + props.getProperty("output"));
	    
		//Enable mapper if there's any 
		if(props.containsKey("mapper")) {
			job.setMapperClass((Class<Mapper>) Class.forName(props.getProperty("mapper")));
			LOG.info("\t-mapper: " + props.getProperty("mapper"));
		}
		
		//Enable reducer if there's any
		if(props.containsKey("reducer"))
		{	
			job.setReducerClass((Class<Reducer>) Class.forName(props.getProperty("reducer")));
			LOG.info("\t-reducer: " + props.getProperty("reducer"));
		}
		
		if(props.containsKey("combiner")) {
			job.setCombinerClass((Class<? extends Reducer>) Class.forName(props.getProperty("combiner")));
			LOG.info("\t-combiner: " + props.getProperty("combiner"));
		}
		
		if(props.containsKey("partitioner")) {
			job.setPartitionerClass((Class<? extends Partitioner>) Class.forName(props.getProperty("partitioner")));
			LOG.info("\t-partitioner: " + props.getProperty("partitioner"));
		}
		
		if(props.containsKey("name")) {
			job.setJobName(props.getProperty("name"));
		} else {
			job.setJobName(this.getClass().getCanonicalName());
		}
		
		job.setJarByClass(job.getMapperClass());

		if(props.containsKey("parallel")) {
			job.setNumReduceTasks(Integer.parseInt(props.getProperty("parallel")));			
		} else {
			job.setNumReduceTasks(1);
		}

		//TODO: refactor this code 
		if(props.containsKey("inputFormatClass")) {
			LOG.info("Set inputFormatClass=" + props.getProperty("inputFormatClass"));
			job.setInputFormatClass((Class<? extends InputFormat>) Class.forName(props.getProperty("inputFormatClass")));
		}
		if(props.containsKey("outputFormatClass")) {
			LOG.info("Set outputFormatClass=" + props.getProperty("outputFormatClass"));
			job.setOutputFormatClass((Class<? extends OutputFormat>) Class.forName(props.getProperty("outputFormatClass")));
		}
		if(props.containsKey("mapOutputKeyClass")) {
			LOG.info("Set mapOutputKeyClass=" + props.getProperty("mapOutputKeyClass"));
			job.setMapOutputKeyClass(Class.forName(props.getProperty("mapOutputKeyClass")));
		}
		if(props.containsKey("mapOutputValueClass")) {
			LOG.info("Set mapOutputValueClass=" + props.getProperty("mapOutputValueClass"));
			job.setMapOutputValueClass(Class.forName(props.getProperty("mapOutputValueClass")));
		}
		if(props.containsKey("outputKeyClass")) {
			LOG.info("Set outputKeyClass=" + props.getProperty("outputKeyClass"));
			job.setOutputKeyClass(Class.forName(props.getProperty("outputKeyClass")));
		}
		if(props.containsKey("outputValueClass")) {
			LOG.info("Set outputValueClass=" + props.getProperty("outputValueClass"));
			job.setOutputValueClass(Class.forName(props.getProperty("outputValueClass")));
		}
		
		//FileInputFormat.setInputPaths(job, new Path(props.getProperty("input")));
		FileInputFormat.addInputPaths(job, props.getProperty("input"));
	    FileOutputFormat.setOutputPath(job, new Path(props.getProperty("output")));

	    FileOutputFormat.setCompressOutput(job, true);
	    FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
	    
	    //Processing extra properties
	    for(Object keyObj: props.keySet()) {
	    	String key = keyObj.toString();
	    	//LOG.info("CONF-" + key + "-" + props.con);
	    	if(key.startsWith("cache.")) {
	    		String fileLocation = props.getProperty(key);
	    		String hdfsFileLocation = null;
	    		//TODO: correctly determine the set of input locations.
	    		//Currently this is varying for hdfs format from different environment
	    		//And I have to tag all input data with hdfs if not s3.
	    		hdfsFileLocation = "/tmp/" + key;
	    		if(fileLocation.startsWith("s3:")) {
	    			S3Client s3 = S3Client.getInstance();
	    			s3.copyToHDFS(fileLocation, hdfsFileLocation);
	    		} else if(fileLocation.startsWith("hdfs")){
	    			hdfsFileLocation = fileLocation;

	    			HadoopUtils.copyToHDFS(conf, fileLocation, hdfsFileLocation, true);
	    		} else if(fileLocation.startsWith("")){
	    			//TODO: currently do nothing, i.e. caching local file or hdfs to hdfs.
	    			HadoopUtils.copyToHDFS(fileLocation, hdfsFileLocation, true);
	    		} else {
	    			throw new RuntimeException("Do not recognize the file location");
	    		}
	    		
	    		LOG.info("Caching " + fileLocation + " to " + hdfsFileLocation + "...");
	    		//Copy from HDFS to the distributed cache
	    		job.addCacheFile(new Path(hdfsFileLocation).toUri());
	    		
	    	} else if(key.startsWith("conf.")) {
	    		String confName = key; //key.substring("conf.".length());
	    		String confVal = props.getProperty(key);
	    		if(confVal.equals("")) {
	    			LOG.info("Set configuration: " + confName + "=" + true);
		    		conf.setBoolean(confName, true);
	    		} else {
	    			LOG.info("Set configuration: " + confName + "=" + confVal);
		    		conf.setStrings(confName, confVal);
	    		}
	    	} else if(key.startsWith("moutput")) {
	    		String[] moutputInfo = props.get(key).toString().split(",");
	    		String moutputName = key.substring("moutput".length());
	    		LOG.info("Adding multiple output: " + moutputName);
	    		MultipleOutputs.addNamedOutput(job,  
	    				moutputName, 
	    				(Class<? extends OutputFormat>) Class.forName(moutputInfo[0]), 
	    				Class.forName(moutputInfo[1]), 
	    				Class.forName(moutputInfo[2]));
	    	}
	    }

		// Delete the output directory if it exists already.
	    Path outputDir = new Path(props.getProperty("output"));
	    FileSystem.get(outputDir.toUri(), conf).delete(outputDir, true);

	    long startTime = System.currentTimeMillis();
	    job.waitForCompletion(true);
	    LOG.info("Job Finished in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");

		return 0;
	}
	
	public static void main(String[] args) throws Exception {
	    ToolRunner.run(new SimpleJob(), args);
	}
	
	public static interface Step {
		public void summary(Job job);
	}
}
