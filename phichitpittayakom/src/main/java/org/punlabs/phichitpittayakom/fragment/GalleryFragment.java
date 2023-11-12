package org.punlabs.phichitpittayakom.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.khopan.api.common.card.CardBuilder;
import com.khopan.api.common.fragment.ContextedFragment;
import com.khopan.api.common.utils.LayoutUtils;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.view.AutoScaleImageView;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;
import th.ac.phichitpittayakom.gallery.GalleryDetail;

public class GalleryFragment extends ContextedFragment {
	private final GalleryDetail gallery;
	private final Bitmap[] images;

	public GalleryFragment(GalleryDetail gallery, Bitmap[] images) {
		this.gallery = gallery;
		this.images = images;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
		NestedScrollView scrollView = (NestedScrollView) view;
		scrollView.setFillViewport(true);
		scrollView.setOverScrollMode(NestedScrollView.OVER_SCROLL_ALWAYS);
		LinearLayout linearLayout = new LinearLayout(this.context);
		LayoutUtils.setLayoutTransition(linearLayout);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linearLayout);
		CardBuilder builder = new CardBuilder(linearLayout, this.context);
		builder.card().title(this.gallery.getTitle());
		String content = this.gallery.getContent();

		if(content != null && !content.isEmpty()) {
			builder.separate();
			builder.card().title(content);
		}

		for(Bitmap bitmap : this.images) {
			builder.separate();
			RoundLinearLayout imageLayout = new RoundLinearLayout(this.context);
			imageLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			imageLayout.setOrientation(RoundLinearLayout.VERTICAL);
			imageLayout.setBackgroundColor(this.context.getColor(R.color.oui_background_color));
			linearLayout.addView(imageLayout);
			AutoScaleImageView imageView = new AutoScaleImageView(this.context, bitmap);
			imageView.setLayoutParams(new RoundLinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			imageLayout.addView(imageView);
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
		return new NestedScrollView(this.context);
	}
}
