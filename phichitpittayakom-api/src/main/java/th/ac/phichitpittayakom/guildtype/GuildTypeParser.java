package th.ac.phichitpittayakom.guildtype;

public class GuildTypeParser {
	private final GuildType guildType;

	private GuildTypeParser(String guildType) {
		if(guildType == null || guildType.isEmpty()) {
			this.guildType = GuildType.UNDEFINED;
			return;
		}

		guildType = guildType.replaceAll("\\s+", "");

		for(GuildType type : GuildType.values()) {
			if(type.getStringRepresentation().equals(guildType)) {
				this.guildType = type;
				return;
			}
		}

		this.guildType = GuildType.UNDEFINED;
	}

	@Override
	public String toString() {
		return this.guildType.toString();
	}

	public static GuildType parse(String guildType) {
		return new GuildTypeParser(guildType).guildType;
	}
}
