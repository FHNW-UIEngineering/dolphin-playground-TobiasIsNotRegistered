package myapp.controller.util;

import java.beans.PropertyChangeListener;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.ModelStoreEvent;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;
import org.opendolphin.core.server.DTO;
import org.opendolphin.core.server.ServerPresentationModel;
import org.opendolphin.core.server.Slot;
import org.opendolphin.core.server.action.DolphinServerAction;
import org.opendolphin.core.server.comm.ActionRegistry;

import myapp.presentationmodel.PMDescription;
import myapp.util.AdditionalTag;
import myapp.util.AttributeDescription;
import myapp.util.BasicCommands;
import myapp.util.DolphinMixin;
import myapp.util.Language;
import myapp.util.ValueType;
import myapp.util.veneer.PresentationModelVeneer;

/**
 * @author Dieter Holz
 */
public abstract class Controller extends DolphinServerAction implements DolphinMixin {
    @Override
    public Dolphin getDolphin() {
        return getServerDolphin();
    }

    @Override
    public void registerIn(ActionRegistry actionRegistry) {
        actionRegistry.register(BasicCommands.INITIALIZE_BASE_PMS  , (command, response) -> initializeBasePMs());
        actionRegistry.register(BasicCommands.INITIALIZE_CONTROLLER, (command, response) -> initializeController());
    }

    protected abstract void initializeBasePMs();

    protected void setDefaultValues() {
    }

    protected void setupModelStoreListener() {
    }

    protected void setupValueChangedListener() {
    }

    protected void setupBinding() {
    }

    private void initializeController() {
        setupModelStoreListener();
        setupValueChangedListener();
        setupBinding();
        setDefaultValues();
        rebaseAll();
    }

    private void rebaseAll() {
        Collection<ServerPresentationModel> allPMs = getServerDolphin().getServerModelStore().listPresentationModels();
        allPMs.forEach(ServerPresentationModel::rebase);
    }

    public ServerPresentationModel createPM(String pmId, String pmType, DTO dto) {
        return getServerDolphin().presentationModel(pmId, pmType, dto);
    }

    public ServerPresentationModel createPM(PMDescription type, DTO dto) {
        long id = getEntityId(dto);
        return createPM(type, id, dto);
    }

    public ServerPresentationModel createPM(PMDescription type, long id, DTO dto) {
        return getServerDolphin().presentationModel(type.pmId(id), type.getName(), dto);
    }

    public ServerPresentationModel createEmptyPM(PMDescription type, long id) {
        String                 pmId       = type.pmId(id);
        String                 entityType = type.getEntityName();
        AttributeDescription[] attr       = type.getAttributeDescriptions();
        Slot[]                 slots      = new Slot[attr.length];
        for (int i = 0; i < attr.length; i++) {
            String qualifier = null;
            if (entityType != null) {
                qualifier = entityType + "." + attr[i] + ":" + entityId(pmId);
            }
            Object initialValue = getInitialValue(attr[i]);
            if (ValueType.ID.equals(attr[i].getValueType())) {
                initialValue = entityId(pmId);
            }
            slots[i] = new Slot(attr[i].name(), initialValue, qualifier, Tag.VALUE);
        }
        return getServerDolphin().presentationModel(pmId, type.getName(), new DTO(slots));
    }

    protected void rebase(PMDescription type, List<Long> createdPMs) {
        dirtyModels(type, createdPMs).forEach(ServerPresentationModel::rebase);
    }

    protected void reset(PMDescription type, List<Long> createdPMs) {
        dirtyModels(type, createdPMs).forEach(ServerPresentationModel::reset);
    }

    protected List<Slot> dirtyAttributes(PMDescription type, List<Long> createdPMs) {
        List<ServerPresentationModel> dirtyModels = dirtyModels(type, createdPMs);

        return dirtyModels.stream()
                          .flatMap(pm -> pm.getAttributes().stream())
                          .filter(attribute -> attribute.isDirty() || createdPMs.contains(entityId(attribute.getPresentationModel().getId())))
                          .map(att -> new Slot(att.getPropertyName(),
                                               att.getValue(),
                                               att.getQualifier()))
                          .collect(Collectors.toList());

    }

    protected List<DTO> dirtyDTOs(PMDescription type, List<Long> createdPMs) {
        List<ServerPresentationModel> dirtyPMs = dirtyModels(type, createdPMs);

        return dirtyPMs.stream()
                       .map(pm -> pm.getAttributes().stream()
                                    .filter(attribute -> attribute.isDirty() || createdPMs.contains(
                                            entityId(attribute.getPresentationModel().getId())))
                                    .map(att -> new Slot(att.getPropertyName(),
                                                         att.getValue(),
                                                         att.getQualifier()))
                                    .collect(Collectors.toList()))
                       .map(DTO::new)
                       .collect(Collectors.toList());
    }

    private List<ServerPresentationModel> dirtyModels(PMDescription type, List<Long> createdPMs) {
        return getServerDolphin().findAllPresentationModelsByType(type.getName())
                                 .stream()
                                 .filter(pm -> pm.isDirty() || (createdPMs != null && createdPMs.contains(entityId(pm.getId()))))
                                 .collect(Collectors.toList());
    }

    public ServerPresentationModel get(PMDescription type, long id) {
        return get(type.pmId(id));
    }

    public ServerPresentationModel get(String pmId) {
        return (ServerPresentationModel) DolphinMixin.super.get(pmId);
    }

    protected ToAttributeAble propagate(Attribute att) {
        return new ToAttributeAble(att);
    }

    protected class ToAttributeAble {
        private final Attribute originAtt;

        private Function<Object, Object> converter;

        ToAttributeAble(Attribute originAtt) {
            this.originAtt = originAtt;
        }

        public void to(Attribute targetAttribute) {
            originAtt.addPropertyChangeListener(Attribute.VALUE,
                                                evt -> setValueOnTarget(targetAttribute, evt.getNewValue()));

            setValueOnTarget(targetAttribute, originAtt.getValue());
        }

        public ToAttributeAble convertedBy(Function<Object, Object> converter) {
            this.converter = converter;

            return this;
        }

        private void setValueOnTarget(Attribute targetAttribute, Object newValue) {
            // can be the case during 'syncWith' one attribute is already synced the other not
            if (originAtt.getQualifier() == null ||
                targetAttribute.getQualifier() == null ||
                entityId(originAtt.getQualifier()) != entityId(targetAttribute.getQualifier())) {
                return;
            }

            Object valueToBeSet = converter != null ? converter.apply(newValue) : newValue;

            if (targetAttribute.getValue() == null && valueToBeSet != null ||
                targetAttribute.getValue() != null && valueToBeSet == null) {
                targetAttribute.setValue(valueToBeSet);
            } else if (targetAttribute.getValue() instanceof Float && valueToBeSet != null) {
                if (Math.abs((Float) valueToBeSet - (Float) targetAttribute.getValue()) > 0.1) {
                    targetAttribute.setValue(valueToBeSet);
                }
            } else if (targetAttribute.getValue() instanceof Double && valueToBeSet != null) {
                if (Math.abs((Double) valueToBeSet - (Double) targetAttribute.getValue()) > 0.1) {
                    targetAttribute.setValue(valueToBeSet);
                }
            } else if (!targetAttribute.getValue().equals(valueToBeSet)) {
                targetAttribute.setValue(valueToBeSet);
            }
        }
    }

    protected ForPropertyAble handleValueListener(PropertyChangeListener listener) {
        return new ForPropertyAble(listener);
    }

    protected class ForPropertyAble {

        private final PropertyChangeListener listener;

        private ForPropertyAble(PropertyChangeListener listener) {
            this.listener = listener;
        }

        public PMTypeAble forProperty(String pmProperty) {
            return new PMTypeAble(listener, pmProperty);
        }

        public PMTypeAble forAttribute(AttributeDescription att) {
            return new PMTypeAble(listener, att);
        }
    }

    protected class PMTypeAble {

        private final PropertyChangeListener listener;
        private       AttributeDescription   att;
        private       String                 pmProperty;

        private PMTypeAble(PropertyChangeListener listener, String pmProperty) {
            this.listener = listener;
            this.pmProperty = pmProperty;
        }

        private PMTypeAble(PropertyChangeListener listener, AttributeDescription att) {
            this.listener = listener;
            this.att = att;
        }

        public void ofAll(PMDescription type) {
            if (pmProperty != null) {
                getServerDolphin().addModelStoreListener(type.getName(), event -> {
                    PresentationModel pm = event.getPresentationModel();
                    if (event.getType() == ModelStoreEvent.Type.ADDED) {
                        pm.addPropertyChangeListener(pmProperty, listener);
                    } else {
                        pm.removePropertyChangeListener(pmProperty, listener);
                    }
                });
            } else {
                getServerDolphin().addModelStoreListener(type.getName(), event -> {
                    PresentationModel pm = event.getPresentationModel();

                    Attribute attribute = getAttribute(pm, att);
                    if (event.getType() == ModelStoreEvent.Type.ADDED) {
                        attribute.addPropertyChangeListener(Attribute.VALUE, listener);
                    } else {
                        attribute.removePropertyChangeListener(Attribute.VALUE, listener);
                    }
                });
            }
        }
    }

    protected List<Slot> createProxySlots(PMDescription pmType) {
        List<Slot> slots = new ArrayList<>();

        Arrays.stream(pmType.getAttributeDescriptions()).forEach(att -> {
            slots.add(new Slot(att.name(), getInitialValue(att), null, Tag.VALUE));
            slots.add(new Slot(att.name(), att.name().toLowerCase(), att.labelQualifier(), Tag.LABEL));
            slots.add(new Slot(att.name(), att.getValueType().name(), null, Tag.VALUE_TYPE));
            slots.add(new Slot(att.name(), false, null, Tag.MANDATORY));
            slots.add(new Slot(att.name(), true, null, AdditionalTag.VALID));
            slots.add(new Slot(att.name(), null, null, AdditionalTag.VALIDATION_MESSAGE));
            slots.add(new Slot(att.name(), false, null, AdditionalTag.READ_ONLY));
            slots.add(new Slot(att.name(), "", null, AdditionalTag.USER_FACING_STRING));
        });

        return slots;
    }

    protected long getEntityId(DTO dto) {
        return entityId(dto.getSlots().get(0).getQualifier());
    }

    protected Slot getSlot(DTO dto, AttributeDescription att) {
        return dto.getSlots().stream()
                  .filter(slot -> slot.getPropertyName().equals(att.name()))
                  .findAny()
                  .orElseThrow(NoSuchElementException::new);
    }

    protected Slot getIdSlot(PMDescription type, DTO dto) {
        AttributeDescription idDescr = getIDAttributeDescription(type);
        return getSlot(dto, idDescr);
    }

    protected AttributeDescription getIDAttributeDescription(PMDescription type) {
        return Arrays.stream(type.getAttributeDescriptions())
                     .filter(attributeDescription -> ValueType.ID.equals(attributeDescription.getValueType()))
                     .findAny()
                     .orElseThrow(NoSuchElementException::new);
    }

    protected <T> T getValue(DTO dto, AttributeDescription att) {
        return (T) getSlot(dto, att).getValue();
    }

    protected void translate(PresentationModel proxyPM, Language language) {
        ResourceBundle  bundle     = getResourceBundle(proxyPM, language);
        List<Attribute> attributes = proxyPM.getAttributes();
        attributes.stream()
                  .filter(att -> att.getTag().equals(Tag.LABEL))
                  .forEach(att -> {
                      String propertyName = att.getPropertyName();
                      att.setValue(translate(bundle, propertyName));
                  });
    }

    protected void translate(PresentationModelVeneer veneer, Language language) {
        translate(veneer.getPresentationModel(), language);
    }

    protected void translate(Attribute attribute, Language language) {
        ResourceBundle bundle = getResourceBundle(attribute.getPresentationModel(), language);
        attribute.setValue(translate(bundle, attribute.getPropertyName()));
    }

    private ResourceBundle getResourceBundle(PresentationModel pm, Language language) {
        Locale locale = Language.ENGLISH.equals(language) ? Locale.ENGLISH : Locale.GERMAN;

        String type      = pm.getPresentationModelType();
        String className = type.contains(":") ? type.split(":")[1] : type;
        String baseName  = "resourcebundles/" + className;
        try {
            return ResourceBundle.getBundle(baseName, locale);
        } catch (MissingResourceException e) {
            throw new IllegalStateException("resource bundle " + baseName + " not found");
        }
    }

    private String translate(ResourceBundle bundle, String propertyName) {
        String labelText;
        if (bundle.containsKey(propertyName)) {
            labelText = new String(bundle.getString(propertyName).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } else {
            labelText = propertyName.toLowerCase();
        }
        return labelText;
    }

}
