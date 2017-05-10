package myapp.service.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.opendolphin.core.server.DTO;
import org.opendolphin.core.server.Slot;

import myapp.presentationmodel.PMDescription;
import myapp.util.AttributeDescription;

/**
 * @author Dieter Holz
 */
public interface DTOMixin {

    default long createNewId() {
        return System.currentTimeMillis();
    }

    default Slot createSlot(AttributeDescription att, Object value, long entityId) {
        return new Slot(att.name(), value, att.qualifier(entityId));
    }

    default DTO createDTO(PMDescription pmDescription){
        long id = createNewId();
        List<Slot> slots = Arrays.stream(pmDescription.getAttributeDescriptions())
                                   .map(attributeDescription -> createSlot(attributeDescription, getInitialValue(attributeDescription), id))
                                   .collect(Collectors.toList());
        return new DTO(slots);
    }

    default long entityID(DTO dto) {
        return entityId(dto.getSlots().get(0).getQualifier());
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
