package th.ac.phichitpittayakom.name;

import java.util.ArrayList;
import java.util.List;

public class NameListParser {
	private final Name[] names;

	private NameListParser(String names) {
		if(names == null || names.isEmpty()) {
			this.names = new Name[0];
			return;
		}

		List<Name> resultList = new ArrayList<>();
		String[] nameSplit = names.split("\\s+");
		List<String> namePart = new ArrayList<>();
		NamePrefix[] namePrefixes = NamePrefix.values();
		int maxIndex = nameSplit.length - 1;
		int expected = 1;

		for(int i = 0; i < nameSplit.length; i++) {
			String name = nameSplit[i];

			if(name.startsWith(expected + ".") || i == maxIndex) {
				expected++;

				if(i == maxIndex) {
					namePart.add(name);
				}

				if(i != 0) {
					int size = namePart.size();

					if(size == 0) {
						continue;
					}

					String firstPart = namePart.get(0);

					loop: for(NamePrefix namePrefix : namePrefixes) {
						for(String stringPrefix : namePrefix.getStringRepresentations()) {
							if(firstPart.startsWith(stringPrefix)) {
								List<String> nameList = new ArrayList<>();

								try {
									nameList.add(firstPart.substring(stringPrefix.length()));
								} catch(IndexOutOfBoundsException ignored) {
									continue;
								}

								if(size > 1) {
									for(int t = 1; t < size; t++) {
										nameList.add(namePart.get(t));
									}
								}

								resultList.add(new Name(namePrefix, nameList.toArray(new String[0])));
								break loop;
							}
						}
					}

					namePart.clear();
				}
			} else {
				namePart.add(name);
			}
		}

		this.names = resultList.toArray(new Name[0]);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for(int i = 0; i < this.names.length; i++) {
			if(i != 0) {
				builder.append(", ");
			}

			builder.append(this.names[i]);
		}

		return builder.toString();
	}

	public static Name[] parse(String names) {
		return new NameListParser(names).names;
	}
}
