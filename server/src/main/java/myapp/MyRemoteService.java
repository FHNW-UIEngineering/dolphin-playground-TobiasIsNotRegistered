package myapp;

public class MyRemoteService implements MyService {
    @Override
    public void myServiceMethod(PersonVeneer person) {
        System.out.println("Server received Person with first name = " + person.getFirstName());
    }
}
