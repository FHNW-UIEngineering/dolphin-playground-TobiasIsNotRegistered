package myapp;

import java.util.Random;

import myapp.presentationmodel.rockets.RocketAtt;
import org.opendolphin.core.server.DTO;

import myapp.service.SomeService;
import myapp.util.DTOMixin;

public class SomeCombinedService implements SomeService, DTOMixin {
    String[] names = {
            " PGM-11 Redstone   ",
            " SM-65 Atlas ",
            " PMG-19 Jupiter ",
            " PMG-17 Thor ",
            " SM-65A Atlas ",
            " Juno I ",
            " SM-65B Atlas ",
            " Thor-Able ",
            " Juno II ",
            " SM-65C Atlas ",
            " Titan I ",
            " SM-65D Atlas ",
            " Atlas-Able "};

    String[] missions = {
            "(mission)PGM-11 Redstone   ",
            "(mission)SM-65 Atlas ",
            "(mission)PMG-19 Jupiter ",
            "(mission)PMG-17 Thor ",
            "(mission)SM-65A Atlas ",
            "(mission)Juno I ",
            "(mission)SM-65B Atlas ",
            "(mission)Thor-Able ",
            "(mission)Juno II ",
            "(mission)SM-65C Atlas ",
            "(mission)Titan I ",
            "(mission)SM-65D Atlas ",
            "(mission)Atlas-Able "};


    String[] launches = {
            "(launch)PGM-11 Redstone   ",
            "(launch)SM-65 Atlas ",
            "(launch)PMG-19 Jupiter ",
            "(launch)PMG-17 Thor ",
            "(launch)SM-65A Atlas ",
            "(launch)Juno I ",
            "(launch)SM-65B Atlas ",
            "(launch)Thor-Able ",
            "(launch)Juno II ",
            "(launch)SM-65C Atlas ",
            "(launch)Titan I ",
            "(launch)SM-65D Atlas ",
            "(launch)Atlas-Able "};


    @Override
    public DTO loadSomeEntity() {
        long id = createNewId();

        Random r = new Random();
        int getIndex = r.nextInt(names.length);
        String name = names[getIndex];
        String mission = missions[getIndex];
        String launch = launches[getIndex];

        return new DTO(createSlot(RocketAtt.ID, id, id),
                createSlot(RocketAtt.NAME, name, id),
                createSlot(RocketAtt.MISSION, mission, id),
                createSlot(RocketAtt.LAUNCH, launch, id));
    }

}
