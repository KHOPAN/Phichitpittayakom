package org.punlabs.phichitpittayakom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.khopan.api.common.fragment.LoadingFragment;
import com.khopan.api.common.fragment.SingleCenterTextFragment;
import com.khopan.api.common.utils.ActivityUtils;
import com.khopan.api.common.utils.LayoutUtils;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.StudentFragment;

import java.util.Optional;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;
import dev.oneuiproject.oneui.widget.Separator;
import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.Student;

public class SearchStudentActivity extends AppCompatActivity {
	private int mode;
	private EditText searchField;
	private int fragmentLayoutIdentifier;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.mode = 0;
		Intent intent = this.getIntent();
		Bundle extras = intent.getExtras();

		if(extras != null) {
			this.mode = extras.getInt("mode");
		}

		int resource;

		switch(this.mode) {
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
		searchButton.setOnClickListener(view -> this.search());
		searchView.addView(searchButton);
		this.searchField = new EditText(this);
		LinearLayout.LayoutParams searchFieldParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		searchFieldParams.weight = 1;
		searchFieldParams.rightMargin = margin;
		this.searchField.setLayoutParams(searchFieldParams);
		searchView.addView(this.searchField, 0);
		FrameLayout fragmentLayout = new FrameLayout(this);
		LayoutUtils.setLayoutTransition(fragmentLayout);
		fragmentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.fragmentLayoutIdentifier = View.generateViewId();
		fragmentLayout.setId(this.fragmentLayoutIdentifier);
		linearLayout.addView(fragmentLayout);
	}

	private void search() {
		this.setFragment(new LoadingFragment(this.getString(R.string.loading)));
		new Thread(() -> {
			Editable editable = this.searchField.getText();
			String searchQuery = "";

			if(editable != null) {
				searchQuery = editable.toString();
			}

			if(searchQuery.isEmpty()) {
				this.setFragment(new SingleCenterTextFragment(this.getString(R.string.emptySearchField)));
				return;
			}

			switch(this.mode) {
			case 0: this.identifier(searchQuery); break;
			case 1: this.national(searchQuery); break;
			case 2: this.nameParts(searchQuery); break;
			}
		}).start();
	}

	private void identifier(String searchQuery) {
		try {
			long studentIdentifier = Long.parseLong(searchQuery.trim());
			Optional<Student> optional = Phichitpittayakom.student.findStudentById(studentIdentifier);

			if(optional.isPresent()) {
				this.setFragment(new StudentFragment(optional.get(), true));
			} else {
				this.setFragment(new SingleCenterTextFragment(this.getString(R.string.noSearchResult)));
			}
		} catch(NumberFormatException ignored) {
			this.setFragment(new SingleCenterTextFragment(this.getString(R.string.invaildStudentIdentifier)));
		}
	}

	private void national(String searchQuery) {

	}

	private void nameParts(String searchQuery) {

	}

	private void setFragment(Fragment fragment) {
		this.getSupportFragmentManager()
				.beginTransaction()
				.replace(this.fragmentLayoutIdentifier, fragment)
				.commit();
	}
}
