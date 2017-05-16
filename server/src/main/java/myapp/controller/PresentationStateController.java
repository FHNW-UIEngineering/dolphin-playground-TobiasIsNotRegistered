package myapp.controller;

import org.opendolphin.core.server.ServerPresentationModel;

import myapp.presentationmodel.PMDescription;
import myapp.presentationmodel.SpecialPMMixin;
import myapp.presentationmodel.presentationstate.PresentationState;
import myapp.util.Controller;
import myapp.util.Language;

/**
 * @author Dieter Holz
 */
public class PresentationStateController extends Controller implements SpecialPMMixin{

    private PresentationState ps;

    @Override
    protected void initializeBasePMs() {
        ServerPresentationModel presentationStatePM = createProxyPM(PMDescription.PRESENTATION_STATE, SpecialPMMixin.PRESENTATION_STATE_ID);

        ps = new PresentationState(presentationStatePM);
    }

    @Override
    protected void setDefaultValues() {
        ps.language.setValue(Language.ENGLISH);
        ps.cleanData.setValue(true);
        ps.undoDisabled.setValue(true);
        ps.redoDisabled.setValue(true);
    }

    @Override
    protected void setupValueChangedListener() {
        ps.language.valueProperty()
                   .addListener(($, $$, newValue) -> translate(ps, newValue));
    }

}

