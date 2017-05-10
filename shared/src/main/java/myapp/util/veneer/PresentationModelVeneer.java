package myapp.util.veneer;

import org.opendolphin.core.BasePresentationModel;
import org.opendolphin.core.PresentationModel;

/**
 * Capturing the essentials of a veneer: it has a backing presentation model as an immutable object state.
 */

public class PresentationModelVeneer {

    private final BasePresentationModel pm;

    public PresentationModelVeneer(BasePresentationModel pm) {
        this.pm = pm;
    }

    public PresentationModel getPresentationModel() {
        return pm;
    }

    public void rebase(){
       pm.rebase();
    }

    public void reset(){
        pm.reset();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PresentationModelVeneer veneer = (PresentationModelVeneer) o;

        return pm.equals(veneer.pm);
    }

    @Override
    public int hashCode() {
        return pm.hashCode();
    }
}
