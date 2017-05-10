package myapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.opendolphin.core.Dolphin;
import org.opendolphin.core.client.ClientDolphin;

import myapp.presentationmodel.SpecialPMMixin;
import myapp.presentationmodel.person.PersonCommands;
import myapp.util.BasicCommands;
import util.ClientDolphinMixin;

/**
 * The main view of MyApp.
 */

public class MyAppView extends Application implements ClientDolphinMixin, SpecialPMMixin {
    static ClientDolphin clientDolphin;

    @Override
    public void start(Stage stage) throws Exception {
        send(BasicCommands.INITIALIZE_BASE_PMS,
             $ -> {
                 buildUI(stage);
                 send(BasicCommands.INITIALIZE_CONTROLLER,
                      $$ -> send(PersonCommands.LOAD_SOME_PERSON));
             });
    }

    private void buildUI(Stage stage) {
        Pane root = new RootPane(clientDolphin);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.titleProperty().bind(getPresentationState().applicationTitle.labelProperty());

        stage.show();
    }

    @Override
    public Dolphin getDolphin() {
        return clientDolphin;
    }
}
