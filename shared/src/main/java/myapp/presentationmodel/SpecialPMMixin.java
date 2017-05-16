package myapp.presentationmodel;

import org.opendolphin.core.BasePresentationModel;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;

import myapp.presentationmodel.person.Person;
import myapp.presentationmodel.presentationstate.PresentationState;

/**
 * @author Dieter Holz
 */
public interface SpecialPMMixin {
    long   PRESENTATION_STATE_ID = -888L;
    String PRESENTATION_STATE_PM_ID = PMDescription.PRESENTATION_STATE.pmId(PRESENTATION_STATE_ID);

    long   PERSON_PROXY_ID       = -777L;
    String PERSON_PROXY_PM_ID    = PMDescription.PERSON.pmId(PERSON_PROXY_ID);

    default PresentationState getPresentationState() {
        return new PresentationState(getPresentationStatePM());
    }

    default BasePresentationModel getPresentationStatePM() {
        return (BasePresentationModel) getDolphin().getAt(PRESENTATION_STATE_PM_ID);
    }

    default Person getPersonProxy() {
        return new Person(getPersonProxyPM());
    }

    default BasePresentationModel getPersonProxyPM() {
        return (BasePresentationModel) getDolphin().getAt(PERSON_PROXY_PM_ID);
    }

    Dolphin getDolphin();

}
