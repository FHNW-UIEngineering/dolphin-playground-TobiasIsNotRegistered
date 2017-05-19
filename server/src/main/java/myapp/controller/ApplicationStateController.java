package myapp.controller;

import org.opendolphin.core.server.ServerPresentationModel;
import org.opendolphin.core.server.comm.ActionRegistry;

import myapp.presentationmodel.PMDescription;
import myapp.presentationmodel.BasePmMixin;
import myapp.presentationmodel.presentationstate.ApplicationState;
import myapp.util.Controller;
import myapp.util.Language;

/**
 *
 * @author Dieter Holz
 */
public class ApplicationStateController extends Controller implements BasePmMixin {

    private ApplicationState ps;

    @Override
    protected void registerCommands(ActionRegistry actionRegistry) {
        //no specific commands needed
    }

    @Override
    protected void initializeBasePMs() {
        ServerPresentationModel presentationStatePM = createProxyPM(PMDescription.APPLICATION_STATE, APPLICATION_STATE_PM_ID);

        ps = new ApplicationState(presentationStatePM);
    }

    @Override
    protected void setDefaultValues() {
        ps.language.setValue(Language.ENGLISH);
        ps.cleanData.setValue(true);
    }

    @Override
    protected void setupValueChangedListener() {
        ps.language.valueProperty()
                   .addListener(($, $$, language) -> translate(ps, language));
    }

}

