package driveRESTful;

 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.plaf.InputMapUIResource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Got it!";
	}

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void upload(@FormDataParam("file") java.io.File file,
			@FormDataParam("file") FormDataContentDisposition fileDetail
			) {
		
	
		
 		try {
			Auth.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			Auth.dataStoreFactory = new FileDataStoreFactory(UploadMethod.DATA_STORE_DIR);
			// authorization
			Credential credential = Auth.authorize();
			// set up the global Drive instance
			UploadMethod.drive = new Drive.Builder(Auth.httpTransport, Auth.JSON_FACTORY, credential)
					.setApplicationName(UploadMethod.APPLICATION_NAME).build();

		 UploadMethod.uploadFile(true,fileDetail.getFileName());

			// return "succ ";
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(1);

	}

}
