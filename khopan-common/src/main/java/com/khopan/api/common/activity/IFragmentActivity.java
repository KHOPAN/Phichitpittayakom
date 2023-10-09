package com.khopan.api.common.activity;

import androidx.fragment.app.Fragment;

public interface IFragmentActivity {
	Fragment getFragment();
	void setFragment(Fragment fragment);
}
