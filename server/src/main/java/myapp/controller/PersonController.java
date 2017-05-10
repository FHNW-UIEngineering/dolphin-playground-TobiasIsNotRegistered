package myapp.controller;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.opendolphin.core.Dolphin;
import org.opendolphin.core.server.ServerPresentationModel;
import org.opendolphin.core.server.Slot;
import org.opendolphin.core.server.comm.ActionRegistry;

import myapp.presentationmodel.PMDescription;
import myapp.presentationmodel.SpecialPMMixin;
import myapp.presentationmodel.person.Person;
import myapp.presentationmodel.person.PersonCommands;
import myapp.service.MyService;
import myapp.util.Controller;
import myapp.util.DolphinMixin;
import myapp.util.Language;

/**
 * This is an example for a application specific controller.
 * <p>
 * Controllers may have many actions that serve a common purpose.
 * <p>
 * Todo: Replace this with your Controller
 */
public class PersonController extends Controller implements SpecialPMMixin {

    private final MyService service;
    private Person personProxy;

    public PersonController(MyService service) {
        this.service = service;
    }

    @Override
    public void registerIn(ActionRegistry actionRegistry) {
        super.registerIn(actionRegistry);
        actionRegistry.register(PersonCommands.CMD_CALL_MY_SERVICE, (command, response) -> {
//			Person p = new Person(get(PersonPM.ID_4711));
//			service.myServiceMethod(p);
        });
    }

    @Override
    protected void initializeBasePMs() {
        List<Slot>              proxySlots = createProxySlots(PMDescription.PERSON);
        ServerPresentationModel pm = createPM(PERSON_PROXY_PM_ID,
                                              "Proxy:" + PMDescription.PERSON.getName(),
                                              createDTO(proxySlots));
        personProxy = new Person(pm);
    }

    @Override
    protected void setDefaultValues() {
        personProxy.name.setMandatory(true);
        personProxy.age.setValue(10);
        personProxy.isAdult.setValue(false);
    }

    @Override
    protected void setupValueChangedListener() {
        super.setupValueChangedListener();
        getPresentationState().language.valueProperty().addListener((observable, oldValue, newValue) -> {
            translate(personProxy, newValue);
        });

    }

    @Override
    public Dolphin getDolphin() {
        return getServerDolphin();
    }
}
