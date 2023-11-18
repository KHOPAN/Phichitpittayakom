package th.ac.phichitpittayakom;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import th.ac.phichitpittayakom.gallery.Gallery;
import th.ac.phichitpittayakom.gallery.GalleryDetail;
import th.ac.phichitpittayakom.gallery.GalleryPage;

public class GalleryAPI {
	GalleryAPI() {}

	public Optional<GalleryPage> findFirstGalleryPage() {
		return this.findGalleryPageByNumber(1);
	}

	public Optional<GalleryPage> findGalleryPageByNumber(int pageNumber) {
		Document document;

		try {
			document = Jsoup.connect("http://phichitpittayakom.ac.th/gallery_" + pageNumber)
					.cookie("PHPSESSID", "FAKESESSIONID")
					.get();

		} catch(Throwable ignored) {
			return Optional.empty();
		}

		Element galleryWeb = document.getElementById("gallery_web");

		if(galleryWeb == null) {
			return Optional.empty();
		}

		Elements tfootTags = galleryWeb.getElementsByTag("tfoot");
		boolean hasNext = false;

		tfootCheck: if(!tfootTags.isEmpty()) {
			Elements aTags = tfootTags.select("a");

			if(aTags.isEmpty()) {
				break tfootCheck;
			}

			int maxPage = 0;

			for(Element aTag : aTags) {
				try {
					maxPage = Math.max(maxPage, Integer.parseInt(aTag.ownText()));
				} catch(NumberFormatException ignored) {

				}
			}

			if(maxPage == 0) {
				break tfootCheck;
			}

			hasNext = pageNumber < maxPage;
		}

		Elements tbodyTags = galleryWeb.getElementsByTag("tbody");

		if(tbodyTags.isEmpty()) {
			return Optional.empty();
		}

		Element mainContent = tbodyTags.get(0);
		Elements tableDataList = mainContent.select("td[align=center]");

		if(tableDataList.isEmpty()) {
			return Optional.empty();
		}

		List<Gallery> galleryList = new ArrayList<>();

		for(Element element : tableDataList) {
			Elements aTags = element.select("a");
			String galleryIdentifier = "";

			if(!aTags.isEmpty()) {
				galleryIdentifier = aTags.get(0).attr("href");
			}

			Elements strongTags = element.select("strong");
			String title = "";

			if(!strongTags.isEmpty()) {
				title = strongTags.get(0).ownText();
			}

			Elements imgTag = element.select("img");
			String imageIdentifier = "";

			if(!imgTag.isEmpty()) {
				imageIdentifier = imgTag.get(0).attr("src");
			}

			String[] description = element.select(".txtDate").text().replaceAll("\\s+", "").split("/");
			int imageCount = 0;
			int viewCount = 0;

			descriptionCheck: {
				if(description.length < 1) {
					break descriptionCheck;
				}

				String imageCountString = description[0].replaceAll("[^0-9]", "");

				if(!imageCountString.isEmpty()) {
					try {
						imageCount = Integer.parseInt(imageCountString);
					} catch(Throwable ignored) {

					}
				}

				if(description.length < 2) {
					break descriptionCheck;
				}

				String viewCountString = description[1].replaceAll("[^0-9]", "");

				if(!viewCountString.isEmpty()) {
					try {
						viewCount = Integer.parseInt(viewCountString);
					} catch(Throwable ignored) {

					}
				}
			}

			galleryList.add(new Gallery(galleryIdentifier, title, imageIdentifier, imageCount, viewCount));
		}

		return Optional.of(new GalleryPage(pageNumber, hasNext, galleryList.toArray(new Gallery[0])));
	}

	public Optional<GalleryDetail> findGalleryDetailByIdentifier(String galleryIdentifier) {
		Document document;

		try {
			document = Jsoup.connect("http://phichitpittayakom.ac.th/" + galleryIdentifier)
					.cookie("PHPSESSID", "FAKESESSIONID")
					.get();

		} catch(Throwable ignored) {
			return Optional.empty();
		}

		Elements textDataClass = document.select(".content").select(".textData");

		if(textDataClass.isEmpty()) {
			return Optional.empty();
		}

		Element textDataElement = textDataClass.get(0);
		String title = "";
		String content = "";
		String[] images = new String[0];
		int year = 0;
		int month = 1;
		int day = 1;
		int hour = 0;
		int minute = 0;
		int viewCount = 0;

		title: {
			Elements divTags = textDataElement.select("div");

			if(divTags.isEmpty()) {
				break title;
			}

			title = divTags.get(0).ownText();
		}

		content: {
			Elements contentElements = textDataElement.select("table[align=center]").select("td[align=left]");

			if(contentElements.isEmpty()) {
				break content;
			}

			Elements divTags = contentElements.get(0).children();

			if(divTags.isEmpty()) {
				break content;
			}

			StringBuilder builder = new StringBuilder();
			boolean added = false;

			for(int i = 0; i < divTags.size(); i++) {
				Element divElement = divTags.get(i);
				String text = divElement.text();

				if(text.isEmpty()) {
					continue;
				}

				if(added) {
					builder.append('\n');
				}

				builder.append(text);
				added = true;
			}

			content = builder.toString();
		}

		images: {
			if(textDataClass.size() < 2) {
				break images;
			}

			Element imageRoot = textDataClass.get(1);
			Elements imageTags = imageRoot.select("img");

			if(imageTags.isEmpty()) {
				break images;
			}

			List<String> imageList = new ArrayList<>();

			for(Element imageTag : imageTags) {
				String image = imageTag.attr("src");

				if(!image.isEmpty()) {
					imageList.add(image);
				}
			}

			images = imageList.toArray(images);
		}

		dateCount: {
			Elements trTags = textDataElement.select("tr");

			if(trTags.isEmpty()) {
				break dateCount;
			}

			Element divTag = trTags.get(0).select("div").last();

			if(divTag == null) {
				break dateCount;
			}

			String[] dateTime = divTag.text().replaceAll("\\s+", "").split(",");

			if(dateTime.length < 2) {
				break dateCount;
			}

			String[] textList = dateTime[0].split(":");

			if(textList.length < 2) {
				break dateCount;
			}

			String[] dateList = textList[1].split("[^0-9]+");

			if(dateList.length < 2) {
				break dateCount;
			}

			String monthText = textList[1].replaceAll("[0-9]+", "");
			String[] timeList = dateTime[1].split("[^0-9]+");

			if(timeList.length < 3) {
				break dateCount;
			}

			try {
				year = Integer.parseInt(dateList[1]) - 543;
			} catch(NumberFormatException ignored) {

			}

			switch(monthText) {
			case "ม.ค.":
				month = 1;
				break;
			case "ก.พ.":
				month = 2;
				break;
			case "มี.ค.":
				month = 3;
				break;
			case "เม.ย.":
				month = 4;
				break;
			case "พ.ค.":
				month = 5;
				break;
			case "มิ.ย.":
				month = 6;
				break;
			case "ก.ค.":
				month = 7;
				break;
			case "ส.ค.":
				month = 8;
				break;
			case "ก.ย.":
				month = 9;
				break;
			case "ต.ค.":
				month = 10;
				break;
			case "พ.ย.":
				month = 11;
				break;
			case "ธ.ค.":
				month = 12;
				break;
			}

			try {
				day = Integer.parseInt(dateList[0]);
			} catch(NumberFormatException ignored) {

			}

			try {
				hour = Integer.parseInt(timeList[0]);
			} catch(NumberFormatException ignored) {

			}

			try {
				minute = Integer.parseInt(timeList[1]);
			} catch(NumberFormatException ignored) {

			}

			try {
				viewCount = Integer.parseInt(timeList[2]);
			} catch(NumberFormatException ignored) {

			}
		}

		return Optional.of(new GalleryDetail(title, content, images, LocalDateTime.of(year, month, day, hour, minute), viewCount));
	}
}
