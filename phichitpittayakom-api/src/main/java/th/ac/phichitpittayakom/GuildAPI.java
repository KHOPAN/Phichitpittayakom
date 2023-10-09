package th.ac.phichitpittayakom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import th.ac.phichitpittayakom.goal.GoalParser;
import th.ac.phichitpittayakom.grade.Grade;
import th.ac.phichitpittayakom.grade.GradeParser;
import th.ac.phichitpittayakom.guildclass.GuildClass;
import th.ac.phichitpittayakom.guildclass.GuildClassParser;
import th.ac.phichitpittayakom.guildtype.GuildType;
import th.ac.phichitpittayakom.guildtype.GuildTypeParser;
import th.ac.phichitpittayakom.name.Name;
import th.ac.phichitpittayakom.name.NameListParser;
import th.ac.phichitpittayakom.name.NameParser;
import th.ac.phichitpittayakom.nationalid.NationalIDParser;

public class GuildAPI {
	GuildAPI() {}

	public List<Guild> getAllGuild() {
		List<Guild> list = new ArrayList<>();
		Document document;

		try {
			document = Jsoup.connect("https://sppk.sangsiri.net/activity/getData3.php").get();
		} catch(IOException ignored) {
			return list;
		}

		Elements elements = document.select("tr");
		int size = elements.size();

		if(size < 2) {
			return list;
		}

		for(int i = 1; i < size - 1; i++) {
			try {
				Elements guildElement = elements.get(i).select("td");
				Element nameElement = guildElement.get(1);
				String name = nameElement.ownText();
				String identifierText = nameElement.select("b").get(0).ownText();
				long identifier = Long.parseLong(identifierText.substring(1, identifierText.length() - 1));
				String location = guildElement.get(2).ownText();
				Name[] teachers = NameListParser.parse(guildElement.get(3).ownText());
				GuildClass guildClass = GuildClassParser.parse(guildElement.get(4).ownText());
				int memberCount = Integer.parseInt(guildElement.get(5).text());
				int remaining = Integer.parseInt(guildElement.get(6).text());
				int minimumMembers = Integer.parseInt(guildElement.get(7).text());
				int maximumMembers = Integer.parseInt(guildElement.get(8).text());
				list.add(new Guild(name, identifier, location, teachers, guildClass, memberCount, remaining, minimumMembers, maximumMembers));
			} catch(IndexOutOfBoundsException | NumberFormatException ignored) {
				continue;
			}
		}

		list.sort(Comparator.comparingLong(Guild :: getIdentifier));
		return list;
	}

	public List<GuildInfo> getAllGuildInfo() {
		List<GuildInfo> list = new ArrayList<>();
		Document document;

		try {
			document = Jsoup.connect("https://sppk.sangsiri.net/activity/getData3.php").get();
		} catch(IOException ignored) {
			return list;
		}

		Elements elements = document.select("tr");
		int size = elements.size();

		if(size == 0) {
			return list;
		}

		for(int i = 1; i < size - 1; i++) {
			try {
				String identifierText = elements.get(i).select("td").get(1).select("b").get(0).ownText();
				long identifier = Long.parseLong(identifierText.substring(1, identifierText.length() - 1));
				Optional<GuildInfo> optional = Phichitpittayakom.guild.findGuildById(identifier);

				if(optional.isPresent()) {
					list.add(optional.get());
				}
			} catch(IndexOutOfBoundsException | NumberFormatException ignored) {
				continue;
			}
		}

		list.sort(Comparator.comparingLong(Guild :: getIdentifier));
		return list;
	}

	public Optional<GuildInfo> findGuildById(long guildIdentifier) {
		Document document;

		try {
			document = Jsoup.connect("https://sppk.sangsiri.net/activity/act_list4.php?id=" + guildIdentifier).get();
		} catch(IOException ignored) {
			return Optional.empty();
		}

		try {
			Elements elements = document.select("tbody").get(3).select("tr");
			String name = elements.get(1).select("td").get(1).ownText();

			if(name.isEmpty()) {
				return Optional.empty();
			}

			String location = elements.get(2).select("td").get(1).ownText();
			GuildType type = GuildTypeParser.parse(elements.get(3).select("td").get(1).ownText());
			GuildClass guildClass = GuildClassParser.parse(elements.get(4).select("td").get(1).ownText());
			String subjectArea = elements.get(5).select("td").get(1).ownText();
			Element teacherElement = elements.get(6);
			Name[] teacherNames = NameListParser.parse(teacherElement.select("td").get(1).ownText());
			Teacher[] teachers = new Teacher[teacherNames.length];
			Elements imageElements = teacherElement.select("img");

			for(int i = 0; i < teachers.length; i++) {
				String imageIdentifier = imageElements.get(i).attr("src").substring(6, 19);
				teachers[i] = new Teacher(teacherNames[i], NationalIDParser.parse(imageIdentifier), guildIdentifier, imageIdentifier);
			}

			String[] goals = GoalParser.parse(elements.get(7).select("td").get(1).ownText());
			String minimumText = elements.get(8).select("td").get(1).text();
			int minimumMembers = Integer.parseInt(minimumText.substring(0, minimumText.length() - 2).trim());
			String maximumText = elements.get(9).select("td").get(1).text();
			int maximumMembers = Integer.parseInt(maximumText.substring(0, maximumText.length() - 2).trim());
			String memberCountText = elements.get(10).select("td").get(1).text();
			int memberCount = Integer.parseInt(memberCountText.substring(0, memberCountText.length() - 2).trim());
			String remainingText = elements.get(11).select("td").get(1).text();
			int remaining = Integer.parseInt(remainingText.substring(0, remainingText.length() - 2).trim());
			String note = elements.get(12).select("td").get(1).ownText();

			if("-".equals(note)) {
				note = "";
			}

			List<Student> studentList = new ArrayList<>();
			Elements studentElements = document.select("tr[align=center]");

			for(Element studentElement : studentElements) {
				Elements studentTableData = studentElement.select("td");
				long studentIdentifier = Long.parseLong(studentTableData.get(2).text());
				Name studentName = NameParser.parse(studentTableData.get(3).ownText());
				Grade grade = GradeParser.parse(studentTableData.get(4).text());
				int number = Integer.parseInt(studentTableData.get(5).text());
				Student student = new Student(studentIdentifier, studentName, grade, number, guildIdentifier);
				studentList.add(student);
			}

			return Optional.of(new GuildInfo(name, guildIdentifier, location, guildClass, memberCount, remaining, minimumMembers, maximumMembers, type, teachers, subjectArea, goals, note, studentList.toArray(new Student[0])));
		} catch(IndexOutOfBoundsException | NumberFormatException ignored) {
			return Optional.empty();
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
