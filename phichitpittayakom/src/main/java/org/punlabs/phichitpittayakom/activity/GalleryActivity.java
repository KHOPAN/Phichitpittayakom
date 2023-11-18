package org.punlabs.phichitpittayakom.activity;

import android.content.Context;
import android.content.Intent;

import com.khopan.api.common.activity.FragmentedActivity;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import th.ac.phichitpittayakom.gallery.Gallery;

public class GalleryActivity extends FragmentedActivity {
	public GalleryActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {

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
		context.startActivity(new Intent(context, GalleryActivity.class));
	}
}
