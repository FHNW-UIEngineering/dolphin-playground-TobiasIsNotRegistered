package util;

import java.lang.ref.WeakReference;

import javafx.beans.property.SimpleObjectProperty;

import org.opendolphin.core.Attribute;

/**
 * JavaFX property wrapper around an the value of an attribute.
 *
 * Changing the dirty state or any other meta-info of the attribute will not trigger a valueChangeEvent.
 *
 * More or less a copy of ClientAttributeWrapper but can be used for Attributes.
 *
 * @author Dieter Holz
 */
public class AttributeWrapper<T> extends SimpleObjectProperty<T> {
    private final WeakReference<Attribute> attributeRef;
    private final String                   name;

    public AttributeWrapper(Attribute attribute) {
        this.attributeRef = new WeakReference<>(attribute);
        // we cache the attribute's propertyName as the property's name
        // because the value does not change and we want to avoid
        // dealing with null values from WR
        this.name = attribute.getPropertyName();
        attribute.addPropertyChangeListener(Attribute.VALUE, propertyChangeEvent -> fireValueChangedEvent());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void set(T value) {
        Attribute attribute = attributeRef.get();
        if (attribute != null) {
            attribute.setValue(value);
        }
    }

    @Override
    public T get() {
        Attribute attribute = attributeRef.get();
        return attribute != null ? (T)attribute.getValue() : null;
    }
}
