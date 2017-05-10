package myapp.util.veneer;

import javafx.beans.property.StringProperty;

import org.opendolphin.core.PresentationModel;

import myapp.util.AttributeDescription;

/**
 * @author Dieter Holz
 */
public class FX_StringAttribute extends FX_Attribute {
    public FX_StringAttribute(PresentationModel pm, AttributeDescription attributeDescription) {
        super(pm, attributeDescription);
    }

    public StringProperty valueProperty() {
        return new StringAttributeAdapter(getValueAttribute());
    }

    public String getValue(){
        return valueProperty().get();
    }

    public void setValue(String value){
        valueProperty().set(value);
    }
}
