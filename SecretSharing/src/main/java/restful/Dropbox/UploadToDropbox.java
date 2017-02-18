package restful.Dropbox;

import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxPathV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

/**
 * An example command-line application that runs through the web-based OAuth
 * flow (using {@link DbxWebAuth}).
 */
public class UploadToDropbox {
	 

	/**
	 * Uploads a file in a single request. This approach is preferred for small
	 * files since it eliminates unnecessary round-trips to the servers.
	 *
	 * @param dbxClient
	 *            Dropbox user authenticated client
	 * @param localFIle
	 *            local file to upload
	 * @param dropboxPath
	 *            Where to upload the file to within Dropbox
	 */
	private static void uploadFile(DbxClientV2 dbxClient, File localFile, String dropboxPath) {
		try (InputStream in = new FileInputStream(localFile)) {
			FileMetadata metadata = dbxClient.files().uploadBuilder(dropboxPath).withMode(WriteMode.ADD)
					.withClientModified(new Date(localFile.lastModified())).uploadAndFinish(in);

			System.out.println(metadata.toStringMultiline());
		} catch (UploadErrorException ex) {
			System.err.println("Error uploading to Dropbox: " + ex.getMessage());
			System.exit(1);
		} catch (DbxException ex) {
			System.err.println("Error uploading to Dropbox: " + ex.getMessage());
			System.exit(1);
		} catch (IOException ex) {
			System.err.println("Error reading from file \"" + localFile + "\": " + ex.getMessage());
			System.exit(1);
		}
	}

	public static void  uploadToDropboxMethod(String argAuthFile, String localPath,String dropboxPath) throws IOException {
 		Logger.getLogger("").setLevel(Level.WARNING);
 
		// Read auth info file.
		DbxAuthInfo authInfo;
		try {
			authInfo = DbxAuthInfo.Reader.readFromFile(argAuthFile);
		} catch (JsonReader.FileLoadException ex) {
			System.err.println("Error loading <auth-file>: " + ex.getMessage());
			System.exit(1);
			return;
		}

		String pathError = DbxPathV2.findError(dropboxPath);
		if (pathError != null) {
			System.err.println("Invalid <dropbox-path>: " + pathError);
			System.exit(1);
			return;
		}

		File localFile = new File(localPath);
		if (!localFile.exists()) {
			System.err.println("Invalid <local-path>: file does not exist.");
			System.exit(1);
			return;
		}

		if (!localFile.isFile()) {
			System.err.println("Invalid <local-path>: not a file.");
			System.exit(1);
			return;
		}

		// Create a DbxClientV2, which is what you use to make API calls.
		DbxRequestConfig requestConfig = new DbxRequestConfig("examples-upload-file");
		DbxClientV2 dbxClient = new DbxClientV2(requestConfig, authInfo.getAccessToken(), authInfo.getHost());
		uploadFile(dbxClient, localFile, dropboxPath);
		
	 
	}
}
