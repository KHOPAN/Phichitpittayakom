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
			String dateFormat = format.format(calendar.getTime());
			builder.card().title(dateFormat).summary(this.getString(R.string.birthday));
			builder.card().title(birthday).summary(this.getString(R.string.rawBirthday));
			builder.separate();
			this.ageView = builder.card().summary(this.getString(R.string.age)).cardView();
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
		Period period = Period.between(this.birthdate, now);
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
		this.requireActivity().runOnUiThread(() -> this.ageView.setTitle(ageText));
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
