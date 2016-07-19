package myapp;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

import static myapp.MyAppCommands.CMD_CALL_MY_SERVICE;

/**
 * Implementation of the view details, event handling, and binding.
 *
 * @author Dieter Holz
 */
class RootPane extends VBox implements FXBindingMixin {

    private final ClientDolphin clientDolphin;
    private final Person        person;

    private Label     nameLabel;
    private TextField nameFieldA;
    private TextField nameFieldB;
    private Button    saveButton;
    private Button    resetButton;

    RootPane(ClientDolphin clientDolphin, Person person) {
        this.clientDolphin = clientDolphin;
        this.person = person;
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
        nameLabel = new Label();
        nameLabel.getStyleClass().add("heading");

        nameFieldA  = new TextField();
        nameFieldB  = new TextField();
        saveButton  = new Button("save");
        resetButton = new Button("resetButton");
    }

    private void layoutParts() {
        getChildren().addAll(nameLabel, nameFieldA, nameFieldB, saveButton, resetButton);
    }

    private void addEventHandlers() {
        EventHandler<ActionEvent> rebaseHandler = $$ -> send(CMD_CALL_MY_SERVICE, $ -> person.firstName().rebase());
        nameFieldB.setOnAction(rebaseHandler);
        saveButton.setOnAction(rebaseHandler);

        final RotateTransition fadeIn = new RotateTransition(Duration.millis(200), nameFieldB);
        fadeIn.setToAngle(0.0);

        final RotateTransition fadeOut = new RotateTransition(Duration.millis(100), nameFieldB);
        fadeOut.setFromAngle(-3.0);
        fadeOut.setInterpolator(Interpolator.LINEAR);
        fadeOut.setToAngle(3.0);
        fadeOut.setCycleCount(3);
        fadeOut.setOnFinished($ -> {
            person.firstName().reset();
            fadeIn.playFromStart();
        });

        resetButton.setOnAction($ -> fadeOut.playFromStart());
    }

    private void addValueChangedListeners() {
        onDirtyStateChanged(person, (observable, oldValue, newValue) -> {
            if (newValue) {
                nameFieldB.getStyleClass().add("dirty");
            } else {
                nameFieldB.getStyleClass().remove("dirty");
            }
        });
    }

    private void setupBinding() {
        // use JavaFX binding via the 'nameProperty'
        // this is applicable for the value of an attribute only
        nameLabel.textProperty().bind(person.firstNameProperty());
        nameFieldA.textProperty().bindBidirectional(person.firstNameProperty());

        // use FXBindingMixin for bidirectional binding via the 'firstName attribute'
        interrelate(person.firstName()).with(nameFieldB.textProperty());

        // use FXBindingMixin for binding the dirty state of a veneer
        bindDirtyStateOf(person).convertedBy(b -> !b).to(saveButton.disableProperty());

        // the Open-Dolphin way of binding (there should always be a more convenient way provided by FXBindingMixin)
        PresentationModel pm = person.getPresentationModel();

        Converter inv = new Converter<Boolean, Boolean>() {
            @Override
            public Boolean convert(Boolean value) {
                return !value;
            }
        };

        JFXBinder.bindInfo(Attribute.DIRTY_PROPERTY).of(pm).using(inv).to("disabled").of(resetButton);
    }

    @Override
    public Dolphin getDolphin() {
        return clientDolphin;
    }

}
