package org.punlabs.phichitpittayakom.activity;

import android.content.Context;
import android.content.Intent;

import com.khopan.api.common.activity.FragmentedActivity;

import org.punlabs.phichitpittayakom.fragment.StudentFragment;

import th.ac.phichitpittayakom.Student;

public class StudentActivity extends FragmentedActivity {
	public static Student Student;

	public StudentActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();

		if(StudentActivity.Student == null) {
			this.finish();
			return;
		}

		String title = StudentActivity.Student.getName().toString();
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(StudentActivity.Student.getStudentIdentifier() + " ⋅ " + StudentActivity.Student.getGrade() + " ⋅ " + StudentActivity.Student.getNumber());
		this.setFragment(new StudentFragment(StudentActivity.Student, false));
	}

	public static void open(Context context, Student student) {
		StudentActivity.Student = student;
		context.startActivity(new Intent(context, StudentActivity.class));
	}
}
