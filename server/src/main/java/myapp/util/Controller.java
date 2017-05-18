package myapp.util;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.opendolphin.core.Attribute;
import org.opendolphin.core.Dolphin;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;
import org.opendolphin.core.server.DTO;
import org.opendolphin.core.server.ServerPresentationModel;
import org.opendolphin.core.server.Slot;
import org.opendolphin.core.server.action.DolphinServerAction;
import org.opendolphin.core.server.comm.ActionRegistry;

import myapp.presentationmodel.PMDescription;

import myapp.util.veneer.PresentationModelVeneer;

/**
 * @author Dieter Holz
 */
public abstract class Controller extends DolphinServerAction implements DolphinMixin, DTOMixin {
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

    protected void initializeController() {
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

    protected ServerPresentationModel createPM(String pmId, String pmType, DTO dto) {
        return getServerDolphin().presentationModel(pmId, pmType, dto);
    }

    protected ServerPresentationModel createPM(PMDescription type, DTO dto) {
        long id = entityID(dto);
        return createPM(type, id, dto);
    }

    protected ServerPresentationModel createPM(PMDescription type, long id, DTO dto) {
        return getServerDolphin().presentationModel(type.pmId(id), type.getName(), dto);
    }

    protected ServerPresentationModel createProxyPM(PMDescription pmDescription, long id) {
        List<Slot> proxySlots = createProxySlots(pmDescription);

        return createPM(pmDescription.pmId(id),
                        "Proxy:" + pmDescription.getName(),
                        new DTO(proxySlots));
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


    protected List<Slot> createProxySlots(PMDescription pmType) {
        List<Slot> slots = new ArrayList<>();

        Arrays.stream(pmType.getAttributeDescriptions()).forEach(att -> {
            slots.add(new Slot(att.name(), getInitialValue(att)     , null                , Tag.VALUE));
            slots.add(new Slot(att.name(), att.name().toLowerCase() , att.labelQualifier(), Tag.LABEL));
            slots.add(new Slot(att.name(), att.getValueType().name(), null                , Tag.VALUE_TYPE));
            slots.add(new Slot(att.name(), false                    , null                , Tag.MANDATORY));
            slots.add(new Slot(att.name(), true                     , null                , AdditionalTag.VALID));
            slots.add(new Slot(att.name(), null                     , null                , AdditionalTag.VALIDATION_MESSAGE));
            slots.add(new Slot(att.name(), false                    , null                , AdditionalTag.READ_ONLY));
            slots.add(new Slot(att.name(), ""                       , null                , AdditionalTag.USER_FACING_STRING));
        });

        return slots;
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
