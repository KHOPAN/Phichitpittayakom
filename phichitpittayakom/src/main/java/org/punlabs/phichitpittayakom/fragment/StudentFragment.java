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

import org.punlabs.phichitpittayakom.activity.GuildActivity;

import th.ac.phichitpittayakom.GuildInfo;
import th.ac.phichitpittayakom.Student;
import th.ac.phichitpittayakom.Teacher;
import th.ac.phichitpittayakom.grade.Grade;
import th.ac.phichitpittayakom.name.Name;

public class StudentFragment extends ContextedFragment {
	private final Student student;
	private final GuildInfo guild;
	private final boolean showResult;

	public StudentFragment(Student student, GuildInfo guild, boolean showResult) {
		this.student = student;
		this.guild = guild;
		this.showResult = showResult;
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

		if(this.showResult) {
			builder.separate(this.getString(R.string.foundOneSearchResult));
		}

		Name name = this.student.getName();
		builder.card().title(name.getPrefix().toString()).summary(this.getString(R.string.namePrefix));
		String[] names = name.getNames();

		for(int i = 0; i < names.length; i++) {
			builder.card().title(names[i]).summary(this.getString(i == 0 ? R.string.firstName : i == names.length - 1 ? R.string.lastName : R.string.middleName));
		}

		builder.separate();
		Grade grade = this.student.getGrade();
		builder.card().title(this.getString(R.string.gradeNumber, grade.getGrade())).summary(this.getString(R.string.mathayomsuksa));
		builder.card().title(Integer.toString(grade.getRoom())).summary(this.getString(R.string.classRoomNumber));
		builder.separate();
		builder.card().title(Integer.toString(this.student.getNumber())).summary(this.getString(R.string.number));
		builder.card().title(Long.toString(this.student.getStudentIdentifier())).summary(this.getString(R.string.studentIdentifier));
		builder.separate(this.getString(R.string.guild));
		builder.card().title(GuildActivity.title(this.context, this.guild)).summary(GuildActivity.summary(this.context, this.guild));
		Teacher[] teachers = this.guild.getTeachers();

		if(teachers.length != 0) {
			builder.separate(this.getString(teachers.length == 1 ? R.string.guildMaster : R.string.guildMasters));

			for(Teacher teacher : teachers) {
				builder.card().title(teacher.getName().toString()).summary(teacher.getNationalIdentifier().toString());
			}
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new NestedScrollView(this.context);
	}
}
