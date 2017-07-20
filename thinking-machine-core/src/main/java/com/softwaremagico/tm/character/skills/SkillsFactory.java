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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;

public class SkillsFactory extends XmlFactory<AvailableSkill> {
	private final static String GUILD_SKILL_TAG = "guildSkill";
	private final static String GENERALIZABLE_SKILL_TAG = "generalizable";
	private final static String GROUP_SKILL_TAG = "group";
	private final static String NATURAL_SKILL_TAG = "natural";
	private static Map<String, List<AvailableSkill>> skills = new HashMap<>();
	private static List<AvailableSkill> naturalSkills = new ArrayList<>();
	private static List<AvailableSkill> learnedSkills = new ArrayList<>();

	private static ITranslator translatorLearnedSkills = LanguagePool.getTranslator("skills.xml");

	private static SkillsFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (SkillsFactory.class) {
				if (instance == null) {
					instance = new SkillsFactory();
				}
			}
		}
	}

	public static SkillsFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	public List<AvailableSkill> getNaturalSkills(String language) throws InvalidXmlElementException {
		if (naturalSkills.isEmpty()) {
			getElements(language);
		}
		return naturalSkills;
	}

	public List<AvailableSkill> getLearnedSkills(String language) throws InvalidXmlElementException {
		if (learnedSkills.isEmpty()) {
			getElements(language);
		}
		return learnedSkills;
	}

	public boolean isNaturalSkill(String skillName, String language) {
		try {
			for (AvailableSkill availableSkill : getNaturalSkills(language)) {
				if (availableSkill.getName().equals(skillName)) {
					return true;
				}
			}
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		}
		return false;
	}

	@Override
	public List<AvailableSkill> getElements(String language) throws InvalidXmlElementException {
		if (skills.get(language) == null) {
			skills.put(language, new ArrayList<AvailableSkill>());
			String lastSkillName = null;
			int added = 0;
			for (String skillId : translatorLearnedSkills.getAllTranslatedElements()) {
				AvailableSkill skill = createElement(translatorLearnedSkills, skillId, language);
				if (Objects.equals(lastSkillName, skill.getName())) {
					added++;
				} else {
					added = 0;
				}
				skill.setIndexOfGeneralization(added);
				skills.get(language).add(skill);
				lastSkillName = skill.getName();
				if (skill.isNatural()) {
					naturalSkills.add(skill);
				} else {
					learnedSkills.add(skill);
				}
			}
			Collections.sort(skills.get(language));
		}
		return skills.get(language);
	}

	@Override
	protected AvailableSkill createElement(ITranslator translator, String skillId, String language) throws InvalidXmlElementException {
		AvailableSkill skill = new AvailableSkill(skillId, translator.getTranslatedText(skillId, language));
		try {
			String generalizable = translator.getNodeValue(skillId, GENERALIZABLE_SKILL_TAG);
			skill.setGeneralizable(Boolean.parseBoolean(generalizable));
		} catch (NumberFormatException nfe) {
			throw new InvalidSkillException("Invalid generalizable value for skill '" + skillId + "'.");
		}
		try {
			String guildSkill = translator.getNodeValue(skillId, GUILD_SKILL_TAG);
			skill.setFromGuild(Boolean.parseBoolean(guildSkill));
		} catch (NumberFormatException nfe) {
			throw new InvalidSkillException("Invalid guild value for skill '" + skillId + "'.");
		}
		try {
			String group = translator.getNodeValue(skillId, GROUP_SKILL_TAG);
			skill.setSkillGroup(SkillGroup.getSkillGroup(group));
		} catch (NumberFormatException nfe) {
			throw new InvalidSkillException("Invalid group value for skill '" + skillId + "'.");
		}
		try {
			String natural = translator.getNodeValue(skillId, NATURAL_SKILL_TAG);
			skill.setNatural(Boolean.parseBoolean(natural));
		} catch (NumberFormatException nfe) {
			throw new InvalidSkillException("Invalid natural value for skill '" + skillId + "'.");
		}
		return skill;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorLearnedSkills;
	}
}
