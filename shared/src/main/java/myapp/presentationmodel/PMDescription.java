package myapp.presentationmodel;

import myapp.presentationmodel.rockets.RocketAtt;
import myapp.presentationmodel.presentationstate.ApplicationStateAtt;
import myapp.util.AttributeDescription;

/**
 * Specifies/describes all the PresentationModels of this application.
 *
 * Although technically feasible, there shouldn't be any PresentationModel that's not described here.
 *
 * @author Dieter Holz
 */
public enum PMDescription {

    //todo: add all application specific PMDescriptions
    ROCKET("RocketPM", "ROCKET", RocketAtt.values()),



    // ApplicationState is always needed
    APPLICATION_STATE("ApplicationStatePM", null, ApplicationStateAtt.values());

    private final String                 name;
    private final String                 entityName;
    private final AttributeDescription[] attributeDescriptions;

    PMDescription(String pmName, String entityName, AttributeDescription[] attributeDescriptions) {
        this.name                  = pmName;
        this.entityName            = entityName;
        this.attributeDescriptions = attributeDescriptions;
    }

    public String getName() {
        return name;
    }

    public String getEntityName() {
        return entityName;
    }

    public AttributeDescription[] getAttributeDescriptions() {
        return attributeDescriptions;
    }

    public String pmId(long id) {
        return getName() + ":" + id;
    }
}
