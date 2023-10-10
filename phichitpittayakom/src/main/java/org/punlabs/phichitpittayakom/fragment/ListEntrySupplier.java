package org.punlabs.phichitpittayakom.fragment;

import android.content.Context;

public interface ListEntrySupplier<T> {
	void execute(Context context, T entry);
}
