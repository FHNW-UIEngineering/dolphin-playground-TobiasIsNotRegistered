package myapp.util.veneer.dolphinattributeadapter;

import java.lang.ref.WeakReference;

import javafx.beans.property.ReadOnlyBooleanPropertyBase;

import org.opendolphin.core.Attribute;

/**
 * @author Dieter Holz
 */
public class DirtyPropertyAdapter extends ReadOnlyBooleanPropertyBase {
    private final WeakReference<Attribute> attributeRef;
    private final String                   name;

    public DirtyPropertyAdapter(Attribute attribute) {
        this.attributeRef = new WeakReference<>(attribute);
        this.name = attribute.getPropertyName();
        attribute.addPropertyChangeListener(Attribute.DIRTY_PROPERTY, propertyChangeEvent -> fireValueChangedEvent());
    }

    @Override
    public Object getBean() {
        return attributeRef.get();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean get() {
        Attribute attribute = attributeRef.get();
        return attribute != null ? attribute.isDirty() : false;
    }

    @Override
    public Boolean getValue() {
        return get();
    }
}
