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

import org.punlabs.phichitpittayakom.activity.PersonActivity;

import th.ac.phichitpittayakom.person.Person;
import th.ac.phichitpittayakom.person.map.MAPSectionInfo;

public class MAPSectionFragment extends ContextedFragment {
	private final MAPSectionInfo section;

	public MAPSectionFragment(MAPSectionInfo section) {
		this.section = section;
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
		builder.separate(this.section.getLeaderText());
		this.addPerson(builder, this.section.getLeader());
		builder.separate();

		for(Person person : this.section.getPeople()) {
			this.addPerson(builder, person);
		}
	}

	private void addPerson(CardBuilder builder, Person person) {
		builder.card().title(PersonActivity.title(this.context, person)).summary(PersonActivity.summary(this.context, person)).action(cardView -> PersonActivity.action(this.context, person));
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new NestedScrollView(this.context);
	}
}
