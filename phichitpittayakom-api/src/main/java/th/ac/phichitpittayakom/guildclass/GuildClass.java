package th.ac.phichitpittayakom.guildclass;

public enum GuildClass {
	CLASS_LOW("ม.ต้น"),
	CLASS_BOTH("ม.ต้น และ ม.ปลาย"),
	CLASS_HIGH("ม.ปลาย"),
	UNDEFINED("");

	private final String stringRepresentation;

	GuildClass(String stringRepresentation) {
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
