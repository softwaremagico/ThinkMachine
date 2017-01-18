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
import java.util.List;

import com.softwaremagico.tm.file.FileManager;
import com.softwaremagico.tm.file.Path;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.log.MachineLog;

public class SkillFactory {
	private final static String NATURAL_SKILLS_FILE = "skills-natural.txt";
	private final static String LEARNED_SKILLS_FILE = "skills-learned.txt";
	private static List<Skill> naturalSkills;
	private static List<Skill> learnedSkills;

	private final static String skillsPath = "../" + Path.getSkillsRootPath() + Translator.getLanguage();

	public static List<Skill> getNaturalSkills() {
		if (naturalSkills == null) {
			naturalSkills = new ArrayList<>();
			try {
				for (String skillName : FileManager.inLines(skillsPath + File.separator + NATURAL_SKILLS_FILE)) {
					naturalSkills.add(new Skill(skillName));
				}
			} catch (IOException e) {
				e.printStackTrace();
				MachineLog.errorMessage(SkillFactory.class.getName(), e);
			}
			Collections.sort(naturalSkills);
		}
		return naturalSkills;
	}

	public static List<Skill> getLearnedSkills() {
		if (learnedSkills == null) {
			try {
				learnedSkills = new ArrayList<>();
				for (String skillName : FileManager.inLines(skillsPath + File.separator + LEARNED_SKILLS_FILE)) {
					learnedSkills.add(new Skill(skillName));
				}
			} catch (IOException e) {
				e.printStackTrace();
				MachineLog.errorMessage(SkillFactory.class.getName(), e);
			}
			Collections.sort(learnedSkills);
		}
		return learnedSkills;
	}

}
