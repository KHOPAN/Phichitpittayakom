package org.punlabs.phichitpittayakom.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.khopan.api.common.activity.FragmentedActivity;
import com.khopan.api.common.fragment.SingleCenterTextFragment;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.TeacherFragment;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.guild.GuildInfo;
import th.ac.phichitpittayakom.teacher.Teacher;

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
		long identifier = TeacherActivity.Teacher.getGuildIdentifier();
		this.loading();
		this.internet(() -> new Thread(() -> {
			Optional<GuildInfo> guildOptional = Phichitpittayakom.guild.findGuildById(identifier);

			if(!guildOptional.isPresent()) {
				this.setFragment(new SingleCenterTextFragment(this.getString(R.string.noSearchResult)));
				return;
			}

			GuildInfo guild = guildOptional.get();
			Optional<byte[]> imageOptional = TeacherActivity.Teacher.getImage();
			Bitmap image = null;

			if(imageOptional.isPresent()) {
				byte[] imageData = imageOptional.get();
				image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
			}

			this.setFragment(new TeacherFragment(TeacherActivity.Teacher, guild, image));
		}).start());
	}

	public static String title(Context ignoredContext, Teacher teacher) {
		return teacher.getName().toString();
	}

	public static String summary(Context context, Teacher teacher) {
		StringBuilder builder = new StringBuilder();
		builder.append(teacher.getNationalIdentifier().toString());
		LocalDate date = teacher.getBirthdate();

		if(date != null) {
			Period period = Period.between(date, LocalDate.now());
			int age = period.getYears();
			builder.append(" ⋅ ");
			builder.append(age == 1 ? context.getString(R.string.ageOneYear) : context.getString(R.string.ageYears, age));
		}

		return builder.toString();
	}

	public static void action(Context context, Teacher teacher) {
		TeacherActivity.Teacher = teacher;
		context.startActivity(new Intent(context, TeacherActivity.class));
	}
}
