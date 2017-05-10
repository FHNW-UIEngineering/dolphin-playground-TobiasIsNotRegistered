package myapp.util.veneer;

import org.opendolphin.core.PresentationModel;

import myapp.util.AttributeDescription;

/**
 * @author Dieter Holz
 */
public class FX_BooleanAttribute extends FX_Attribute {
    public FX_BooleanAttribute(PresentationModel pm, AttributeDescription attributeDescription) {
        super(pm, attributeDescription);
    }

    public BooleanAttributeAdapter valueProperty() {
        return new BooleanAttributeAdapter(getValueAttribute());
    }

    public boolean getValue(){
        return valueProperty().get();
    }

    public void setValue(boolean value){
        valueProperty().set(value);
    }

}
