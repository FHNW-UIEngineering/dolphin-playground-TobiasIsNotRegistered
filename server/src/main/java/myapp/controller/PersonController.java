package myapp.controller;

import java.util.List;

import org.opendolphin.core.Dolphin;
import org.opendolphin.core.server.DTO;
import org.opendolphin.core.server.ServerPresentationModel;
import org.opendolphin.core.server.comm.ActionRegistry;

import myapp.presentationmodel.BasePmMixin;
import myapp.presentationmodel.PMDescription;
import myapp.presentationmodel.person.Person;
import myapp.presentationmodel.person.PersonCommands;
import myapp.service.SomeService;
import myapp.util.Controller;

/**
 * This is an example for an application specific controller.
 * <p>
 * Controllers may have many actions that serve a common purpose.
 * <p>
 * Todo: Replace this with your Controller
 */
class PersonController extends Controller implements BasePmMixin {

    private final SomeService service;

    private Person personProxy;

    PersonController(SomeService service) {
        this.service = service;
    }

    @Override
    public void registerCommands(ActionRegistry registry) {
        registry.register(PersonCommands.LOAD_SOME_PERSON, ($, $$) -> loadPerson());
        registry.register(PersonCommands.SAVE            , ($, $$) -> save());
        registry.register(PersonCommands.RESET           , ($, $$) -> reset(PMDescription.PERSON));
    }

    @Override
    protected void initializeBasePMs() {
        ServerPresentationModel pm = createProxyPM(PMDescription.PERSON, PERSON_PROXY_PM_ID);

        personProxy = new Person(pm);
    }

    @Override
    protected void setDefaultValues() {
        personProxy.name.setMandatory(true);
    }

    @Override
    protected void setupValueChangedListener() {
        getApplicationState().language.valueProperty().addListener((observable, oldValue, newValue) -> translate(personProxy, newValue));
    }

    ServerPresentationModel loadPerson() {
        DTO dto = service.loadSomeEntity();
        ServerPresentationModel pm = createPM(PMDescription.PERSON, dto);

        personProxy.getPresentationModel().syncWith(pm);

        return pm;
    }

    void save() {
        List<DTO> dtos = dirtyDTOs(PMDescription.PERSON);
        service.save(dtos);
        rebase(PMDescription.PERSON);
    }

    @Override
    public Dolphin getDolphin() {
        return getServerDolphin();
    }
}
