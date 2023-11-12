package org.punlabs.phichitpittayakom.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.khopan.api.common.activity.FragmentedActivity;
import com.khopan.api.common.fragment.SingleCenterTextFragment;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.GalleryFragment;

import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.gallery.Gallery;
import th.ac.phichitpittayakom.gallery.GalleryDetail;

public class GalleryActivity extends FragmentedActivity {
	public static Gallery Gallery;

	public GalleryActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();

		if(GalleryActivity.Gallery == null) {
			this.finish();
			return;
		}

		String title = GalleryActivity.title(this, GalleryActivity.Gallery);
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(GalleryActivity.summary(this, GalleryActivity.Gallery));
		String identifier = GalleryActivity.Gallery.getGalleryIdentifier();
		this.loading();
		this.internet(() -> new Thread(() -> {
			Optional<GalleryDetail> optional = Phichitpittayakom.gallery.findGalleryById(identifier);

			if(!optional.isPresent()) {
				this.setFragment(new SingleCenterTextFragment(this.getString(R.string.noSearchResult)));
				return;
			}

			GalleryDetail gallery = optional.get();
			byte[][] imageData = gallery.getImages();
			Bitmap[] images = new Bitmap[imageData.length];

			for(int i = 0; i < images.length; i++) {
				byte[] image = imageData[i];
				images[i] = BitmapFactory.decodeByteArray(image, 0, image.length);
			}

			this.setFragment(new GalleryFragment(gallery, images));
		}).start());
	}

	public static String title(Context ignoredContext, Gallery gallery) {
		return gallery.getTitle();
	}

	public static String summary(Context context, Gallery gallery) {
		int imageCount = gallery.getImageCount();
		int viewCount = gallery.getViewCount();
		String image = imageCount == 1 ? context.getString(R.string.oneImage) : context.getString(R.string.images, imageCount);
		String view = viewCount == 1 ? context.getString(R.string.oneView) : context.getString(R.string.views, viewCount);
		StringBuilder builder = new StringBuilder();
		builder.append(image);
		builder.append(' ');
		builder.append(view);

		if(gallery.isExternalLink()) {
			builder.append('\n');
			builder.append(gallery.getGalleryIdentifier());
		}

		return builder.toString();
	}

	public static void action(Context context, Gallery gallery) {
		GalleryActivity.Gallery = gallery;
		context.startActivity(new Intent(context, GalleryActivity.class));
	}

	public static BitmapDrawable image(Context context, Gallery gallery) {
		byte[] bitmapData = gallery.getThumbnail();
		Bitmap bitmap = bitmapData == null ? null : BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
		return new BitmapDrawable(context.getResources(), bitmap);
	}
}
