package myapp.controller;

import org.opendolphin.core.server.action.DolphinServerAction;
import org.opendolphin.core.server.comm.ActionRegistry;

import myapp.service.SomeService;

/**
 * At the reception all controllers check in.
 *
 */

public class Reception extends DolphinServerAction {
    private SomeService myService;

    public Reception(SomeService myService) {
        this.myService = myService;
    }

    public void registerIn(ActionRegistry registry) {
        // todo register all your controllers here.
        getServerDolphin().register(new PersonController(myService));

        //always needed
        getServerDolphin().register(new ApplicationStateController());
    }
}
