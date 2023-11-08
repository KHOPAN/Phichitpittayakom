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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;
import th.ac.phichitpittayakom.guild.GuildInfo;
import th.ac.phichitpittayakom.nationalid.NationalID;
import th.ac.phichitpittayakom.teacher.Teacher;

public class TeacherFragment extends ContextedFragment {
	private final Teacher teacher;
	private final GuildInfo guild;
	private final Bitmap image;

	private LocalDate birthdate;
	private CardView ageView;
	private CardView remainingView;
	private CardView lifePercentageView;
	private CardView remainingLifePercentageView;
	private ScheduledFuture<?> future;

	public TeacherFragment(Teacher teacher, GuildInfo guild, Bitmap image) {
		this.teacher = teacher;
		this.guild = guild;
		this.image = image;
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
		StudentFragment.buildName(builder, this.context, this.teacher.getName());
		builder.separate();
		builder.card().title(this.teacher.getNationalIdentifier().toString()).summary(this.getString(R.string.nationalIdentifier));
		String birthday = this.teacher.getBirthday();
		this.birthdate = this.teacher.getBirthdate();

		if(birthday != null && !birthday.isEmpty() && this.birthdate != null) {
			builder.separate();
			DateFormat format = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.US);
			int day = Integer.parseInt(birthday.substring(0, 2));
			int month = Integer.parseInt(birthday.substring(2, 4));
			int year = Integer.parseInt(birthday.substring(4, 8));
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+7")));
			calendar.set(Calendar.YEAR, year - 543);
			calendar.set(Calendar.MONTH, month - 1);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			builder.card().title(format.format(calendar.getTime())).summary(this.getString(R.string.birthday));

			if(this.teacher.getName().isFemale()) {
				calendar.add(Calendar.YEAR, 83);
				calendar.add(Calendar.DAY_OF_MONTH, 14);
			} else {
				calendar.add(Calendar.YEAR, 74);
				calendar.add(Calendar.MONTH, 6);
				calendar.add(Calendar.DAY_OF_MONTH, 7);
			}

			builder.card().title(format.format(calendar.getTime())).summary(this.getString(R.string.deathDate));
			builder.card().title(birthday).summary(this.getString(R.string.rawBirthday));
			builder.separate();
			this.ageView = builder.card().summary(this.getString(R.string.age)).cardView();
			this.remainingView = builder.card().summary(this.getString(R.string.remainingLifeTime)).cardView();
			this.lifePercentageView = builder.card().summary(this.getString(R.string.lifePercentage)).cardView();
			this.remainingLifePercentageView = builder.card().summary(this.getString(R.string.remainingLifePercentage)).cardView();
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

		long epoch = this.birthdate.toEpochDay() * 86400000;
		long currentTime = System.currentTimeMillis();
		LocalDate now = LocalDate.now();
		Period period = Period.between(this.birthdate, now).normalized();
		ZonedDateTime dateTime = Instant.now().atZone(ZoneId.of("GMT+7"));
		int ageYear = period.getYears();
		int ageMonth = period.getMonths();
		int ageDay = period.getDays();
		int ageHour = dateTime.get(ChronoField.HOUR_OF_DAY);
		int ageMinute = dateTime.get(ChronoField.MINUTE_OF_HOUR);
		int ageSecond = dateTime.get(ChronoField.SECOND_OF_MINUTE);
		int ageMillisecond = dateTime.get(ChronoField.MILLI_OF_SECOND);
		StringBuilder builder = new StringBuilder();

		if(ageYear > 0) {
			builder.append(ageYear == 1 ? this.getString(R.string.ageOneYear) : this.getString(R.string.ageYears, ageYear));
			builder.append(' ');
		}

		if(ageMonth > 0) {
			builder.append(ageMonth == 1 ? this.getString(R.string.ageOneMonth) : this.getString(R.string.ageMonths, ageMonth));
			builder.append(' ');
		}

		if(ageDay > 0) {
			builder.append(ageDay == 1 ? this.getString(R.string.ageOneDay) : this.getString(R.string.ageDays, ageDay));
			builder.append(' ');
		}

		builder.append(String.format(Locale.US, "%02d:%02d:%02d.%03d", ageHour, ageMinute, ageSecond, ageMillisecond));
		String ageText = builder.toString();
		boolean isFemale = this.teacher.getName().isFemale();
		Period deathPeriod = isFemale ? Period.of(83, 0, 14) : Period.of(74, 6, 7);
		deathPeriod = deathPeriod.minus(period).normalized();
		int remainingYear = deathPeriod.getYears();
		int remainingMonth = deathPeriod.getMonths();
		int remainingDay = deathPeriod.getDays();
		int remainingHour = 23 - ageHour;
		int remainingMinute = 59 - ageMinute;
		int remainingSecond = 59 - ageSecond;
		int remainingMillisecond = 999 - ageMillisecond;
		builder = new StringBuilder();

		if(remainingYear > 0) {
			builder.append(remainingYear == 1 ? this.getString(R.string.ageOneYear) : this.getString(R.string.ageYears, remainingYear));
			builder.append(' ');
		}

		if(remainingMonth > 0) {
			builder.append(remainingMonth == 1 ? this.getString(R.string.ageOneMonth) : this.getString(R.string.ageMonths, remainingMonth));
			builder.append(' ');
		}

		if(remainingDay > 0) {
			builder.append(remainingDay == 1 ? this.getString(R.string.ageOneDay) : this.getString(R.string.ageDays, remainingDay));
			builder.append(' ');
		}

		builder.append(String.format(Locale.US, "%02d:%02d:%02d.%03d", remainingHour, remainingMinute, remainingSecond, remainingMillisecond));
		String remainingText = builder.toString();
		long ageTime = currentTime - epoch;
		long total = isFemale ? 2618697600000L : 2349820800000L;
		double percentage = (((double) ageTime) / ((double) total)) * 100.0d;
		String percentageText = String.format(Locale.US, "%.10f%%", percentage);
		String remainingPercentageText = String.format(Locale.US, "%.10f%%", 100.0d - percentage);
		this.requireActivity().runOnUiThread(() -> {
			this.ageView.setTitle(ageText);
			this.remainingView.setTitle(remainingText);
			this.lifePercentageView.setTitle(percentageText);
			this.remainingLifePercentageView.setTitle(remainingPercentageText);
		});
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
