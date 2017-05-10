package myapp.util.veneer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;

import org.opendolphin.core.Attribute;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;

import myapp.util.AdditionalTag;
import myapp.util.AttributeDescription;

/**
 * @author Dieter Holz
 */
abstract class FX_Attribute {
    private final Attribute valueAttribute;

    private final StringProperty          label;
    private final BooleanProperty         mandatory;
    private final BooleanProperty         readOnly;
    private final BooleanProperty         valid;
    private final StringProperty          validationMessage;
    private final ReadOnlyBooleanProperty dirty;
    private final StringProperty          userFacingString;

    public FX_Attribute(PresentationModel pm, AttributeDescription attributeDescription) {
        this.valueAttribute = pm.getAt(attributeDescription.name());

        label             = new StringAttributeAdapter(pm.getAt(attributeDescription.name(), Tag.LABEL));
        mandatory         = new BooleanAttributeAdapter(pm.getAt(attributeDescription.name(), Tag.MANDATORY));
        readOnly          = new BooleanAttributeAdapter(pm.getAt(attributeDescription.name(), AdditionalTag.READ_ONLY));
        valid             = new BooleanAttributeAdapter(pm.getAt(attributeDescription.name(), AdditionalTag.VALID));
        validationMessage = new StringAttributeAdapter(pm.getAt(attributeDescription.name(), AdditionalTag.VALIDATION_MESSAGE));
        dirty             = new DirtyPropertyAdapter(pm.getAt(attributeDescription.name()));
        userFacingString  = new StringAttributeAdapter(pm.getAt(attributeDescription.name(), AdditionalTag.USER_FACING_STRING));
    }

    protected Attribute getValueAttribute() {
        return valueAttribute;
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public boolean isMandatory() {
        return mandatory.get();
    }

    public BooleanProperty mandatoryProperty() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory.set(mandatory);
    }

    public boolean isReadOnly() {
        return readOnly.get();
    }

    public BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly.set(readOnly);
    }

    public boolean isValid() {
        return valid.get();
    }

    public BooleanProperty validProperty() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid.set(valid);
    }

    public String getValidationMessage() {
        return validationMessage.get();
    }

    public StringProperty validationMessageProperty() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage.set(validationMessage);
    }

    public boolean isDirty() {
        return dirty.get();
    }

    public ReadOnlyBooleanProperty dirtyProperty() {
        return dirty;
    }

    public String getUserFacingString() {
        return userFacingString.get();
    }

    public StringProperty userFacingStringProperty() {
        return userFacingString;
    }

    public void setUserFacingString(String userFacingString) {
        this.userFacingString.set(userFacingString);
    }
}
