package com.khopan.api.common.utils;

import com.khopan.api.common.activity.IFragmentActivity;
import com.khopan.api.common.fragment.CenterTextFragment;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorUtils {
	private ErrorUtils() {}

	public static String throwableToString(Throwable Errors) {
		StringWriter writer = new StringWriter();
		PrintWriter outputStream = new PrintWriter(writer);
		Errors.printStackTrace(outputStream);
		return writer.toString();
	}

	public static void setFragment(IFragmentActivity activity, Throwable Errors) {
		activity.setFragment(new CenterTextFragment(Errors.toString(), ErrorUtils.throwableToString(Errors)));
	}
}
