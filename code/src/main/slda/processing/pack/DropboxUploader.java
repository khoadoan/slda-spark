package slda.processing.pack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;
import com.dropbox.core.json.JsonReader;
public class DropboxUploader {
	public static void main(String[] args) throws IOException, DbxException, InterruptedException {
        // Get your app key and secret from the Dropbox developers website.
        final String APP_KEY = args[0];
        final String APP_SECRET = args[1];
        final String inputFolder = args[2];
        final String outputFolder = args[3];
        //final URL argAuthFileOutput = DropboxUploader.class.getClassLoader().getResource("dropbox.auth");
        
        String argAuthFileOutput = args.length == 5 ? args[4] : null;
        
        if(argAuthFileOutput == null) { //manual authentication
        	DbxRequestConfig config;
            String accessToken;
            DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

            config = new DbxRequestConfig(
                "slda/1.0", Locale.getDefault().toString());
            DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
            
            
            String authorizeUrl = webAuth.start();
            
            System.out.println("1. Go to: " + authorizeUrl);
            System.out.println("2. Click \"Allow\" (you might have to log in first)");
            System.out.println("3. Copy the authorization code.");
            String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
            
            DbxAuthFinish authFinish = webAuth.finish(code);
            accessToken = authFinish.accessToken;
            
            // Save auth information to output file.
            DbxAuthInfo authInfo = new DbxAuthInfo(authFinish.accessToken, appInfo.host);
            try {
                DbxAuthInfo.Writer.writeToFile(authInfo, "/tmp/dropbox.auth");
                System.out.println("Saved authorization information to \"" + argAuthFileOutput + "\".");
            }
            catch (IOException ex) {
                System.err.println("Error saving to <auth-file-out>: " + ex.getMessage());
                System.err.println("Dumping to stderr instead:");
                DbxAuthInfo.Writer.writeToStream(authInfo, System.err);
                System.exit(1); return;
            }
        }

        DbxAuthInfo authInfo = null;
        
        try {
            authInfo = DbxAuthInfo.Reader.readFromFile(argAuthFileOutput);
        }
        catch (JsonReader.FileLoadException ex) {
            System.err.println("Error loading <auth-file>: " + ex.getMessage());
            System.exit(1);
        }
        
        String userLocale = Locale.getDefault().toString();
        DbxRequestConfig requestConfig = new DbxRequestConfig("examples-upload-file", userLocale);
        DbxClient dbxClient = new DbxClient(requestConfig, authInfo.accessToken, authInfo.host);
        
        DbxClient client = new DbxClient(requestConfig, authInfo.accessToken, authInfo.host);
        System.out.println("Linked account: " + client.getAccountInfo().displayName);
        uploadFiles(client, new File(inputFolder), outputFolder);
	}
	private static boolean deleteFlag = true;
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
    					if(deleteFlag)
    					{
    						//uploadFile.del
    					}
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
