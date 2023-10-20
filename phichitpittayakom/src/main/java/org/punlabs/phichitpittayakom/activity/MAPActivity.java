package org.punlabs.phichitpittayakom.activity;

import com.khopan.api.common.activity.FragmentedActivity;
import com.khopan.api.common.fragment.SingleCenterTextFragment;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.MAPFragment;

import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.person.map.ManagementAndPersonnel;

public class MAPActivity extends FragmentedActivity {
	public MAPActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();
		String title = this.getString(R.string.managementAndPersonnel);
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(this.getString(R.string.collapseTitle));
		this.loading();
		this.internet(() -> new Thread(() -> {
			Optional<ManagementAndPersonnel> optional = Phichitpittayakom.school.getManagementAndPersonnel();

			if(!optional.isPresent()) {
				this.setFragment(new SingleCenterTextFragment(this.getString(R.string.noSearchResult)));
				return;
			}

			ManagementAndPersonnel map = optional.get();
			this.setFragment(new MAPFragment(map));
		}).start());
	}
}
