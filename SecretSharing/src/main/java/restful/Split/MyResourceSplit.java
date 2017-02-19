package restful.Split;

import java.io.File;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import algorithms.IDA.FileSplitter;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresourceSplit")
public class MyResourceSplit {
	static final String UPLOAD_FILE_PATH = "D:/TEST/";

 

	@POST
	@Path("/split")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response split(@FormDataParam("file") java.io.File file,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("max") int max,
			@FormDataParam("min") int min) throws Exception {
		System.out.println(fileDetail.getFileName());
		FileSplitter.splitOriginalFile(new File(UPLOAD_FILE_PATH + fileDetail.getFileName()), max, min);
		 java.net.URI location = new java.net.URI("../providers.jsp");
		    return Response.temporaryRedirect(location).build();

	}

	 

}
