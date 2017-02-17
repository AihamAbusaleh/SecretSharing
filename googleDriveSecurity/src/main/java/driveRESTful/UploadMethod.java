package driveRESTful;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import algorithm.aes256sha256.CryptoUtils;

public class UploadMethod {
	public static Drive drive;
	public static final String APPLICATION_NAME = "GoogleDriveTest";
	static final String UPLOAD_FILE_PATH = "D:/TEST/";
	 

	public static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".store/restfulA");

	public static File uploadFile(boolean useDirectUpload, String myFile) throws Exception {
		java.io.File ORIGINAL_FILE = new java.io.File(UPLOAD_FILE_PATH + myFile);
		System.out.println(ORIGINAL_FILE.getAbsolutePath());
		java.io.File getEncFile = CryptoUtils.encrypt(ORIGINAL_FILE, ORIGINAL_FILE);
		System.out.println(getEncFile.getAbsolutePath());
		java.io.File DECRYPTED_FILE = new java.io.File(getEncFile.getAbsolutePath());
		File fileMetadata = new File();
		fileMetadata.setTitle(DECRYPTED_FILE.getName());

		FileContent mediaContent = new FileContent("*/*", DECRYPTED_FILE);

		Drive.Files.Insert insert = drive.files().insert(fileMetadata, mediaContent);
		MediaHttpUploader uploader = insert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(useDirectUpload);
		return insert.execute();
	}
	/*
	 * // Test Method to upload file into specified folder(parent id) in google
	 * drive account public static File insertFile(Drive service, String title,
	 * String description, String parentId, String mimeType, String filename) {
	 * // File's metadata. File body = new File(); body.setTitle(title);
	 * body.setDescription(description); body.setMimeType(mimeType);
	 * 
	 * // Set the parent folder. if (parentId != null && parentId.length() > 0)
	 * { body.setParents(Arrays.asList(new ParentReference().setId(parentId)));
	 * }
	 * 
	 * // File's content. java.io.File fileContent = new java.io.File(filename);
	 * FileContent mediaContent = new FileContent(mimeType, fileContent); try {
	 * File file = service.files().insert(body, mediaContent).execute();
	 * 
	 * // Uncomment the following line to print the File ID.
	 * System.out.println("File ID: " + file.getId());
	 * 
	 * return file; } catch (IOException e) {
	 * System.out.println("An error occured: " + e); return null; } }
	 */
}
