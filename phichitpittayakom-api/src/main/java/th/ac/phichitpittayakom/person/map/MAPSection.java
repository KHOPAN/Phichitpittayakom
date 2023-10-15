package th.ac.phichitpittayakom.person.map;

import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;

public class MAPSection {
	private final String name;
	private final String identifier;

	public MAPSection() {
		this.name = "";
		this.identifier = "";
	}

	public MAPSection(String name, String identifier) {
		this.name = name;
		this.identifier = identifier;
	}

	public String getName() {
		return this.name;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public Optional<MAPSectionInfo> getAdditionalInfo() {
		return Phichitpittayakom.school.findMAPSectionById(this.identifier);
	}

	@Override
	public String toString() {
		return this.name;
	}
}
