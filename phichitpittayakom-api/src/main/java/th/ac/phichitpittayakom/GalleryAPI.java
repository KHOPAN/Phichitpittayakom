package th.ac.phichitpittayakom;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import th.ac.phichitpittayakom.gallery.Gallery;
import th.ac.phichitpittayakom.gallery.GalleryDetail;

public class GalleryAPI {
	GalleryAPI() {}

	public List<Gallery> getAllGallery() {
		List<Gallery> list = new ArrayList<>();
		Document document;

		try {
			document = Jsoup.connect("http://phichitpittayakom.ac.th/gallery").get();
		} catch(Throwable ignored) {
			return list;
		}

		Elements elements = document.select("#gallery_web").select("tfoot").select("a");
		int size = elements.size();

		if(size < 2) {
			return list;
		}

		List<GalleryPage> pageList = new ArrayList<>();
		Element element = elements.get(size - 2);
		int max = Integer.parseInt(element.text());
		this.galleryPage(pageList, document);
		List<Thread> threadList = new ArrayList<>();

		for(int i = 2; i <= max; i++) {
			int finalIndex = i;
			Thread thread = new Thread(() -> {
				Document gallery;

				try {
					gallery = Jsoup.connect("http://phichitpittayakom.ac.th/gallery_" + finalIndex).get();
				} catch(Throwable ignored) {
					return;
				}

				this.galleryPage(pageList, gallery);
			});

			threadList.add(thread);
			thread.start();
		}

		for(Thread thread : threadList) {
			try {
				thread.join();
			} catch(Throwable ignored) {

			}
		}

		int count = 0;

		for(GalleryPage page : pageList) {
			if(page == null) {
				count++;
			}
		}

		for(int i = 0; i < count; i++) {
			pageList.remove(null);
		}

		pageList.sort(Comparator.comparingInt(x -> x.pageNumber));

		for(GalleryPage page : pageList) {
			list.addAll(page.galleryList);
		}

		return list;
	}

	private void galleryPage(List<GalleryPage> list, Document document) {
		GalleryPage page = new GalleryPage();
		page.pageNumber = Integer.parseInt(document.select(".number.current").text());
		List<Gallery> galleryList = new ArrayList<>();
		Elements elements = document.select("#gallery_web").select("td[align=center]");

		for(Element element : elements) {
			Elements imgClass = element.select(".img");
			String thumbnailIdentifier = imgClass.select("img").attr("src");
			Optional<byte[]> optional = Phichitpittayakom.school.findImageById(thumbnailIdentifier);
			byte[] thumbnail = null;

			if(optional.isPresent()) {
				thumbnail = optional.get();
			}

			String title = element.select("strong").text();
			String[] span = element.select("span").text().replaceAll("\\s+", "").split("/");
			int imageCount = Integer.parseInt(span[0].substring(6, span[0].length() - 3));
			int viewCount = Integer.parseInt(span[1].substring(2, span[1].length() - 6));
			String identifier = imgClass.select("a").attr("href");
			boolean external = identifier.startsWith("http");
			galleryList.add(new Gallery(thumbnail, title, imageCount, viewCount, identifier, external));
		}

		page.galleryList = galleryList;
		list.add(page);
	}

	public Optional<GalleryDetail> findGalleryById(String galleryIdentifier) {
		Document document;

		try {
			document = Jsoup.connect("http://phichitpittayakom.ac.th/" + galleryIdentifier)
					.cookie("PHPSESSID", "FAKESESSIONID")
					.get();

		} catch(Throwable ignored) {
			return Optional.empty();
		}

		Elements contentElements = document.select(".content");
		String title = contentElements.select("div").get(0).text();
		Elements textData = contentElements.select(".textData");
		Elements elements = textData.select("td").get(1).select("div");
		StringBuilder builder = new StringBuilder();
		boolean added = false;

		for(int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			String text = element.ownText();

			if(!text.isEmpty()) {
				if(added) {
					builder.append('\n');
				}

				builder.append(text);
				added = true;
			}
		}

		String content = builder.toString();
		Elements imageElements = textData.get(1).select("img");
		List<byte[]> imageList = new ArrayList<>();

		for(Element imageElement : imageElements) {
			Optional<byte[]> optional = Phichitpittayakom.school.findImageById(imageElement.attr("src"));

			if(optional.isPresent()) {
				imageList.add(optional.get());
			}
		}

		byte[][] images = imageList.toArray(new byte[0][]);
		LocalDateTime postDate = LocalDateTime.now();
		int viewCount = 0;
		return Optional.of(new GalleryDetail(title, content, images, postDate, viewCount));
	}

	private class GalleryPage {
		private int pageNumber;
		private List<Gallery> galleryList;
	}
}
