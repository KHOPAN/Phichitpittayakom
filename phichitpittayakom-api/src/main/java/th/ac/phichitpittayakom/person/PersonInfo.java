package th.ac.phichitpittayakom.person;

import th.ac.phichitpittayakom.name.Name;

public class PersonInfo extends Person {
	private final String education;
	private final String majors;
	private final String phone;
	private final String email;

	public PersonInfo() {
		this.education = "";
		this.majors = "";
		this.phone = "";
		this.email = "";
	}

	public PersonInfo(Name name, String position, String imageIdentifier, String personIdentifier, String education, String majors, String phone, String email) {
		super(name, position, imageIdentifier, personIdentifier);
		this.education = education;
		this.majors = majors;
		this.phone = phone;
		this.email = email;
	}

	public String getEducation() {
		return this.education;
	}

	public String getMajors() {
		return this.majors;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getEmail() {
		return this.email;
	}

	@Override
	public String toString() {
		return this.getName() + (this.phone != null || !this.phone.isEmpty() ? (" " + this.phone) : "");
	}
}
