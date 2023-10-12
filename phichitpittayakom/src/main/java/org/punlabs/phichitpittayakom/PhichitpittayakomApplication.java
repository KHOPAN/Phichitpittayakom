package org.punlabs.phichitpittayakom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.khopan.api.common.card.CardBuilder;
import com.khopan.api.common.utils.ActivityUtils;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.activity.AllGuildActivity;
import org.punlabs.phichitpittayakom.activity.AllStudentActivity;
import org.punlabs.phichitpittayakom.activity.SearchStudentActivity;

public class PhichitpittayakomApplication extends AppCompatActivity {
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		LinearLayout linearLayout = ActivityUtils.initializeView(this, this.getString(R.string.title), this.getString(R.string.collapseTitle), this.getString(R.string.subtitle), false);
		CardBuilder builder = new CardBuilder(linearLayout, this);
		builder.separate(this.getString(R.string.searchStudentBy));
		this.searchActivity(builder, R.string.studentIdentifier, 0);
		this.searchActivity(builder, R.string.nationalIdentifier, 1);
		this.searchActivity(builder, R.string.nameParts, 2);
		builder.separate();
		builder.card().title(this.getString(R.string.allStudent)).action(cardView -> this.startActivity(new Intent(this, AllStudentActivity.class)));
		builder.card().title(this.getString(R.string.allGuild)).action(cardView -> this.startActivity(new Intent(this, AllGuildActivity.class)));
	}

	private void searchActivity(CardBuilder builder, @StringRes int resource, int mode) {
		builder.card().title(this.getString(resource)).action(view -> {
			Intent intent = new Intent(this, SearchStudentActivity.class);
			Bundle extras = new Bundle();
			extras.putInt("mode", mode);
			intent.putExtras(extras);
			this.startActivity(intent);
		});
	}
}
