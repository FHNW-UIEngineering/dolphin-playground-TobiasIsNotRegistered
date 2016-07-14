package myapp;

import javafx.application.Application;

import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientModelStore;
import org.opendolphin.core.client.comm.BlindCommandBatcher;
import org.opendolphin.core.client.comm.HttpClientConnector;
import org.opendolphin.core.client.comm.JavaFXUiThreadHandler;
import org.opendolphin.core.comm.JsonCodec;

public class ClientStarter {

	public static void main(String[] args) throws Exception {
		ClientDolphin clientDolphin = new ClientDolphin();
		clientDolphin.setClientModelStore(new ClientModelStore(clientDolphin));

		BlindCommandBatcher commandBatcher = new BlindCommandBatcher();
		commandBatcher.setMergeValueChanges(true);

		HttpClientConnector connector = new HttpClientConnector(clientDolphin,
		                                                        commandBatcher,
		                                                        "http://localhost:8080/myFirstDolphin/myApp/");
		connector.setCodec(new JsonCodec());
		connector.setUiThreadHandler(new JavaFXUiThreadHandler());

		clientDolphin.setClientConnector(connector);

		MyApp.clientDolphin = clientDolphin;
		Application.launch(MyApp.class);
	}
}
