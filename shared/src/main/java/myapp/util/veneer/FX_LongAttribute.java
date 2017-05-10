package myapp.util.veneer;

import javafx.beans.property.LongProperty;

import org.opendolphin.core.PresentationModel;

import myapp.util.AttributeDescription;

/**
 * @author Dieter Holz
 */
public class FX_LongAttribute extends FX_Attribute {
    public FX_LongAttribute(PresentationModel pm, AttributeDescription attributeDescription) {
        super(pm, attributeDescription);
    }

    public LongProperty valueProperty() {
        return new LongAttributeAdapter(getValueAttribute());
    }

    public long getValue(){
        return valueProperty().get();
    }

    public void setValue(long value){
        valueProperty().set(value);
    }
}
