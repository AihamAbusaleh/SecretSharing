package driveRESTful;

import java.io.InputStreamReader;
import java.util.Collections;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;

public class Auth {

	static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	static FileDataStoreFactory dataStoreFactory;
	static HttpTransport httpTransport;

	static Credential authorize() throws Exception {
		// load client secres
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(MyResource.class.getResourceAsStream("/client_secrets.json")));
 		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			
			System.exit(1);
		}
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				clientSecrets, Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(dataStoreFactory)
						.build();
		// authorize 
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}
}
