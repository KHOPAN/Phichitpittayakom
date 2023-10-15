package th.ac.phichitpittayakom.person;

import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.name.Name;

public class Person {
	private final Name name;
	private final String position;
	private final String imageIdentifier;
	private final String personIdentifier;

	public Person() {
		this.name = new Name();
		this.position = "";
		this.imageIdentifier = "";
		this.personIdentifier = "";
	}

	public Person(Name name, String position, String imageIdentifier, String personIdentifier) {
		this.name = name;
		this.position = position;
		this.imageIdentifier = imageIdentifier;
		this.personIdentifier = personIdentifier;
	}

	public Name getName() {
		return this.name;
	}

	public String getPosition() {
		return this.position;
	}

	public String getImageIdentifier() {
		return this.imageIdentifier;
	}

	public Optional<byte[]> getImage() {
		return Phichitpittayakom.school.findTeacherImageById(this.imageIdentifier);
	}

	public String getPersonIdentifier() {
		return this.personIdentifier;
	}

	@Override
	public String toString() {
		return this.name + " " + this.position;
	}
}
