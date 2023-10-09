package org.punlabs.phichitpittayakom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import com.khopan.api.common.utils.ActivityUtils;
import com.khopan.api.common.utils.LayoutUtils;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;
import dev.oneuiproject.oneui.widget.Separator;

public class SearchStudentActivity extends AppCompatActivity {
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		int mode = 0;
		Intent intent = this.getIntent();
		Bundle extras = intent.getExtras();

		if(extras != null) {
			mode = extras.getInt("mode");
		}

		int resource;

		switch(mode) {
		case 1:
			resource = R.string.searchStudentByNationalIdentifier;
			break;
		case 2:
			resource = R.string.searchStudentByNameParts;
			break;
		default:
			resource = R.string.searchStudentByStudentIdentifier;
			break;
		}

		String title = this.getString(resource);
		FrameLayout frameLayout = ActivityUtils.initializeForFragment(this, title, title, this.getString(R.string.collapseTitle), true);
		NestedScrollView scrollView = new NestedScrollView(this);
		scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		scrollView.setFillViewport(true);
		scrollView.setOverScrollMode(NestedScrollView.OVER_SCROLL_ALWAYS);
		LayoutUtils.forceEnableScrollbar(scrollView, true, false);
		frameLayout.addView(scrollView);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linearLayout);
		Separator separator = new Separator(this);
		String search = this.getString(R.string.search);
		separator.setText(search);
		linearLayout.addView(separator);
		RoundLinearLayout searchLayout = new RoundLinearLayout(this);
		LayoutUtils.setLayoutTransition(searchLayout);
		searchLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		searchLayout.setOrientation(RoundLinearLayout.VERTICAL);
		searchLayout.setBackgroundColor(this.getColor(R.color.oui_background_color));
		linearLayout.addView(searchLayout);
		LinearLayout searchView = new LinearLayout(this);
		LayoutUtils.setLayoutTransition(searchView);
		LinearLayout.LayoutParams searchViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		int margin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14.0f, this.getResources().getDisplayMetrics()));
		searchViewParams.leftMargin = margin;
		searchViewParams.topMargin = margin;
		searchViewParams.rightMargin = margin;
		searchViewParams.bottomMargin = margin;
		searchViewParams.gravity = Gravity.CENTER;
		searchView.setLayoutParams(searchViewParams);
		searchView.setOrientation(LinearLayout.HORIZONTAL);
		searchView.setGravity(Gravity.CENTER);
		searchLayout.addView(searchView);
		AppCompatButton searchButton = new AppCompatButton(this);
		searchButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		searchButton.setText(search);
		searchButton.setFocusable(true);
		searchButton.setClickable(true);
		searchButton.setOnClickListener(view -> {});
		searchView.addView(searchButton);
		EditText searchField = new EditText(this);
		LinearLayout.LayoutParams searchFieldParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		searchFieldParams.weight = 1;
		searchFieldParams.rightMargin = margin;
		searchField.setLayoutParams(searchFieldParams);
		searchView.addView(searchField, 0);
	}
}
