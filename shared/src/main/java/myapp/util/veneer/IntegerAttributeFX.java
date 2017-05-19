package myapp.util.veneer;

import javafx.beans.property.IntegerProperty;

import org.opendolphin.core.PresentationModel;

import myapp.util.AttributeDescription;
import myapp.util.veneer.dolphinattributeadapter.IntegerAttributeAdapter;

/**
 * @author Dieter Holz
 */
public class IntegerAttributeFX extends AttributeFX<IntegerProperty, Number> {
    private static final String REGEX          = "[+-]?[\\d']{1,14}";
    private static final String FORMAT_PATTERN = "%,d";

    public IntegerAttributeFX(PresentationModel pm, AttributeDescription attributeDescription) {
        super(pm, attributeDescription,
              REGEX,
              new IntegerAttributeAdapter(valueAttribute(pm, attributeDescription)));
    }

    @Override
    protected String format(Number value) {
        if(value == null){
            return "";
        }
        return String.format(DEFAULT_LOCALE, FORMAT_PATTERN, value.intValue());
    }

    @Override
    protected Integer convertToValue(String string) {
        return Integer.parseInt(string.replaceAll("'", ""));
    }

    public int getValue() {
        return valueProperty().get();
    }

    public void setValue(int value) {
        valueProperty().set(value);
    }
}
