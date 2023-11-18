package org.punlabs.phichitpittayakom.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.khopan.api.common.activity.FragmentedActivity;
import com.khopan.api.common.fragment.SingleCenterTextFragment;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.GalleryFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.gallery.Gallery;
import th.ac.phichitpittayakom.gallery.GalleryDetail;

public class GalleryActivity extends FragmentedActivity {
	private static Gallery Gallery;

	public GalleryActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();
		String title = GalleryActivity.title(this, GalleryActivity.Gallery);
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(GalleryActivity.summary(this, GalleryActivity.Gallery));
		this.loading();
		String identifier = GalleryActivity.Gallery.getGalleryIdentifier();
		this.internet(() -> new Thread(() -> {
			Optional<GalleryDetail> optional = Phichitpittayakom.gallery.findGalleryDetailByIdentifier(identifier);

			if(!optional.isPresent()) {
				this.setFragment(new SingleCenterTextFragment(this.getString(R.string.noSearchResult)));
				return;
			}

			GalleryDetail gallery = optional.get();
			List<Bitmap> imageList = new ArrayList<>();
			String[] imagePaths = gallery.getImages();

			for(String imagePath : imagePaths) {
				Optional<byte[]> imageData = Phichitpittayakom.school.findImageById(imagePath);

				if(!imageData.isPresent()) {
					continue;
				}

				byte[] bitmapData = imageData.get();
				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
				imageList.add(bitmap);
			}

			this.setFragment(new GalleryFragment(gallery, imageList.toArray(new Bitmap[0])));
		}).start());
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
		GalleryActivity.Gallery = gallery;
		Intent intent = new Intent(context, GalleryActivity.class);
		context.startActivity(intent);
	}
}
