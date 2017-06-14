package myapp;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import myapp.presentationmodel.rockets.Rocket;
import myapp.presentationmodel.rockets.RocketsCommands;
import org.opendolphin.binding.Converter;
import org.opendolphin.binding.JFXBinder;
import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;

import myapp.presentationmodel.BasePmMixin;
import myapp.presentationmodel.rockets.RocketAtt;
import myapp.presentationmodel.presentationstate.ApplicationState;
import myapp.presentationmodel.presentationstate.ApplicationStateAtt;
import myapp.util.AdditionalTag;
import myapp.util.Language;
import myapp.util.ViewMixin;
import myapp.util.veneer.AttributeFX;
import myapp.util.veneer.BooleanAttributeFX;

/**
 * Implementation of the view details, event handling, and binding.
 *
 * @author Dieter Holz
 *
 * todo : Replace it with your application UI
 */
class RootPane extends GridPane implements ViewMixin, BasePmMixin {

    private static final String DIRTY_STYLE     = "dirty";
    private static final String INVALID_STYLE   = "invalid";
    private static final String MANDATORY_STYLE = "mandatory";

    // clientDolphin is the single entry point to the PresentationModel-Layer
    private final ClientDolphin clientDolphin;

    private Label headerLabel;

    private Label idLabel;
    private Label idField;

    private Label     nameLabel;
    private TextField nameField;

    private Label missionLabel;
    private TextField missionField;

    private Label launchLabel;
    private TextField launchField;

    private Button nextButton;
    private Button germanButton;
    private Button englishButton;

    private final Rocket rocketProxy;

    //always needed
    private final ApplicationState ps;

    RootPane(ClientDolphin clientDolphin) {
        this.clientDolphin = clientDolphin;
        ps = getApplicationState();
        rocketProxy = getRocketProxy();

        init();
    }

    @Override
    public Dolphin getDolphin() {
        return clientDolphin;
    }

    @Override
    public void initializeSelf() {
        addStylesheetFiles("/fonts/fonts.css", "/myapp/myApp.css");
        getStyleClass().add("rootPane");
    }

    @Override
    public void initializeParts() {
        headerLabel = new Label();
        headerLabel.getStyleClass().add("heading");

        idLabel = new Label();
        idField = new Label();

        nameLabel = new Label();
        nameField = new TextField();

        missionLabel = new Label();
        missionField = new TextField();

        launchLabel = new Label();
        launchField = new TextField();

        nextButton    = new Button("Next");
        germanButton  = new Button("German");
        englishButton = new Button("English");
    }

    @Override
    public void layoutParts() {
        ColumnConstraints grow = new ColumnConstraints();
        grow.setHgrow(Priority.ALWAYS);

        getColumnConstraints().setAll(new ColumnConstraints(), grow);
        setVgrow(headerLabel, Priority.ALWAYS);

        add(headerLabel    , 0, 0, 5, 1);
        add(idLabel        , 0, 1);
        add(idField        , 1, 1, 4, 1);
        add(nameLabel      , 0, 2);
        add(nameField      , 1, 2, 4, 1);
        add(missionLabel, 0, 3);
        add(missionField, 1, 3, 4, 1);
        add(launchLabel   , 0, 4);
        add(launchField, 1, 4, 4, 1);
        add(new HBox(5, nextButton, germanButton, englishButton), 0, 5, 5, 1);
    }

    @Override
    public void setupEventHandlers() {
        // all events either send a command (needs to be registered in a controller on the server side)
        // or set a value on an Attribute

        ApplicationState ps = getApplicationState();
        nextButton.setOnAction(   $ -> clientDolphin.send(RocketsCommands.LOAD_ROCKET));

        germanButton.setOnAction( $ -> ps.language.setValue(Language.GERMAN));
        englishButton.setOnAction($ -> ps.language.setValue(Language.ENGLISH));
    }

    @Override
    public void setupValueChangedListeners() {
        rocketProxy.name.dirtyProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , DIRTY_STYLE, newValue));
        rocketProxy.mission.dirtyProperty().addListener((observable, oldValue, newValue)     -> updateStyle(missionField, DIRTY_STYLE, newValue));
        rocketProxy.launch.dirtyProperty().addListener((observable, oldValue, newValue) -> updateStyle(launchField, DIRTY_STYLE, newValue));

        rocketProxy.name.validProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , INVALID_STYLE, !newValue));
        rocketProxy.mission.validProperty().addListener((observable, oldValue, newValue)     -> updateStyle(missionField, INVALID_STYLE, !newValue));
        rocketProxy.launch.validProperty().addListener((observable, oldValue, newValue) -> updateStyle(launchField, INVALID_STYLE, !newValue));

        rocketProxy.name.mandatoryProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , MANDATORY_STYLE, newValue));
        rocketProxy.mission.mandatoryProperty().addListener((observable, oldValue, newValue)     -> updateStyle(missionField, MANDATORY_STYLE, newValue));
        rocketProxy.launch.mandatoryProperty().addListener((observable, oldValue, newValue) -> updateStyle(launchField, MANDATORY_STYLE, newValue));
    }

    @Override
    public void setupBindings() {
        //setupBindings_DolphinBased();
        setupBindings_VeneerBased();
    }

    private void setupBindings_DolphinBased() {
        // you can fetch all existing PMs from the modelstore via clientDolphin
        ClientPresentationModel rocketProxyPM = clientDolphin.getAt(BasePmMixin.ROCKET_PROXY_PM_ID);

        //JFXBinder is ui toolkit agnostic. We have to use Strings
        JFXBinder.bind(RocketAtt.NAME.name())
                 .of(rocketProxyPM)
                 .using(value -> value + ", " + rocketProxyPM.getAt(RocketAtt.MISSION.name()).getValue())
                 .to("text")
                 .of(headerLabel);

        JFXBinder.bind(RocketAtt.MISSION.name())
                 .of(rocketProxyPM)
                 .using(value -> rocketProxyPM.getAt(RocketAtt.NAME.name()).getValue() + ", " + value)
                 .to("text")
                 .of(headerLabel);

        JFXBinder.bind(RocketAtt.LAUNCH.name())
                .of(rocketProxyPM)
                .using(value -> rocketProxyPM.getAt(RocketAtt.LAUNCH.name()).getValue() + ", " + value)
                .to("text")
                .of(headerLabel);

        JFXBinder.bind(RocketAtt.LAUNCH.name(), Tag.LABEL).of(rocketProxyPM).to("text").of(launchLabel);
        JFXBinder.bind(RocketAtt.LAUNCH.name()).of(rocketProxyPM).to("text").of(launchField);
        JFXBinder.bind("text").of(launchField).to(RocketAtt.LAUNCH.name()).of(rocketProxyPM);

        JFXBinder.bind(RocketAtt.NAME.name(), Tag.LABEL).of(rocketProxyPM).to("text").of(nameLabel);
        JFXBinder.bind(RocketAtt.NAME.name()).of(rocketProxyPM).to("text").of(nameField);
        JFXBinder.bind("text").of(nameField).to(RocketAtt.NAME.name()).of(rocketProxyPM);

        JFXBinder.bind(RocketAtt.MISSION.name(), Tag.LABEL).of(rocketProxyPM).to("text").of(missionLabel);
        JFXBinder.bind(RocketAtt.MISSION.name()).of(rocketProxyPM).to("text").of(missionField);
        Converter toIntConverter = value -> {
            try {
                int newValue = Integer.parseInt(value.toString());
                rocketProxyPM.getAt(RocketAtt.MISSION.name(), AdditionalTag.VALID).setValue(true);
                rocketProxyPM.getAt(RocketAtt.MISSION.name(), AdditionalTag.VALIDATION_MESSAGE).setValue("OK");

                return newValue;
            } catch (NumberFormatException e) {
                rocketProxyPM.getAt(RocketAtt.MISSION.name(), AdditionalTag.VALID).setValue(false);
                rocketProxyPM.getAt(RocketAtt.MISSION.name(), AdditionalTag.VALIDATION_MESSAGE).setValue("Not a number");
                return rocketProxyPM.getAt(RocketAtt.MISSION.name()).getValue();
            }
        };
        JFXBinder.bind("text").of(missionField).using(toIntConverter).to(RocketAtt.MISSION.name()).of(rocketProxyPM);

        JFXBinder.bind(RocketAtt.LAUNCH.name(), Tag.LABEL).of(rocketProxyPM).to("text").of(missionLabel);

        PresentationModel presentationStatePM = clientDolphin.getAt(BasePmMixin.APPLICATION_STATE_PM_ID);

        JFXBinder.bind(ApplicationStateAtt.LANGUAGE.name()).of(presentationStatePM).using(value -> value.equals(Language.GERMAN.name())).to("disable").of(germanButton);
        JFXBinder.bind(ApplicationStateAtt.LANGUAGE.name()).of(presentationStatePM).using(value -> value.equals(Language.ENGLISH.name())).to("disable").of(englishButton);
    }

    private void setupBindings_VeneerBased(){
        headerLabel.textProperty().bind(rocketProxy.name.valueProperty().concat(", ").concat(rocketProxy.mission.valueProperty()));

        idLabel.textProperty().bind(rocketProxy.id.labelProperty());
        idField.textProperty().bind(rocketProxy.id.valueProperty().asString());

        setupBinding(nameLabel   , nameField      , rocketProxy.name);
        setupBinding(missionLabel, missionField, rocketProxy.mission);
        setupBinding(launchLabel, launchField, rocketProxy.launch);


        germanButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Language.GERMAN.equals(ps.language.getValue()), ps.language.valueProperty()));
        englishButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Language.ENGLISH.equals(ps.language.getValue()), ps.language.valueProperty()));


    }

    private void setupBinding(Label label, TextField field, AttributeFX attribute) {
        setupBinding(label, attribute);

        field.textProperty().bindBidirectional(attribute.userFacingStringProperty());
        field.tooltipProperty().bind(Bindings.createObjectBinding(() -> new Tooltip(attribute.getValidationMessage()),
                                                                  attribute.validationMessageProperty()
                                                                 ));
    }

    private void setupBinding(Label label, CheckBox checkBox, BooleanAttributeFX attribute) {
        setupBinding(label, attribute);
        checkBox.selectedProperty().bindBidirectional(attribute.valueProperty());
    }

    private void setupBinding(Label label, AttributeFX attribute){
        label.textProperty().bind(Bindings.createStringBinding(() -> attribute.getLabel() + (attribute.isMandatory() ? " *" : "  "),
                                                               attribute.labelProperty(),
                                                               attribute.mandatoryProperty()));
    }

    private void updateStyle(Node node, String style, boolean value){
        if(value){
            node.getStyleClass().add(style);
        }
        else {
            node.getStyleClass().remove(style);
        }
    }
}
