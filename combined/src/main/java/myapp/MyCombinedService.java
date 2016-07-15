package myapp;

public class MyCombinedService implements MyService {
    @Override
    public void myServiceMethod(Object message) {
        System.out.println("Combined received message = " + message);
    }
}
