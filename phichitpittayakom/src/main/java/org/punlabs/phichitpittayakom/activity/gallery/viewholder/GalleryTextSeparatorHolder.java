package org.punlabs.phichitpittayakom.activity.gallery.viewholder;

import android.content.Context;

import org.punlabs.phichitpittayakom.activity.gallery.GalleryData;
import org.punlabs.phichitpittayakom.activity.gallery.GalleryViewHolder;
import org.punlabs.phichitpittayakom.activity.gallery.data.GalleryTextSeparatorData;

import dev.oneuiproject.oneui.widget.Separator;

public class GalleryTextSeparatorHolder extends GalleryViewHolder {
	private final Separator separator;

	public GalleryTextSeparatorHolder(Separator separator) {
		super(separator);
		this.separator = separator;
	}

	@Override
	public void bind(GalleryData data) {
		if(data instanceof GalleryTextSeparatorData) {
			this.separator.setText(((GalleryTextSeparatorData) data).getText());
		}
	}

	public static GalleryTextSeparatorHolder create(Context context) {
		return new GalleryTextSeparatorHolder(new Separator(context));
	}
}
