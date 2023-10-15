package th.ac.phichitpittayakom.name;

public enum NamePrefix {
	DEK_CHAI(true, "เด็กชาย", "ด.ช."),
	NAI(true, "นาย"),
	DEK_YING(false, "เด็กหญิง", "ด.ญ."),
	NANG_SAO(false, "นางสาว", "น.ส."),
	NANG(false, "นาง"),
	DOCTOR(true, "ดร."),
	MR(true, "Mr."),
	MISS(false, "Miss"),
	MS(false, "Ms."),
	MRS(false, "Mrs."),
	UNDEFINED(false, "");

	private final boolean isMale;
	private final String[] stringRepresentations;

	NamePrefix(boolean isMale, String... stringRepresentations) {
		this.isMale = isMale;
		this.stringRepresentations = stringRepresentations;
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

	public String[] getStringRepresentations() {
		return this.stringRepresentations;
	}

	@Override
	public String toString() {
		return this.stringRepresentations[0];
	}
}
