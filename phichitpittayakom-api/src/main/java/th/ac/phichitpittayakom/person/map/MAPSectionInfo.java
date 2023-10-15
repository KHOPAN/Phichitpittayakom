package th.ac.phichitpittayakom.person.map;

import th.ac.phichitpittayakom.person.Person;

public class MAPSectionInfo extends MAPSection {
	private final Person leader;
	private final Person[] people;

	public MAPSectionInfo() {
		this.leader = new Person();
		this.people = new Person[0];
	}

	public MAPSectionInfo(String name, String identifier, Person leader, Person[] people) {
		super(name, identifier);
		this.leader = leader;
		this.people = people;
	}

	public Person getLeader() {
		return this.leader;
	}

	public Person[] getPeople() {
		return this.people;
	}
}
