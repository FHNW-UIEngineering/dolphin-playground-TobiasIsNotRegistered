package myapp.util.veneer;

import java.util.Locale;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;

import org.opendolphin.core.PresentationModel;

import myapp.util.AttributeDescription;

/**
 * @author Dieter Holz
 */
public class FX_IntegerAttribute extends FX_Attribute {
    private static final Pattern INTEGER_PATTERN = Pattern.compile("[+-]?[\\d']+");
    private static final Locale  CH              = new Locale("de", "CH");
    private static final String  FORMAT_PATTERN  = "%,d";

    public FX_IntegerAttribute(PresentationModel pm, AttributeDescription attributeDescription) {
        super(pm, attributeDescription);

        setUserFacingString(format(getValue()));

        userFacingStringProperty().addListener((observable, oldValue, newValue) -> {
            if(Platform.isFxApplicationThread()){
                Platform.runLater(() -> reactToUserInput(newValue));
            }
            else {
                reactToUserInput(newValue);
            }
        });

        valueProperty().addListener((observable, oldValue, newValue) -> {
            setUserFacingString(format(newValue.intValue()));
        });

    }

    private String format(int value){
       return String.format(CH, FORMAT_PATTERN, value);
    }

    private void reactToUserInput(String userInput){
        if(isMandatory() && userInput == null || userInput.isEmpty()){
            setValid(false);
            setValidationMessage("mandatory");
            return;
        }

        if(!INTEGER_PATTERN.matcher(userInput).matches()){
            setValid(false);
            setValidationMessage("not a number");
        }
        else {
            setValid(true);
            setValidationMessage(null);
            setValue(Integer.parseInt(userInput.replaceAll("'", "")));
        }
    }

    public IntegerProperty valueProperty() {
        return new IntegerAttributeAdapter(getValueAttribute());
    }

    public int getValue(){
        return valueProperty().get();
    }

    public void setValue(int value){
        valueProperty().set(value);
    }
}
