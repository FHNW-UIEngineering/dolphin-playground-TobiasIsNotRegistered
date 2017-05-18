package myapp.controller;

import org.opendolphin.core.server.ServerPresentationModel;

import myapp.presentationmodel.PMDescription;
import myapp.presentationmodel.BasePmMixin;
import myapp.presentationmodel.presentationstate.ApplicationState;
import myapp.util.Controller;
import myapp.util.Language;

/**
 * @author Dieter Holz
 */
public class PresentationStateController extends Controller implements BasePmMixin {

    private ApplicationState ps;

    @Override
    protected void initializeBasePMs() {
        ServerPresentationModel presentationStatePM = createProxyPM(PMDescription.APPLICATION_STATE, BasePmMixin.APPLICATION_STATE_ID);

        ps = new ApplicationState(presentationStatePM);
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

