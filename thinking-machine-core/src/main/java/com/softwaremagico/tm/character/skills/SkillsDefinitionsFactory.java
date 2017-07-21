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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;

public class SkillsDefinitionsFactory extends XmlFactory<SkillDefinition> {
	private final static String GUILD_SKILL_TAG = "guildSkill";
	private final static String SPECIALIZABLE_SKILL_TAG = "specializations";
	private final static String GROUP_SKILL_TAG = "group";
	private final static String NATURAL_SKILL_TAG = "natural";
	private final static String NUMBER_TO_SHOW_TAG = "numberToShow";
	private static List<SkillDefinition> naturalSkills = new ArrayList<>();
	private static List<SkillDefinition> learnedSkills = new ArrayList<>();

	private static ITranslator translatorLearnedSkills = LanguagePool.getTranslator("skills.xml");

	private static SkillsDefinitionsFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (SkillsDefinitionsFactory.class) {
				if (instance == null) {
					instance = new SkillsDefinitionsFactory();
				}
			}
		}
	}

	public static SkillsDefinitionsFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	public List<SkillDefinition> getNaturalSkills(String language) throws InvalidXmlElementException {
		if (naturalSkills.isEmpty()) {
			getElements(language);
		}
		return naturalSkills;
	}

	public List<SkillDefinition> getLearnedSkills(String language) throws InvalidXmlElementException {
		if (learnedSkills.isEmpty()) {
			getElements(language);
		}
		return learnedSkills;
	}

	public boolean isNaturalSkill(String skillName, String language) {
		try {
			for (SkillDefinition availableSkill : getNaturalSkills(language)) {
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
	public List<SkillDefinition> getElements(String language) throws InvalidXmlElementException {
		if (elements.get(language) == null) {
			elements.put(language, new ArrayList<SkillDefinition>());
			for (String skillId : translatorLearnedSkills.getAllTranslatedElements()) {
				SkillDefinition skill = createElement(translatorLearnedSkills, skillId, language);
				elements.get(language).add(skill);
				if (skill.isNatural()) {
					naturalSkills.add(skill);
					Collections.sort(naturalSkills);
				} else {
					learnedSkills.add(skill);
					Collections.sort(learnedSkills);
				}
			}
			Collections.sort(elements.get(language));
		}
		return elements.get(language);
	}

	@Override
	protected SkillDefinition createElement(ITranslator translator, String skillId, String language) throws InvalidXmlElementException {
		SkillDefinition skill = new SkillDefinition(skillId, translator.getTranslatedText(skillId, language));
		try {
			String generalizable = translator.getNodeValue(skillId, SPECIALIZABLE_SKILL_TAG);
			if (generalizable != null) {
				String[] generalizations = generalizable.replaceAll(", ", ",").split(",");
				skill.setSpecializations(new HashSet<String>(Arrays.asList(generalizations)));
			}
		} catch (NumberFormatException nfe) {
			throw new InvalidSkillException("Invalid generalizable value for skill '" + skillId + "'.");
		}
		try {
			String numberToShow = translator.getNodeValue(skillId, NUMBER_TO_SHOW_TAG);
			if (numberToShow != null) {
				skill.setNumberToShow(Integer.parseInt(numberToShow));
			}
		} catch (NumberFormatException nfe) {
			throw new InvalidSkillException("Invalid number value for skill '" + skillId + "'.");
		}
		String guildSkill = translator.getNodeValue(skillId, GUILD_SKILL_TAG);
		skill.setFromGuild(Boolean.parseBoolean(guildSkill));
		String group = translator.getNodeValue(skillId, GROUP_SKILL_TAG);
		skill.setSkillGroup(SkillGroup.getSkillGroup(group));

		String natural = translator.getNodeValue(skillId, NATURAL_SKILL_TAG);
		skill.setNatural(Boolean.parseBoolean(natural));

		return skill;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorLearnedSkills;
	}
}
