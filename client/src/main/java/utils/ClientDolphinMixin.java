package utils;

import java.util.List;

import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;
import org.opendolphin.core.client.comm.OnFinishedHandler;
import org.opendolphin.core.client.comm.OnFinishedHandlerAdapter;

import util.DolphinMixin;

/**
 * @author Dieter Holz
 */
public interface ClientDolphinMixin extends DolphinMixin {

    /*
    when a PM has a self-provided ID, it usually has no specific type. We can safely use null.
     */
    default ClientPresentationModel presentationModel(String pmId, Enum[] attr){
        return presentationModel(pmId, null, attr);
    }

	default ClientPresentationModel presentationModel(String pmId, String type, Enum[] attr) {
		ClientAttribute[] clientAttributes = new ClientAttribute[attr.length];
		for (int i = 0; i < attr.length; i++) {
			String qualifier = null;
			if (type != null) {
				qualifier = type + "." + attr[i] + ":" + pmId;
			}
			clientAttributes[i] = new ClientAttribute(attr[i].name(), null, qualifier);
		}
		return getClientDolphin().presentationModel(pmId, type, clientAttributes);
	}

    default void send(String commandName, OnFinishedHandler handler) {
  		getClientDolphin().send(commandName, handler);
  	}

  	default void send(String commandName) {
  		getClientDolphin().send(commandName);
  	}


	default ClientDolphin getClientDolphin() {
		return (ClientDolphin) getDolphin();
	}

}
