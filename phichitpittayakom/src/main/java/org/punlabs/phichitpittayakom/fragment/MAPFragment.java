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

import org.punlabs.phichitpittayakom.activity.MAPSectionActivity;

import th.ac.phichitpittayakom.person.map.MAPSection;
import th.ac.phichitpittayakom.person.map.ManagementAndPersonnel;

public class MAPFragment extends ContextedFragment {
	private final ManagementAndPersonnel map;

	public MAPFragment(ManagementAndPersonnel map) {
		this.map = map;
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

		for(MAPSection section : this.map.getSections()) {
			builder.card().title(MAPSectionActivity.title(this.context, section)).action(cardView -> MAPSectionActivity.action(this.context, section));
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new NestedScrollView(this.context);
	}
}
