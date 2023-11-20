package org.punlabs.phichitpittayakom.activity.gallery.data;

import org.punlabs.phichitpittayakom.activity.gallery.GalleryData;
import org.punlabs.phichitpittayakom.activity.gallery.GalleryDataType;

public class GalleryCardViewData extends GalleryData {
	private final String text;

	public GalleryCardViewData(String text) {
		super(GalleryDataType.CARD_VIEW);
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
