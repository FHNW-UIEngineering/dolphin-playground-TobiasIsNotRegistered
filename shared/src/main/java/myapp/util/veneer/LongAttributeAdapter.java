package myapp.util.veneer;

import javafx.beans.property.SimpleLongProperty;

import org.opendolphin.core.Attribute;

/**
 * @author Dieter Holz
 */
public class LongAttributeAdapter extends SimpleLongProperty {
    private final ObjectAttributeAdapter<Long> wrapper;

    public LongAttributeAdapter(Attribute attribute) {
        wrapper = new ObjectAttributeAdapter<>(attribute);
        wrapper.addListener((observable, oldValue, newValue) -> fireValueChangedEvent());
    }

    @Override
    public String getName() {
        return wrapper.getName();
    }

    @Override
    public void set(long newValue) {
        wrapper.set(newValue);
    }

    @Override
    public void setValue(Number v) {
        set(v.longValue());
    }

    @Override
    public long get() {
        return wrapper.getValue();
    }

    @Override
    public Long getValue() {
        return get();
    }
}
