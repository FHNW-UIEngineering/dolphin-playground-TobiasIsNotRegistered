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
    long PRESENTATION_STATE_ID = -888L;

    String PERSON_PROXY_PM_ID = "PersonProxy:000";

    default PresentationState getPresentationState() {
        return new PresentationState(getPresentationStatePM());
    }

    default BasePresentationModel getPresentationStatePM() {
        return (BasePresentationModel) getDolphin().getAt(PMDescription.PRESENTATION_STATE.pmId(SpecialPMMixin.PRESENTATION_STATE_ID));
    }

    default Person getPersonProxy(){
        return new Person(getPersonProxyPM());
    }

    default BasePresentationModel getPersonProxyPM() {
        return (BasePresentationModel) getDolphin().getAt(SpecialPMMixin.PERSON_PROXY_PM_ID);
    }

    Dolphin getDolphin();

}
