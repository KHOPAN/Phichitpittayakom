package th.ac.phichitpittayakom.guildtype;

public enum GuildType {
	NEWLY_ESTABLISHED("ตั้งใหม่"),
	CONTINUOUS("ต่อเนื่อง"),
	UNDEFINED("");

	private final String stringRepresentation;

	GuildType(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

	public String getStringRepresentation() {
		return this.stringRepresentation;
	}

	@Override
	public String toString() {
		return this.stringRepresentation;
	}
}
