package th.ac.phichitpittayakom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import th.ac.phichitpittayakom.gallery.Gallery;
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

			galleryList.add(new Gallery(title, imageIdentifier));
		}

		return Optional.of(new GalleryPage(pageNumber, hasNext, galleryList.toArray(new Gallery[0])));
	}
}
