package th.ac.phichitpittayakom.grade;

public class GradeParser {
	private final Grade grade;

	private GradeParser(String grade) {
		if(grade == null || grade.isEmpty()) {
			this.grade = new Grade();
			return;
		}

		try {
			grade = grade.substring(2).trim();
		} catch(IndexOutOfBoundsException Exception) {
			this.grade = new Grade();
			return;
		}

		String[] split = grade.split("/");

		if(split.length < 2) {
			this.grade = new Grade();
			return;
		}

		int gradeNumber = 0;
		int room = 0;

		try {
			gradeNumber = Integer.parseInt(split[0]);
		} catch(NumberFormatException ignored) {

		}

		try {
			room = Integer.parseInt(split[1]);
		} catch(NumberFormatException ignored) {

		}

		this.grade = new Grade(gradeNumber, room);
	}

	@Override
	public String toString() {
		return this.grade.toString();
	}

	public static Grade parse(String grade) {
		return new GradeParser(grade).grade;
	}
}
