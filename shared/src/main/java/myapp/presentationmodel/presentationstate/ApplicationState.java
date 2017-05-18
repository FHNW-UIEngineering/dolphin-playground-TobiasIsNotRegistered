package myapp.presentationmodel.presentationstate;

import org.opendolphin.core.BasePresentationModel;

import myapp.util.veneer.FX_BooleanAttribute;
import myapp.util.veneer.FX_EnumAttribute;
import myapp.util.Language;
import myapp.util.veneer.FX_StringAttribute;
import myapp.util.veneer.PresentationModelVeneer;

/**
 * @author Dieter Holz
 */
public class ApplicationState extends PresentationModelVeneer {

    public ApplicationState(BasePresentationModel pm) {
        super(pm);
    }

    public final FX_StringAttribute         applicationTitle = new FX_StringAttribute(getPresentationModel() , ApplicationStateAtt.APPLICATION_TITLE);
    public final FX_EnumAttribute<Language> language         = new FX_EnumAttribute(getPresentationModel()   , ApplicationStateAtt.LANGUAGE, Language.class);
    public final FX_BooleanAttribute        cleanData        = new FX_BooleanAttribute(getPresentationModel(), ApplicationStateAtt.CLEAN_DATA);
    public final FX_BooleanAttribute        redoDisabled     = new FX_BooleanAttribute(getPresentationModel(), ApplicationStateAtt.REDO_DISABLED);
    public final FX_BooleanAttribute        undoDisabled     = new FX_BooleanAttribute(getPresentationModel(), ApplicationStateAtt.UNDO_DISABLED);

}
