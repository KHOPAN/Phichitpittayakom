package org.punlabs.phichitpittayakom.activity.gallery.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.punlabs.phichitpittayakom.activity.gallery.GalleryData;
import org.punlabs.phichitpittayakom.activity.gallery.GalleryViewHolder;

public class GalleryEmptyViewHolder extends GalleryViewHolder {
	public GalleryEmptyViewHolder(View view) {
		super(view);
	}

	@Override
	public void bind(GalleryData data) {

	}

	public static GalleryEmptyViewHolder create(Context context) {
		View view = new View(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(dev.oneuiproject.oneui.design.R.dimen.sesl_list_subheader_min_height));
		view.setLayoutParams(params);
		return new GalleryEmptyViewHolder(view);
	}
}
