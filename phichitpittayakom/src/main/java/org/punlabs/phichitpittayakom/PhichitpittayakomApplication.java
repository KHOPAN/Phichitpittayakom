package org.punlabs.phichitpittayakom;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.khopan.api.common.card.CardBuilder;
import com.khopan.api.common.utils.ActivityUtils;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

public class PhichitpittayakomApplication extends AppCompatActivity {
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		LinearLayout linearLayout = ActivityUtils.initializeView(this, this.getString(R.string.title), this.getString(R.string.collapseTitle), this.getString(R.string.subtitle), false);
		CardBuilder builder = new CardBuilder(linearLayout, this);
		builder.card().title("Hello, world!").summary("This is some test text");
	}
}
