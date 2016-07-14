package myapp;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransitionBuilder;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import org.opendolphin.binding.Converter;
import org.opendolphin.binding.JFXBinder;
import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.client.ClientDolphin;

import utils.FXBindingMixin;

import static myapp.MyAppCommands.CMD_LOG;

/**
 * @author Dieter Holz
 */
class RootPane extends VBox implements FXBindingMixin {

	private final ClientDolphin clientDolphin;

	private TextField firstNameField;
	private Button    saveButton;
	private Button    resetButton;

	public RootPane(ClientDolphin clientDolphin) {
		this.clientDolphin = clientDolphin;
		init();
		initializeParts();
		layoutParts();
		addEventHandlers();
		addValueChangedListeners();
		setupBinding();
	}

	private void init() {
		getStyleClass().add("rootPane");
	}

	private void initializeParts() {
		firstNameField = new TextField();
		saveButton = new Button("save");
		resetButton = new Button("resetButton");
	}

	private void layoutParts() {
		getChildren().addAll(firstNameField, saveButton, resetButton);
	}

	private void addEventHandlers() {
		EventHandler<ActionEvent> rebaseHandler = event -> clientDolphin.send(CMD_LOG,
		                                                                      presentationModels -> new PersonVeneer(get(PersonPM.ID_4711)).firstName()
		                                                                                                                                   .rebase());
		firstNameField.setOnAction(rebaseHandler);
		saveButton.setOnAction(rebaseHandler);

		//todo get rid of the builders
		final Transition fadeIn = RotateTransitionBuilder.create().node(firstNameField).toAngle(0).duration(Duration.millis(200)).build();
		final Transition fadeOut = RotateTransitionBuilder.create().node(firstNameField).fromAngle(-3).interpolator(Interpolator.LINEAR).
				toAngle(3).cycleCount(3).duration(Duration.millis(100)).
				                                                  onFinished(actionEvent -> {
					                                                  new PersonVeneer(get(PersonPM.ID_4711)).firstName().reset();
					                                                  fadeIn.playFromStart();
				                                                  }).build();

		resetButton.setOnAction(actionEvent -> fadeOut.playFromStart());
	}

	private void addValueChangedListeners() {
		PresentationModel pm = get(PersonPM.ID_4711);

		onDirtyStateChanged(pm, evt -> {
			System.out.println("dirty changed " + evt.getNewValue());
			if ((Boolean) evt.getNewValue()) {
				firstNameField.getStyleClass().add("dirty");
			} else {
				firstNameField.getStyleClass().remove("dirty");
			}
		});
	}

	private void setupBinding() {
		PresentationModel pm = get(PersonPM.ID_4711);
		PersonVeneer      p  = new PersonVeneer(pm);

		bindBidirectional(p.firstName()).to(firstNameField.textProperty());

		Converter inv = new Converter<Boolean, Boolean>() {
			@Override
			public Boolean convert(Boolean value) {
				return !value;
			}
		};
		JFXBinder.bindInfo(Attribute.DIRTY_PROPERTY).of(pm).using(inv).to("disabled").of(saveButton);
		JFXBinder.bindInfo(Attribute.DIRTY_PROPERTY).of(pm).using(inv).to("disabled").of(resetButton);
	}

	@Override
	public Dolphin getDolphin() {
		return clientDolphin;
	}

}
