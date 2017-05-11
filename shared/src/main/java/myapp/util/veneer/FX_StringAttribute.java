package myapp.util.veneer;

import javafx.beans.property.StringProperty;

import org.opendolphin.core.PresentationModel;

import myapp.util.AttributeDescription;

/**
 * @author Dieter Holz
 */
public class FX_StringAttribute extends FX_Attribute<StringProperty, String> {
    private static final String REGEX = ".*";

    public FX_StringAttribute(PresentationModel pm, AttributeDescription attributeDescription) {
        super(pm, attributeDescription,
              REGEX,
              new StringAttributeAdapter(valueAttribute(pm, attributeDescription)));
    }

    @Override
    protected String format(String value) {
        return value;
    }

    @Override
    protected String convertToValue(String string) {
        return string;
    }

    public String getValue(){
        return valueProperty().get();
    }

    public void setValue(String value){
        valueProperty().set(value);
    }
}
