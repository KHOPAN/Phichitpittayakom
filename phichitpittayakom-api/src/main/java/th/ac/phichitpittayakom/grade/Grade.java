package th.ac.phichitpittayakom.grade;

public class Grade {
	private final int grade;
	private final int room;

	public Grade() {
		this.grade = 0;
		this.room = 0;
	}

	Grade(int grade, int room) {
		this.grade = grade;
		this.room = room;
	}

	public int getGrade() {
		return this.grade;
	}

	public int getRoom() {
		return this.room;
	}

	@Override
	public String toString() {
		return "ม." + this.grade + "/" + this.room;
	}

	public static int compare(Grade x, Grade y) {
		if(x.grade == y.grade) {
			return Integer.compare(x.room, y.room);
		}

		return Integer.compare(x.grade, y.grade);
	}
}
