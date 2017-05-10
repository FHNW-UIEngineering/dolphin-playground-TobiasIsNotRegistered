package myapp.presentationmodel;

import java.util.Arrays;

import myapp.presentationmodel.person.PersonAtt;
import myapp.presentationmodel.presentationstate.PresentationStateAtt;
import myapp.util.AttributeDescription;

/**
 * @author Dieter Holz
 */
public enum PMDescription {
    //todo: add all application specific PMDescriptions
    PERSON("PersonPM", "PERSON", PersonAtt.values(), true),

    // PresentionState is always needed
    PRESENTATION_STATE("PresentationStatePM", null, PresentationStateAtt.values(), false);


    private final String                 name;
    private final String                 entityName;
    private final AttributeDescription[] attributeDescriptions;
    private final boolean                isUndoAble;

    PMDescription(String pmName, String entityName, AttributeDescription[] attributeDescriptions, boolean isUndoAble) {
        this.name                  = pmName;
        this.entityName            = entityName;
        this.attributeDescriptions = attributeDescriptions;
        this.isUndoAble            = isUndoAble;
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

    public boolean isUndoAble() {
        return isUndoAble;
    }

    public static boolean isUndoAble(String pmName){
        return Arrays.stream(values())
                     .filter(pm ->  pm.getName().equals(pmName))
                     .map(pm -> pm.isUndoAble)
                     .findAny()
                     .orElse(true);
    }
}
