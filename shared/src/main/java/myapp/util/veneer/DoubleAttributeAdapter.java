package myapp.util.veneer;

import javafx.beans.property.SimpleDoubleProperty;

import org.opendolphin.core.Attribute;

/**
 * @author Dieter Holz
 */
public class DoubleAttributeAdapter extends SimpleDoubleProperty {
    private final ObjectAttributeAdapter<Double> wrapper;

    public DoubleAttributeAdapter(Attribute attribute) {
        wrapper = new ObjectAttributeAdapter<Double>(attribute);
        wrapper.addListener((observable, oldValue, newValue) -> fireValueChangedEvent());
    }

    @Override
    public String getName() {
        return wrapper.getName();
    }

    @Override
    public void set(double newValue) {
        wrapper.set(newValue);
    }

    @Override
    public void setValue(Number v) {
        set(v.doubleValue());
    }

    @Override
    public double get() {
        return wrapper.getValue();
    }

    @Override
    public Double getValue() {
        return get();
    }
}
