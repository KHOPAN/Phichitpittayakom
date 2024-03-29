package org.punlabs.phichitpittayakom.activity;

import com.khopan.api.common.activity.FragmentedActivity;
import com.sec.sesl.org.punlabs.phichitpittayakom.R;

import org.punlabs.phichitpittayakom.fragment.ListFragment;

import java.util.List;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.teacher.Teacher;

public class AllTeacherActivity extends FragmentedActivity {
	public AllTeacherActivity() {
		super("", "", "");
	}

	@Override
	public void initialize() {
		this.toolbarLayout.setNavigationButtonAsBack();
		String title = this.getString(R.string.allTeacher);
		this.toolbarLayout.setTitle(title, title);
		this.toolbarLayout.setExpandedSubtitle(this.getString(R.string.collapseTitle));
		this.loading();
		this.internet(() -> new Thread(() -> {
			List<Teacher> teacherList = Phichitpittayakom.teacher.getAllTeacher();
			this.setFragment(new ListFragment<>(teacherList, TeacherActivity :: title, TeacherActivity :: summary, TeacherActivity :: action));
		}).start());
	}
}
