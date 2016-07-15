package myapp;

public class MyCombinedService implements MyService {
    @Override
    public void myServiceMethod(PersonVeneer person) {
        System.out.println("Combined received person with first name = " + person.getFirstName());
    }
}
