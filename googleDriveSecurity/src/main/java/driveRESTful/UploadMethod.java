package driveRESTful;


import java.io.IOException;
import java.io.InputStream;

import javax.swing.plaf.synth.SynthScrollBarUI;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class UploadMethod {
	 public static Drive drive;
	 public static final String APPLICATION_NAME = "GoogleDriveTest";
	 // static final String UPLOAD_FILE_PATH = "D:/images.jpg";
	 // static final java.io.File UPLOAD_FILE = new java.io.File(UPLOAD_FILE_PATH);
	 
	 public static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".store/drive_restfulTest");
	 
	public  static File uploadFile(boolean useDirectUpload, String fileName) throws IOException {
	    java.io.File UPLOAD_FILE = new java.io.File("D:/" + fileName);
	    System.out.println(UPLOAD_FILE);
	 	File fileMetadata = new File();
	 	fileMetadata.setTitle(UPLOAD_FILE.getName());
		FileContent mediaContent = new FileContent("image/jepg", UPLOAD_FILE);
		Drive.Files.Insert insert = drive.files().insert(fileMetadata, mediaContent);
		MediaHttpUploader uploader = insert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(useDirectUpload);
		return insert.execute();
	}
}
