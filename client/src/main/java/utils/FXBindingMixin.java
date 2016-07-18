package utils;

import javafx.beans.property.Property;

import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;

import util.DolphinMixin;

/**
 * @author Dieter Holz
 */
public interface FXBindingMixin extends DolphinMixin {

	default ToAttributeAble bind(Property property) {
		return new ToAttributeAble(property, getDolphin());
	}

	default OfAble bind(String attributeName, Tag tag) {
		return new OfAble(attributeName, tag, getDolphin());
	}

	default OfAble bind(String attributeName) {
		return bind(attributeName, Tag.VALUE);
	}

	// todo dk: grammatically correct would be "bindBidirectionally"
	default ToBidirectionalPropertyAble bindBidirectional(Attribute attribute) {
		return new ToBidirectionalPropertyAble(attribute);
	}

	default OfBidirectionalAble bindBidirectional(String attributeName) {
		return bindBidirectional(attributeName, Tag.VALUE);
	}

	default OfBidirectionalAble bindBidirectional(String attributeName, Tag tag) {
		return new OfBidirectionalAble(attributeName, tag);
	}

	class OfAbleTerminal {
		private final Property property;
		private final String   attributeName;
		private final Tag      tag;
		private final Dolphin  dolphin;

		public OfAbleTerminal(Property property, String attributeName, Tag tag, Dolphin dolphin) {
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

		public OfAble(String attributeName, Tag tag, Dolphin clientDolphin) {
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

	class OfBidirectionalAble {
		private final String attributeName;
		private final Tag    tag;

		public OfBidirectionalAble(String attributeName, Tag tag) {
			this.attributeName = attributeName;
			this.tag = tag;
		}

		public ToBidirectionalPropertyAble of(PresentationModel pm) {
			return new ToBidirectionalPropertyAble(pm.getAt(attributeName, tag));
		}
	}

	class ToBidirectionalPropertyAble {
		private final Attribute attribute;

		public ToBidirectionalPropertyAble(Attribute attribute) {
			this.attribute = attribute;
		}

		public void to(Property property) {
			attribute.addPropertyChangeListener(Attribute.VALUE, evt -> property.setValue(evt.getNewValue()));
			property.setValue(attribute.getValue());
			property.addListener((observable, oldValue, newValue) -> attribute.setValue(newValue));
			attribute.setValue(property.getValue());
		}
	}

	class ToPropertyAble {
		private final Attribute attribute;

		public ToPropertyAble(Attribute attribute) {
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

		public ToAttributeAble(Property property, Dolphin clientDolphin) {
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
