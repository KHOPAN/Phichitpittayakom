package th.ac.phichitpittayakom.goal;

import java.util.ArrayList;
import java.util.List;

public class GoalParser {
	private final String[] goals;

	private GoalParser(String goal) {
		if(goal == null || goal.isEmpty()) {
			this.goals = new String[0];
			return;
		}

		List<String> textList = new ArrayList<>();
		String[] parts = goal.split("\\s+");
		int expected = 1;
		int counter = 0;
		StringBuilder builder = new StringBuilder();

		for(String part : parts) {
			if(part.equals(expected + ".")) {
				expected += 1;

				if(expected > 2) {
					textList.add(builder.toString());
					builder = new StringBuilder();
					counter = 0;
				}
			} else {
				if(counter != 0) {
					builder.append(' ');
				}

				builder.append(part);
				counter++;
			}
		}

		textList.add(builder.toString());
		this.goals = textList.toArray(new String[0]);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for(int i = 0; i < this.goals.length; i++) {
			if(i != 0) {
				builder.append(", ");
			}

			builder.append(this.goals[i]);
		}

		return builder.toString();
	}

	public static String[] parse(String goal) {
		return new GoalParser(goal).goals;
	}
}
