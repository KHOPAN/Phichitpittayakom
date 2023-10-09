package com.khopan.api.common.utils;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.khopan.api.common.R;
import com.khopan.api.common.activity.IFragmentActivity;
import com.khopan.api.common.fragment.LoadingFragment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class LayoutUtils {
	private LayoutUtils() {}

	public static View createEmptySeparator(Context context) {
		View separator = new View(context);
		separator.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(R.dimen.sesl_list_subheader_min_height)));
		return separator;
	}

	public static void showErrorDialog(Context context, Throwable Errors) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		Errors.printStackTrace(printWriter);
		new AlertDialog.Builder(context)
				.setMessage(stringWriter.toString())
				.setNeutralButton("OK", (dialog, button) -> {})
				.show();
	}

	public static void forceEnableScrollbar(View view, boolean vertical, boolean horizontal) {
		Context context = view.getContext();

		if(context == null) {
			return;
		}

		try {
			String methodName = "initializeScrollbars";
			@SuppressLint("DiscouragedPrivateApi")
			Method method = View.class.getDeclaredMethod(methodName, TypedArray.class);
			method.setAccessible(true);
			method.invoke(view, context.obtainStyledAttributes(null, new int[] {}));
		} catch(Throwable ignored) {

		}

		view.setVerticalScrollBarEnabled(vertical);
		view.setHorizontalScrollBarEnabled(horizontal);
	}

	public static void setLayoutTransition(ViewGroup view) {
		LayoutTransition transition = new LayoutTransition();
		transition.enableTransitionType(LayoutTransition.APPEARING);
		transition.enableTransitionType(LayoutTransition.DISAPPEARING);
		transition.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
		transition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
		transition.enableTransitionType(LayoutTransition.CHANGING);
		view.setLayoutTransition(transition);
	}

	public static <T extends Context & IFragmentActivity> void loading(T activity) {
		activity.setFragment(new LoadingFragment(activity.getString(R.string.loading)));
	}
}
