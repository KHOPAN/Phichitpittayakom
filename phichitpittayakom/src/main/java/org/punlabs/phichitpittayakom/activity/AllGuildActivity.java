package org.punlabs.phichitpittayakom.activity;

import com.khopan.api.common.activity.FragmentedActivity;
import com.khopan.api.common.fragment.LoadingFragment;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.ListFragment;

import java.util.List;

import th.ac.phichitpittayakom.Guild;
import th.ac.phichitpittayakom.Phichitpittayakom;

public class AllGuildActivity extends FragmentedActivity {
	public AllGuildActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();
		String title = this.getString(R.string.allGuild);
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(this.getString(R.string.collapseTitle));
		this.setFragment(new LoadingFragment(this.getString(R.string.loading)));
		new Thread(() -> {
			List<Guild> guildList = Phichitpittayakom.guild.getAllGuild();
			this.setFragment(new ListFragment<>(guildList, (context, guild) -> guild.getName(), (context, guild) -> guild.getIdentifier() + " ⋅ " + guild.getLocation() + " ⋅ " + guild.getGuildClass() + " ⋅ " + guild.getMemberCount() + "/" + guild.getMaximumMembers(), (context, guild) -> {}));
		}).start();
	}
}
