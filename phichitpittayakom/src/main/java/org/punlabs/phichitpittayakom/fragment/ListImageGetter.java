package org.punlabs.phichitpittayakom.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

public interface ListImageGetter<T> {
	BitmapDrawable getImage(Context context, T entry);
}
