package restful.Dropbox;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("/myresourceDropbox")
public class MyResourceDropbox {
	static final String UPLOAD_FILE_PATH = "D:/TEST/";

	static final String ArgAuthFile = "D:/client_secretsDropbox.json";

	static final String DropboxPath = "/";

	@POST
	@Path("/uploadToDropbox")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String upload(FormDataMultiPart files) throws MalformedURLException {
		// @FormDataParam("file") FormDataContentDisposition fileDetail
		List<FormDataBodyPart> fields = files.getFields("file");

		try {

			for (FormDataBodyPart f : fields) {
				UploadToDropbox.uploadToDropboxMethod(ArgAuthFile,
						UPLOAD_FILE_PATH + f.getFormDataContentDisposition().getFileName(),
						DropboxPath + f.getFormDataContentDisposition().getFileName());
			}

			return fields.size() + " Files were seccsesfully uploaded .....";
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(1);
		return "Faild to upload the Files. .. .";

	}

}
