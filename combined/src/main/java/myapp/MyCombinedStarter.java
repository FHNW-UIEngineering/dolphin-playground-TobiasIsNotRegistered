package myapp;

import javafx.application.Application;

import org.opendolphin.core.server.ServerDolphin;

import myapp.controller.Reception;
import myapp.service.impl.SomeRemoteService;
import myapp.util.DefaultCombinedDolphinProvider;

/**
 * Starts a JavaFX client and controller with services as one combined, local application.
 */

public class MyCombinedStarter {

    public static void main(String[] args) throws Exception {
        DefaultCombinedDolphinProvider dolphinProvider = new DefaultCombinedDolphinProvider();

        registerApplicationActions(dolphinProvider.getServerDolphin());
        MyAppView.clientDolphin = dolphinProvider.getClientDolphin();

        Application.launch(MyAppView.class);
    }

    private static void registerApplicationActions(ServerDolphin serverDolphin) {
        //todo: instantiate all your services here and provide them to the Reception
        SomeCombinedService myService = new SomeCombinedService();

        serverDolphin.register(new Reception(myService));
    }

}
