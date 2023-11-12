package th.ac.phichitpittayakom.gallery;

import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;

public class Gallery {
	private final String thumbnail;
	private final String title;
	private final int imageCount;
	private final int viewCount;

	public Gallery() {
		this.thumbnail = "";
		this.title = "";
		this.imageCount = 0;
		this.viewCount = 0;
	}

	public Gallery(String thumbnail, String title, int imageCount, int viewCount) {
		this.thumbnail = thumbnail;
		this.title = title;
		this.imageCount = imageCount;
		this.viewCount = viewCount;
	}

	public String getThumbnailIdentifier() {
		return this.thumbnail;
	}

	public Optional<byte[]> getThumbnail() {
		return Phichitpittayakom.school.findImageById(this.thumbnail);
	}

	public String getTitle() {
		return this.title;
	}

	public int getImageCount() {
		return this.imageCount;
	}

	public int getViewCount() {
		return this.viewCount;
	}
}
