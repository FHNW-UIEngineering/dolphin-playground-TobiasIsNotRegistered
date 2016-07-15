package myapp;

import javafx.application.Application;

import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientModelStore;
import org.opendolphin.core.client.comm.BlindCommandBatcher;
import org.opendolphin.core.client.comm.InMemoryClientConnector;
import org.opendolphin.core.client.comm.JavaFXUiThreadHandler;
import org.opendolphin.core.server.DefaultServerDolphin;
import org.opendolphin.core.server.ServerDolphin;
import org.opendolphin.core.server.ServerDolphinFactory;

public class CombinedAppStarter {

	public static void main(String[] args) throws Exception {
		ClientDolphin        clientDolphin = new ClientDolphin();
		DefaultServerDolphin serverDolphin = (DefaultServerDolphin) ServerDolphinFactory.create();

		BlindCommandBatcher commandBatcher = new BlindCommandBatcher();
		commandBatcher.setMergeValueChanges(true);

		InMemoryClientConnector clientConnector = new InMemoryClientConnector(clientDolphin, serverDolphin.getServerConnector(), commandBatcher);
		clientConnector.setSleepMillis(100);
		clientConnector.setUiThreadHandler(new JavaFXUiThreadHandler());

		clientDolphin.setClientConnector(clientConnector);
		clientDolphin.setClientModelStore(new ClientModelStore(clientDolphin));

		serverDolphin.registerDefaultActions();

		registerApplicationActions(serverDolphin);

		MyAppView.clientDolphin = clientDolphin;
		Application.launch(MyAppView.class);
	}

	private static void registerApplicationActions(ServerDolphin serverDolphin) {
		serverDolphin.register(new Reception());
	}

}
