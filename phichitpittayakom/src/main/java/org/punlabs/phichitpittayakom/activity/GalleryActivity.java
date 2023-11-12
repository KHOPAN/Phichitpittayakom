package org.punlabs.phichitpittayakom.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.khopan.api.common.activity.FragmentedActivity;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.ListFragment;

import java.util.List;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.gallery.Gallery;

public class GalleryActivity extends FragmentedActivity {
	public GalleryActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();
		String title = this.getString(R.string.gallery);
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(this.getString(R.string.collapseTitle));
		this.loading();
		this.internet(() -> new Thread(() -> {
			List<Gallery> galleryList = Phichitpittayakom.school.getAllGallery();
			this.setFragment(new ListFragment<>(galleryList, (context, gallery) -> gallery.getTitle(), (context, gallery) -> gallery.getImageCount() + " / " + gallery.getViewCount(), (context, gallery) -> {}, (context, gallery) -> {
				byte[] bitmapData = gallery.getThumbnail();
				Bitmap bitmap = bitmapData == null ? null : BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
				return new BitmapDrawable(this.getResources(), bitmap);
			}));
		}).start());
	}
}
