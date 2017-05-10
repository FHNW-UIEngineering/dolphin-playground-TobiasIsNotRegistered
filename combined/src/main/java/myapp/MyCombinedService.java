package myapp;

import myapp.service.MyService;

public class MyCombinedService implements MyService {
    @Override
    public void myServiceMethod() {
        System.out.println("Combined received person with first name = " );
    }
}
