package th.ac.phichitpittayakom.teacher;

import java.time.LocalDate;
import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.name.Name;
import th.ac.phichitpittayakom.nationalid.NationalID;

public class Teacher {
	private final Name name;
	private final NationalID nationalIdentifier;
	private final long guildIdentifier;
	private final String imageIdentifier;
	private final String birthday;
	private final LocalDate birthdate;

	public Teacher() {
		this.name = new Name();
		this.nationalIdentifier = new NationalID();
		this.guildIdentifier = 0L;
		this.imageIdentifier = "";
		this.birthday = "";
		this.birthdate = null;
	}

	public Teacher(Name name, NationalID nationalIdentifier, long guildIdentifier, String imageIdentifier, String birthday) {
		this.name = name;
		this.nationalIdentifier = nationalIdentifier;
		this.guildIdentifier = guildIdentifier;
		this.imageIdentifier = imageIdentifier;
		this.birthday = birthday;

		if(this.birthday == null || this.birthday.length() < 6) {
			this.birthdate = null;
			return;
		}

		this.birthdate = LocalDate.of(Integer.parseInt(this.birthday.substring(4, 8)) - 543, Integer.parseInt(this.birthday.substring(2, 4)), Integer.parseInt(this.birthday.substring(0, 2)));
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
		return Phichitpittayakom.school.findImageById(this.imageIdentifier);
	}

	public String getBirthday() {
		return this.birthday;
	}

	public LocalDate getBirthdate() {
		return this.birthdate;
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
