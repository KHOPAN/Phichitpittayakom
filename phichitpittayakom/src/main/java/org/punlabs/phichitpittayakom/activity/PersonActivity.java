package org.punlabs.phichitpittayakom.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.khopan.api.common.activity.FragmentedActivity;

import org.punlabs.phichitpittayakom.fragment.PersonFragment;

import java.util.Optional;

import th.ac.phichitpittayakom.person.Person;

public class PersonActivity extends FragmentedActivity {
	private static Person Person;

	public PersonActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();

		if(PersonActivity.Person == null) {
			this.finish();
			return;
		}

		String title = PersonActivity.title(this, PersonActivity.Person);
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(PersonActivity.summary(this, PersonActivity.Person));
		this.loading();
		Person person = PersonActivity.Person;
		this.internet(() -> new Thread(() -> {
			Optional<byte[]> imageOptional = person.getImage();
			Bitmap image = null;

			if(imageOptional.isPresent()) {
				byte[] imageData = imageOptional.get();
				image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
			}

			this.setFragment(new PersonFragment(person, image));
		}).start());
	}

	public static String title(Context ignoredContext, Person person) {
		return person.getName().toString();
	}

	public static String summary(Context ignoredontext, Person person) {
		return person.getPosition();
	}

	public static void action(Context context, Person person) {
		PersonActivity.Person = person;
		context.startActivity(new Intent(context, PersonActivity.class));
	}
}
