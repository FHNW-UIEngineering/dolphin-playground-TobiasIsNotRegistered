package myapp.presentationmodel.rockets;

import org.opendolphin.core.BasePresentationModel;

import myapp.util.veneer.LongAttributeFX;
import myapp.util.veneer.StringAttributeFX;
import myapp.util.veneer.PresentationModelVeneer;

/**
 * @author asd
 */
public class Rocket extends PresentationModelVeneer {
    public Rocket(BasePresentationModel pm) {
        super(pm);
    }

    public final LongAttributeFX    id      = new LongAttributeFX(getPresentationModel()   , RocketAtt.ID);
    public final StringAttributeFX  name    = new StringAttributeFX(getPresentationModel() , RocketAtt.NAME);
    public final StringAttributeFX  mission = new StringAttributeFX(getPresentationModel(), RocketAtt.MISSION);
    public final StringAttributeFX  launch = new StringAttributeFX(getPresentationModel(), RocketAtt.LAUNCH);

}
