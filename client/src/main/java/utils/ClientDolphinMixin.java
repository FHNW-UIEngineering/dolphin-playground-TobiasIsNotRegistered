package utils;

import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;

import util.DolphinMixin;

/**
 * @author Dieter Holz
 */
public interface ClientDolphinMixin extends DolphinMixin {

	default ClientPresentationModel presentationModel(String pmId, String type, Enum[] attr) {
		ClientAttribute[] clientAttributes = new ClientAttribute[attr.length];
		for (int i = 0; i < attr.length; i++) {
			clientAttributes[i] = new ClientAttribute(attr[i].name(), null, type + "." + attr[i] + ":" + pmId);
		}
		return getClientDolphin().presentationModel(pmId, type, clientAttributes);
	}

	default ClientDolphin getClientDolphin() {
		return (ClientDolphin) getDolphin();
	}

}
