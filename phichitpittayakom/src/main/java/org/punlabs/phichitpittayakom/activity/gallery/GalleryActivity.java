package org.punlabs.phichitpittayakom.activity.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.activity.gallery.data.GalleryCardViewData;
import org.punlabs.phichitpittayakom.activity.gallery.data.GalleryEmptySeparatorData;
import org.punlabs.phichitpittayakom.activity.gallery.data.GalleryImageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.oneuiproject.oneui.layout.ToolbarLayout;
import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.gallery.Gallery;
import th.ac.phichitpittayakom.gallery.GalleryDetail;

public class GalleryActivity extends AppCompatActivity {
	private final List<GalleryData> dataList;

	private Gallery gallery;
	private LinearLayoutManager manager;
	private GalleryViewAdapter adapter;
	private GalleryDetail detail;
	private String[] images;
	private int index;

	public GalleryActivity() {
		this.dataList = new ArrayList<>();
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Intent intent = this.getIntent();
		this.gallery = GalleryActivity.parseGallery(intent.getExtras());
		ToolbarLayout toolbarLayout = new ToolbarLayout(this, null);
		this.setContentView(toolbarLayout);
		String title = GalleryActivity.title(this, this.gallery);
		toolbarLayout.setTitle(title, title);
		toolbarLayout.setExpandedSubtitle(GalleryActivity.summary(this, this.gallery));
		toolbarLayout.setExpanded(false, false);
		toolbarLayout.setNavigationButtonAsBack();
		RecyclerView recyclerView = new RecyclerView(this);
		recyclerView.setLayoutParams(new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.manager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(this.manager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		this.adapter = new GalleryViewAdapter(this, this.dataList);
		recyclerView.setAdapter(this.adapter);
		recyclerView.addOnScrollListener(new GalleryScrollListener(this, this.adapter));
		recyclerView.seslSetLastRoundedCorner(true);
		recyclerView.seslSetSmoothScrollEnabled(true);
		toolbarLayout.addView(recyclerView);
		this.dataList.add(new GalleryCardViewData(this.gallery.getTitle()));
		this.load();
	}

	public void load() {
		if(this.detail == null) {
			this.adapter.loading();
			new Thread(this :: loadDetail).start();
			return;
		}

		if(this.images == null || this.images.length == 0 || this.index >= this.images.length) {
			return;
		}

		this.adapter.loading();
		new Thread(this :: loadImage).start();
	}

	public void tryLoad() {
		int visible = this.manager.getChildCount();
		int total = this.manager.getItemCount();
		int pastVisible = this.manager.findFirstVisibleItemPosition();

		if((pastVisible + visible) < total) {
			return;
		}

		this.load();
	}

	private void loadDetail() {
		String identifier = this.gallery.getGalleryIdentifier();

		if(identifier == null || identifier.isEmpty()) {
			this.runOnUiThread(this.adapter :: unloading);
			return;
		}

		Optional<GalleryDetail> optional = this.tryLoadDetail(identifier);

		if(!optional.isPresent()) {
			this.runOnUiThread(this.adapter :: unloading);
			return;
		}

		this.detail = optional.get();
		this.images = this.detail.getImages();
		String title = this.gallery.getTitle();
		String content = this.detail.getContent();
		this.runOnUiThread(() -> {
			this.adapter.unloading();

			if(!content.isEmpty() && !content.equals(title)) {
				int index = this.dataList.size();
				this.dataList.add(new GalleryEmptySeparatorData());
				this.dataList.add(new GalleryCardViewData(content));
				this.adapter.notifyItemRangeInserted(index, 2);
			}

			this.tryLoad();
		});
	}

	private Optional<GalleryDetail> tryLoadDetail(String identifier) {
		Optional<GalleryDetail> optional;
		int count = 0;

		while(!(optional = Phichitpittayakom.gallery.findGalleryDetailByIdentifier(identifier)).isPresent()) {
			count++;

			if(count >= 3) {
				return Optional.empty();
			}
		}

		return optional;
	}

	private void loadImage() {
		String image = this.images[this.index];
		this.index++;

		if(image == null || image.isEmpty()) {
			return;
		}

		Optional<byte[]> optional = this.tryLoadImage(image);

		if(!optional.isPresent()) {
			this.runOnUiThread(this.adapter :: unloading);
			return;
		}

		byte[] rawImage = optional.get();
		Bitmap bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
		this.runOnUiThread(() -> {
			this.adapter.unloading();
			int index = this.dataList.size();
			this.dataList.add(new GalleryEmptySeparatorData());
			this.dataList.add(new GalleryImageData(bitmap));
			this.adapter.notifyItemRangeInserted(index, 2);
			this.tryLoad();
		});
	}

	private Optional<byte[]> tryLoadImage(String identifier) {
		Optional<byte[]> optional;
		int count = 0;

		while(!(optional = Phichitpittayakom.school.findImageById(identifier)).isPresent()) {
			count++;

			if(count >= 3) {
				return Optional.empty();
			}
		}

		return optional;
	}

	public static String title(Context ignoredContext, Gallery gallery) {
		return gallery.getTitle();
	}

	public static String summary(Context context, Gallery gallery) {
		StringBuilder builder = new StringBuilder();
		int imageCount = gallery.getImageCount();
		int viewCount = gallery.getViewCount();
		builder.append(imageCount == 1 ? context.getString(R.string.oneImage) : context.getString(R.string.images, imageCount));
		builder.append(' ');
		builder.append(viewCount == 1 ? context.getString(R.string.oneView) : context.getString(R.string.views, viewCount));
		return builder.toString();
	}

	public static void action(Context context, Gallery gallery) {
		Intent intent = new Intent(context, GalleryActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("galleryIdentifier", gallery.getGalleryIdentifier());
		bundle.putString("title", gallery.getTitle());
		bundle.putString("thumbnailIdentifier", gallery.getThumbnailIdentifier());
		bundle.putInt("imageCount", gallery.getImageCount());
		bundle.putInt("viewCount", gallery.getViewCount());
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public static Gallery parseGallery(Bundle bundle) {
		if(bundle == null) {
			return new Gallery();
		}

		String galleryIdentifier = bundle.getString("galleryIdentifier", "");
		String title = bundle.getString("title", "");
		String thumbnailIdentifier = bundle.getString("thumbnailIdentifier", "");
		int imageCount = bundle.getInt("imageCount", 0);
		int viewCount = bundle.getInt("viewCount", 0);
		return new Gallery(galleryIdentifier, title, thumbnailIdentifier, imageCount, viewCount);
	}
}
