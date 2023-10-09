package com.khopan.api.common.fragment;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khopan.api.common.R;

public class CenterButtonTextFragment extends ContextedFragment {
	private String text;
	private String buttonText;
	private CenterButtonTextListener centerButtonTextListener;

	private AppCompatButton button;
	private TextView textView;

	public CenterButtonTextFragment() {
		this.text = "";
		this.buttonText = "";
		this.centerButtonTextListener = null;
	}

	public CenterButtonTextFragment(String text, String buttonText, CenterButtonTextListener centerButtonTextListener) {
		this.text = text == null ? "" : text;
		this.buttonText = buttonText == null ? "" : buttonText;
		this.centerButtonTextListener = centerButtonTextListener;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
		ConstraintLayout constraintLayout = (ConstraintLayout) view;
		int constraintLayoutIdentifier = View.generateViewId();
		constraintLayout.setId(constraintLayoutIdentifier);
		LayoutTransition transition = new LayoutTransition();
		transition.enableTransitionType(LayoutTransition.CHANGING);
		constraintLayout.setLayoutTransition(transition);
		TypedValue value = new TypedValue();
		this.context.getTheme().resolveAttribute(R.attr.buttonStyle, value, true);
		this.button = new AppCompatButton(new ContextThemeWrapper(this.context, value.data));
		int buttonIdentifier = View.generateViewId();
		this.button.setId(buttonIdentifier);
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
		this.textView = new TextView(this.context);
		ConstraintLayout.LayoutParams textViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textViewParams.leftToLeft = constraintLayoutIdentifier;
		textViewParams.topToTop = constraintLayoutIdentifier;
		textViewParams.rightToRight = constraintLayoutIdentifier;
		textViewParams.bottomToTop = buttonIdentifier;
		this.textView.setLayoutParams(textViewParams);
		this.textView.setText(this.text);
		this.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
		this.textView.setTextColor(this.context.getColorStateList(R.color.oui_btn_colored_background));
		this.textView.setGravity(Gravity.CENTER);
		constraintLayout.addView(this.textView);
	}

	public String getText() {
		return this.text;
	}

	public String getButtonText() {
		return this.buttonText;
	}

	public CenterButtonTextListener getCenterButtonTextListener() {
		return this.centerButtonTextListener;
	}

	public void setText(String text) {
		this.text = text == null ? "" : text;
		this.textView.setText(this.text);
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText == null ? "" : buttonText;
		this.button.setText(this.buttonText);
	}

	public void setCenterButtonTextListener(CenterButtonTextListener centerButtonTextListener) {
		this.centerButtonTextListener = centerButtonTextListener;
	}

	private void button() {
		if(this.centerButtonTextListener != null) {
			this.centerButtonTextListener.onButtonPressed();
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new ConstraintLayout(this.context);
	}

	public interface CenterButtonTextListener {
		void onButtonPressed();
	}
}
