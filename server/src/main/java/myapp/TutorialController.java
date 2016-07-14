package myapp;

import org.opendolphin.core.server.action.DolphinServerAction;
import org.opendolphin.core.server.comm.ActionRegistry;

import static myapp.TutorialConstants.ATT_FIRSTNAME;
import static myapp.TutorialConstants.CMD_LOG;
import static myapp.TutorialConstants.PM_PERSON;

/*
	This is a controller that may have many actions that serve a common purpose.
 */

public class TutorialController extends DolphinServerAction {
	final ILogService service;

	public TutorialController(ILogService service) {
		this.service = service;
	}

	public void registerIn(ActionRegistry actionRegistry) {
		actionRegistry.register(CMD_LOG, (command, response) -> {
			service.log(getServerDolphin().getAt(PM_PERSON).getAt(ATT_FIRSTNAME).getValue());
		});
	}
}
