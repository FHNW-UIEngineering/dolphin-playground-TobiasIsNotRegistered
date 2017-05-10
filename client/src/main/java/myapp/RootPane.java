package myapp;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.opendolphin.core.Dolphin;
import org.opendolphin.core.client.ClientDolphin;

import myapp.presentationmodel.SpecialPMMixin;
import myapp.presentationmodel.person.Person;
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
    private Label     nameLabel;
    private TextField nameField;

    private Label     ageLabel;
    private TextField ageField;

    private Label    isAdultLabel;
    private CheckBox isAdultCheckBox;

    private Button saveButton;
    private Button resetButton;

    RootPane(ClientDolphin clientDolphin) {
        this.clientDolphin = clientDolphin;

        init();
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

        nameLabel = new Label();
        nameField = new TextField();

        ageLabel = new Label();
        ageField = new TextField();

        isAdultLabel = new Label();
        isAdultCheckBox = new CheckBox();

        saveButton = new Button("save");
        resetButton = new Button("reset");
    }

    @Override
    public void layoutParts() {
        ColumnConstraints grow = new ColumnConstraints();
        grow.setHgrow(Priority.ALWAYS);

        getColumnConstraints().setAll(new ColumnConstraints(), grow);
        setVgrow(headerLabel, Priority.ALWAYS);

        add(headerLabel, 0, 0, 2,1);
        addRow(1, nameLabel, nameField);
        addRow(2, ageLabel, ageField);
        addRow(3, isAdultLabel, isAdultCheckBox);
        addRow(4, saveButton, resetButton);
    }

    @Override
    public void setupValueChangedListeners() {
//        getPersonProxyPM().addPropertyChangeListener(PresentationModel.DIRTY_PROPERTY, evt -> {
//            saveButton.setDisable(!(Boolean) evt.getNewValue());
//            resetButton.setDisable(!(Boolean) evt.getNewValue());
//        });
    }

    @Override
    public void setupBindings() {
        Person person = getPersonProxy();

        headerLabel.textProperty().bind(person.name.valueProperty().concat(", ").concat(person.age.valueProperty()));

        nameLabel.textProperty().bind(Bindings.createStringBinding(() -> person.name.getLabel() + (person.name.isMandatory() ? " *" : "  "),
                                                                   person.name.labelProperty(),
                                                                   person.name.mandatoryProperty()));
        nameField.textProperty().bindBidirectional(person.name.valueProperty());


        ageLabel.textProperty().bind(Bindings.createStringBinding(() -> person.age.getLabel() + (person.age.isMandatory() ? " *" : "  "),
                                                                   person.age.labelProperty(),
                                                                   person.age.mandatoryProperty()));
        ageField.textProperty().bindBidirectional(person.age.userFacingStringProperty());

        isAdultLabel.textProperty().bind(Bindings.createStringBinding(() -> person.isAdult.getLabel() + (person.isAdult.isMandatory() ? " *" : "  "),
                                                                   person.isAdult.labelProperty(),
                                                                   person.isAdult.mandatoryProperty()));

        isAdultCheckBox.selectedProperty().bindBidirectional(person.isAdult.valueProperty());

        bindDirtyStateOf(person).not().to(saveButton.disableProperty());
        bindDirtyStateOf(person).not().to(resetButton.disableProperty());
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

    @Override
    public Dolphin getDolphin() {
        return clientDolphin;
    }

}
