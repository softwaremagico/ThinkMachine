package com.softwaremagico.tm.character.skills;

public enum SkillRandomProbability {
	LOW, FAIR, GOOD;

	public static SkillRandomProbability get(String probabilityName) {
		for (SkillRandomProbability probability : SkillRandomProbability.values()) {
			if (probability.name().equalsIgnoreCase(probabilityName)) {
				return probability;
			}
		}
		return null;
	}
}
