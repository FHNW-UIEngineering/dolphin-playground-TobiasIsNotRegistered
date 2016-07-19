package util;

import java.util.function.Function;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;

import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;

/**
 * @author Dieter Holz
 */
public interface FXBindingMixin extends ClientDolphinMixin {

	default ToAttributeAble bind(Property property) {
		return new ToAttributeAble(property, getDolphin());
	}

	default OfAble bind(String attributeName, Tag tag) {
		return new OfAble(attributeName, tag, getDolphin());
	}

	default OfAble bind(String attributeName) {
		return bind(attributeName, Tag.VALUE);
	}

	default WithPropertyAble interrelate(Attribute attribute) {
		return new WithPropertyAble(attribute);
	}

	default OfInterrelateAble interrelate(String attributeName) {
		return interrelate(attributeName, Tag.VALUE);
	}

	default OfInterrelateAble interrelate(String attributeName, Tag tag) {
		return new OfInterrelateAble(attributeName, tag);
	}

    default DirtyToPropertyAble bindDirtyStateOf(Veneer veneer){
	    return new DirtyToPropertyAble(veneer.getPresentationModel());
    }

    class DirtyToPropertyAble {

        private final PresentationModel          pm;
        private final Function<Boolean, Boolean> converter;

        DirtyToPropertyAble(PresentationModel pm){
            this(pm, b -> b);
        }

        DirtyToPropertyAble(PresentationModel pm, Function<Boolean, Boolean> converter) {
            this.pm = pm;
            this.converter = converter;
        }

   		public void to(BooleanProperty property) {
            pm.addPropertyChangeListener(Attribute.DIRTY_PROPERTY, evt -> property.setValue(converter.apply((Boolean) evt.getNewValue())));
            property.setValue(converter.apply(pm.isDirty()));
        }

   		public DirtyToPropertyAble convertedBy(Function<Boolean, Boolean> converter){
            return new DirtyToPropertyAble(pm, converter);
        }
   	}

	class OfAbleTerminal {
		private final Property property;
		private final String   attributeName;
		private final Tag      tag;
		private final Dolphin  dolphin;

        OfAbleTerminal(Property property, String attributeName, Tag tag, Dolphin dolphin) {
			this.property = property;
			this.attributeName = attributeName;
			this.tag = tag;
			this.dolphin = dolphin;
		}

		public void of(PresentationModel pm) {
			Attribute attribute = pm.getAt(attributeName, tag);
			property.addListener((observable, oldValue, newValue) -> attribute.setValue(newValue));
			attribute.setValue(property.getValue());
		}

		public void of(String pmId) {
			of(dolphin.getAt(pmId));
		}
	}

	class OfAble {
		private final String  attributeName;
		private final Tag     tag;
		private final Dolphin clientDolphin;

		OfAble(String attributeName, Tag tag, Dolphin clientDolphin) {
			this.attributeName = attributeName;
			this.tag = tag;
			this.clientDolphin = clientDolphin;
		}

		public ToPropertyAble of(PresentationModel pm) {
			return new ToPropertyAble(pm.getAt(attributeName, tag));
		}

		public ToPropertyAble of(String pmId) {
			return of(clientDolphin.getAt(pmId));
		}
	}

	class OfInterrelateAble {
		private final String attributeName;
		private final Tag    tag;

		OfInterrelateAble(String attributeName, Tag tag) {
			this.attributeName = attributeName;
			this.tag = tag;
		}

		public WithPropertyAble of(PresentationModel pm) {
			return new WithPropertyAble(pm.getAt(attributeName, tag));
		}
	}

	class WithPropertyAble {
		private final Attribute attribute;

		WithPropertyAble(Attribute attribute) {
			this.attribute = attribute;
		}

		public void with(Property property) {
			attribute.addPropertyChangeListener(Attribute.VALUE, evt -> property.setValue(evt.getNewValue()));
			property.setValue(attribute.getValue());
			property.addListener((observable, oldValue, newValue) -> attribute.setValue(newValue));
			attribute.setValue(property.getValue());
		}
	}

	class ToPropertyAble {
		private final Attribute attribute;

		ToPropertyAble(Attribute attribute) {
			this.attribute = attribute;
		}

		public void to(Property property) {
			attribute.addPropertyChangeListener(Attribute.VALUE, evt -> property.setValue(evt.getNewValue()));
			property.setValue(attribute.getValue());
		}
	}

	class ToAttributeAble {
		private final Property property;
		private final Dolphin  clientDolphin;

		ToAttributeAble(Property property, Dolphin clientDolphin) {
			this.property = property;
			this.clientDolphin = clientDolphin;
		}

		public void to(Attribute attribute) {
			property.addListener((observable, oldValue, newValue) -> attribute.setValue(newValue));
			attribute.setValue(property.getValue());
		}

		public OfAbleTerminal to(String attributeName) {
			return new OfAbleTerminal(property, attributeName, Tag.VALUE, clientDolphin);
		}

		public OfAbleTerminal to(String attributeName, Tag tag) {
			return new OfAbleTerminal(property, attributeName, tag, clientDolphin);
		}
	}
}
