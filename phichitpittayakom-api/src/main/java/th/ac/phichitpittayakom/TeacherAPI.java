package th.ac.phichitpittayakom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import th.ac.phichitpittayakom.guild.Guild;
import th.ac.phichitpittayakom.guild.GuildInfo;
import th.ac.phichitpittayakom.teacher.Teacher;

public class TeacherAPI {
	TeacherAPI() {}

	public List<Teacher> getAllTeacher() {
		List<Guild> guildList = Phichitpittayakom.guild.getAllGuild();
		List<Thread> threadList = new ArrayList<>();
		List<Teacher> teacherList = new ArrayList<>();

		for(int i = 0; i < guildList.size(); i++) {
			Guild guild = guildList.get(i);
			Thread thread = new Thread(() -> {
				Optional<GuildInfo> optional = guild.getAdditionalInfo();

				if(optional.isPresent()) {
					GuildInfo information = optional.get();
					teacherList.addAll(Arrays.asList(information.getTeachers()));
				}
			});

			thread.start();
			threadList.add(thread);
		}

		for(Thread thread : threadList) {
			try {
				thread.join();
			} catch(InterruptedException ignored) {

			}
		}

		teacherList.sort(Teacher :: compare);
		return teacherList;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
