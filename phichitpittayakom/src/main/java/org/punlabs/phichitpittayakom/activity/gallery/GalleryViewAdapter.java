package org.punlabs.phichitpittayakom.activity.gallery;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.punlabs.phichitpittayakom.activity.gallery.data.GalleryLoadingData;
import org.punlabs.phichitpittayakom.activity.gallery.viewholder.GalleryCardViewHolder;
import org.punlabs.phichitpittayakom.activity.gallery.viewholder.GalleryEmptyViewHolder;
import org.punlabs.phichitpittayakom.activity.gallery.viewholder.GalleryImageViewHolder;
import org.punlabs.phichitpittayakom.activity.gallery.viewholder.GalleryLoadingViewHolder;
import org.punlabs.phichitpittayakom.activity.gallery.viewholder.GalleryTextSeparatorHolder;

import java.util.List;
import java.util.Objects;

public class GalleryViewAdapter extends RecyclerView.Adapter<GalleryViewHolder> {
	private final Context context;
	private final List<GalleryData> dataList;

	boolean loading;

	public GalleryViewAdapter(Context context, List<GalleryData> dataList) {
		this.context = context;
		this.dataList = dataList;
	}

	public synchronized void loading() {
		if(this.loading) {
			return;
		}

		int index = this.dataList.size();
		this.dataList.add(new GalleryLoadingData());
		this.notifyItemInserted(index);
		this.loading = true;
	}

	public synchronized void unloading() {
		if(!this.loading) {
			return;
		}

		int size = this.dataList.size();

		if(size < 1) {
			return;
		}

		int index = size - 1;
		GalleryData data = this.dataList.get(index);

		if(!(data instanceof GalleryLoadingData)) {
			return;
		}

		this.dataList.remove(index);
		this.notifyItemRemoved(index);
		this.loading = false;
	}

	@Override
	public int getItemViewType(int position) {
		return this.dataList.get(position).getType().getViewType();
	}

	@NonNull
	@Override
	public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if(viewType == -1) {
			return GalleryLoadingViewHolder.create(this.context);
		}

		switch(Objects.requireNonNull(GalleryDataType.parse(viewType))) {
		case TEXT_SEPARATOR:
			return GalleryTextSeparatorHolder.create(this.context);
		case LOADING:
			return GalleryLoadingViewHolder.create(this.context);
		case CARD_VIEW:
			return GalleryCardViewHolder.create(this.context);
		case IMAGE_VIEW:
			return GalleryImageViewHolder.create(this.context);
		default:
			return GalleryEmptyViewHolder.create(this.context);
		}
	}

	@Override
	public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
		holder.bind(this.dataList.get(position));
	}

	@Override
	public int getItemCount() {
		return this.dataList.size();
	}
}
