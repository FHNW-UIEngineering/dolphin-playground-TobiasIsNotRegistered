package myapp.util.veneer;

import javafx.beans.property.SimpleObjectProperty;

import org.opendolphin.core.PresentationModel;

import myapp.util.AttributeDescription;

/**
 * @author Dieter Holz
 */
public class FX_EnumAttribute<T extends Enum<T>> extends FX_Attribute {

    private final Class<T> clazz;

    public FX_EnumAttribute(PresentationModel pm, AttributeDescription attributeDescription, Class<T> clazz) {
        super(pm, attributeDescription);
        this.clazz = clazz;
    }

    public SimpleObjectProperty<T> valueProperty() {
        return new EnumAttributeAdapter(getValueAttribute(), clazz);
    }

    public void setValue(T value){
        valueProperty().setValue(value);
    }

    public T getValue(){
        return valueProperty().getValue();
    }
}
