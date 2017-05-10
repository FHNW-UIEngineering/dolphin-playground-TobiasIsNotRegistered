package myapp.util;

/**
 * @author Dieter Holz
 */
public enum ValueType {
    ID,
    STRING,
    FLOAT("%,.2f"),
    DOUBLE("%,.2f"),
    INT("%,.2d"),
    LONG("%,.2d"),
    BOOLEAN,
    FOREIGN_KEY("%.0d"),
    YEAR("%.0d"),
    ISO2_COUNTRY_CODE;

    private String format;

    ValueType(){
        this("s");
    }

    ValueType(String format){
        this.format = format;
    }

    public static boolean isNumber(ValueType type){
        return ID.equals(type) || DOUBLE.equals(type) || FLOAT.equals(type) || INT.equals(type) || LONG.equals(type) || YEAR.equals(type);
    }

    public static boolean isWholeNumber(ValueType type){
        return ID.equals(type) || INT.equals(type) || LONG.equals(type) || YEAR.equals(type);
    }

    public String format() {
        return format;
    }
}
