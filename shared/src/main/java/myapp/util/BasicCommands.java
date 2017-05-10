package myapp.util;


/**
 * @author Dieter Holz
 */
public interface BasicCommands {

    String INITIALIZE_BASE_PMS   = unique("initializeBasePMs");
    String INITIALIZE_CONTROLLER = unique("initializeController");
    String SET_DEFAULT_VALUES    = unique("setDefaultValues");
    String SAVE                  = unique("Save");
    String CREATE                = unique("Create");
    String DELETE                = unique("Delete");

    static String unique(String key) {
        return BasicCommands.class.getName() + "." + key;
    }
}
