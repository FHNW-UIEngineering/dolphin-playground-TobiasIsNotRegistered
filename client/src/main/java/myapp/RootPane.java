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

import org.opendolphin.binding.Converter;
import org.opendolphin.binding.JFXBinder;
import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;

import myapp.presentationmodel.BasePmMixin;
import myapp.presentationmodel.person.Person;
import myapp.presentationmodel.person.PersonAtt;
import myapp.presentationmodel.person.PersonCommands;
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

    private Label     ageLabel;
    private TextField ageField;

    private Label    isAdultLabel;
    private CheckBox isAdultCheckBox;

    private Button saveButton;
    private Button resetButton;
    private Button nextButton;
    private Button germanButton;
    private Button englishButton;

    private final Person personProxy;

    //always needed
    private final ApplicationState ps;

    RootPane(ClientDolphin clientDolphin) {
        this.clientDolphin = clientDolphin;
        ps = getApplicationState();
        personProxy = getPersonProxy();

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

        ageLabel = new Label();
        ageField = new TextField();

        isAdultLabel    = new Label();
        isAdultCheckBox = new CheckBox();

        saveButton    = new Button("Save");
        resetButton   = new Button("Reset");
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
        add(ageLabel       , 0, 3);
        add(ageField       , 1, 3, 4, 1);
        add(isAdultLabel   , 0, 4);
        add(isAdultCheckBox, 1, 4, 4, 1);
        add(new HBox(5, saveButton, resetButton, nextButton, germanButton, englishButton), 0, 5, 5, 1);
    }

    @Override
    public void setupEventHandlers() {
        // all events either send a command (needs to be registered in a controller on the server side)
        // or set a value on an Attribute

        ApplicationState ps = getApplicationState();
        saveButton.setOnAction(   $ -> clientDolphin.send(PersonCommands.SAVE));
        resetButton.setOnAction(  $ -> clientDolphin.send(PersonCommands.RESET));
        nextButton.setOnAction(   $ -> clientDolphin.send(PersonCommands.LOAD_SOME_PERSON));

        germanButton.setOnAction( $ -> ps.language.setValue(Language.GERMAN));
        englishButton.setOnAction($ -> ps.language.setValue(Language.ENGLISH));
    }

    @Override
    public void setupValueChangedListeners() {
        personProxy.name.dirtyProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , DIRTY_STYLE, newValue));
        personProxy.age.dirtyProperty().addListener((observable, oldValue, newValue)     -> updateStyle(ageField       , DIRTY_STYLE, newValue));
        personProxy.isAdult.dirtyProperty().addListener((observable, oldValue, newValue) -> updateStyle(isAdultCheckBox, DIRTY_STYLE, newValue));

        personProxy.name.validProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , INVALID_STYLE, !newValue));
        personProxy.age.validProperty().addListener((observable, oldValue, newValue)     -> updateStyle(ageField       , INVALID_STYLE, !newValue));
        personProxy.isAdult.validProperty().addListener((observable, oldValue, newValue) -> updateStyle(isAdultCheckBox, INVALID_STYLE, !newValue));

        personProxy.name.mandatoryProperty().addListener((observable, oldValue, newValue)    -> updateStyle(nameField      , MANDATORY_STYLE, newValue));
        personProxy.age.mandatoryProperty().addListener((observable, oldValue, newValue)     -> updateStyle(ageField       , MANDATORY_STYLE, newValue));
        personProxy.isAdult.mandatoryProperty().addListener((observable, oldValue, newValue) -> updateStyle(isAdultCheckBox, MANDATORY_STYLE, newValue));
    }

    @Override
    public void setupBindings() {
        //setupBindings_DolphinBased();
        setupBindings_VeneerBased();
    }

    private void setupBindings_DolphinBased() {
        // you can fetch all existing PMs from the modelstore via clientDolphin
        ClientPresentationModel personProxyPM = clientDolphin.getAt(BasePmMixin.PERSON_PROXY_PM_ID);

        //JFXBinder is ui toolkit agnostic. We have to use Strings
        JFXBinder.bind(PersonAtt.NAME.name())
                 .of(personProxyPM)
                 .using(value -> value + ", " + personProxyPM.getAt(PersonAtt.AGE.name()).getValue())
                 .to("text")
                 .of(headerLabel);

        JFXBinder.bind(PersonAtt.AGE.name())
                 .of(personProxyPM)
                 .using(value -> personProxyPM.getAt(PersonAtt.NAME.name()).getValue() + ", " + value)
                 .to("text")
                 .of(headerLabel);

        JFXBinder.bind(PersonAtt.NAME.name(), Tag.LABEL).of(personProxyPM).to("text").of(nameLabel);
        JFXBinder.bind(PersonAtt.NAME.name()).of(personProxyPM).to("text").of(nameField);
        JFXBinder.bind("text").of(nameField).to(PersonAtt.NAME.name()).of(personProxyPM);

        JFXBinder.bind(PersonAtt.AGE.name(), Tag.LABEL).of(personProxyPM).to("text").of(ageLabel);
        JFXBinder.bind(PersonAtt.AGE.name()).of(personProxyPM).to("text").of(ageField);
        Converter toIntConverter = value -> {
            try {
                int newValue = Integer.parseInt(value.toString());
                personProxyPM.getAt(PersonAtt.AGE.name(), AdditionalTag.VALID).setValue(true);
                personProxyPM.getAt(PersonAtt.AGE.name(), AdditionalTag.VALIDATION_MESSAGE).setValue("OK");

                return newValue;
            } catch (NumberFormatException e) {
                personProxyPM.getAt(PersonAtt.AGE.name(), AdditionalTag.VALID).setValue(false);
                personProxyPM.getAt(PersonAtt.AGE.name(), AdditionalTag.VALIDATION_MESSAGE).setValue("Not a number");
                return personProxyPM.getAt(PersonAtt.AGE.name()).getValue();
            }
        };
        JFXBinder.bind("text").of(ageField).using(toIntConverter).to(PersonAtt.AGE.name()).of(personProxyPM);

        JFXBinder.bind(PersonAtt.IS_ADULT.name(), Tag.LABEL).of(personProxyPM).to("text").of(isAdultLabel);
        JFXBinder.bind(PersonAtt.IS_ADULT.name()).of(personProxyPM).to("selected").of(isAdultCheckBox);
        JFXBinder.bind("selected").of(isAdultCheckBox).to(PersonAtt.IS_ADULT.name()).of(personProxyPM);

        Converter not = value -> !(boolean) value;
        JFXBinder.bindInfo(Attribute.DIRTY_PROPERTY).of(personProxyPM).using(not).to("disable").of(saveButton);
        JFXBinder.bindInfo(Attribute.DIRTY_PROPERTY).of(personProxyPM).using(not).to("disable").of(resetButton);

        PresentationModel presentationStatePM = clientDolphin.getAt(BasePmMixin.APPLICATION_STATE_PM_ID);

        JFXBinder.bind(ApplicationStateAtt.LANGUAGE.name()).of(presentationStatePM).using(value -> value.equals(Language.GERMAN.name())).to("disable").of(germanButton);
        JFXBinder.bind(ApplicationStateAtt.LANGUAGE.name()).of(presentationStatePM).using(value -> value.equals(Language.ENGLISH.name())).to("disable").of(englishButton);
    }

    private void setupBindings_VeneerBased(){
        headerLabel.textProperty().bind(personProxy.name.valueProperty().concat(", ").concat(personProxy.age.valueProperty()));

        idLabel.textProperty().bind(personProxy.id.labelProperty());
        idField.textProperty().bind(personProxy.id.valueProperty().asString());

        setupBinding(nameLabel   , nameField      , personProxy.name);
        setupBinding(ageLabel    , ageField       , personProxy.age);
        setupBinding(isAdultLabel, isAdultCheckBox, personProxy.isAdult);

        germanButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Language.GERMAN.equals(ps.language.getValue()), ps.language.valueProperty()));
        englishButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Language.ENGLISH.equals(ps.language.getValue()), ps.language.valueProperty()));

        saveButton.disableProperty().bind(personProxy.dirtyProperty().not());
        resetButton.disableProperty().bind(personProxy.dirtyProperty().not());
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
