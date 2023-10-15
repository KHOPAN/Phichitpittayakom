package th.ac.phichitpittayakom.name;

public class Name {
	private final NamePrefix prefix;
	private final String[] names;

	public Name() {
		this.prefix = NamePrefix.UNDEFINED;
		this.names = new String[0];
	}

	public Name(NamePrefix prefix, String[] names) {
		this.prefix = prefix;
		this.names = names;
	}

	public NamePrefix getPrefix() {
		return this.prefix;
	}

	public boolean isMale() {
		return this.prefix.isMale();
	}

	public boolean isFemale() {
		return this.prefix.isFemale();
	}

	public String[] getNames() {
		return this.names;
	}

	public String getFirstName() {
		if(this.names.length == 0) {
			return "";
		}

		return this.names[0];
	}

	public String getMiddleName() {
		if(this.names.length < 3) {
			return "";
		}

		return this.names[1];
	}

	public String getLastName() {
		if(this.names.length == 0) {
			return "";
		}

		return this.names[this.names.length - 1];
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.prefix);

		for(String name : this.names) {
			builder.append(' ');
			builder.append(name);
		}

		return builder.toString();
	}
}
