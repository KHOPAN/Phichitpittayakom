package org.punlabs.phichitpittayakom.activity.gallery;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryScrollListener extends RecyclerView.OnScrollListener {
	private final GalleryActivity activity;
	private final GalleryViewAdapter adapter;

	public GalleryScrollListener(GalleryActivity activity, GalleryViewAdapter adapter) {
		this.activity = activity;
		this.adapter = adapter;
	}

	@Override
	public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
		if(dy < 0 || this.adapter.loading) {
			return;
		}

		new Thread(() -> this.activity.runOnUiThread(this.activity :: tryLoad)).start();
	}
}
