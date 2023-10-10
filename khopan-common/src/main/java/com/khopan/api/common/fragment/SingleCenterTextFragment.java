package com.khopan.api.common.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khopan.api.common.R;
import com.khopan.api.common.utils.LayoutUtils;

public class SingleCenterTextFragment extends ContextedFragment {
	private String text;
	private TextView textView;

	public SingleCenterTextFragment(String text) {
		this.text = text == null ? "" : text;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text == null ? "" : text;

		if(this.textView != null) {
			this.textView.setText(this.text);
		}
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
		ConstraintLayout constraintLayout = (ConstraintLayout) view;
		LayoutUtils.setLayoutTransition(constraintLayout);
		int constraintLayoutIdentifier = View.generateViewId();
		constraintLayout.setId(constraintLayoutIdentifier);
		this.textView = new TextView(this.context);
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
		constraintLayout.addView(this.textView);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new ConstraintLayout(this.context);
	}
}
