package myapp.presentationmodel.person;

/**
 * @author Dieter Holz
 *
 * todo: specify all commands you need in your corresponding controller
 */
public interface PersonCommands {
	String LOAD_SOME_PERSON = unique("loadSomePerson");
	String SAVE             = unique("save");
	String RESET            = unique("reset");

	static String unique(String key) {
		return PersonCommands.class.getName() + "." + key;
	}

}
