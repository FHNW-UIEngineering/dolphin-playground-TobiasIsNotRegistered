package myapp;

import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransitionBuilder;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.PaneBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.opendolphin.binding.JFXBinder;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;
import org.opendolphin.core.client.ClientAttribute;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;
import org.opendolphin.core.client.comm.OnFinishedHandlerAdapter;

import groovy.lang.Closure;

import static myapp.TutorialConstants.ATT_FIRSTNAME;
import static myapp.TutorialConstants.CMD_LOG;
import static myapp.TutorialConstants.PM_PERSON;

public class MyApp extends Application {
	static ClientDolphin clientDolphin;

	private TextField         textField;
	private Button            button;
	private Button            reset;
	private PresentationModel textAttributeModel;

	public MyApp() {
		textAttributeModel = clientDolphin.presentationModel(PM_PERSON, new ClientAttribute(ATT_FIRSTNAME, "", null, Tag.VALUE));
	}

	@Override
	public void start(Stage stage) throws Exception {
		Pane root = PaneBuilder.create().children(
				VBoxBuilder.create().id("content").children(
						textField = TextFieldBuilder.create().id("firstname").build(),
						button = ButtonBuilder.create().text("save").build(),
						reset = ButtonBuilder.create().text("reset").build()
				                                           ).build()
		                                         ).build();

		addClientSideAction();
		setupBinding();

		Scene scene = new Scene(root, 300, 250);
		stage.setScene(scene);
		stage.setTitle(getClass().getName());
		scene.getStylesheets().add("/step_6/tutorial.css");

		stage.show();
	}

	private void setupBinding() {
		JFXBinder.bind("text").of(textField).to(ATT_FIRSTNAME).of(textAttributeModel);
		JFXBinder.bind(ATT_FIRSTNAME).of(textAttributeModel).to("text").of(textField);

		Closure dirtyStyle = new DirtyStyle(textField);
		JFXBinder.bindInfo("dirty").of(textAttributeModel).using(dirtyStyle).to("style").of(textField);

		Inverter inv = new Inverter();
		JFXBinder.bindInfo("dirty").of(textAttributeModel).using(inv).to("disabled").of(button);
		JFXBinder.bindInfo("dirty").of(textAttributeModel).using(inv).to("disabled").of(reset);
	}

	private static class DirtyStyle extends Closure {
		private TextField textField;

		public DirtyStyle(TextField textField) {
			super(null);
			this.textField = textField;
		}

		@SuppressWarnings("unused")
		public String call(Boolean dirty) {
			if (dirty) {
				textField.getStyleClass().add("dirty");
			} else {
				textField.getStyleClass().remove("dirty");
			}
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	private void addClientSideAction() {
		textField.setOnAction(new RebaseHandler(textAttributeModel));
		button.setOnAction(new RebaseHandler(textAttributeModel));
		final Transition fadeIn = RotateTransitionBuilder.create().node(textField).toAngle(0).duration(Duration.millis(200)).build();
		final Transition fadeOut = RotateTransitionBuilder.create().node(textField).fromAngle(-3).interpolator(Interpolator.LINEAR).
				toAngle(3).cycleCount(3).duration(Duration.millis(100)).
				                                                  onFinished(new EventHandler<ActionEvent>() {
					                                                  @Override
					                                                  public void handle(ActionEvent actionEvent) {
						                                                  textAttributeModel.getAt(ATT_FIRSTNAME).reset();
						                                                  fadeIn.playFromStart();
					                                                  }
				                                                  }).build();

		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				fadeOut.playFromStart();
			}
		});
	}

	private static class Inverter extends Closure {
		public Inverter() {
			super(null);
		}

		@SuppressWarnings("unused")
		protected Object call(Boolean dirtyState) {
			return !dirtyState;
		}
	}

	private static class RebaseHandler implements EventHandler {
		private PresentationModel model;

		public RebaseHandler(PresentationModel model) {
			this.model = model;
		}

		@Override
		public void handle(Event event) {
			clientDolphin.send(CMD_LOG, new OnFinishedHandlerAdapter() {
				@Override
				public void onFinished(List<ClientPresentationModel> presentationModels) {
					model.getAt(ATT_FIRSTNAME).rebase();
				}
			});
		}

	}
}
