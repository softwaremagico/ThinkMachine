package com.softwaremagico.tm.character.skills;

/*-
 * #%L
 * The Thinking Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *  
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *  
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softwaremagico.tm.file.FileManager;
import com.softwaremagico.tm.file.Path;
import com.softwaremagico.tm.log.MachineLog;

public class SkillFactory {
	private final static String NATURAL_SKILLS_FILE = "skills-natural.txt";
	private final static String LEARNED_SKILLS_FILE = "skills-learned.txt";
	// Language and skills.
	private static Map<String, List<AvailableSkill>> naturalSkills = new HashMap<>();
	private static Map<String, List<AvailableSkill>> learnedSkills = new HashMap<>();

	private final static String skillsPath = "../" + Path.getSkillsRootPath();

	public static List<AvailableSkill> getNaturalSkills(String language) {
		if (naturalSkills.get(language) == null) {
			naturalSkills.put(language, new ArrayList<AvailableSkill>());
			try {
				for (String skillName : FileManager.inLines(skillsPath + language + File.separator + NATURAL_SKILLS_FILE)) {
					naturalSkills.get(language).add(new AvailableSkill(skillName, true));
				}
			} catch (IOException e) {
				e.printStackTrace();
				MachineLog.errorMessage(SkillFactory.class.getName(), e);
			}
			Collections.sort(naturalSkills.get(language));
		}
		return naturalSkills.get(language);
	}

	public static List<AvailableSkill> getLearnedSkills(String language) {
		if (learnedSkills.get(language) == null) {
			try {
				learnedSkills.put(language, new ArrayList<AvailableSkill>());
				for (String skillName : FileManager.inLines(skillsPath + language + File.separator + LEARNED_SKILLS_FILE)) {
					learnedSkills.get(language).add(new AvailableSkill(skillName, false));
				}
			} catch (IOException e) {
				e.printStackTrace();
				MachineLog.errorMessage(SkillFactory.class.getName(), e);
			}
			Collections.sort(learnedSkills.get(language));
		}
		return learnedSkills.get(language);
	}

}
