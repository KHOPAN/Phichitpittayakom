package th.ac.phichitpittayakom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import th.ac.phichitpittayakom.name.Name;
import th.ac.phichitpittayakom.name.NameParser;
import th.ac.phichitpittayakom.person.Person;
import th.ac.phichitpittayakom.person.PersonInfo;
import th.ac.phichitpittayakom.person.map.MAPSection;
import th.ac.phichitpittayakom.person.map.MAPSectionInfo;
import th.ac.phichitpittayakom.person.map.ManagementAndPersonnel;

public class SchoolAPI {
	SchoolAPI() {}

	public Optional<ManagementAndPersonnel> getManagementAndPersonnel() {
		Document document;

		try {
			document = Jsoup.connect("http://phichitpittayakom.ac.th/th").get();
		} catch(IOException ignored) {
			return Optional.empty();
		}

		try {
			Element root = document.select("tbody").get(5);
			String title = root.select("td").get(0).text();
			Elements elements = root.select("li");
			List<MAPSection> sectionList = new ArrayList<>();

			for(Element element : elements) {
				String name = element.text();
				String identifier = element.select("a").attr("href");
				sectionList.add(new MAPSection(name, identifier));
			}

			return Optional.of(new ManagementAndPersonnel(title, sectionList.toArray(new MAPSection[0])));
		} catch(IndexOutOfBoundsException ignored) {
			return Optional.empty();
		}
	}

	public Optional<MAPSectionInfo> findMAPSectionById(String mapSectionIdentifier) {
		Document document;

		try {
			document = Jsoup.connect("http://phichitpittayakom.ac.th/" + mapSectionIdentifier).get();
		} catch(IOException ignored) {
			return Optional.empty();
		}

		try {
			Elements elements = document.select(".content").get(0).select("tbody");
			String title = elements.get(0).text();
			Elements data = elements.get(1).select("td");
			List<Person> personList = new ArrayList<>();
			Element leader = data.get(0);
			List<TextNode> list = new ArrayList<>();
			Element div = leader.select("div").get(0);
			list.addAll(div.textNodes());
			list.removeIf(node -> node.isBlank());
			Name leaderName = NameParser.parse(list.get(0).text().trim());
			String leaderPosition = list.get(1).text().trim();
			String leaderText = "";

			if(list.size() > 2) {
				leaderText = list.get(2).text().trim();

				if(leaderText.startsWith("เบอร์โทร")) {
					leaderText = leaderPosition;
				}
			} else {
				leaderText = div.select("font").text().trim();
			}

			Element anchor = div.select("a").get(0);
			Person leaderPerson = new Person(leaderName, leaderPosition, anchor.select("img").get(0).attr("src"), anchor.attr("href"));

			for(int i = 1; i < data.size(); i++) {
				Element element = data.get(i);
				List<TextNode> nodeList = new ArrayList<>();
				nodeList.addAll(element.textNodes());
				nodeList.removeIf(node -> node.isBlank());
				Name name = NameParser.parse(nodeList.get(0).text());
				String position = element.select("font").text().trim();
				String personIdentifier = element.select("a").attr("href");
				String imageIdentifier = element.select("img").attr("src");
				personList.add(new Person(name, position, imageIdentifier, personIdentifier));
			}

			return Optional.of(new MAPSectionInfo(title, mapSectionIdentifier, leaderPerson, personList.toArray(new Person[0]), leaderText));
		} catch(IndexOutOfBoundsException | NullPointerException ignored) {
			return Optional.empty();
		}
	}

	public Optional<byte[]> findImageById(String imageIdentifier) {
		Response response;

		try {
			response = Jsoup.connect(imageIdentifier).ignoreContentType(true).execute();
		} catch(IOException ignored) {
			return Optional.empty();
		}

		byte[] data = response.bodyAsBytes();

		if(data == null || data.length == 0) {
			return Optional.empty();
		}

		return Optional.of(data);
	}

	public Optional<PersonInfo> findPersonById(String personIdentifier) {
		Document document;

		try {
			document = Jsoup.connect("http://phichitpittayakom.ac.th/" + personIdentifier).get();
		} catch(IOException Exception) {
			return Optional.empty();
		}

		try {
			Elements tables = document.select(".content").get(0).select("tbody");
			String subjectArea = tables.get(0).select("td").get(1).text();
			Elements content = tables.get(3).select("tr");
			Name name = NameParser.parse(content.get(0).select("td").get(1).text());
			String position = content.get(1).select("td").get(1).text();
			String education = content.get(2).select("td").get(1).text();
			String majors = content.get(3).select("td").get(1).text();
			String phone = content.get(4).select("td").get(1).text();
			String email = content.get(5).select("td").get(1).text();
			String imageIdentifier = tables.get(2).select("img").attr("src");
			return Optional.of(new PersonInfo(name, position, imageIdentifier, personIdentifier, subjectArea, education, majors, phone, email));
		} catch(IndexOutOfBoundsException | NullPointerException ignored) {
			return Optional.empty();
		}
	}
}
