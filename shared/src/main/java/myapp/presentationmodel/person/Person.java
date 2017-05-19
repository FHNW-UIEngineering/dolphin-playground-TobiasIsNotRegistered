package myapp.presentationmodel.person;

import org.opendolphin.core.BasePresentationModel;

import myapp.util.veneer.BooleanAttributeFX;
import myapp.util.veneer.IntegerAttributeFX;
import myapp.util.veneer.LongAttributeFX;
import myapp.util.veneer.StringAttributeFX;
import myapp.util.veneer.PresentationModelVeneer;

/**
 * @author Dieter Holz
 */
public class Person extends PresentationModelVeneer {
    public Person(BasePresentationModel pm) {
        super(pm);
    }

    public final LongAttributeFX    id      = new LongAttributeFX(getPresentationModel()   , PersonAtt.ID);
    public final StringAttributeFX  name    = new StringAttributeFX(getPresentationModel() , PersonAtt.NAME);
    public final IntegerAttributeFX age     = new IntegerAttributeFX(getPresentationModel(), PersonAtt.AGE);
    public final BooleanAttributeFX isAdult = new BooleanAttributeFX(getPresentationModel(), PersonAtt.IS_ADULT);
}
