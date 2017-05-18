package myapp.util;

/**
 * @author Dieter Holz
 */
public enum ValueType {
    //todo: add your application specific value types
    ID,
    STRING,
    FLOAT("%,.2f"),
    DOUBLE("%,.2f"),
    INT("%,d"),
    LONG("%,2d"),
    BOOLEAN,
    FOREIGN_KEY("%.0d"),
    YEAR("%.0d"),
    ISO2_COUNTRY_CODE;

    private String formatPattern;

    ValueType(){
        this("s");
    }

    ValueType(String formatPattern){
        this.formatPattern = formatPattern;
    }

    public static boolean isNumber(ValueType type){
        return ID.equals(type) || DOUBLE.equals(type) || FLOAT.equals(type) || INT.equals(type) || LONG.equals(type) || YEAR.equals(type);
    }

    public static boolean isWholeNumber(ValueType type){
        return ID.equals(type) || INT.equals(type) || LONG.equals(type) || YEAR.equals(type);
    }

    public String formatPattern() {
        return formatPattern;
    }
}
