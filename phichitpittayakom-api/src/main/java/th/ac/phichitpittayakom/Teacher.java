package th.ac.phichitpittayakom;

import java.util.Optional;

import th.ac.phichitpittayakom.name.Name;
import th.ac.phichitpittayakom.nationalid.NationalID;

public class Teacher {
	private final Name name;
	private final NationalID nationalIdentifier;
	private final long guildIdentifier;
	private final String imageIdentifier;

	public Teacher() {
		this.name = new Name();
		this.nationalIdentifier = new NationalID();
		this.guildIdentifier = 0L;
		this.imageIdentifier = "";
	}

	Teacher(Name name, NationalID nationalIdentifier, long guildIdentifier, String imageIdentifier) {
		this.name = name;
		this.nationalIdentifier = nationalIdentifier;
		this.guildIdentifier = guildIdentifier;
		this.imageIdentifier = imageIdentifier;
	}

	public Name getName() {
		return this.name;
	}

	public NationalID getNationalIdentifier() {
		return this.nationalIdentifier;
	}

	public long getGuildIdentifier() {
		return this.guildIdentifier;
	}

	public String getImageIdentifier() {
		return this.imageIdentifier;
	}

	public Optional<byte[]> getImage() {
		return Phichitpittayakom.teacher.findTeacherImageById(this.imageIdentifier);
	}

	@Override
	public String toString() {
		return this.name + " [" + this.nationalIdentifier + "]";
	}

	public static int compare(Teacher x, Teacher y) {
		if(x == null && y == null) {
			return 0;
		} else if(x == null) {
			return -1;
		} else if(y == null) {
			return 1;
		}

		return NationalID.compare(x.nationalIdentifier, y.nationalIdentifier);
	}
}
