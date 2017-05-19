package myapp.util.veneer.dolphinattributeadapter;

import javafx.beans.property.SimpleObjectProperty;

import org.opendolphin.core.Attribute;

/**
 * @author Dieter Holz
 */
public class EnumAttributeAdapter<T extends Enum<T>> extends SimpleObjectProperty<T> {
    private final ObjectAttributeAdapter<T> wrapper;

    public EnumAttributeAdapter(Attribute attribute, Class<T> clazz) {
        wrapper = new ObjectAttributeAdapter<>(attribute, new EnumConverter<>(clazz));
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
