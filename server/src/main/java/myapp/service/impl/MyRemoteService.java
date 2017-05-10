package myapp.service.impl;

import myapp.service.MyService;

public class MyRemoteService implements MyService {
    @Override
    public void myServiceMethod() {
        System.out.println("Server received Person with first name = " );
    }
}
