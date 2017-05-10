package myapp.service;

import java.util.List;

import org.opendolphin.core.server.DTO;

/**
 * Service interface that will be implemented differently for combined, remote, and test scenarios.
 */
public interface SomeService {
    DTO loadSomeEntity();

    void save(List<DTO> dtos);

}
