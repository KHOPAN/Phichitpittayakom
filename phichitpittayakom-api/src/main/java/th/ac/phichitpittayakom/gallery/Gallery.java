package th.ac.phichitpittayakom.gallery;

import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;

public class Gallery {
	private final String title;
	private final String thumbnailIdentifier;

	public Gallery() {
		this.title = "";
		this.thumbnailIdentifier = "";
	}

	public Gallery(String title, String thumbnailIdentifier) {
		this.title = title;
		this.thumbnailIdentifier = thumbnailIdentifier;
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
}
