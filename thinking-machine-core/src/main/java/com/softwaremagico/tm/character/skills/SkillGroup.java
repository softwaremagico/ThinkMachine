package com.softwaremagico.tm.character.skills;

public enum SkillGroup {

	ANALYTICAL,

	COMBAT,

	CONTROL,

	CREATIVE,

	LORE,

	MALEFACTION,

	PHYSICAL,

	SCIENCE,

	SOCIAL,

	TECHNICAL;

	public static SkillGroup getSkillGroup(String tag) {
		for (SkillGroup skillGroup : SkillGroup.values()) {
			if (skillGroup.name().toLowerCase().equals(tag.toLowerCase())) {
				return skillGroup;
			}
		}
		return null;
	}
}
