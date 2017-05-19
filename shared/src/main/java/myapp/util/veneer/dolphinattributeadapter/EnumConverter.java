package myapp.util.veneer.dolphinattributeadapter;

/**
 * @author Dieter Holz
 */
public class EnumConverter<T extends Enum<T>> implements AttributeValueConverter<T> {
    private final Class<T> enumClass;

    public EnumConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Object toAttributeValue(T value) {
        return value.name();
    }

    @Override
    public T toPropertyValue(Object value) {
        return (value == null || ((String)value).isEmpty()) ?
               null :
               Enum.valueOf(enumClass, (String) value);
    }
}
