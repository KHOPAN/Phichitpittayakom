package org.punlabs.phichitpittayakom.activity;

import android.content.Context;
import android.content.Intent;

import com.khopan.api.common.activity.FragmentedActivity;
import com.khopan.api.common.fragment.LoadingFragment;
import com.khopan.api.common.fragment.SingleCenterTextFragment;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.StudentFragment;

import java.util.Optional;

import th.ac.phichitpittayakom.GuildInfo;
import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.Student;

public class StudentActivity extends FragmentedActivity {
	public static Student Student;

	public StudentActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();

		if(StudentActivity.Student == null) {
			this.finish();
			return;
		}

		String title = StudentActivity.Student.getName().toString();
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(StudentActivity.Student.getStudentIdentifier() + " ⋅ " + StudentActivity.Student.getGrade() + " ⋅ " + StudentActivity.Student.getNumber());
		long identifier = StudentActivity.Student.getGuildIdentifier();
		this.setFragment(new LoadingFragment(this.getString(R.string.loading)));
		new Thread(() -> {
			Optional<GuildInfo> optional = Phichitpittayakom.guild.findGuildById(identifier);

			if(!optional.isPresent()) {
				this.setFragment(new SingleCenterTextFragment(this.getString(R.string.noSearchResult)));
				return;
			}

			GuildInfo guild = optional.get();
			this.setFragment(new StudentFragment(StudentActivity.Student, guild, false));
		}).start();
	}

	public static void open(Context context, Student student) {
		StudentActivity.Student = student;
		context.startActivity(new Intent(context, StudentActivity.class));
	}
}
