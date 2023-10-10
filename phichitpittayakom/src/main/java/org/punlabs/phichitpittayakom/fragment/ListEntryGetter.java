package org.punlabs.phichitpittayakom.fragment;

import android.content.Context;

public interface ListEntryGetter<T> {
	String getString(Context context, T entry);
}
