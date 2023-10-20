package th.ac.phichitpittayakom.person.map;

import th.ac.phichitpittayakom.person.Person;

public class MAPSectionInfo extends MAPSection {
	private final Person leader;
	private final Person[] people;
	private final String leaderText;

	public MAPSectionInfo() {
		this.leader = new Person();
		this.people = new Person[0];
		this.leaderText = "";
	}

	public MAPSectionInfo(String name, String identifier, Person leader, Person[] people, String leaderText) {
		super(name, identifier);
		this.leader = leader;
		this.people = people;
		this.leaderText = leaderText;
	}

	public Person getLeader() {
		return this.leader;
	}

	public Person[] getPeople() {
		return this.people;
	}

	public String getLeaderText() {
		return this.leaderText;
	}
}
