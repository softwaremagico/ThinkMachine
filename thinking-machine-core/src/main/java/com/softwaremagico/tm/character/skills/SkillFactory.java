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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class SkillFactory {
	private final static String GUILD_SKILL_TAG = "guildSkill";
	private final static String GENERALIZABLE_SKILL_TAG = "generalizable";
	// Language and skills.
	private static Map<String, List<AvailableSkill>> naturalSkills = new HashMap<>();
	private static Map<String, List<AvailableSkill>> learnedSkills = new HashMap<>();

	private static ITranslator translatorNaturalSkills = LanguagePool.getTranslator("skills_natural.xml");
	private static ITranslator translatorLearnedSkills = LanguagePool.getTranslator("skills_learned.xml");

	public static List<AvailableSkill> getNaturalSkills(String language) {
		if (naturalSkills.get(language) == null) {
			naturalSkills.put(language, new ArrayList<AvailableSkill>());
			for (String skillId : translatorNaturalSkills.getAllTranslatedElements()) {
				naturalSkills.get(language).add(createSkill(translatorNaturalSkills, skillId, language, true));
			}
			Collections.sort(naturalSkills.get(language));
		}
		return naturalSkills.get(language);
	}

	public static List<AvailableSkill> getLearnedSkills(String language) {
		if (learnedSkills.get(language) == null) {
			learnedSkills.put(language, new ArrayList<AvailableSkill>());
			String lastSkillName = null;
			int added = 0;
			for (String skillId : translatorLearnedSkills.getAllTranslatedElements()) {
				AvailableSkill skill = createSkill(translatorLearnedSkills, skillId, language, false);
				if (Objects.equals(lastSkillName, skill.getName())) {
					added++;
				} else {
					added = 0;
				}
				skill.setIndexOfGeneralization(added);
				learnedSkills.get(language).add(skill);
				lastSkillName = skill.getName();
			}
			Collections.sort(learnedSkills.get(language));
		}
		return learnedSkills.get(language);
	}

	public static boolean isNaturalSkill(String skillName, String language) {
		for (AvailableSkill availableSkill : getNaturalSkills(language)) {
			if (availableSkill.getName().equals(skillName)) {
				return true;
			}
		}
		return false;
	}

	private static AvailableSkill createSkill(ITranslator translator, String skillId, String language, boolean isNatural) {
		AvailableSkill skill = new AvailableSkill(skillId, translator.getTranslatedText(skillId, language), isNatural);
		String generalizable = translator.getNodeValue(skillId, GENERALIZABLE_SKILL_TAG);
		skill.setGeneralizable(Boolean.parseBoolean(generalizable));
		String guildSkill = translator.getNodeValue(skillId, GUILD_SKILL_TAG);
		skill.setFromGuild(Boolean.parseBoolean(guildSkill));
		return skill;
	}
}
