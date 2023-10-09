package com.khopan.api.common.fragment;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import com.khopan.api.common.R;

public class CenterTextFragment extends ContextedFragment {
	private TextView textView;
	private TextView descriptionView;

	private String text;
	private String description;

	public CenterTextFragment(String text, String description) {
		this.text = text == null ? "" : text;
		this.description = description == null ? "" : description;
	}

	public String getText() {
		return this.text;
	}

	public String getDescription() {
		return this.description;
	}

	public void setText(String text) {
		this.text = text == null ? "" : text;
		this.textView.setText(this.text);
	}

	public void setDescription(String description) {
		this.description = description == null ? "" : description;
		this.descriptionView.setText(this.description);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
		ConstraintLayout constraintLayout = (ConstraintLayout) view;
		int constraintLayoutIdentifier = View.generateViewId();
		constraintLayout.setId(constraintLayoutIdentifier);
		LayoutTransition transition = new LayoutTransition();
		transition.enableTransitionType(LayoutTransition.CHANGING);
		constraintLayout.setLayoutTransition(transition);
		/*this.textView = new TextView(this.context);
		ConstraintLayout.LayoutParams textViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textViewParams.leftToLeft = constraintLayoutIdentifier;
		textViewParams.topToTop = constraintLayoutIdentifier;
		textViewParams.rightToRight = constraintLayoutIdentifier;
		textViewParams.bottomToBottom = constraintLayoutIdentifier;
		this.textView.setLayoutParams(textViewParams);
		this.textView.setText(this.text);
		this.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.0f);
		this.textView.setTextColor(this.context.getColorStateList(R.color.oui_btn_colored_background));
		this.textView.setGravity(Gravity.CENTER);
		int numberViewIdentifier = View.generateViewId();
		this.textView.setId(numberViewIdentifier);
		constraintLayout.addView(this.textView);
		this.descriptionView = new TextView(this.context);
		ConstraintLayout.LayoutParams descriptionViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		descriptionViewParams.leftToLeft = constraintLayoutIdentifier;
		descriptionViewParams.topToBottom = numberViewIdentifier;
		descriptionViewParams.rightToRight = constraintLayoutIdentifier;
		this.descriptionView.setLayoutParams(descriptionViewParams);
		this.descriptionView.setText(this.description);
		this.descriptionView.setTextColor(this.context.getColorStateList(R.color.oui_btn_colored_background));
		this.descriptionView.setGravity(Gravity.CENTER);
		this.descriptionView.setEnabled(false);
		constraintLayout.addView(this.descriptionView);*/
		LinearLayout linearLayout = new LinearLayout(this.context);
		ConstraintLayout.LayoutParams linearLayoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		linearLayoutParams.leftToLeft = constraintLayoutIdentifier;
		linearLayoutParams.topToTop = constraintLayoutIdentifier;
		linearLayoutParams.rightToRight = constraintLayoutIdentifier;
		linearLayoutParams.bottomToBottom = constraintLayoutIdentifier;
		linearLayout.setLayoutParams(linearLayoutParams);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		constraintLayout.addView(linearLayout);

		this.textView = new TextView(this.context);
		this.textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.textView.setText(this.text);
		this.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.0f);
		this.textView.setTextColor(this.context.getColorStateList(R.color.oui_btn_colored_background));
		this.textView.setGravity(Gravity.CENTER);
		linearLayout.addView(this.textView);

		this.descriptionView = new TextView(this.context);
		this.descriptionView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.descriptionView.setText(this.description);
		this.descriptionView.setText(this.description);
		this.descriptionView.setTextColor(this.context.getColorStateList(R.color.oui_btn_colored_background));
		this.descriptionView.setGravity(Gravity.CENTER);
		this.descriptionView.setEnabled(false);
		linearLayout.addView(this.descriptionView);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new ConstraintLayout(this.context);
	}
}
