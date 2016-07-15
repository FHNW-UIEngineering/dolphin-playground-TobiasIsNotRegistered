package myapp;

/**
 * Service interface that will be implemented differently for combined, remote, and test scenarios.
 */
public interface MyService {
    void myServiceMethod(PersonVeneer person);
}
