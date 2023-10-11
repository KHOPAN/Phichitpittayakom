package com.khopan.api.common.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.khopan.api.common.R;
import com.khopan.api.common.utils.LayoutUtils;
import com.khopan.api.common.utils.NetworkUtils;

import dev.oneuiproject.oneui.layout.ToolbarLayout;

public abstract class FragmentedActivity extends AppCompatActivity implements IFragmentActivity {
	protected final String expandedTitle;
	protected final String collapseTitle;
	protected final String expandedSubtitle;

	protected Fragment fragment;

	protected ToolbarLayout toolbarLayout;
	protected FrameLayout fragmentLayout;
	protected int frameLayoutIdentifier;

	public FragmentedActivity(String expandedTitle, String collapseTitle, String expandedSubtitle) {
		this.expandedTitle = expandedTitle;
		this.collapseTitle = collapseTitle;
		this.expandedSubtitle = expandedSubtitle;
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.toolbarLayout = new ToolbarLayout(this, null);
		LayoutUtils.setLayoutTransition(this.toolbarLayout);
		this.setContentView(this.toolbarLayout);
		this.toolbarLayout.setTitle(this.expandedTitle, this.collapseTitle);
		this.toolbarLayout.setExpandedSubtitle(this.expandedSubtitle);
		this.toolbarLayout.setExpanded(false, false);
		this.fragmentLayout = new FrameLayout(this);
		LayoutUtils.setLayoutTransition(this.fragmentLayout);
		this.fragmentLayout.setLayoutParams(new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.frameLayoutIdentifier = View.generateViewId();
		this.fragmentLayout.setId(this.frameLayoutIdentifier);
		this.toolbarLayout.addView(this.fragmentLayout);
		this.initialize();
	}

	public void loading() {
		LayoutUtils.loading(this);
	}

	public void internet(Runnable action) {
		NetworkUtils.assertInternet(this, action);
	}

	@Override
	public Fragment getFragment() {
		return this.fragment;
	}

	@Override
	public void setFragment(Fragment fragment) {
		this.fragment = fragment;

		if(this.fragment == null) {
			this.fragmentLayout.removeAllViews();
		} else {
			this.getSupportFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
					.replace(this.frameLayoutIdentifier, fragment)
					.commit();
		}
	}

	public abstract void initialize();

	@Override
	public void onBackPressed() {
		if(!(this.fragment instanceof OnBackListener && ((OnBackListener) this.fragment).onBackPressed())) {
			super.onBackPressed();
		}
	}
}
