package th.ac.phichitpittayakom.name;

public enum NamePrefix {
	DEK_CHAI("เด็กชาย", true),
	NAI("นาย", true),
	DEK_YING("เด็กหญิง", false),
	NANG_SAO("นางสาว", false),
	NANG("นาง", false),
	MR("Mr.", true),
	MISS("Miss", false),
	MS("Ms.", false),
	MRS("Mrs.", false),
	UNDEFINED("", false);

	private final String stringRepresentation;
	private final boolean isMale;

	NamePrefix(String stringRepresentation, boolean isMale) {
		this.stringRepresentation = stringRepresentation;
		this.isMale = isMale;
	}

	public String getStringRepresentation() {
		return this.stringRepresentation;
	}

	public boolean isMale() {
		return this.isMale;
	}

	public boolean isFemale() {
		if(NamePrefix.UNDEFINED.equals(this)) {
			return false;
		}

		return !this.isMale;
	}

	@Override
	public String toString() {
		return this.stringRepresentation;
	}
}
