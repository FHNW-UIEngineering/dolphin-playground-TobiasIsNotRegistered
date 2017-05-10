package myapp.util.veneer;

import javafx.beans.property.SimpleIntegerProperty;

import org.opendolphin.core.Attribute;

/**
 * @author Dieter Holz
 */
public class IntegerAttributeAdapter extends SimpleIntegerProperty {
    private final ObjectAttributeAdapter<Integer> wrapper;

    public IntegerAttributeAdapter(Attribute attribute) {
        wrapper = new ObjectAttributeAdapter<>(attribute);
        wrapper.addListener((observable, oldValue, newValue) -> fireValueChangedEvent());
    }

    @Override
    public String getName() {
        return wrapper.getName();
    }

    @Override
    public void set(int newValue) {
        wrapper.set(newValue);
    }

    @Override
    public void setValue(Number v) {
        set(v.intValue());
    }

    @Override
    public int get() {
        return wrapper.getValue();
    }

    @Override
    public Integer getValue() {
        return get();
    }
}
