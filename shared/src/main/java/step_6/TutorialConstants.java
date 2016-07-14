package step_6;

public class TutorialConstants {
    public static final String PM_PERSON = unique("modelId");
    public static final String ATT_FIRSTNAME = "firstName";
    public static final String CMD_LOG = unique("LogOnServer");

    private static String unique(String key) {
        return TutorialConstants.class.getName() + "." + key;
    }

}
