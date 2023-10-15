package org.punlabs.phichitpittayakom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.khopan.api.common.card.CardBuilder;
import com.khopan.api.common.fragment.ContextedFragment;
import com.khopan.api.common.utils.LayoutUtils;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.activity.StudentActivity;
import org.punlabs.phichitpittayakom.activity.TeacherActivity;

import java.util.Locale;

import th.ac.phichitpittayakom.guild.GuildInfo;
import th.ac.phichitpittayakom.student.Student;
import th.ac.phichitpittayakom.teacher.Teacher;

public class GuildFragment extends ContextedFragment {
	private final GuildInfo guild;

	public GuildFragment(GuildInfo guild) {
		this.guild = guild;
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
		builder.card().title(Long.toString(this.guild.getIdentifier())).summary(this.getString(R.string.guildIdentifier));
		builder.card().title(this.guild.getName()).summary(this.getString(R.string.guildName));
		builder.card().title(this.guild.getLocation()).summary(this.getString(R.string.guildLocation));
		builder.card().title(this.guild.getGuildClass().getStringRepresentation()).summary(this.getString(R.string.guildClass));
		builder.card().title(this.guild.getSubjectArea()).summary(this.getString(R.string.subjectArea));
		String[] goals = this.guild.getGoals();

		if(goals.length != 0) {
			builder.separate(this.getString(goals.length == 1 ? R.string.goal : R.string.goals));

			for(int i = 0; i < goals.length; i++) {
				builder.card().title((i + 1) + ". " + goals[i]);
			}

			builder.separate();
		}

		int memberCount = this.guild.getMemberCount();
		int maximumMembers = this.guild.getMaximumMembers();
		builder.card().title(String.format(Locale.getDefault(), "%.2f%%", (((double) memberCount) / ((double) maximumMembers)) * 100.0d)).summary(this.getString(R.string.memberCountPercentage));
		builder.separate();
		int minimumMembers = this.guild.getMinimumMembers();
		builder.card().title(Integer.toString(minimumMembers)).summary(this.getString(minimumMembers == 1 ? R.string.minimumMember : R.string.minimumMembers));
		builder.card().title(Integer.toString(maximumMembers)).summary(this.getString(maximumMembers == 1 ? R.string.maximumMember : R.string.maximumMembers));
		builder.card().title(Integer.toString(memberCount)).summary(this.getString(R.string.memberCount));
		builder.card().title(Integer.toString(this.guild.getRemainingCount())).summary(this.getString(R.string.remainingCount));
		Teacher[] teachers = this.guild.getTeachers();

		if(teachers.length != 0) {
			builder.separate(this.getString(teachers.length == 1 ? R.string.guildMaster : R.string.guildMasters));

			for(Teacher teacher : teachers) {
				builder.card().title(TeacherActivity.title(this.context, teacher)).summary(TeacherActivity.summary(this.context, teacher)).action(cardView -> TeacherActivity.action(this.context, teacher));
			}
		}

		Student[] students = this.guild.getMembers();

		if(students.length != 0) {
			builder.separate(this.getString(students.length == 1 ? R.string.member : R.string.members));

			for(Student student : students) {
				builder.card().title(StudentActivity.title(this.context, student)).summary(StudentActivity.summary(this.context, student)).action(cardView -> StudentActivity.action(this.context, student));
			}
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new NestedScrollView(this.context);
	}
}
