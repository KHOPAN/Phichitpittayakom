package org.punlabs.phichitpittayakom.activity.gallery;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class GalleryViewHolder extends RecyclerView.ViewHolder {
	public GalleryViewHolder(View view) {
		super(view);
	}

	public abstract void bind(GalleryData data);
}
