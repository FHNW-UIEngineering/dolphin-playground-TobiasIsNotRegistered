package myapp;

import org.opendolphin.core.Attribute;
import org.opendolphin.core.PresentationModel;

/**
 * @author Dieter Holz
 */
public class PersonVeneer {

	private final PresentationModel pm;

	public PersonVeneer(PresentationModel pm){
		this.pm = pm;
	}

	public Attribute firstName() {
		return pm.getAt(PersonPM.ATT.FIRSTNAME.name());
	}

	public String getFirstName() {
		return (String) firstName().getValue();
	}

	public void setFirstName(String firstName) {
		firstName().setValue(firstName);
	}

	public PresentationModel getPresentationModel(){
		return pm;
	}

}
