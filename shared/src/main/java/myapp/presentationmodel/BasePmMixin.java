package myapp.presentationmodel;

import myapp.presentationmodel.rockets.Rocket;
import org.opendolphin.core.BasePresentationModel;
import org.opendolphin.core.Dolphin;

import myapp.presentationmodel.person.Person;
import myapp.presentationmodel.presentationstate.ApplicationState;
import sun.plugin2.message.ReleaseRemoteObjectMessage;

/**
 * @author Sami-Klaus
 */
public interface BasePmMixin {
    //todo: for all your basePMs (as delivered by your Controllers) specify constants and getter-methods like these
    String ROCKET_PROXY_PM_ID = PMDescription.ROCKET.pmId(-777L);

    default BasePresentationModel getRocketProxyPM() {
        return (BasePresentationModel) getDolphin().getAt(ROCKET_PROXY_PM_ID);
    }

    default Rocket getRocketProxy() {
        return new Rocket(getRocketProxyPM());
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
