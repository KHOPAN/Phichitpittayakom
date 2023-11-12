package org.punlabs.phichitpittayakom.activity;

import com.khopan.api.common.activity.FragmentedActivity;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.ListFragment;

import java.util.List;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.gallery.Gallery;

public class AllGalleryActivity extends FragmentedActivity {
	public AllGalleryActivity() {
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
			List<Gallery> galleryList = Phichitpittayakom.gallery.getAllGallery();
			this.setFragment(new ListFragment<>(galleryList, GalleryActivity :: title, GalleryActivity :: summary, GalleryActivity :: action, GalleryActivity :: image));
		}).start());
	}
}
