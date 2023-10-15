package org.punlabs.phichitpittayakom.activity;

import com.khopan.api.common.activity.FragmentedActivity;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.ListFragment;

import java.util.List;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.guild.Guild;

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
		this.loading();
		this.internet(() -> new Thread(() -> {
			List<Guild> guildList = Phichitpittayakom.guild.getAllGuild();
			this.setFragment(new ListFragment<>(guildList, GuildActivity :: title, GuildActivity :: summary, GuildActivity :: action));
		}).start());
	}
}
