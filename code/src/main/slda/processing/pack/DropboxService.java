package slda.processing.pack;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxHost;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.json.JsonReader.FileLoadException;

public class DropboxService {
	private DbxAuthInfo authInfo;
	private DbxClient client;
	private int maxRetries = 10;
	
	public DropboxService(File argAuthFile) {
		 try {
			authInfo = DbxAuthInfo.Reader.readFromFile(argAuthFile);
	        String userLocale = Locale.getDefault().toString();
            DbxRequestConfig requestConfig = new DbxRequestConfig("upload-file", userLocale);
	        client = new DbxClient(requestConfig, authInfo.accessToken, authInfo.host);
	      } catch (Exception ex) {
	            throw new RuntimeException("Error loading <auth-file>: " + argAuthFile.getAbsolutePath(), ex);
	      }
	}
	
	public DropboxService(String accessToken) {
		try {
			authInfo = new DbxAuthInfo(accessToken, DbxHost.Default);
	        String userLocale = Locale.getDefault().toString();
            DbxRequestConfig requestConfig = new DbxRequestConfig("upload-file", userLocale);
	        client = new DbxClient(requestConfig, authInfo.accessToken, authInfo.host);
	      } catch (Exception ex) {
	            throw new RuntimeException("Error loading with access token: " + accessToken, ex);
	      }
	}
	
	public void download(OutputStream s, String filepath) {
		int retries = maxRetries/2;
		try {
			while(retries-- > 0) {
				try {
					client.getFile(filepath, null, s);
					return;
				} catch(Exception ex) {
					Thread.sleep(15000);
				}
			}
		} catch(Exception ex) {
			throw new RuntimeException("Dropbox Error: Cannot complete the downloading of " + filepath);
		}
	}
	
	public List<DbxEntry> list(String boxFolder) {
		int retries = maxRetries;
		try {
			while(retries-- > 0) {
				try {
					DbxEntry.WithChildren listing = client.getMetadataWithChildren(boxFolder);
					return listing.children; 
				} catch(Exception ex) {
					Thread.sleep(15000);
				}
			}
		} catch(Exception ex) {
			throw new RuntimeException("Dropbox Error: Cannot complete the listing!");
		}
		
		throw new RuntimeException("Dropbox Error: Cannot complete the listing!");
	}

	public void upload(String boxFilename, byte[] data) {
		int retries = maxRetries;
    	while(retries-- > 0) {
            try {
            	System.out.println("---> Retries remaining: " + retries);
        		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
				DbxEntry.File uploadedFile = client.uploadFile(boxFilename,
                    DbxWriteMode.add(), data.length, inputStream);
				break;
            } catch(Exception ex) {
            	System.out.println("---> Retry in 30s.");
            	try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }finally { 
                
            }
    	}
	}
	
	public void upload(String localFolder, String boxFolder) throws DbxException, IOException, InterruptedException {
        System.out.println("Linked account: " + client.getAccountInfo().displayName);
        uploadFiles(client, new File(localFolder), boxFolder);
	}
	
	public static void uploadFiles(DbxClient client, File inputFile, String outputFolder) throws DbxException, IOException, InterruptedException {
		for(File uploadFile: inputFile.listFiles()) {
			if(uploadFile.isDirectory()) {	
				uploadFiles(client, uploadFile, String.format("%s/%s", outputFolder, uploadFile.getName()));
			}
			else {
	            String outputLocation = String.format("%s/%s", outputFolder, uploadFile.getName());
	            System.out.printf("Uploading %s --> %s\n", uploadFile.getName(), outputLocation);
				int retries = 50;
            	while(retries-- > 0) {
            		FileInputStream inputStream = null;
    	            try {
    	            	System.out.println("---> Retries remaining: " + retries);
    					inputStream = new FileInputStream(uploadFile);
    					DbxEntry.File uploadedFile = client.uploadFile(outputLocation,
		                    DbxWriteMode.add(), uploadFile.length(), inputStream);
    					break;
    	            } catch(Exception ex) {
    	            	System.out.println("---> Retry in 30s.");
    	            	Thread.sleep(30000);
    	            }finally { 
    	                if(inputStream != null)
    	                	inputStream.close();
    	            }
            	}
			}
		}
	}
}
