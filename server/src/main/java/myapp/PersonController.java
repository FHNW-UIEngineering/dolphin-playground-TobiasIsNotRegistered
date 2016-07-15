package myapp;

import org.opendolphin.core.Dolphin;
import org.opendolphin.core.server.action.DolphinServerAction;
import org.opendolphin.core.server.comm.ActionRegistry;

import util.DolphinMixin;

import static myapp.MyAppCommands.CMD_LOG;
/*
	This is a controller that may have many actions that serve a common purpose.
 */

public class PersonController extends DolphinServerAction implements DolphinMixin {
	final MyService service;

	public PersonController(MyService service) {
		this.service = service;
	}

	public void registerIn(ActionRegistry actionRegistry) {
		actionRegistry.register(CMD_LOG, (command, response) -> {
			PersonVeneer p = new PersonVeneer(get(PersonPM.ID_4711));
			service.myServiceMethod(p.getFirstName());
		});
	}

	@Override
	public Dolphin getDolphin() {
		return getServerDolphin();
	}
}
