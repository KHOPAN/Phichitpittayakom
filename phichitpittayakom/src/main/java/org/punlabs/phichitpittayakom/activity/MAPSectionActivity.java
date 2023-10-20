package org.punlabs.phichitpittayakom.activity;

import android.content.Context;
import android.content.Intent;

import com.khopan.api.common.activity.FragmentedActivity;
import com.khopan.api.common.fragment.SingleCenterTextFragment;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.MAPSectionFragment;

import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.person.map.MAPSection;
import th.ac.phichitpittayakom.person.map.MAPSectionInfo;

public class MAPSectionActivity extends FragmentedActivity {
	private static MAPSection Section;

	public MAPSectionActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();

		if(MAPSectionActivity.Section == null) {
			this.finish();
			return;
		}

		String title = MAPSectionActivity.title(this, MAPSectionActivity.Section);
		this.toolbarLayout.setTitle(title, title);
		this.loading();
		String identifier = MAPSectionActivity.Section.getIdentifier();
		this.internet(() -> new Thread(() -> {
			Optional<MAPSectionInfo> optional = Phichitpittayakom.school.findMAPSectionById(identifier);

			if(!optional.isPresent()) {
				this.setFragment(new SingleCenterTextFragment(this.getString(R.string.noSearchResult)));
				return;
			}

			MAPSectionInfo section = optional.get();
			this.setFragment(new MAPSectionFragment(section));
		}).start());
	}

	public static String title(Context ignoredContext, MAPSection section) {
		return section.getName();
	}

	public static void action(Context context, MAPSection section) {
		MAPSectionActivity.Section = section;
		context.startActivity(new Intent(context, MAPSectionActivity.class));
	}
}
