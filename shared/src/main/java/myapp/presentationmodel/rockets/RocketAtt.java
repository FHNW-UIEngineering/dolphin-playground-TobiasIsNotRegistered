package myapp.presentationmodel.rockets;

import myapp.presentationmodel.PMDescription;
import myapp.util.AttributeDescription;
import myapp.util.ValueType;

/**
 * tasd
 */
public enum RocketAtt implements AttributeDescription {
    ID(ValueType.ID),
    NAME(ValueType.STRING),
    MISSION(ValueType.STRING),
    LAUNCH(ValueType.STRING);

    private final ValueType valueType;

    RocketAtt(ValueType type) {
        valueType = type;
    }

    @Override
    public ValueType getValueType() {
        return valueType;
    }

    @Override
    public PMDescription getPMDescription() {
        return PMDescription.ROCKET;
    }
}
