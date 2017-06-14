package myapp.controller;

import java.util.List;

import org.opendolphin.core.Dolphin;
import org.opendolphin.core.server.DTO;
import org.opendolphin.core.server.ServerPresentationModel;
import org.opendolphin.core.server.comm.ActionRegistry;

import myapp.presentationmodel.BasePmMixin;
import myapp.presentationmodel.PMDescription;
import myapp.presentationmodel.rockets.Rocket;
import myapp.presentationmodel.rockets.RocketsCommands;
import myapp.service.SomeService;
import myapp.util.Controller;

/**
 * This is an example for an application specific controller.
 * <p>
 * Controllers may have many actions that serve a common purpose.
 * <p>
 * Todo: Replace this with your Controller
 */
class RocketController extends Controller implements BasePmMixin {

    private final SomeService service;

    private Rocket rocketProxy;

    RocketController(SomeService service) {
        this.service = service;
    }

    @Override
    public void registerCommands(ActionRegistry registry) {
        registry.register(RocketsCommands.LOAD_ROCKET, ($, $$) -> loadRocket());
    }

    @Override
    protected void initializeBasePMs() {
        ServerPresentationModel pm = createProxyPM(PMDescription.ROCKET, ROCKET_PROXY_PM_ID);

        rocketProxy = new Rocket(pm);
    }

    @Override
    protected void setDefaultValues() {
        rocketProxy.name.setMandatory(true);
    }

    @Override
    protected void setupValueChangedListener() {
        getApplicationState().language.valueProperty().addListener((observable, oldValue, newValue) -> translate(rocketProxy, newValue));
    }

    ServerPresentationModel loadRocket() {
        DTO dto = service.loadSomeEntity();
        ServerPresentationModel pm = createPM(PMDescription.ROCKET, dto);

        rocketProxy.getPresentationModel().syncWith(pm);

        return pm;
    }


    @Override
    public Dolphin getDolphin() {
        return getServerDolphin();
    }
}
