package myapp;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import org.opendolphin.core.Dolphin;
import org.opendolphin.core.client.ClientDolphin;

import myapp.presentationmodel.SpecialPMMixin;
import myapp.presentationmodel.person.Person;
import myapp.presentationmodel.person.PersonCommands;
import myapp.presentationmodel.presentationstate.PresentationState;
import myapp.util.Language;
import util.FXBindingMixin;
import util.ViewMixin;

/**
 * Implementation of the view details, event handling, and binding.
 *
 * @author Dieter Holz
 */
class RootPane extends GridPane implements ViewMixin, FXBindingMixin, SpecialPMMixin {

    private final ClientDolphin clientDolphin;

    private Label     headerLabel;

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

    private final PresentationState ps;
    private final Person personProxy;

    RootPane(ClientDolphin clientDolphin) {
        this.clientDolphin = clientDolphin;
        ps = getPresentationState();
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
        PresentationState ps = getPresentationState();
        saveButton.setOnAction(   $ -> send(PersonCommands.SAVE));
        resetButton.setOnAction(  $ -> send(PersonCommands.RESET));
        nextButton.setOnAction(   $ -> send(PersonCommands.LOAD_SOME_PERSON));

        germanButton.setOnAction( $ -> ps.language.setValue(Language.GERMAN));
        englishButton.setOnAction($ -> ps.language.setValue(Language.ENGLISH));
    }

    @Override
    public void setupValueChangedListeners() {

        Person person = getPersonProxy();

        String dirtyStyle = "dirty";
        person.name.dirtyProperty().addListener((observable, oldValue, newValue) -> updateStyle(nameField, dirtyStyle, newValue));
        person.age.dirtyProperty().addListener((observable, oldValue, newValue) -> updateStyle(ageField, dirtyStyle, newValue));
        person.isAdult.dirtyProperty().addListener((observable, oldValue, newValue) -> updateStyle(isAdultCheckBox, dirtyStyle, newValue));

        String invalidStyle = "invalid";
        person.age.validProperty().addListener((observable, oldValue, newValue) -> updateStyle(ageField, invalidStyle, !newValue));
    }


    @Override
    public void setupBindings() {

        headerLabel.textProperty().bind(personProxy.name.valueProperty().concat(", ").concat(personProxy.age.valueProperty()));

        idLabel.textProperty().bind(personProxy.id.labelProperty());
        idField.textProperty().bind(personProxy.id.valueProperty().asString());

        nameLabel.textProperty().bind(Bindings.createStringBinding(() -> personProxy.name.getLabel() + (personProxy.name.isMandatory() ? " *" : "  "),
                                                                   personProxy.name.labelProperty(),
                                                                   personProxy.name.mandatoryProperty()));
        nameField.textProperty().bindBidirectional(personProxy.name.valueProperty());

        ageLabel.textProperty().bind(Bindings.createStringBinding(() -> personProxy.age.getLabel() + (personProxy.age.isMandatory() ? " *" : "  "),
                                                                  personProxy.age.labelProperty(),
                                                                  personProxy.age.mandatoryProperty()));
        ageField.textProperty().bindBidirectional(personProxy.age.userFacingStringProperty());

        isAdultLabel.textProperty().bind(Bindings.createStringBinding(() -> personProxy.isAdult.getLabel() + (personProxy.isAdult.isMandatory() ? " *" : "  "),
                                                                      personProxy.isAdult.labelProperty(),
                                                                      personProxy.isAdult.mandatoryProperty()));

        isAdultCheckBox.selectedProperty().bindBidirectional(personProxy.isAdult.valueProperty());

        bindDirtyStateOf(personProxy).not().to(saveButton.disableProperty());
        bindDirtyStateOf(personProxy).not().to(resetButton.disableProperty());

        germanButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Language.GERMAN.equals(ps.language.getValue()), ps.language.valueProperty()));
        englishButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Language.ENGLISH.equals(ps.language.getValue()), ps.language.valueProperty()));
    }

//    private void setupBinding() {
//        // use JavaFX binding via the 'nameProperty'
//        // this is applicable for the value of an attribute only
//        headerLabel.textProperty().bind(personProxy.firstNameProperty());
//        nameField.textProperty().bindBidirectional(personProxy.firstNameProperty());
//
//        // use FXBindingMixin for bidirectional binding via the 'firstName attribute'
//        interrelate(personProxy.firstName()).with(ageField.textProperty());
//
//        // use FXBindingMixin for binding the dirty state of a veneer
//        bindDirtyStateOf(personProxy).convertedBy(b -> !b).to(saveButton.disableProperty());
//
//        // the Open-Dolphin way of binding (there should always be a more convenient way provided by FXBindingMixin)
//        PresentationModel pm = personProxy.getPresentationModel();
//
//        Converter inv = new Converter<Boolean, Boolean>() {
//            @Override
//            public Boolean convert(Boolean value) {
//                return !value;
//            }
//        };
//
//        JFXBinder.bindInfo(Attribute.DIRTY_PROPERTY).of(pm).using(inv).to("disabled").of(resetButton);
//    }

    private void updateStyle(Node node, String style, boolean value){
        if(value){
            node.getStyleClass().add(style);
        }
        else {
            node.getStyleClass().remove(style);
        }
    }
}
