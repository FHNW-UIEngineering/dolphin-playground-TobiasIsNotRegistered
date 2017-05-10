package myapp.service.impl;

import java.util.List;
import java.util.Random;

import org.opendolphin.core.server.DTO;

import myapp.presentationmodel.person.PersonAtt;
import myapp.service.SomeService;
import myapp.service.util.DTOMixin;

public class SomeRemoteService implements SomeService, DTOMixin {
    String[] names = {"Neil Amstrong"  , "Michael Collins" , "Edwin Aldrin",      // Apollo 11
                      "Charles Conrad" , "Richard Gordon"  , "Alan Bean",         // Apollo 12
                      "James Lovell"   , "John Swigert"    , "Fred Haise",        // Apollo 13
                      "Alan Shepard"   , "Stuart Roosa"    , "Edgar Mitchell",    // Apollo 14
                      "David Scott"    , "Alfred Worden"   , "James Irwin",       // Apollo 15
                      "John Young"     , "Thomas Mattingly", "Charles Duke",      // Apollo 16
                      "Eugene Cernan"  , "Ronald Evans"    , "Harrison Schmitt"}; // Apollo 17

    @Override
    public DTO loadSomeEntity() {
        long id = createNewId();

        Random r = new Random();
        return new DTO(createSlot(PersonAtt.ID      , id                            , id),
                       createSlot(PersonAtt.NAME    , names[r.nextInt(names.length)], id),
                       createSlot(PersonAtt.AGE     , r.nextInt(43)                 , id),
                       createSlot(PersonAtt.IS_ADULT, false                         , id));
    }

    @Override
    public void save(List<DTO> dtos) {
        System.out.println(" Data to be saved");
        dtos.stream()
            .flatMap(dto -> dto.getSlots().stream())
            .map(slot -> String.join(", ", slot.getPropertyName(), slot.getValue().toString(), slot.getQualifier()))
            .forEach(System.out::println);
    }
}
