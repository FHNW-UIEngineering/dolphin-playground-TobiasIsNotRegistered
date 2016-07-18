package myapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.opendolphin.core.Dolphin;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;

import utils.ClientDolphinMixin;

/**
 * The main view of MyApp.
 */

public class MyAppView extends Application implements ClientDolphinMixin {
	static    ClientDolphin clientDolphin;
	protected Person        person;

	public MyAppView() {
	}

	@Override
	public void init() throws Exception {
		// todo dk: when a PM has a self-provided ID, it usually has no specific type. We can safely use null.
		ClientPresentationModel pm = presentationModel(PersonPM.ID_4711, PersonPM.TYPE, PersonPM.ATT.values());

		person = new Person(pm);
		person.setFirstName("");

		pm.rebase();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Pane root = new RootPane(clientDolphin, person);

		Scene scene = new Scene(root, 300, 250);
		scene.getStylesheets().add(MyAppView.class.getResource("/fonts/fonts.css").toExternalForm());
		scene.getStylesheets().add(MyAppView.class.getResource("/myapp/myApp.css").toExternalForm());

		stage.setScene(scene);
		stage.setTitle(getClass().getName());

		stage.show();
	}

	@Override
	public Dolphin getDolphin() {
		return clientDolphin;
	}
}
