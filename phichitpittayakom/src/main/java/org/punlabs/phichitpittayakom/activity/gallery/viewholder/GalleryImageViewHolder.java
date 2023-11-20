package org.punlabs.phichitpittayakom.activity.gallery.viewholder;

import android.content.Context;

import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.activity.gallery.GalleryData;
import org.punlabs.phichitpittayakom.activity.gallery.GalleryViewHolder;
import org.punlabs.phichitpittayakom.activity.gallery.data.GalleryImageData;
import org.punlabs.phichitpittayakom.view.AutoScaleImageView;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;

public class GalleryImageViewHolder extends GalleryViewHolder {
	private final AutoScaleImageView imageView;

	public GalleryImageViewHolder(RoundLinearLayout root, AutoScaleImageView imageView) {
		super(root);
		this.imageView = imageView;
	}

	@Override
	public void bind(GalleryData data) {
		if(data instanceof GalleryImageData) {
			this.imageView.setImage(((GalleryImageData) data).getImage());
		}
	}

	public static GalleryImageViewHolder create(Context context) {
		RoundLinearLayout linearLayout = new RoundLinearLayout(context);
		linearLayout.setOrientation(RoundLinearLayout.VERTICAL);
		linearLayout.setBackgroundColor(context.getColor(R.color.oui_background_color));
		AutoScaleImageView imageView = new AutoScaleImageView(context);
		linearLayout.addView(imageView);
		return new GalleryImageViewHolder(linearLayout, imageView);
	}
}
