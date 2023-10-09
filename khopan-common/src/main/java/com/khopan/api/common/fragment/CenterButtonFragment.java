package com.khopan.api.common.fragment;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CenterButtonFragment extends ContextedFragment {
	private String buttonText;
	private CenterButtonListener centerButtonListener;

	private AppCompatButton button;

	public CenterButtonFragment() {
		this.buttonText = "";
		this.centerButtonListener = null;
	}

	public CenterButtonFragment(String buttonText, CenterButtonListener centerButtonListener) {
		this.buttonText = buttonText == null ? "" : buttonText;
		this.centerButtonListener = centerButtonListener;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
		ConstraintLayout constraintLayout = (ConstraintLayout) view;
		int constraintLayoutIdentifier = View.generateViewId();
		constraintLayout.setId(constraintLayoutIdentifier);
		LayoutTransition transition = new LayoutTransition();
		transition.enableTransitionType(LayoutTransition.CHANGING);
		constraintLayout.setLayoutTransition(transition);
		this.button = new AppCompatButton(this.context);
		ConstraintLayout.LayoutParams buttonParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		buttonParams.leftToLeft = constraintLayoutIdentifier;
		buttonParams.topToTop = constraintLayoutIdentifier;
		buttonParams.rightToRight = constraintLayoutIdentifier;
		buttonParams.bottomToBottom = constraintLayoutIdentifier;
		this.button.setLayoutParams(buttonParams);
		this.button.setText(this.buttonText);
		this.button.setFocusable(true);
		this.button.setClickable(true);
		this.button.setOnClickListener(instance -> this.button());
		constraintLayout.addView(this.button);
	}

	public String getButtonText() {
		return this.buttonText;
	}

	public CenterButtonListener getCenterButtonListener() {
		return this.centerButtonListener;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText == null ? "" : buttonText;
		this.button.setText(this.buttonText);
	}

	public void setCenterButtonListener(CenterButtonListener centerButtonListener) {
		this.centerButtonListener = centerButtonListener;
	}

	private void button() {
		if(this.centerButtonListener != null) {
			this.centerButtonListener.onButtonPressed();
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new ConstraintLayout(this.context);
	}

	public interface CenterButtonListener {
		void onButtonPressed();
	}
}
