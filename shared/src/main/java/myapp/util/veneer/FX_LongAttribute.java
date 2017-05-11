package myapp.util.veneer;

import javafx.beans.property.LongProperty;

import org.opendolphin.core.PresentationModel;

import myapp.util.AttributeDescription;

/**
 * @author Dieter Holz
 */
public class FX_LongAttribute extends FX_Attribute<LongProperty, Number> {
    private static final String REGEX          = "[+-]?[\\d']+";
    private static final String FORMAT_PATTERN = "%,d";

    public FX_LongAttribute(PresentationModel pm, AttributeDescription attributeDescription) {
        super(pm, attributeDescription,
              REGEX,
              new LongAttributeAdapter(valueAttribute(pm, attributeDescription)));
    }

    @Override
    protected String format(Number value) {
        if(value == null){
            return "";
        }
        return String.format(DEFAULT_LOCALE, FORMAT_PATTERN, value.longValue());
    }

    @Override
    protected Number convertToValue(String string) {
        return Long.parseLong(string.replaceAll("'", ""));
    }

    public long getValue() {
        return valueProperty().get();
    }

    public void setValue(long value) {
        valueProperty().set(value);
    }
}
