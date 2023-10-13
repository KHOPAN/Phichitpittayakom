package org.punlabs.phichitpittayakom.activity;

import com.khopan.api.common.activity.FragmentedActivity;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.ListFragment;

import java.util.List;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.Student;

public class AllStudentActivity extends FragmentedActivity {
	public AllStudentActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();
		String title = this.getString(R.string.allStudent);
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(this.getString(R.string.collapseTitle));
		this.loading();
		this.internet(() -> new Thread(() -> {
			List<Student> studentList = Phichitpittayakom.student.getAllStudent();
			this.setFragment(new ListFragment<>(studentList, StudentActivity :: title, StudentActivity :: summary, StudentActivity :: action));
		}).start());
	}
}
