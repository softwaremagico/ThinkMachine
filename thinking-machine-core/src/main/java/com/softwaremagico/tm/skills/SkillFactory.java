package com.softwaremagico.tm.skills;

import java.util.List;

import com.softwaremagico.tm.file.FileManager;

public class SkillFactory {
	private final static String NATURAL_SKILLS_FILE = "skills-natural.txt";
	private final static String LEARNED_SKILLS_FILE = "skills-learned.txt";
	private static List<String> naturalSkills;
	private static List<String> learnedSkills;

	public static List<String> getNaturalSkills() {
		if (naturalSkills == null) {
			naturalSkills = FileManager.getFileFromResources(NATURAL_SKILLS_FILE);
		}
		return naturalSkills;
	}

	public static List<String> getLearnedSkills() {
		if (learnedSkills == null) {
			learnedSkills = FileManager.getFileFromResources(LEARNED_SKILLS_FILE);
		}
		return learnedSkills;
	}

}
