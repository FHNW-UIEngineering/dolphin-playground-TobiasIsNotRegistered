package myapp;

import org.junit.Before;
import org.junit.Test;
import org.opendolphin.LogConfig;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.client.ClientPresentationModel;
import org.opendolphin.core.client.comm.InMemoryClientConnector;
import org.opendolphin.core.client.comm.SynchronousInMemoryClientConnector;
import org.opendolphin.core.comm.DefaultInMemoryConfig;

import myapp.controller.Reception;
import util.ClientDolphinMixin;

import static myapp.presentationmodel.person.PersonCommands.CMD_CALL_MY_SERVICE;
import static org.junit.Assert.assertEquals;

public class MyAppTests implements ClientDolphinMixin {

	private DefaultInMemoryConfig config;

	@Override
	public Dolphin getDolphin() {
		return config.getClientDolphin();
	}

	@Before
	public void setup() {
		setupConfig();
		config.getServerDolphin().registerDefaultActions();
		config.getClientDolphin().getClientConnector().setUiThreadHandler(runnable -> runnable.run());
		LogConfig.noLogs(); // Do not log any dolphin messages.
	}

	private void setupConfig() {
		config = new DefaultInMemoryConfig();
		InMemoryClientConnector inMemoryClientConnector = new SynchronousInMemoryClientConnector(config.getClientDolphin(), config.getServerDolphin()
		                                                                                                                          .getServerConnector());
		config.getClientDolphin().setClientConnector(inMemoryClientConnector);
	}

	@Test
	public void testCallAction() throws Exception {
		// there is a Person PM with the first name set"
		ClientPresentationModel pm = presentationModel(PersonPM.ID_4711, null, PersonPM.ATT.values());
		Person clientPerson = new Person(pm);
		clientPerson.setFirstName("ClientFirstName");

		// and and action is registered that changes the first name
		config.getServerDolphin().register(new Reception(serverPerson -> {
            assertEquals("ClientFirstName", serverPerson.getFirstName());
            serverPerson.setFirstName("ServerFirstName");
        }));

		// then calling the action changes the first name on the client
		config.getClientDolphin().send(CMD_CALL_MY_SERVICE);
		assertEquals("ServerFirstName", clientPerson.getFirstName());
	}

}
