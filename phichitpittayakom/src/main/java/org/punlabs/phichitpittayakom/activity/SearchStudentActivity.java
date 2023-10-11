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

import org.punlabs.phichitpittayakom.fragment.ListFragment;
import org.punlabs.phichitpittayakom.fragment.StudentFragment;

import java.util.List;
import java.util.Optional;

import dev.oneuiproject.oneui.widget.RoundLinearLayout;
import dev.oneuiproject.oneui.widget.Separator;
import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.Student;
import th.ac.phichitpittayakom.nationalid.NationalID;
import th.ac.phichitpittayakom.nationalid.NationalIDParser;

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
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		frameLayout.addView(linearLayout);
		NestedScrollView scrollView = new NestedScrollView(this);
		scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		scrollView.setOverScrollMode(NestedScrollView.OVER_SCROLL_NEVER);
		linearLayout.addView(scrollView);
		LinearLayout scrollLayout = new LinearLayout(this);
		LayoutUtils.setLayoutTransition(scrollLayout);
		scrollLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(scrollLayout);
		Separator separator = new Separator(this);
		String search = this.getString(R.string.search);
		separator.setText(search);
		scrollLayout.addView(separator);
		RoundLinearLayout searchLayout = new RoundLinearLayout(this);
		LayoutUtils.setLayoutTransition(searchLayout);
		searchLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		searchLayout.setOrientation(RoundLinearLayout.VERTICAL);
		searchLayout.setBackgroundColor(this.getColor(R.color.oui_background_color));
		scrollLayout.addView(searchLayout);
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
		NationalID nationalIdentifier = NationalIDParser.parse(searchQuery);

		if(!nationalIdentifier.isValid()) {
			this.setFragment(new SingleCenterTextFragment(this.getString(R.string.invalidNationalIdentifier)));
			return;
		}

		Optional<Student> optional = Phichitpittayakom.student.findStudentByNationalID(nationalIdentifier);

		if(optional.isPresent()) {
			this.setFragment(new StudentFragment(optional.get(), true));
		} else {
			this.setFragment(new SingleCenterTextFragment(this.getString(R.string.noSearchResult)));
		}
	}

	private void nameParts(String searchQuery) {
		List<Student> studentList = Phichitpittayakom.student.findStudentsByName(searchQuery);

		if(studentList.isEmpty()) {
			this.setFragment(new SingleCenterTextFragment(this.getString(R.string.noSearchResult)));
			return;
		}

		this.setFragment(new ListFragment<>(studentList, (context, student) -> student.getName().toString(), (context, student) -> student.getStudentIdentifier() + " ⋅ " + student.getGrade() + " ⋅ " + student.getNumber(), StudentActivity :: open));
	}

	private void setFragment(Fragment fragment) {
		this.getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
				.replace(this.fragmentLayoutIdentifier, fragment)
				.commit();
	}
}
