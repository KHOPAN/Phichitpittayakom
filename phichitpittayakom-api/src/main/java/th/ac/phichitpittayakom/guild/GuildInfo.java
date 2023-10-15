package th.ac.phichitpittayakom.guild;

import th.ac.phichitpittayakom.guildclass.GuildClass;
import th.ac.phichitpittayakom.guildtype.GuildType;
import th.ac.phichitpittayakom.name.Name;
import th.ac.phichitpittayakom.student.Student;
import th.ac.phichitpittayakom.teacher.Teacher;

public class GuildInfo extends Guild {
	private final GuildType type;
	private final String subjectArea;
	private final Teacher[] teachers;
	private final String[] goals;
	private final String note;
	private final Student[] members;

	public GuildInfo() {
		this.type = GuildType.UNDEFINED;
		this.subjectArea = "";
		this.teachers = new Teacher[0];
		this.goals = new String[0];
		this.note = "";
		this.members = new Student[0];
	}

	public GuildInfo(String name, long identifier, String location, GuildClass guildClass, int memberCount, int remaining, int minimumMembers, int maximumMembers, GuildType type, Teacher[] teachers, String subjectArea, String[] goals, String note, Student[] members) {
		super(name, identifier, location, GuildInfo.convertTeacherName(teachers), guildClass, memberCount, remaining, minimumMembers, maximumMembers);
		this.type = type;
		this.subjectArea = subjectArea;
		this.teachers = teachers;
		this.goals = goals;
		this.note = note;
		this.members = members;
	}

	public GuildType getType() {
		return this.type;
	}

	public String getSubjectArea() {
		return this.subjectArea;
	}

	public Teacher[] getTeachers() {
		return this.teachers;
	}

	public String[] getGoals() {
		return this.goals;
	}

	public String getNote() {
		return this.note;
	}

	public Student[] getMembers() {
		return this.members;
	}

	private static Name[] convertTeacherName(Teacher[] teachers) {
		Name[] names = new Name[teachers.length];

		for(int i = 0; i < names.length; i++) {
			names[i] = teachers[i].getName();
		}

		return names;
	}
}
