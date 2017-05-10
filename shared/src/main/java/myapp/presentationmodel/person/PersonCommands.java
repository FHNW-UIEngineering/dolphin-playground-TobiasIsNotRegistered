package myapp.presentationmodel.person;

/**
 * @author Dieter Holz
 */
public interface PersonCommands {
	String CMD_CALL_MY_SERVICE = unique("CallMyService");

	static String unique(String key) {
		return PersonCommands.class.getName() + "." + key;
	}
}
