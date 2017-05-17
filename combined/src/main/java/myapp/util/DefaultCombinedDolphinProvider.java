package myapp.util;

import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientModelStore;
import org.opendolphin.core.client.comm.BlindCommandBatcher;
import org.opendolphin.core.client.comm.InMemoryClientConnector;
import org.opendolphin.core.client.comm.JavaFXUiThreadHandler;
import org.opendolphin.core.server.DefaultServerDolphin;
import org.opendolphin.core.server.ServerDolphinFactory;

/**
 * @author Dieter Holz
 */
public class DefaultCombinedDolphinProvider {
    private final ClientDolphin        clientDolphin;
    private final DefaultServerDolphin serverDolphin;

    public DefaultCombinedDolphinProvider() {
        clientDolphin = new ClientDolphin();
        serverDolphin = (DefaultServerDolphin) ServerDolphinFactory.create();

        BlindCommandBatcher commandBatcher = new BlindCommandBatcher();
        commandBatcher.setMergeValueChanges(true);
        commandBatcher.setDeferMillis(200);

        InMemoryClientConnector clientConnector = new InMemoryClientConnector(clientDolphin, serverDolphin.getServerConnector(), commandBatcher);
        clientConnector.setSleepMillis(10);
        clientConnector.setUiThreadHandler(new JavaFXUiThreadHandler());
        clientConnector.setStrictMode(false);

        clientDolphin.setClientConnector(clientConnector);
        clientDolphin.setClientModelStore(new ClientModelStore(clientDolphin));

        serverDolphin.registerDefaultActions();
    }

    public ClientDolphin getClientDolphin() {
        return clientDolphin;
    }

    public DefaultServerDolphin getServerDolphin() {
        return serverDolphin;
    }
}
