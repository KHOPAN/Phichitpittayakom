package th.ac.phichitpittayakom.person.map;

public class ManagementAndPersonnel {
	private final String title;
	private final MAPSection[] sections;

	public ManagementAndPersonnel(String title, MAPSection[] sections) {
		this.title = title;
		this.sections = sections;
	}

	public String getTitle() {
		return this.title;
	}

	public MAPSection[] getSections() {
		return this.sections;
	}

	@Override
	public String toString() {
		return this.title;
	}
}
