package myapp;

/**
 * @author Dieter Holz
 */
public interface MyAppCommands {
	String CMD_CALL_MY_SERVICE = unique("CallMyService");

	static String unique(String key) {
		return MyAppCommands.class.getName() + "." + key;
	}
}
