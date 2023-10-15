package th.ac.phichitpittayakom.name;

public class NameParser {
	private final Name name;

	private NameParser(String name) {
		if(name == null || name.isEmpty()) {
			this.name = new Name();
			return;
		}

		name = name.trim();
		NamePrefix[] namePrefixes = NamePrefix.values();

		for(NamePrefix namePrefix : namePrefixes) {
			for(String stringPrefix : namePrefix.getStringRepresentations()) {
				if(name.startsWith(stringPrefix)) {
					String nameText = "";

					try {
						nameText = name.substring(stringPrefix.length()).trim();
					} catch(IndexOutOfBoundsException Exception) {
						this.name = new Name();
						return;
					}

					String[] names = nameText.split("\\s+");
					this.name = new Name(namePrefix, names);
					return;
				}
			}
		}

		this.name = new Name();
	}

	@Override
	public String toString() {
		return this.name.toString();
	}

	public static Name parse(String name) {
		return new NameParser(name).name;
	}
}
