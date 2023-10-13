package org.punlabs.phichitpittayakom.activity;

import android.content.Context;
import android.content.Intent;

import com.khopan.api.common.activity.FragmentedActivity;
import com.khopan.api.common.fragment.LoadingFragment;
import com.khopan.api.common.fragment.SingleCenterTextFragment;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.GuildFragment;

import java.util.Locale;
import java.util.Optional;

import th.ac.phichitpittayakom.Guild;
import th.ac.phichitpittayakom.GuildInfo;
import th.ac.phichitpittayakom.Phichitpittayakom;

public class GuildActivity extends FragmentedActivity {
	private static Guild Guild;

	public GuildActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();

		if(GuildActivity.Guild == null) {
			this.finish();
			return;
		}

		String title = GuildActivity.title(this, GuildActivity.Guild);
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(GuildActivity.summary(this, GuildActivity.Guild));
		long identifier = GuildActivity.Guild.getIdentifier();
		this.setFragment(new LoadingFragment(this.getString(R.string.loading)));
		new Thread(() -> {
			Optional<GuildInfo> optional = Phichitpittayakom.guild.findGuildById(identifier);

			if(!optional.isPresent()) {
				this.setFragment(new SingleCenterTextFragment(this.getString(R.string.noSearchResult)));
				return;
			}

			GuildInfo guild = optional.get();
			this.setFragment(new GuildFragment(guild));
		}).start();
	}

	public static String title(Context ignoredContext, Guild guild) {
		return guild.getName();
	}

	public static String summary(Context ignoredContext, Guild guild) {
		String percentage = String.format(Locale.getDefault(), "%.2f%%", (((double) guild.getMemberCount()) / ((double) guild.getMaximumMembers())) * 100.0d);
		return percentage + " ⋅ " + guild.getIdentifier() + " ⋅ " + guild.getGuildClass() + " ⋅ " + guild.getLocation();
	}

	public static void action(Context context, Guild guild) {
		GuildActivity.Guild = guild;
		context.startActivity(new Intent(context, GuildActivity.class));
	}
}
