package myapp.presentationmodel.presentationstate;

import org.opendolphin.core.BasePresentationModel;

import myapp.util.veneer.BooleanAttributeFX;
import myapp.util.veneer.EnumAttributeFX;
import myapp.util.Language;
import myapp.util.veneer.StringAttributeFX;
import myapp.util.veneer.PresentationModelVeneer;

/**
 * @author Dieter Holz
 */
public class ApplicationState extends PresentationModelVeneer {

    public ApplicationState(BasePresentationModel pm) {
        super(pm);
    }

    public final StringAttributeFX         applicationTitle = new StringAttributeFX(getPresentationModel() , ApplicationStateAtt.APPLICATION_TITLE);
    public final EnumAttributeFX<Language> language         = new EnumAttributeFX<>(getPresentationModel()   , ApplicationStateAtt.LANGUAGE, Language.class);
    public final BooleanAttributeFX        cleanData        = new BooleanAttributeFX(getPresentationModel(), ApplicationStateAtt.CLEAN_DATA);
}
