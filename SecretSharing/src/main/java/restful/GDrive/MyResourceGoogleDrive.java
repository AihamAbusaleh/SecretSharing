package restful.GDrive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;

import algorithms.IDA.FileSplitter;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresourceGoogleDrive")
public class MyResourceGoogleDrive {
	static final String UPLOAD_FILE_PATH = "D:/TEST/";

 

	 

	@POST
	@Path("/uploadToDrive")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String upload(FormDataMultiPart files)
			throws MalformedURLException {
		// @FormDataParam("file") FormDataContentDisposition fileDetail
		List<FormDataBodyPart> fields = files.getFields("file");

		try {
			Auth.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			Auth.dataStoreFactory = new FileDataStoreFactory(UploadMethod.DATA_STORE_DIR);
			// authorization
			Credential credential = Auth.authorize();
			// set up the global Drive instance
			UploadMethod.drive = new Drive.Builder(Auth.httpTransport, Auth.JSON_FACTORY, credential)
					.setApplicationName(UploadMethod.APPLICATION_NAME).build();
			for (FormDataBodyPart f : fields) {
				UploadMethod.uploadFile(true, f.getFormDataContentDisposition().getFileName());
			}

			return  fields.size() + " Files were seccsesfully uploaded .....";
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(1);
		return "Faild to upload the Files. .. .";

	}

}
