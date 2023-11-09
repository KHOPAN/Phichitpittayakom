package org.punlabs.phichitpittayakom.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.khopan.api.common.card.CardBuilder;
import com.khopan.api.common.card.CardView;
import com.khopan.api.common.fragment.ContextedFragment;
import com.khopan.api.common.utils.LayoutUtils;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.activity.GuildActivity;
import org.punlabs.phichitpittayakom.activity.TeacherActivity;
import org.punlabs.phichitpittayakom.view.AutoScaleImageView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;
import th.ac.phichitpittayakom.guild.GuildInfo;
import th.ac.phichitpittayakom.name.Name;
import th.ac.phichitpittayakom.nationalid.NationalID;
import th.ac.phichitpittayakom.teacher.Teacher;

public class TeacherFragment extends ContextedFragment {
	private final Teacher teacher;
	private final GuildInfo guild;
	private final Bitmap image;
	private final DateTimeFormatter formatter;

	private LocalDate birthdate;
	private long birthdateEpoch;
	private LocalDate deathDate;
	private long deathDateEpoch;
	private CardView ageView;
	private CardView remainingView;
	private CardView lifePercentageView;
	private ScheduledFuture<?> future;

	public TeacherFragment(Teacher teacher, GuildInfo guild, Bitmap image) {
		this.teacher = teacher;
		this.guild = guild;
		this.image = image;
		this.formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.getDefault());
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
		NestedScrollView scrollView = (NestedScrollView) view;
		scrollView.setFillViewport(true);
		scrollView.setOverScrollMode(NestedScrollView.OVER_SCROLL_ALWAYS);
		LinearLayout linearLayout = new LinearLayout(this.context);
		LayoutUtils.setLayoutTransition(linearLayout);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linearLayout);
		CardBuilder builder = new CardBuilder(linearLayout, this.context);
		Name name = this.teacher.getName();
		StudentFragment.buildName(builder, this.context, name);
		builder.separate();
		builder.card().title(this.teacher.getNationalIdentifier().toString()).summary(this.getString(R.string.nationalIdentifier));
		String birthday = this.teacher.getBirthday();
		this.birthdate = this.teacher.getBirthdate();

		if(birthday != null && !birthday.isEmpty() && this.birthdate != null) {
			builder.separate();
			builder.card().title(birthday).summary(this.getString(R.string.rawBirthday));
			builder.separate();
			int day = Integer.parseInt(birthday.substring(0, 2));
			int month = Integer.parseInt(birthday.substring(2, 4));
			int year = Integer.parseInt(birthday.substring(4, 8));
			this.birthdate = LocalDate.of(year - 543, month, day);
			this.birthdateEpoch = this.birthdate.toEpochDay() * 86400000L;
			builder.card().title(this.formatter.format(this.birthdate)).summary(this.getString(R.string.birthday));
			boolean female = name.isFemale();
			Period deathAge = female ? Period.of(83, 0, 14) : Period.of(74, 6, 7);
			this.deathDate = this.birthdate.plus(deathAge);
			this.deathDateEpoch = this.deathDate.toEpochDay() * 86400000L;
			builder.card().title(this.formatter.format(this.deathDate)).summary(this.getString(R.string.deathDate));
			builder.separate();
			this.ageView = builder.card().summary(this.getString(R.string.age)).cardView();
			this.remainingView = builder.card().summary(this.getString(R.string.remainingLifeTime)).cardView();
			builder.separate();
			this.lifePercentageView = builder.card().summary(this.getString(R.string.lifePercentage)).cardView();
			this.update();
		}

		builder.separate(this.getString(R.string.guild));
		builder.card().title(GuildActivity.title(this.context, this.guild)).summary(GuildActivity.summary(this.context, this.guild)).action(cardView -> GuildActivity.action(this.context, this.guild));
		Teacher[] teachers = this.guild.getTeachers();
		List<Teacher> teacherList = new ArrayList<>();

		for(Teacher teacher : teachers) {
			if(teacher == null) {
				continue;
			}

			teacherList.add(teacher);
		}

		teacherList.removeIf(teacher -> NationalID.compare(teacher.getNationalIdentifier(), this.teacher.getNationalIdentifier()) == 0);

		if(teacherList.size() > 0) {
			builder.separate(this.getString(R.string.guildMasterWith));

			for(Teacher teacher : teacherList) {
				builder.card().title(TeacherActivity.title(this.context, teacher)).summary(TeacherActivity.summary(this.context, teacher)).action(cardView -> TeacherActivity.action(this.context, teacher));
			}
		}

		if(this.image == null) {
			return;
		}

		builder.separate(this.getString(R.string.image));
		RoundLinearLayout imageLayout = new RoundLinearLayout(this.context);
		imageLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		imageLayout.setOrientation(RoundLinearLayout.VERTICAL);
		imageLayout.setBackgroundColor(this.context.getColor(R.color.oui_background_color));
		linearLayout.addView(imageLayout);
		AutoScaleImageView imageView = new AutoScaleImageView(this.context, this.image);
		imageView.setLayoutParams(new RoundLinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		imageLayout.addView(imageView);
		WindowManager manager = this.context.getSystemService(WindowManager.class);
		Display display = manager.getDefaultDisplay();
		float framerate = display.getRefreshRate();
		long pauseTime = Math.round(1000.0d / ((double) framerate));
		this.future = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this :: update, 0, pauseTime, TimeUnit.MILLISECONDS);
	}

	private void update() {
		if(this.birthdate == null) {
			return;
		}

		LocalDate now = LocalDate.now();
		long time = System.currentTimeMillis();
		Period birthPeriod = Period.between(this.birthdate, now);
		ZonedDateTime dateTime = Instant.now().atZone(ZoneId.of("GMT+7"));
		int hours = dateTime.get(ChronoField.HOUR_OF_DAY);
		int minutes = dateTime.get(ChronoField.MINUTE_OF_HOUR);
		int seconds = dateTime.get(ChronoField.SECOND_OF_MINUTE);
		int milliseconds = dateTime.get(ChronoField.MILLI_OF_SECOND);
		String birthText = this.formatDateTime(birthPeriod, hours, minutes, seconds, milliseconds);
		Period remainingPeriod = Period.between(now, this.deathDate);
		hours = 23 - hours;
		minutes = 59 - minutes;
		seconds = 59 - seconds;
		milliseconds = 999 - milliseconds;
		String remainingText = this.formatDateTime(remainingPeriod, hours, minutes, seconds, milliseconds);
		long usedLifeTime = time - this.birthdateEpoch;
		long totalLifeTime = this.deathDateEpoch - this.birthdateEpoch;
		double lifePercentage = ((double) usedLifeTime) / ((double) totalLifeTime) * 100.0d;
		String lifePercentageText = String.format(Locale.US, "%.10f%%", lifePercentage);
		this.requireActivity().runOnUiThread(() -> {
			try {
				this.ageView.setTitle(birthText);
				this.remainingView.setTitle(remainingText);
				this.lifePercentageView.setTitle(lifePercentageText);
			} catch(Throwable ignored) {

			}
		});
	}

	private String formatDateTime(Period period, int hour, int minute, int second, int millisecond) {
		int years = period.getYears();
		int months = period.getMonths();
		int days = period.getDays();
		StringBuilder builder = new StringBuilder();

		if(years > 0) {
			builder.append(years == 1 ? this.getString(R.string.ageOneYear) : this.getString(R.string.ageYears, years));
			builder.append(' ');
		}

		if(months > 0) {
			builder.append(months == 1 ? this.getString(R.string.ageOneMonth) : this.getString(R.string.ageMonths, months));
			builder.append(' ');
		}

		if(days > 0) {
			builder.append(days == 1 ? this.getString(R.string.ageOneDay) : this.getString(R.string.ageDays, days));
			builder.append(' ');
		}

		builder.append(String.format(Locale.US, "%02d:%02d:%02d.%03d", hour, minute, second, millisecond));
		return builder.toString();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if(this.future != null) {
			this.future.cancel(true);
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new NestedScrollView(this.context);
	}
}
