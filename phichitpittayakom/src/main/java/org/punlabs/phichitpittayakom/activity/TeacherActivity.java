package org.punlabs.phichitpittayakom.activity;

import android.content.Context;
import android.content.Intent;

import com.khopan.api.common.activity.FragmentedActivity;
import com.khopan.api.common.fragment.LoadingFragment;
import com.khopan.api.common.fragment.SingleCenterTextFragment;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.TeacherFragment;

import java.util.Optional;

import th.ac.phichitpittayakom.GuildInfo;
import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.Teacher;

public class TeacherActivity extends FragmentedActivity {
	private static Teacher Teacher;

	public TeacherActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();

		if(TeacherActivity.Teacher == null) {
			this.finish();
			return;
		}

		String title = TeacherActivity.title(this, TeacherActivity.Teacher);
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(TeacherActivity.summary(this, TeacherActivity.Teacher));
		this.setFragment(new LoadingFragment(this.getString(R.string.loading)));
		long identifier = TeacherActivity.Teacher.getGuildIdentifier();
		new Thread(() -> {
			Optional<GuildInfo> optional = Phichitpittayakom.guild.findGuildById(identifier);

			if(!optional.isPresent()) {
				this.setFragment(new SingleCenterTextFragment(this.getString(R.string.noSearchResult)));
				return;
			}

			GuildInfo guild = optional.get();
			this.setFragment(new TeacherFragment(TeacherActivity.Teacher, guild));
		}).start();
	}

	public static String title(Context ignoredContext, Teacher teacher) {
		return teacher.getName().toString();
	}

	public static String summary(Context ignoredContext, Teacher teacher) {
		return teacher.getNationalIdentifier().toString();
	}

	public static void action(Context context, Teacher teacher) {
		TeacherActivity.Teacher = teacher;
		context.startActivity(new Intent(context, TeacherActivity.class));
	}
}