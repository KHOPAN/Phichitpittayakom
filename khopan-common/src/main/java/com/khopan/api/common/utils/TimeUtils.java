package com.khopan.api.common.utils;

import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {
	private TimeUtils() {}

	public static String formatTime(Calendar time) {
		return String.format(Locale.getDefault(), "%02d/%02d/%02d %02d:%02d", time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.MONTH) + 1, time.get(Calendar.YEAR), time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
	}
}
