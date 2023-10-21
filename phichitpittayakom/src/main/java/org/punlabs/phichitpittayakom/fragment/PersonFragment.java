package org.punlabs.phichitpittayakom.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.widget.NestedScrollView;

import com.khopan.api.common.card.CardBuilder;
import com.khopan.api.common.fragment.ContextedFragment;
import com.khopan.api.common.utils.LayoutUtils;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.view.AutoScaleImageView;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;
import th.ac.phichitpittayakom.person.PersonInfo;

public class PersonFragment extends ContextedFragment {
	private final PersonInfo person;
	private final Bitmap image;

	public PersonFragment(PersonInfo person, Bitmap image) {
		this.person = person;
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
		StudentFragment.buildName(builder, this.context, this.person.getName());
		builder.separate();
		this.build(builder, this.person.getSubjectArea(), R.string.subjectArea);
		this.build(builder, this.person.getPosition(), R.string.position);
		this.build(builder, this.person.getEducation(), R.string.education);
		this.build(builder, this.person.getMajors(), R.string.majors);
		this.build(builder, this.person.getPhone(), R.string.phone);
		this.build(builder, this.person.getEmail(), R.string.email);

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

	private void build(CardBuilder builder, String title, @StringRes int summary) {
		if(title == null || title.isEmpty()) {
			return;
		}

		builder.card().title(title).summary(this.getString(summary));
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new NestedScrollView(this.context);
	}
}
