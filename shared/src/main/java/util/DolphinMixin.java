package util;

import java.beans.PropertyChangeListener;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;

import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;

/**
 * @author Dieter Holz
 */
public interface DolphinMixin {
    Dolphin getDolphin();

    default PresentationModel get(String pmId) {
        return getDolphin().getAt(pmId);
    }

    default Attribute get(String pmId, String attributeName) {
        return get(pmId).getAt(attributeName);
    }

    default Attribute get(PresentationModel pm, String attributeName) {
        return pm.getAt(attributeName);
    }

    default Attribute get(String pmId, String attributeName, Tag tag) {
        return get(pmId).getAt(attributeName, tag);
    }

    default Attribute get(PresentationModel pm, String attributeName, Tag tag) {
        return pm.getAt(attributeName, tag);
    }

    default Object getValue(String pmId, String attributeName) {
        return get(pmId, attributeName).getValue();
    }

    default <T> T getValue(PresentationModel pm, String attributeName) {
        return (T) pm.getAt(attributeName).getValue();
    }

    default void setValue(String pmId, String attributeName, Object newValue) {
        get(pmId, attributeName).setValue(newValue);
    }

    default void onDirtyStateChanged(PresentationModel pm, PropertyChangeListener listener) {
        pm.addPropertyChangeListener(Attribute.DIRTY_PROPERTY, listener);
    }

    default void onDirtyStateChanged(Veneer veneer, ChangeListener<Boolean> listener) {
        PresentationModel pm = veneer.getPresentationModel();

        SimpleBooleanProperty dummyProperty = new SimpleBooleanProperty(pm.isDirty());
        onDirtyStateChanged(pm, evt -> listener.changed(dummyProperty, (Boolean) evt.getOldValue(), (Boolean) evt.getNewValue()));
    }
}
