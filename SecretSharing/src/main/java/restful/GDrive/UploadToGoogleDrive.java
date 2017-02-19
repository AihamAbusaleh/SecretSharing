package restful.GDrive;

import java.io.IOException;
import java.io.InputStream;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import algorithms.AES.CryptoUtils;
 
public class UploadToGoogleDrive {
	public static Drive drive;
	public static final String APPLICATION_NAME = "GoogleDriveTest";
	static final String UPLOAD_FILE_PATH = "D:/TEST/";
	 

	public static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".store/restfulA");

	public static File uploadFile(boolean useDirectUpload, String myFile) throws Exception {
 
		java.io.File ORIGINAL_FILE = new java.io.File(UPLOAD_FILE_PATH + myFile);
		java.io.File getEncFile = CryptoUtils.encrypt(ORIGINAL_FILE, ORIGINAL_FILE);
 		java.io.File ENCRYPTED_FILE = new java.io.File(getEncFile.getAbsolutePath());
		File fileMetadata = new File();
		fileMetadata.setTitle(ENCRYPTED_FILE.getName());

		FileContent mediaContent = new FileContent("*/*", ENCRYPTED_FILE);

		Drive.Files.Insert insert = drive.files().insert(fileMetadata, mediaContent);
		MediaHttpUploader uploader = insert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(useDirectUpload);
		return insert.execute();
	}
	
	

	 
}
