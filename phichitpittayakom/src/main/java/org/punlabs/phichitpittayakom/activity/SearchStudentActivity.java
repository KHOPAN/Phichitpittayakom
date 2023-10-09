package org.punlabs.phichitpittayakom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.khopan.api.common.utils.ActivityUtils;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

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
		LinearLayout linearLayout = ActivityUtils.initializeView(this, title, title, this.getString(R.string.collapseTitle), true);
	}
}
