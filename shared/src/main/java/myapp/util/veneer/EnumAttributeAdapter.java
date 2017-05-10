package myapp.util.veneer;

import javafx.beans.property.SimpleObjectProperty;

import org.opendolphin.core.Attribute;

/**
 * @author Dieter Holz
 */
public class EnumAttributeAdapter<T extends Enum<T>> extends SimpleObjectProperty<T> {
    private final ObjectAttributeAdapter<T> wrapper;

    public EnumAttributeAdapter(Attribute valueAttribute, Class<T> clazz) {
        wrapper = new ObjectAttributeAdapter<T>(valueAttribute, new EnumConverter<T>(clazz));
        wrapper.addListener((observable, oldValue, newValue) -> fireValueChangedEvent());
    }

    @Override
    public String getName() {
        return wrapper.getName();
    }

    @Override
    public void set(T newValue) {
        wrapper.set(newValue);
    }

    @Override
    public T get() {
        return wrapper.getValue();
    }

    @Override
    public T getValue() {
        return get();
    }
}
