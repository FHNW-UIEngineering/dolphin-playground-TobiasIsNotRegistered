package myapp.presentationmodel.rockets;

/**
 * @author Dagobert Duck :)
 *
 * todo: specify all commands you need in your corresponding controller
 */
public interface RocketsCommands {
    String LOAD_ROCKET = unique("loadRockets");


    //returns unique name by adding the classname in front of the key
    static String unique(String key) {
        return RocketsCommands.class.getName() + "." + key;
    }

}
