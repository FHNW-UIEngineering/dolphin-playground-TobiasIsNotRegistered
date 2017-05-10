package myapp.controller;

import java.util.List;

import org.opendolphin.core.Attribute;
import org.opendolphin.core.BasePresentationModel;
import org.opendolphin.core.server.ServerPresentationModel;
import org.opendolphin.core.server.Slot;

import myapp.presentationmodel.PMDescription;
import myapp.presentationmodel.SpecialPMMixin;
import myapp.presentationmodel.presentationstate.PresentationState;
import myapp.presentationmodel.presentationstate.PresentationStateAtt;
import myapp.util.Controller;
import myapp.util.Language;

/**
 * @author Dieter Holz
 */
public class PresentationStateController extends Controller implements SpecialPMMixin{

    private PresentationState ps;

    @Override
    protected void initializeBasePMs() {
        ps = createPresentationStatePM();
    }

    @Override
    protected void setDefaultValues() {
        ps.language.setValue(Language.ENGLISH);
    }

    @Override
    protected void setupValueChangedListener() {
        ps.language.valueProperty()
                   .addListener(($, $$, newValue) -> translate(ps, newValue));
    }

    @Override
    protected void setupBinding() {

    }

    private PresentationState createPresentationStatePM() {
        List<Slot> proxySlots = createProxySlots(PMDescription.PRESENTATION_STATE);

        ServerPresentationModel presentationStatePM = createPM(PMDescription.PRESENTATION_STATE,
                                                               SpecialPMMixin.PRESENTATION_STATE_ID,
                                                               createDTO(proxySlots));


        PresentationState ps = new PresentationState(presentationStatePM);
        ps.cleanData.setValue(true);
        ps.undoDisabled.setValue(true);
        ps.redoDisabled.setValue(true);
        presentationStatePM.rebase();

        return ps;
    }


}

