package org.punlabs.phichitpittayakom.activity.gallery.data;

import android.graphics.Bitmap;

import org.punlabs.phichitpittayakom.activity.gallery.GalleryData;
import org.punlabs.phichitpittayakom.activity.gallery.GalleryDataType;

public class GalleryImageData extends GalleryData {
	private final Bitmap image;

	public GalleryImageData(Bitmap image) {
		super(GalleryDataType.IMAGE_VIEW);
		this.image = image;
	}

	public Bitmap getImage() {
		return this.image;
	}
}
