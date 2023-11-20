package org.punlabs.phichitpittayakom.activity.gallery;

public abstract class GalleryData {
	private final GalleryDataType type;

	public GalleryData(GalleryDataType type) {
		this.type = type;
	}

	public GalleryDataType getType() {
		return this.type;
	}
}
