package com.khopan.api.common.fragment;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SeslProgressBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khopan.api.common.R;

public class LoadingFragment extends ContextedFragment {
	private final String text;

	public LoadingFragment(String text) {
		this.text = text;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new ConstraintLayout(this.context);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
		ConstraintLayout constraintLayout = (ConstraintLayout) view;
		int constraintLayoutIdentifier = View.generateViewId();
		constraintLayout.setId(constraintLayoutIdentifier);
		LayoutTransition transition = new LayoutTransition();
		transition.enableTransitionType(LayoutTransition.CHANGING);
		constraintLayout.setLayoutTransition(transition);
		SeslProgressBar progressBar = new SeslProgressBar(new ContextThemeWrapper(this.context, R.style.Widget_AppCompat_ProgressBar));
		int progressBarIdentifier = View.generateViewId();
		progressBar.setId(progressBarIdentifier);
		ConstraintLayout.LayoutParams progressBarParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		progressBarParams.leftToLeft = constraintLayoutIdentifier;
		progressBarParams.topToTop = constraintLayoutIdentifier;
		progressBarParams.rightToRight = constraintLayoutIdentifier;
		progressBarParams.bottomToBottom = constraintLayoutIdentifier;
		progressBar.setLayoutParams(progressBarParams);
		progressBar.setIndeterminate(true);
		constraintLayout.addView(progressBar);
		TextView textView = new TextView(this.context);
		ConstraintLayout.LayoutParams textViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textViewParams.leftToLeft = constraintLayoutIdentifier;
		textViewParams.topToBottom = progressBarIdentifier;
		textViewParams.rightToRight = constraintLayoutIdentifier;
		textView.setLayoutParams(textViewParams);
		textView.setText(this.text);
		textView.setGravity(Gravity.CENTER);
		constraintLayout.addView(textView);
	}
}
