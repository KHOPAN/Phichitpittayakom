package th.ac.phichitpittayakom.guild;

import java.util.Optional;

import th.ac.phichitpittayakom.Phichitpittayakom;
import th.ac.phichitpittayakom.guildclass.GuildClass;
import th.ac.phichitpittayakom.name.Name;

public class Guild {
	private final String name;
	private final long identifier;
	private final String location;
	private final Name[] teacherNames;
	private final GuildClass guildClass;
	private final int memberCount;
	private final int remainingCount;
	private final int minimumMembers;
	private final int maximumMembers;

	public Guild() {
		this.name = "";
		this.identifier = 0L;
		this.location = "";
		this.teacherNames = new Name[0];
		this.guildClass = GuildClass.UNDEFINED;
		this.memberCount = 0;
		this.remainingCount = 0;
		this.minimumMembers = 0;
		this.maximumMembers = 0;
	}

	public Guild(String name, long identifier, String location, Name[] teacherNames, GuildClass guildClass, int memberCount, int remainingCount, int minimumMembers, int maximumMembers) {
		this.name = name;
		this.identifier = identifier;
		this.location = location;
		this.teacherNames = teacherNames;
		this.guildClass = guildClass;
		this.memberCount = memberCount;
		this.remainingCount = remainingCount;
		this.minimumMembers = minimumMembers;
		this.maximumMembers = maximumMembers;
	}

	public String getName() {
		return this.name;
	}

	public long getIdentifier() {
		return this.identifier;
	}

	public String getLocation() {
		return this.location;
	}

	public Name[] getTeacherNames() {
		return this.teacherNames;
	}

	public GuildClass getGuildClass() {
		return this.guildClass;
	}

	public int getMemberCount() {
		return this.memberCount;
	}

	public int getRemainingCount() {
		return this.remainingCount;
	}

	public int getMinimumMembers() {
		return this.minimumMembers;
	}

	public int getMaximumMembers() {
		return this.maximumMembers;
	}

	public Optional<GuildInfo> getAdditionalInfo() {
		return Phichitpittayakom.guild.findGuildById(this.identifier);
	}

	@Override
	public String toString() {
		return this.name + " [" + this.identifier + "]";
	}

	public static int compare(Guild x, Guild y) {
		if(x == null && y == null) {
			return 0;
		} else if(x == null) {
			return -1;
		} else if(y == null) {
			return 1;
		}

		return Long.compare(x.identifier, y.identifier);
	}
}
