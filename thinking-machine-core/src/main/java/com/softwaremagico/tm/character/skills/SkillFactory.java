package com.softwaremagico.tm.character.skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.softwaremagico.tm.file.FileManager;

public class SkillFactory {
	private final static String NATURAL_SKILLS_FILE = "skills-natural.txt";
	private final static String LEARNED_SKILLS_FILE = "skills-learned.txt";
	private static List<Skill> naturalSkills;
	private static List<Skill> learnedSkills;

	public static List<Skill> getNaturalSkills() {
		if (naturalSkills == null) {
			naturalSkills = new ArrayList<>();
			for (String skillName : FileManager.getFileFromResources(NATURAL_SKILLS_FILE)) {
				naturalSkills.add(new Skill(skillName));
			}
			Collections.sort(naturalSkills);
		}
		return naturalSkills;
	}

	public static List<Skill> getLearnedSkills() {
		if (learnedSkills == null) {
			learnedSkills = new ArrayList<>();
			for (String skillName : FileManager.getFileFromResources(LEARNED_SKILLS_FILE)) {
				learnedSkills.add(new Skill(skillName));
			}
			Collections.sort(learnedSkills);
		}
		return learnedSkills;
	}

}
