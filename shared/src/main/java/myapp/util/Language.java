package myapp.util;

import java.util.Locale;

/**
 * @author Dieter Holz
 */
public enum Language {
    ENGLISH(Locale.ENGLISH),
    GERMAN(new Locale("de", "CH"));

    private final Locale locale;

    Language(Locale locale){
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
