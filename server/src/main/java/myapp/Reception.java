package myapp;

import org.opendolphin.core.server.action.DolphinServerAction;
import org.opendolphin.core.server.comm.ActionRegistry;

/**
	At the reception all controllers check in.
*/

public class Reception extends DolphinServerAction {
    private MyService myService;

    public Reception(MyService myService) {
        this.myService = myService;
    }

    public void registerIn(ActionRegistry registry) {
        // register all your controllers here.
        getServerDolphin().register(new PersonController(myService));
    }
}
