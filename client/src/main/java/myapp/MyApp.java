package myapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.opendolphin.core.Dolphin;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;

import utils.ClientDolphinMixin;

public class MyApp extends Application implements ClientDolphinMixin {
	static ClientDolphin clientDolphin;

	public MyApp() {
	}

	@Override
	public void init() throws Exception {
		ClientPresentationModel pm = presentationModel(PersonPM.ID_4711, PersonPM.TYPE, PersonPM.ATT.values());

		PersonVeneer p = new PersonVeneer(pm);
		p.setFirstName("");

		pm.rebase();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Pane root = new RootPane(clientDolphin);

		Scene scene = new Scene(root, 300, 250);
		scene.getStylesheets().add(MyApp.class.getResource("/myapp/myApp.css").toExternalForm());

		stage.setScene(scene);
		stage.setTitle(getClass().getName());

		stage.show();
	}

	@Override
	public Dolphin getDolphin() {
		return clientDolphin;
	}
}
