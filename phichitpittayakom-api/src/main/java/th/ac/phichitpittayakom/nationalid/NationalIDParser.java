package th.ac.phichitpittayakom.nationalid;

import java.util.ArrayList;
import java.util.List;

public class NationalIDParser {
	private final NationalID nationalIdentifier;

	private NationalIDParser(String nationalIdentifier) {
		if(nationalIdentifier == null || nationalIdentifier.isEmpty()) {
			this.nationalIdentifier = new NationalID();
			return;
		}

		nationalIdentifier = nationalIdentifier.trim();
		List<Integer> digitList = new ArrayList<>();

		for(int i = 0; i < nationalIdentifier.length(); i++) {
			char character = nationalIdentifier.charAt(i);

			if(character == '0' || character == '1' || character == '2' || character == '3' || character == '4' || character == '5' || character == '6' || character == '7' || character == '8' || character == '9') {
				try {
					digitList.add(Integer.parseInt(Character.toString(character)));
				} catch(NumberFormatException ignored) {
					continue;
				}
			}
		}

		if(digitList.size() < 13) {
			this.nationalIdentifier = new NationalID();
			return;
		}

		this.nationalIdentifier = new NationalID(digitList.get(0), digitList.get(1), digitList.get(2), digitList.get(3), digitList.get(4), digitList.get(5), digitList.get(6), digitList.get(7), digitList.get(8), digitList.get(9), digitList.get(10), digitList.get(11), digitList.get(12));
	}

	@Override
	public String toString() {
		return this.nationalIdentifier.toString();
	}

	public static NationalID parse(String nationalIdentifier) {
		return new NationalIDParser(nationalIdentifier).nationalIdentifier;
	}
}
