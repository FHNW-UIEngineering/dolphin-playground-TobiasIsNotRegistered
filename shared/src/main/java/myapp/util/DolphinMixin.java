package myapp.util;

import java.beans.PropertyChangeListener;

import java.util.stream.Stream;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;

import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;

import myapp.presentationmodel.PMDescription;
import myapp.util.veneer.PresentationModelVeneer;

/**
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

    default void onDirtyStateChanged(PresentationModel pm, PropertyChangeListener listener) {
        pm.addPropertyChangeListener(Attribute.DIRTY_PROPERTY, listener);
    }

    default void onDirtyStateChanged(PresentationModelVeneer veneer, ChangeListener<Boolean> listener) {
        PresentationModel pm = veneer.getPresentationModel();

        SimpleBooleanProperty dummyProperty = new SimpleBooleanProperty(pm.isDirty());
        onDirtyStateChanged(pm, evt -> listener.changed(dummyProperty, (Boolean) evt.getOldValue(), (Boolean) evt.getNewValue()));
    }

    default String pmId(String type, long entityId){
        return type + ":" + entityId;
    }

    default long entityId(String pmId){
        return Long.valueOf(pmId.split(":")[1]);
    }

    default Object getInitialValue(AttributeDescription att) {
            switch (att.getValueType()){
                case FLOAT:
                    return 0f;
                case DOUBLE:
                    return 0d;
                case BOOLEAN:
                    return true;
                case ID:
                    return 0L;
                case FOREIGN_KEY:
                    return 0L;
                case INT:
                    return 0;
                case LONG:
                    return 0L;
                case YEAR:
                    return (short)2016;
                case STRING:
                    return "";
                default:
                    return null;
            }
        }

}
