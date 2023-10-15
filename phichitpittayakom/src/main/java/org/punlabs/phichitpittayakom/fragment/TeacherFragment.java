package org.punlabs.phichitpittayakom.fragment;

import android.graphics.Bitmap;
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
import org.punlabs.phichitpittayakom.activity.TeacherActivity;
import org.punlabs.phichitpittayakom.view.AutoScaleImageView;

import java.util.ArrayList;
import java.util.List;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;
import th.ac.phichitpittayakom.guild.GuildInfo;
import th.ac.phichitpittayakom.nationalid.NationalID;
import th.ac.phichitpittayakom.teacher.Teacher;

public class TeacherFragment extends ContextedFragment {
	private final Teacher teacher;
	private final GuildInfo guild;
	private final Bitmap image;

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
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new NestedScrollView(this.context);
	}
}
