package myapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.opendolphin.LogConfig;
import org.opendolphin.core.client.comm.InMemoryClientConnector;
import org.opendolphin.core.client.comm.SynchronousInMemoryClientConnector;
import org.opendolphin.core.client.comm.UiThreadHandler;
import org.opendolphin.core.comm.DefaultInMemoryConfig;

import static myapp.MyAppCommands.CMD_LOG;
import static myapp.PersonVeneer.PERSON_ID;
import static myapp.PersonVeneer.ATT_FIRSTNAME;
import static org.junit.Assert.assertEquals;

public class MyAppTests {

	private DefaultInMemoryConfig config;

	@Before
	public void setup() {
		setupConfig();
		config.getServerDolphin().registerDefaultActions();
		config.getClientDolphin().getClientConnector().setUiThreadHandler(new UiThreadHandler() {
			@Override
			public void executeInsideUiThread(Runnable runnable) {
				runnable.run();
			}
		});
		LogConfig.noLogs(); // Do not log any dolphin messages.
		config.getClientDolphin().presentationModel(PERSON_ID, Collections.singletonList(ATT_FIRSTNAME));
	}

	private void setupConfig() {
		config = new DefaultInMemoryConfig();
		InMemoryClientConnector inMemoryClientConnector = new SynchronousInMemoryClientConnector(config.getClientDolphin(), config.getServerDolphin()
		                                                                                                                          .getServerConnector());
		config.getClientDolphin().setClientConnector(inMemoryClientConnector);
	}

	/**
	 * with 0.8 and above
	 */
//    private void setupConfig() {
//        config = new DefaultInMemoryConfig(false);
//    }
	@Test
	public void testCallAction() throws Exception {
		setFirstName("Dolphin");
		final List<Boolean> serverSideCalled = new ArrayList<>();
		config.getServerDolphin().register(new Reception(new ILogService() {
			@Override
			public void log(Object message) {
				serverSideCalled.add(Boolean.TRUE);
				assertEquals("Dolphin", message);
			}
		}));
		config.getClientDolphin().send(CMD_LOG);
		assertEquals(1, serverSideCalled.size());
	}

	private void setFirstName(String firstName) {
		config.getClientDolphin().getAt(PERSON_ID).getAt(ATT_FIRSTNAME).setValue(firstName);
	}
}
