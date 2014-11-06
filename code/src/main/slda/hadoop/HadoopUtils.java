package slda.hadoop;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * This is a utility for various convenient method to communicate with HDFS.
 * @author Khoa
 *
 */
public class HadoopUtils {
	private static final Logger LOG = Logger.getLogger(HadoopUtils.class);
	
	public static void copyToHDFS(String localFilename, String hdfsFilename, boolean overwritten) {
		copyToHDFS(new Configuration(), localFilename, hdfsFilename, overwritten);
	}
	
	/**
	 * Copy a local file to HDFS, given a configuration.
	 * @param conf A hadoop configuration object
	 * @param localFilename full path to the local file
	 * @param hdfsFilename HDFS full path (or relative to the User's home folder)
	 * @param overwritten should the remote file be overwritten if it exists
	 */
	public static void copyToHDFS(Configuration conf, String localFilename, String hdfsFilename, boolean overwritten) {
		try 
		{
			FileSystem hdfs = FileSystem.get(conf);
			Path hdfsPath = new Path(hdfsFilename);
			if(hdfs.exists(hdfsPath)) {
				if(overwritten) { 
					hdfs.delete(hdfsPath, true);
				} else {
					//Exit if the file already exists and we don't want to overwrite it
					return;
				}
			}
			OutputStream o = hdfs.create(hdfsPath);
			FileInputStream fis = new FileInputStream(localFilename);
			LOG.info("Starting to copy " + localFilename + " to " + hdfsFilename);
			IOUtils.copyBytes(fis, o, 1024);
			LOG.info("Finish copying");
			fis.close();
			o.close();
		} catch (Exception ex) {
			throw new RuntimeException("Error while copying" + localFilename + " to HDFS at " + hdfsFilename, ex);
		}
	}
	
	/**
	 * Create an input stream from an HDFS path
	 * @param conf a hadoop configuration object
	 * @param hdfsFilename full HDFS path
	 * @return input stream
	 */
	public static InputStream readFromHDFS(Configuration conf, String hdfsFilename) {
		try{
			Path hdfsPath = new Path(hdfsFilename);
			FileSystem hdfs = FileSystem.get(new URI(hdfsFilename), conf);
			
			if(hdfs.exists(hdfsPath)) {
				return hdfs.open(hdfsPath);
			} else {
				throw new RuntimeException("File does not exist: " + hdfsFilename);
			}
		} catch(Exception ex) {
			throw new RuntimeException("Error while opening or reading " + hdfsFilename, ex);
		}
	}
}
