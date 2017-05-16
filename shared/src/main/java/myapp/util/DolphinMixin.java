package myapp.util;

import java.util.stream.Stream;

import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;

import myapp.presentationmodel.PMDescription;

/**
 * Some useful convenience methods for dealing with PMDescription and AttributeDescription
 *
 * @author Dieter Holz
 */
public interface DolphinMixin {
    Dolphin getDolphin();

    default Stream<PresentationModel> findAllPresentationModelsByType(PMDescription type){
        return getDolphin().findAllPresentationModelsByType(type.getName()).stream();
    }

    default PresentationModel get(PMDescription type, long id){
        return getDolphin().getAt(type.pmId(id));
    }

    default PresentationModel get(String pmId) {
        return getDolphin().getAt(pmId);
    }

    default Attribute getAttribute(PresentationModel pm, AttributeDescription att){
        return pm.getAt(att.name());
    }

    default Attribute getAttribute(String pmId, AttributeDescription att, Tag tag) {
        return get(pmId).getAt(att.name(), tag);
    }

    default Attribute getAttribute(PresentationModel pm, AttributeDescription att, Tag tag) {
        return pm.getAt(att.name(), tag);
    }

    default <T> T getValue(String pmId, AttributeDescription att) {
        return (T) get(pmId).getAt(att.name()).getValue();
    }

    default <T> T getValue(PresentationModel pm, AttributeDescription att) {
        return (T) pm.getAt(att.name()).getValue();
    }

    default void setValue(String pmId, AttributeDescription att, Object newValue) {
        setValue(pmId, att, Tag.VALUE, newValue);
    }

    default void setValue(String pmId, AttributeDescription att, Tag tag, Object newValue) {
        getAttribute(pmId, att, tag).setValue(newValue);
    }

    default void setLabel(PresentationModel pm, AttributeDescription att, String label){
        getAttribute(pm, att, Tag.LABEL).setValue(label);
    }

}
