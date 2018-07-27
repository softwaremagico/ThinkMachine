package com.softwaremagico.tm.character.blessings;

public enum BlessingGroup {
	APPEARANCE, BEHAVIOUR, INJURIES, KNACKS, REPUTATION, SIZE;

	public static BlessingGroup get(String tag) {
		if (tag != null) {
			for (BlessingGroup benefitGroup : BlessingGroup.values()) {
				if (benefitGroup.name().equalsIgnoreCase(tag)) {
					return benefitGroup;
				}
			}
		}
		return null;
	}
}
