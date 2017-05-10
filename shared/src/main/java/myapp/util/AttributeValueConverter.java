package myapp.util;

/**
 * @author Dieter Holz
 */
public interface AttributeValueConverter<T> {
    Object toAttributeValue(T value);

    T toPropertyValue(Object value);
}
