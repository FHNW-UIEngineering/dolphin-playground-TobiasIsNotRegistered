package myapp.presentationmodel;

import org.opendolphin.core.BasePresentationModel;
import org.opendolphin.core.Dolphin;

import myapp.presentationmodel.person.Person;
import myapp.presentationmodel.presentationstate.ApplicationState;

/**
 * @author Dieter Holz
 */
public interface BasePmMixin {
    //todo: for all your basePMs (as delivered by your Controllers) specify constants and getter-methods like these
    String PERSON_PROXY_PM_ID = PMDescription.PERSON.pmId(-777L);

    default BasePresentationModel getPersonProxyPM() {
        return (BasePresentationModel) getDolphin().getAt(PERSON_PROXY_PM_ID);
    }

    default Person getPersonProxy() {
        return new Person(getPersonProxyPM());
    }

    // always needed
    String APPLICATION_STATE_PM_ID = PMDescription.APPLICATION_STATE.pmId(-888);

    default BasePresentationModel getApplicationStatePM() {
        return (BasePresentationModel) getDolphin().getAt(APPLICATION_STATE_PM_ID);
    }

    default ApplicationState getApplicationState() {
        return new ApplicationState(getApplicationStatePM());
    }

    Dolphin getDolphin();
}
