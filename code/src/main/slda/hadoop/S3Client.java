package slda.hadoop;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.mortbay.log.Log;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


/**
 * @author Khoa
 * A utility to connect to the S3 file storage. 
 * <b>Prerequisites:</b> Create your credentials file at ~/.aws/credentials (C:\Users\USER_NAME\.aws\credentials for Windows users) 
 * and save the following lines after replacing the underlined values with your own.
 * [default]
 *
 * aws_access_key_id = YOUR_ACCESS_KEY_ID
 *
 * aws_secret_access_key = YOUR_SECRET_ACCESS_KEY
 */
public enum S3Client
{
    INSTANCE;
    private AmazonS3 s3;
    
    S3Client()
    {
    	try {
        	s3 = new AmazonS3Client();
    	} catch (Exception ex) {
    		throw new RuntimeException("Cannot connect to S3");
    	}
    }
    
    // Static getter
    public static S3Client getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * Copy a file from S3 to HDFS
     * @param bucketName
     * @param key
     * @param destination
     */
    public void copyToHDFS(String bucketName, String key, String destination) {
    	S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
    	try {
    		Configuration conf = new Configuration();
			FileSystem hdfs = FileSystem.get(conf);
			OutputStream o = hdfs.create(new Path(destination));
			IOUtils.copyBytes(object.getObjectContent(), o, conf);
		} catch (IOException e) {
			throw new RuntimeException("Error while copying file from s3 to hdfs");
		}
    }
    
    /**
     * Copy a file from S3 to a local location
     * @param bucketName
     * @param key
     * @param destination the full path to the file that we will store the s3's object content
     */
    public void copyToLocal(String bucketName, String key, String destination) {
    	try {
    		Log.info("copying from bucket " + bucketName + " with key " + key);
    		S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
			org.apache.commons.io.IOUtils.copy(object.getObjectContent(), new FileOutputStream(destination));
		} catch (Exception e) {
			throw new RuntimeException("Error while copying file from s3 to hdfs", e);
		}
    }
    
    public void copyToLocal(String s3Url, String destination) {
    	String[] s3Parts = getS3Parts(s3Url);
        copyToLocal(s3Parts[0], s3Parts[1], destination);
    }
    
    public void copyToHDFS(String s3Url, String destination) {
    	String[] s3Parts = getS3Parts(s3Url);
    	copyToHDFS(s3Parts[0], s3Parts[1], destination);
    }
    
    protected String[] getS3Parts(String s3Url) {
    	String[] s3Parts = new String[2];
    	try {
    		int slashIndex = s3Url.indexOf('/', "s3://".length());
    		s3Parts[0] = s3Url.substring("s3://".length(), slashIndex);
    		s3Parts[1] = s3Url.substring(slashIndex + 1);
    	} catch (Exception ex) {
    		throw new RuntimeException("Invalid s3 url: " + s3Url, ex);
    	}
    	return s3Parts;
    }
}