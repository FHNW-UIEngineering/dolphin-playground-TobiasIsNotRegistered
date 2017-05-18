package myapp.presentationmodel.presentationstate;

import myapp.presentationmodel.PMDescription;
import myapp.util.AttributeDescription;
import myapp.util.ValueType;

/**
 * @author Dieter Holz
 */

public enum ApplicationStateAtt implements AttributeDescription {
    // todo: add all application specific attributes


    // these are almost always needed
    APPLICATION_TITLE(ValueType.STRING),
    LANGUAGE(ValueType.STRING),
    UNDO_DISABLED(ValueType.BOOLEAN),
    REDO_DISABLED(ValueType.BOOLEAN),
    CLEAN_DATA(ValueType.BOOLEAN);

    private final ValueType valueType;

    ApplicationStateAtt(ValueType type) {
        valueType = type;
    }

    public ValueType getValueType() {
        return valueType;
    }

    @Override
    public PMDescription getPMDescription() {
        return PMDescription.APPLICATION_STATE;
    }
}
