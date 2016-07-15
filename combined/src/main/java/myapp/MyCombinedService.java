package myapp;

public class MyCombinedService implements MyService {
    @Override
    public void myServiceMethod(Person person) {
        System.out.println("Combined received person with first name = " + person.getFirstName());
    }
}
