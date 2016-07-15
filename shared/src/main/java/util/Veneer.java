package util;

import org.opendolphin.core.PresentationModel;

/**
 * Capturing the essentials of a veneer: it has a backing presentation model as an immutable object state.
 */

public class Veneer {

    private final PresentationModel pm;

   	public Veneer(PresentationModel pm){
   		this.pm = pm;
   	}

    public PresentationModel getPresentationModel(){
   		return pm;
   	}
}
