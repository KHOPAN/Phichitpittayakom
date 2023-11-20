package org.punlabs.phichitpittayakom.activity.gallery.data;

import org.punlabs.phichitpittayakom.activity.gallery.GalleryData;
import org.punlabs.phichitpittayakom.activity.gallery.GalleryDataType;

public class GalleryTextSeparatorData extends GalleryData {
	private final String text;

	public GalleryTextSeparatorData(String text) {
		super(GalleryDataType.TEXT_SEPARATOR);
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
