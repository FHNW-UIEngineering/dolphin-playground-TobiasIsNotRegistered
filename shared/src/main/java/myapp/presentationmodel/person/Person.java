package myapp.presentationmodel.person;

import org.opendolphin.core.BasePresentationModel;

import myapp.util.veneer.FX_BooleanAttribute;
import myapp.util.veneer.FX_IntegerAttribute;
import myapp.util.veneer.FX_LongAttribute;
import myapp.util.veneer.FX_StringAttribute;
import myapp.util.veneer.PresentationModelVeneer;

/**
 * @author Dieter Holz
 */
public class Person extends PresentationModelVeneer {
    public Person(BasePresentationModel pm) {
        super(pm);
    }

    public final FX_LongAttribute    id      = new FX_LongAttribute(getPresentationModel()   , PersonAtt.ID);
    public final FX_StringAttribute  name    = new FX_StringAttribute(getPresentationModel() , PersonAtt.NAME);
    public final FX_IntegerAttribute age     = new FX_IntegerAttribute(getPresentationModel(), PersonAtt.AGE);
    public final FX_BooleanAttribute isAdult = new FX_BooleanAttribute(getPresentationModel(), PersonAtt.IS_ADULT);
}
