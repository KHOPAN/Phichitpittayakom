package org.punlabs.phichitpittayakom.activity.gallery.viewholder;

import android.content.Context;

import com.khopan.api.common.card.CardView;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.activity.gallery.GalleryViewHolder;
import org.punlabs.phichitpittayakom.activity.gallery.data.GalleryCardViewData;
import org.punlabs.phichitpittayakom.activity.gallery.GalleryData;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;

public class GalleryCardViewHolder extends GalleryViewHolder {
	private final CardView cardView;

	public GalleryCardViewHolder(RoundLinearLayout root, CardView cardView) {
		super(root);
		this.cardView = cardView;
	}

	@Override
	public void bind(GalleryData data) {
		if(data instanceof GalleryCardViewData) {
			this.cardView.setTitle(((GalleryCardViewData) data).getText());
		}
	}

	public static GalleryCardViewHolder create(Context context) {
		RoundLinearLayout linearLayout = new RoundLinearLayout(context);
		linearLayout.setOrientation(RoundLinearLayout.VERTICAL);
		linearLayout.setBackgroundColor(context.getColor(R.color.oui_background_color));
		CardView cardView = new CardView(context);
		linearLayout.addView(cardView);
		return new GalleryCardViewHolder(linearLayout, cardView);
	}
}
