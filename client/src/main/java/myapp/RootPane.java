package myapp;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransitionBuilder;
import javafx.animation.Transition;
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

    private Label     firstNameLabel;
    private TextField firstNameFieldJavaFXBinding;
    private TextField firstNameFieldDolphinBinding;
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
        firstNameLabel = new Label();
        firstNameLabel.getStyleClass().add("heading");

        firstNameFieldJavaFXBinding  = new TextField();
        firstNameFieldDolphinBinding = new TextField();
        saveButton                   = new Button("save");
        resetButton                  = new Button("resetButton");
    }

    private void layoutParts() {
        getChildren().addAll(firstNameLabel, firstNameFieldJavaFXBinding, firstNameFieldDolphinBinding, saveButton, resetButton);
    }

    private void addEventHandlers() {
        EventHandler<ActionEvent> rebaseHandler = event -> clientDolphin.send(CMD_CALL_MY_SERVICE, $ -> person.firstName().rebase());
        firstNameFieldDolphinBinding.setOnAction(rebaseHandler);
        saveButton.setOnAction(rebaseHandler);

        //todo get rid of the builders
        final Transition fadeIn = RotateTransitionBuilder.create().node(firstNameFieldDolphinBinding).toAngle(0).duration(Duration.millis(200)).build();
        final Transition fadeOut = RotateTransitionBuilder.create().node(firstNameFieldDolphinBinding).fromAngle(-3).interpolator(Interpolator.LINEAR).
                toAngle(3).cycleCount(3).duration(Duration.millis(100)).
                                                                  onFinished($ -> {
                                                                      person.firstName().reset();
                                                                      fadeIn.playFromStart();
                                                                  }).build();

        resetButton.setOnAction($ -> fadeOut.playFromStart());
    }

    private void addValueChangedListeners() {
        onDirtyStateChanged(person.getPresentationModel(), evt -> {
            System.out.println("dirty changed " + evt.getNewValue());
            if ((Boolean) evt.getNewValue()) {
                firstNameFieldDolphinBinding.getStyleClass().add("dirty");
            } else {
                firstNameFieldDolphinBinding.getStyleClass().remove("dirty");
            }
        });
    }

    private void setupBinding() {
        PresentationModel pm = person.getPresentationModel();

        // use JavaFX binding via the 'nameProperty'
        firstNameLabel.textProperty().bind(person.nameProperty());
        firstNameFieldJavaFXBinding.textProperty().bindBidirectional(person.nameProperty());

        // use OpenDolphin binding via the 'firstName attribute'
        bindBidirectional(person.firstName()).to(firstNameFieldDolphinBinding.textProperty());

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
