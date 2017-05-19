package myapp.util.veneer.dolphinattributeadapter;

import java.lang.ref.WeakReference;

import java.util.Objects;

import javafx.beans.property.SimpleObjectProperty;

import org.opendolphin.core.Attribute;
import org.opendolphin.core.PresentationModel;

/**
 * JavaFX property wrapper around the value of a dolphin attribute.
 * <p>
 * Changing the dirty state or any other meta-info of the attribute will not trigger a valueChangeEvent.
 *
 * @author Dieter Holz
 */
public class ObjectAttributeAdapter<T> extends SimpleObjectProperty<T> {
    private final WeakReference<Attribute> attributeRef;
    private final String                   name;

    private final AttributeValueConverter<T> converter;

    public ObjectAttributeAdapter(Attribute attribute, AttributeValueConverter<T> converter) {
        this.attributeRef = new WeakReference<>(attribute);
        this.converter = converter;
        // we cache the attribute's propertyName as the property's name
        // because the value does not change and we want to avoid
        // dealing with null values from WR
        this.name = attribute.getPropertyName();
        attribute.addPropertyChangeListener(Attribute.VALUE, propertyChangeEvent -> fireValueChangedEvent());
    }

    public ObjectAttributeAdapter(Attribute attribute) {
        this(attribute, new AttributeValueConverter<T>() {
            @Override
            public Object toAttributeValue(T value) {
                return value;
            }

            @Override
            public T toPropertyValue(Object value) {
                return (T) value;
            }
        });
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void set(T value) {
        Attribute attribute = attributeRef.get();
        if (attribute != null && !Objects.equals(attribute.getValue(), value)) {
//            if (Platform.isFxApplicationThread()) {
//                Platform.runLater(() -> attribute.setValue(converter.toAttributeValue(value)));
//            } else {
                attribute.setValue(converter.toAttributeValue(value));
//            }
        }
    }

    @Override
    public void setValue(T v) {
        set(v);
    }

    @Override
    public T get() {
        Attribute attribute = attributeRef.get();
        return attribute != null ? converter.toPropertyValue(attribute.getValue()) : null;
    }

    public Attribute getAttribute(){
        return attributeRef.get();
    }

    public PresentationModel getPresentationModel(){
        return getAttribute().getPresentationModel();
    }
}
