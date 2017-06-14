package myapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.opendolphin.core.server.DTO;
import org.opendolphin.core.server.DefaultServerDolphin;
import org.opendolphin.core.server.ServerConnector;
import org.opendolphin.core.server.ServerModelStore;
import org.opendolphin.core.server.ServerPresentationModel;

import myapp.presentationmodel.PMDescription;
import myapp.presentationmodel.person.Person;
import myapp.presentationmodel.person.RocketAtt;
import myapp.service.SomeService;
import myapp.util.DTOMixin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Dieter Holz
 */
public class PersonControllerTest {
    PersonController controller;
    ServerModelStore serverModelStore;
    ServiceStub service;

    @Before
    public void setup() {
        service = new ServiceStub();
        controller = new PersonController(service);

        serverModelStore = new TestModelStore();
        controller.setServerDolphin(new DefaultServerDolphin(serverModelStore, new ServerConnector()));
    }

    @Test
    public void testInitializeBasePMs(){
        //given

        //when
        controller.initializeBasePMs();
        Person p = controller.getPersonProxy();

        //then
        assertNotNull(p);
        assertFalse(p.isDirty());
    }

    @Test
    public void testDirtyState(){
        //given
        controller.initializeBasePMs();
        Person p = controller.getPersonProxy();
        String name = p.name.getValue();

        //when
        p.name.setValue(name + " some new value");

        //then
        assertTrue(p.isDirty());
        assertTrue(p.name.isDirty());
    }

    @Test
    public void testLoadPerson(){
        //given
        controller.initializeBasePMs();
        Person p = controller.getPersonProxy();
        p.name.setValue("bla");

        //when
        ServerPresentationModel pm = controller.loadPerson();

        //then
        assertFalse(p.isDirty());

        //when
        p.name.setValue("xyz");

        //then
        assertEquals("xyz", pm.getAt(RocketAtt.NAME.name()).getValue());

    }

    @Test
    public void testSave(){
        //given
        controller.initializeBasePMs();
        Person p = controller.getPersonProxy();
        controller.loadPerson();

        p.name.setValue("abc");

        //when
        controller.save();

        //then
        assertEquals(1, service.saveCounter);
        assertFalse(p.isDirty());
    }

    private class ServiceStub implements SomeService, DTOMixin {
        int saveCounter;

        @Override
        public DTO loadSomeEntity() {
            return createDTO(PMDescription.PERSON);
        }

        @Override
        public void save(List<DTO> dtos) {
            saveCounter++;
        }
    }

    private class TestModelStore extends ServerModelStore{
        TestModelStore(){
            setCurrentResponse(new ArrayList<>());
        }
    }



}