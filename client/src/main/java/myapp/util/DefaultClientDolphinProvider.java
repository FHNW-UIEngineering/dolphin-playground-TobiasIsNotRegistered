package myapp.util;

import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientModelStore;
import org.opendolphin.core.client.comm.BlindCommandBatcher;
import org.opendolphin.core.client.comm.HttpClientConnector;
import org.opendolphin.core.client.comm.JavaFXUiThreadHandler;
import org.opendolphin.core.comm.JsonCodec;

/**
 * @author Dieter Holz
 */
public class DefaultClientDolphinProvider {

    public static ClientDolphin getClientDolphin(String serverURL) {
        ClientDolphin clientDolphin = new ClientDolphin();
        clientDolphin.setClientModelStore(new ClientModelStore(clientDolphin));

        BlindCommandBatcher commandBatcher = new BlindCommandBatcher();
        commandBatcher.setMergeValueChanges(true);
        commandBatcher.setDeferMillis(200);

        HttpClientConnector connector = new HttpClientConnector(clientDolphin,
                                                                commandBatcher,
                                                                serverURL);
        connector.setCodec(new JsonCodec());
        connector.setUiThreadHandler(new JavaFXUiThreadHandler());
        connector.setStrictMode(false);


        clientDolphin.setClientConnector(connector);

        return clientDolphin;
    }
}
