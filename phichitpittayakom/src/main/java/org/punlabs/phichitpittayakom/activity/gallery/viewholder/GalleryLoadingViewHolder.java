package org.punlabs.phichitpittayakom.activity.gallery.viewholder;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SeslProgressBar;

import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.activity.gallery.GalleryData;
import org.punlabs.phichitpittayakom.activity.gallery.GalleryViewHolder;

public class GalleryLoadingViewHolder extends GalleryViewHolder {
	public GalleryLoadingViewHolder(View view) {
		super(view);
	}

	@Override
	public void bind(GalleryData data) {

	}

	public static GalleryLoadingViewHolder create(Context context) {
		LinearLayout linearLayout = new LinearLayout(context);
		ViewGroup.MarginLayoutParams linearLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		linearLayoutParams.topMargin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, context.getResources().getDisplayMetrics()));
		linearLayout.setLayoutParams(linearLayoutParams);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		SeslProgressBar progressBar = new SeslProgressBar(new ContextThemeWrapper(context, com.khopan.api.common.R.style.Widget_AppCompat_ProgressBar_Small));
		LinearLayout.LayoutParams progressBarParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		progressBarParams.gravity = Gravity.CENTER_HORIZONTAL;
		progressBar.setLayoutParams(progressBarParams);
		progressBar.setIndeterminate(true);
		linearLayout.addView(progressBar);
		TextView textView = new TextView(context);
		LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textViewParams.bottomMargin = linearLayoutParams.topMargin;
		textView.setLayoutParams(textViewParams);
		textView.setText(context.getString(R.string.loading));
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		linearLayout.addView(textView);
		return new GalleryLoadingViewHolder(linearLayout);
	}
}
