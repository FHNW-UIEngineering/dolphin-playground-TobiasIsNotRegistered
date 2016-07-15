package myapp;

import javafx.application.Application;

import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientModelStore;
import org.opendolphin.core.client.comm.BlindCommandBatcher;
import org.opendolphin.core.client.comm.HttpClientConnector;
import org.opendolphin.core.client.comm.JavaFXUiThreadHandler;
import org.opendolphin.core.comm.JsonCodec;

/**
	Starts a JavaFX client locally that connects to the remote application, which runs on the server.
 */

// todo : refactor the non-application specific default setup into its own class
// todo : make the host/port configurable from the command line

public class MyRemoteStarter {

	public static void main(String[] args) throws Exception {
		ClientDolphin clientDolphin = new ClientDolphin();
		clientDolphin.setClientModelStore(new ClientModelStore(clientDolphin));

		BlindCommandBatcher commandBatcher = new BlindCommandBatcher();
		commandBatcher.setMergeValueChanges(true);

		HttpClientConnector connector = new HttpClientConnector(clientDolphin,
		                                                        commandBatcher,
		                                                        "http://localhost:8080/myApp/server/");
		connector.setCodec(new JsonCodec());
		connector.setUiThreadHandler(new JavaFXUiThreadHandler());

		clientDolphin.setClientConnector(connector);

		MyApp.clientDolphin = clientDolphin;
		Application.launch(MyApp.class);
	}
}
