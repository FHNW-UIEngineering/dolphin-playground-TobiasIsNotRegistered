package myapp.presentationmodel.person;

import myapp.presentationmodel.PMDescription;
import myapp.util.AttributeDescription;
import myapp.util.ValueType;

/**
 * @author Dieter Holz
 */
public enum  PersonAtt implements AttributeDescription{
    ID(ValueType.ID),
    NAME(ValueType.STRING),
    AGE(ValueType.INT),
    IS_ADULT(ValueType.BOOLEAN);

    private final ValueType valueType;

    PersonAtt(ValueType type) {
        valueType = type;
    }

    public ValueType getValueType() {
        return valueType;
    }

    @Override
    public PMDescription getPMDescription() {
        return PMDescription.PERSON;
    }
}
