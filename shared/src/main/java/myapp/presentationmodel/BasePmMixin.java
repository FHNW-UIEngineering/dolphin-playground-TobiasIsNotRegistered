package myapp.presentationmodel;

import org.opendolphin.core.BasePresentationModel;
import org.opendolphin.core.Dolphin;

import myapp.presentationmodel.person.Person;
import myapp.presentationmodel.presentationstate.ApplicationState;

/**
 * @author Dieter Holz
 */
public interface BasePmMixin {
    long   APPLICATION_STATE_ID    = -888L;
    String APPLICATION_STATE_PM_ID = PMDescription.APPLICATION_STATE.pmId(APPLICATION_STATE_ID);

    long   PERSON_PROXY_ID       = -777L;
    String PERSON_PROXY_PM_ID    = PMDescription.PERSON.pmId(PERSON_PROXY_ID);

    default ApplicationState getApplicationState() {
        return new ApplicationState(getApplicationStatePM());
    }

    default BasePresentationModel getApplicationStatePM() {
        return (BasePresentationModel) getDolphin().getAt(APPLICATION_STATE_PM_ID);
    }

    default Person getPersonProxy() {
        return new Person(getPersonProxyPM());
    }

    default BasePresentationModel getPersonProxyPM() {
        return (BasePresentationModel) getDolphin().getAt(PERSON_PROXY_PM_ID);
    }

    Dolphin getDolphin();

}
