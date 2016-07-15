package myapp;

public class MyRemoteService implements MyService {
    @Override
    public void myServiceMethod(Object message) {
        System.out.println("Server received message = " + message);
    }
}
