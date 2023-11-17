package org.punlabs.phichitpittayakom.activity;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SeslProgressBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.api.common.card.CardView;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.oneuiproject.oneui.layout.ToolbarLayout;
import dev.oneuiproject.oneui.widget.RoundLinearLayout;
import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.gallery.Gallery;
import th.ac.phichitpittayakom.gallery.GalleryPage;

public class AllGalleryActivity extends AppCompatActivity {
	private final List<GalleryEntry> galleryList;

	private LinearLayoutManager layoutManager;
	private Adapter adapter;
	private boolean loading;
	private int pageNumber;
	private boolean hasNext;

	public AllGalleryActivity() {
		this.galleryList = new ArrayList<>();
		this.pageNumber = 1;
		this.hasNext = true;
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		ToolbarLayout toolbarLayout = new ToolbarLayout(this, null);
		this.setContentView(toolbarLayout);
		String title = this.getString(R.string.gallery);
		toolbarLayout.setTitle(title, title);
		toolbarLayout.setExpandedSubtitle(this.getString(R.string.collapseTitle));
		toolbarLayout.setExpanded(false, false);
		toolbarLayout.setNavigationButtonAsBack();
		/*RoundLinearLayout linearLayout = new RoundLinearLayout(this);
		linearLayout.setLayoutParams(new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(RoundLinearLayout.VERTICAL);
		linearLayout.setBackgroundColor(this.getColor(R.color.oui_background_color));
		toolbarLayout.addView(linearLayout);*/
		RecyclerView recyclerView = new RecyclerView(this);
		//recyclerView.setLayoutParams(new RoundLinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		recyclerView.setLayoutParams(new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(this.layoutManager);
		this.adapter = new Adapter();
		recyclerView.setAdapter(this.adapter);
		recyclerView.seslSetLastRoundedCorner(true);
		recyclerView.seslSetFastScrollerEnabled(true);
		recyclerView.seslSetGoToTopEnabled(true);
		recyclerView.seslSetSmoothScrollEnabled(true);
		recyclerView.addOnScrollListener(new Listener());
		recyclerView.setBackgroundColor(this.getColor(R.color.oui_background_color));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		//linearLayout.addView(recyclerView);
		toolbarLayout.addView(recyclerView);
		this.loading();
	}

	private void loadGallery() {
		if(!this.hasNext) {
			this.runOnUiThread(this :: unloading);
			return;
		}

		Optional<GalleryPage> optional = this.tryThreeTimes(this.pageNumber);

		if(!optional.isPresent()) {
			this.runOnUiThread(this :: unloading);
			return;
		}

		GalleryPage page = optional.get();
		Gallery[] galleryList = page.getGalleryList();
		this.runOnUiThread(() -> {
			this.hasNext = page.hasNextPage();
			int index = this.galleryList.size();

			for(Gallery gallery : galleryList) {
				this.galleryList.add(new GalleryEntry(gallery, this.galleryList.size()));
			}

			this.adapter.notifyItemRangeInserted(index, galleryList.length);
			this.pageNumber++;
			this.unloading();
		});
	}

	private Optional<GalleryPage> tryThreeTimes(int pageNumber) {
		Optional<GalleryPage> optional;
		int count = 0;

		while(!(optional = Phichitpittayakom.gallery.findGalleryPageByNumber(pageNumber)).isPresent()) {
			count++;

			if(count >= 3) {
				return Optional.empty();
			}
		}

		return optional;
	}

	private void loading() {
		this.loading = true;
		this.adapter.notifyItemInserted(this.galleryList.size());
		new Thread(AllGalleryActivity.this :: loadGallery).start();
	}

	private void unloading() {
		this.loading = false;
		this.adapter.notifyItemRemoved(this.galleryList.size());
	}

	private class Listener extends RecyclerView.OnScrollListener {
		@Override
		public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
			if(dy < 0 || AllGalleryActivity.this.loading) {
				return;
			}

			int visible = AllGalleryActivity.this.layoutManager.getChildCount();
			int total = AllGalleryActivity.this.layoutManager.getItemCount();
			int pastVisible = AllGalleryActivity.this.layoutManager.findFirstVisibleItemPosition();

			if((pastVisible + visible) < total) {
				return;
			}

			recyclerView.post(AllGalleryActivity.this :: loading);
		}
	}

	private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
		@Override
		public int getItemViewType(int position) {
			if(AllGalleryActivity.this.loading && position == AllGalleryActivity.this.galleryList.size()) {
				return 1;
			}

			return 0;
		}

		@NonNull
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			if(viewType == 1) {
				return LoadingHolder.create(AllGalleryActivity.this);
			}

			return new Holder(new CardView(AllGalleryActivity.this));
		}

		@Override
		public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
			if(!(viewHolder instanceof Holder)) {
				return;
			}

			Holder holder = (Holder) viewHolder;
			GalleryEntry entry = AllGalleryActivity.this.galleryList.get(position);
			holder.apply(entry.gallery, position);

			if(entry.thumbnail == null) {
				return;
			}

			holder.cardView.setImage(entry.thumbnail);
		}

		@Override
		public int getItemCount() {
			return AllGalleryActivity.this.galleryList.size() + (AllGalleryActivity.this.loading ? 1 : 0);
		}
	}

	private static class Holder extends RecyclerView.ViewHolder {
		private final CardView cardView;

		private Holder(@NonNull CardView cardView) {
			super(cardView);
			this.cardView = cardView;
		}

		private void apply(Gallery gallery, int index) {
			this.cardView.setTitle(gallery.getTitle());
			this.cardView.setDividerVisible(index != 0);
		}
	}

	private static class LoadingHolder extends RecyclerView.ViewHolder {
		private LoadingHolder(@NonNull View progressBar) {
			super(progressBar);
		}

		private static LoadingHolder create(Context context) {
			LinearLayout linearLayout = new LinearLayout(context);
			ViewGroup.MarginLayoutParams linearLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			linearLayoutParams.topMargin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, context.getResources().getDisplayMetrics()));
			linearLayout.setLayoutParams(linearLayoutParams);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			SeslProgressBar progressBar = new SeslProgressBar(new ContextThemeWrapper(context, com.khopan.api.common.R.style.Widget_AppCompat_ProgressBar_Small));
			LinearLayout.LayoutParams progressBarParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			progressBarParams.gravity = Gravity.CENTER_HORIZONTAL;
			progressBar.setLayoutParams(progressBarParams);
			progressBar.setIndeterminate(true);
			linearLayout.addView(progressBar);
			TextView textView = new TextView(context);
			LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			textViewParams.bottomMargin = linearLayoutParams.topMargin;
			textView.setLayoutParams(textViewParams);
			textView.setText(context.getString(R.string.loading));
			textView.setGravity(Gravity.CENTER_HORIZONTAL);
			linearLayout.addView(textView);
			return new LoadingHolder(linearLayout);
		}
	}

	private class GalleryEntry {
		private final Gallery gallery;

		private Drawable thumbnail;

		private GalleryEntry(Gallery gallery, int index) {
			this.gallery = gallery;
			new Thread(() -> {
				Optional<byte[]> optional = this.tryThreeTimes();

				if(!optional.isPresent()) {
					return;
				}

				byte[] imageData = optional.get();
				Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
				this.thumbnail = new BitmapDrawable(AllGalleryActivity.this.getResources(), bitmap);
				AllGalleryActivity.this.runOnUiThread(() -> {
					if(index >= AllGalleryActivity.this.layoutManager.findFirstVisibleItemPosition() && index <= AllGalleryActivity.this.layoutManager.findLastVisibleItemPosition()) {
						AllGalleryActivity.this.adapter.notifyItemChanged(index);
					}
				});
			}).start();
		}

		private Optional<byte[]> tryThreeTimes() {
			Optional<byte[]> optional;
			int count = 0;

			while(!(optional = this.gallery.getThumbnail()).isPresent()) {
				count++;

				if(count >= 3) {
					return Optional.empty();
				}
			}

			return optional;
		}
	}
}
