package myapp;

import javafx.application.Application;

import myapp.util.DefaultClientDolphinProvider;

/**
 * Starts a JavaFX client locally that connects to the remote application, which runs on the server.
 */

public class MyRemoteStarter {

    public static void main(String[] args) throws Exception {
        String host = "localhost:8080";
        if (args.length == 1) {
            host = args[0];
        }

        String serverURL = "http://" + host + "/myApp/server/";

        MyAppView.clientDolphin = DefaultClientDolphinProvider.getClientDolphin(serverURL);

        Application.launch(MyAppView.class);
    }
}
