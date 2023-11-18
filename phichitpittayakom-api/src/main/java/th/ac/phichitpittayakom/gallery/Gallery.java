package th.ac.phichitpittayakom.gallery;

import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;

public class Gallery {
	private final String galleryIdentifier;
	private final String title;
	private final String thumbnailIdentifier;
	private final int imageCount;
	private final int viewCount;

	public Gallery() {
		this.galleryIdentifier = "";
		this.title = "";
		this.thumbnailIdentifier = "";
		this.imageCount = 0;
		this.viewCount = 0;
	}

	public Gallery(String galleryIdentifier, String title, String thumbnailIdentifier, int imageCount, int viewCount) {
		this.galleryIdentifier = galleryIdentifier;
		this.title = title;
		this.thumbnailIdentifier = thumbnailIdentifier;
		this.imageCount = imageCount;
		this.viewCount = viewCount;
	}

	public String getGalleryIdentifier() {
		return this.galleryIdentifier;
	}

	public Optional<GalleryDetail> getDetail() {
		return Phichitpittayakom.gallery.findGalleryDetailByIdentifier(this.galleryIdentifier);
	}

	public String getTitle() {
		return this.title;
	}

	public String getThumbnailIdentifier() {
		return this.thumbnailIdentifier;
	}

	public Optional<byte[]> getThumbnail() {
		return Phichitpittayakom.school.findImageById(this.thumbnailIdentifier);
	}

	public int getImageCount() {
		return this.imageCount;
	}

	public int getViewCount() {
		return this.viewCount;
	}
}
