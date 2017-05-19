package myapp.util;


/**
 * @author Dieter Holz
 */
public interface BasicCommands {

    String INITIALIZE_BASE_PMS   = unique("initializeBasePMs");
    String INITIALIZE_CONTROLLER = unique("initializeController");

    static String unique(String key) {
        return BasicCommands.class.getName() + "." + key;
    }
}
