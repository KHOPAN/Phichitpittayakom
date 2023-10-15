package th.ac.phichitpittayakom.student;

import th.ac.phichitpittayakom.grade.Grade;
import th.ac.phichitpittayakom.name.Name;

public class Student {
	private final long studentIdentifier;
	private final Name name;
	private final Grade grade;
	private final int number;
	private final long guildIdentifier;

	public Student() {
		this.studentIdentifier = 0L;
		this.name = new Name();
		this.grade = new Grade();
		this.number = 0;
		this.guildIdentifier = 0L;
	}

	public Student(long studentIdentifier, Name name, Grade grade, int number, long guildIdentifier) {
		this.studentIdentifier = studentIdentifier;
		this.name = name;
		this.grade = grade;
		this.number = number;
		this.guildIdentifier = guildIdentifier;
	}

	public long getStudentIdentifier() {
		return this.studentIdentifier;
	}

	public Name getName() {
		return this.name;
	}

	public boolean isMale() {
		return this.name.isMale();
	}

	public boolean isFemale() {
		return this.name.isFemale();
	}

	public Grade getGrade() {
		return this.grade;
	}

	public int getNumber() {
		return this.number;
	}

	public long getGuildIdentifier() {
		return this.guildIdentifier;
	}

	@Override
	public String toString() {
		return this.studentIdentifier + " " + this.name + " " + this.grade + " เลขที่ " + this.number;
	}

	public static int compare(Student x, Student y) {
		if(x == null && y == null) {
			return 0;
		} else if(x == null) {
			return -1;
		} else if(y == null) {
			return 1;
		}

		int compare = Grade.compare(x.grade, y.grade);

		if(compare == 0) {
			return Integer.compare(x.number, y.number);
		}

		return compare;
	}
}
