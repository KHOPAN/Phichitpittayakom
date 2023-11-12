package th.ac.phichitpittayakom.gallery;

import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;

public class Gallery {
	private final byte[] thumbnail;
	private final String title;
	private final int imageCount;
	private final int viewCount;
	private final String galleryIdentifier;
	private final boolean external;

	public Gallery() {
		this.thumbnail = new byte[0];
		this.title = "";
		this.imageCount = 0;
		this.viewCount = 0;
		this.galleryIdentifier = "";
		this.external = false;
	}

	public Gallery(byte[] thumbnail, String title, int imageCount, int viewCount, String galleryIdentifier, boolean external) {
		this.thumbnail = thumbnail;
		this.title = title;
		this.imageCount = imageCount;
		this.viewCount = viewCount;
		this.galleryIdentifier = galleryIdentifier;
		this.external = external;
	}

	public byte[] getThumbnail() {
		return this.thumbnail;
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

	public String getGalleryIdentifier() {
		return this.galleryIdentifier;
	}

	public boolean isExternalLink() {
		return this.external;
	}

	public Optional<GalleryDetail> getDetail() {
		return this.external ? Optional.empty() : Phichitpittayakom.gallery.findGalleryById(this.galleryIdentifier);
	}
}
