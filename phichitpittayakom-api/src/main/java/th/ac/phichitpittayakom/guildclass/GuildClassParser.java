package th.ac.phichitpittayakom.guildclass;

public class GuildClassParser {
	private final GuildClass guildClass;

	private GuildClassParser(String guildType) {
		if(guildType == null || guildType.isEmpty()) {
			this.guildClass = GuildClass.UNDEFINED;
			return;
		}

		guildType = guildType.replaceAll("\\s+", "");

		switch(guildType) {
		case "ม.ต้น":
			this.guildClass = GuildClass.CLASS_LOW;
			break;
		case "ม.ต้น+ม.ปลาย":
			this.guildClass = GuildClass.CLASS_BOTH;
			break;
		case "ม.ปลาย":
			this.guildClass = GuildClass.CLASS_HIGH;
			break;
		default:
			this.guildClass = GuildClass.UNDEFINED;
			break;
		}
	}

	@Override
	public String toString() {
		return this.guildClass.toString();
	}

	public static GuildClass parse(String guildClass) {
		return new GuildClassParser(guildClass).guildClass;
	}
}
