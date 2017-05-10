package myapp.util;

import org.opendolphin.core.Tag;

/**
 * @author Dieter Holz
 */
public interface AdditionalTag {
    Tag READ_ONLY          = Tag.tagFor.get("readOnly");
    Tag VALID              = Tag.tagFor.get("valid");
    Tag VALIDATION_MESSAGE = Tag.tagFor.get("validationMessage");
    Tag USER_FACING_STRING = Tag.tagFor.get("userFacingString");
}
