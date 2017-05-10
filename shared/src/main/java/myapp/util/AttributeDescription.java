package myapp.util;

import myapp.presentationmodel.PMDescription;

/**
 * @author Dieter Holz
 */
public interface AttributeDescription {
    PMDescription getPMDescription();

    ValueType getValueType();

    String name();

    default String qualifier(long entityId){
        return getPMDescription().getEntityName() + "." + name() + ":" + entityId;
    }

    default String labelQualifier() {
        return getPMDescription().getEntityName() + "." + name() + ":Label";
    }
}
