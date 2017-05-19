package myapp.util.veneer;

import javafx.beans.property.BooleanProperty;

import org.opendolphin.core.PresentationModel;

import myapp.util.AttributeDescription;
import myapp.util.veneer.dolphinattributeadapter.BooleanAttributeAdapter;

/**
 * @author Dieter Holz
 */
public class BooleanAttributeFX extends AttributeFX<BooleanProperty, Boolean> {
    private static final String TRUE  = "true";
    private static final String FALSE = "false";
    private static final String REGEX = "((?i)" + TRUE + "){1}|((?i)" + FALSE + "){1}";

    public BooleanAttributeFX(PresentationModel pm, AttributeDescription attributeDescription) {
        super(pm, attributeDescription,
              REGEX,
              new BooleanAttributeAdapter(valueAttribute(pm, attributeDescription)));
    }

    @Override
    protected String format(Boolean value) {
        if(value == null){
            return "";
        }
        return value ? TRUE : FALSE;
    }

    @Override
    protected Boolean convertToValue(String string) {
        return TRUE.equals(string);
    }

    public boolean getValue(){
        return valueProperty().get();
    }

    public void setValue(boolean value){
        valueProperty().set(value);
    }

}
