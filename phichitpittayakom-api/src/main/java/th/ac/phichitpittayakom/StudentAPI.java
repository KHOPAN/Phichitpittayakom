package th.ac.phichitpittayakom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import th.ac.phichitpittayakom.grade.Grade;
import th.ac.phichitpittayakom.grade.GradeParser;
import th.ac.phichitpittayakom.guild.Guild;
import th.ac.phichitpittayakom.guild.GuildInfo;
import th.ac.phichitpittayakom.name.Name;
import th.ac.phichitpittayakom.name.NameParser;
import th.ac.phichitpittayakom.nationalid.NationalID;
import th.ac.phichitpittayakom.student.Student;

public class StudentAPI {
	StudentAPI() {}

	public List<Student> getAllStudent() {
		List<Guild> guildList = Phichitpittayakom.guild.getAllGuild();
		List<Thread> threadList = new ArrayList<>();
		List<Student> studentList = new ArrayList<>();

		for(int i = 0; i < guildList.size(); i++) {
			Guild guild = guildList.get(i);
			Thread thread = new Thread(() -> {
				Optional<GuildInfo> optional = guild.getAdditionalInfo();

				if(optional.isPresent()) {
					GuildInfo information = optional.get();
					studentList.addAll(Arrays.asList(information.getMembers()));
				}
			});

			thread.start();
			threadList.add(thread);
		}

		for(Thread thread : threadList) {
			try {
				thread.join();
			} catch(InterruptedException ignored) {

			}
		}

		studentList.sort(Student :: compare);
		return studentList;
	}

	public Optional<Student> findStudentById(long studentIdentifier) {
		Document document;

		try {
			document = Jsoup.connect("https://sppk.sangsiri.net/activity/act_std_search.php")
					.data("cMethod", "S1")
					.data("optSeach1", "std_id")
					.data("txt_Search", Long.toString(studentIdentifier))
					.data("submit", "%04I%19%2B2")
					.post();

		} catch(IOException ignored) {
			return Optional.empty();
		}

		Elements elements = document.select("tr[align=center]");

		if(elements.size() < 1) {
			return Optional.empty();
		}

		return this.processStudentInternal(elements.get(0));
	}

	public Optional<Student> findStudentByNationalID(NationalID nationalIdentifier) {
		Document document;

		try {	
			document = Jsoup.connect("https://sppk.sangsiri.net/activity/act_std_search.php")
					.data("cMethod", "S1")
					.data("optSeach1", "citizen_id")
					.data("txt_Search", nationalIdentifier.toStringNoSpace())
					.data("submit", "%04I%19%2B2")
					.post();

		} catch(IOException ignored) {
			return Optional.empty();
		}

		Elements elements = document.select("tr[align=center]");

		if(elements.size() == 0) {
			return Optional.empty();
		}

		return this.processStudentInternal(elements.get(0));
	}

	public List<Student> findStudentsByName(String namePart) {
		List<Student> studentList = new ArrayList<>();
		Document document;

		try {
			document = Jsoup.connect("https://sppk.sangsiri.net/activity/act_std_search.php")
					.data("cMethod", "S1")
					.data("optSeach1", "std_fname_std_lname")
					.data("txt_Search", namePart)
					.data("submit", "%04I%19%2B2")
					.post();

		} catch(IOException ignored) {
			return studentList;
		}

		Elements elements = document.select("tr[align=center]");

		for(Element element : elements) {
			Optional<Student> optional = this.processStudentInternal(element);

			if(optional.isPresent()) {
				studentList.add(optional.get());
			}
		}

		return studentList;
	}

	private Optional<Student> processStudentInternal(Element element) {
		Elements elements = element.select("td");
		int size = elements.size();

		if(size == 0) {
			return Optional.empty();
		}

		try {
			long studentIdentifier = Long.parseLong(elements.get(1).text());
			Name name = NameParser.parse(elements.get(2).text());
			Grade grade = GradeParser.parse(elements.get(3).text());
			int number = Integer.parseInt(elements.get(4).text());
			String identifierText = elements.get(5).select("b").get(0).ownText();
			long guildIdentifier = Long.parseLong(identifierText.substring(1, identifierText.length() - 1));
			Student student = new Student(studentIdentifier, name, grade, number, guildIdentifier);
			return Optional.of(student);
		} catch(IndexOutOfBoundsException | NumberFormatException ignored) {
			return Optional.empty();
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
