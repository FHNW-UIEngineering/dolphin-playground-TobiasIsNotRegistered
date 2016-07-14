package myapp;

import org.opendolphin.core.server.action.DolphinServerAction;
import org.opendolphin.core.server.comm.ActionRegistry;

/*
	At the reception all controllers check in.
*/

public class Reception extends DolphinServerAction {
    private ILogService logService;

    public Reception(ILogService logService) {
        this.logService = logService;
    }

    public Reception() {
        this(new LogServiceImpl());
    }

    public void registerIn(ActionRegistry registry) {
        // register all your controllers here.
        getServerDolphin().register(new PersonController(logService));
    }
}
