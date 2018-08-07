package com.softwaremagico.tm.character.skills;

/*-
 * #%L
 * Think Machine (Core)
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.traits.InvalidBlessingException;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class SkillsDefinitionsFactory extends XmlFactory<SkillDefinition> {
	private final static ITranslator translatorSkill = LanguagePool.getTranslator("skills.xml");

	private final static String NAME = "name";
	private final static String FACTION_SKILL_TAG = "factionSkill";
	private final static String SPECIALIZABLE_SKILL_TAG = "specializations";
	private final static String GROUP_SKILL_TAG = "group";
	private final static String NATURAL_SKILL_TAG = "natural";
	private final static String NUMBER_TO_SHOW_TAG = "numberToShow";
	private static Map<String, List<SkillDefinition>> naturalSkills = new HashMap<>();
	private static Map<String, List<SkillDefinition>> learnedSkills = new HashMap<>();

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

	@Override
	public void clearCache() {
		naturalSkills = new HashMap<>();
		learnedSkills = new HashMap<>();
		super.clearCache();
	}

	public static SkillsDefinitionsFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	public List<SkillDefinition> getNaturalSkills(String language) {
		return naturalSkills.get(language);
	}

	public List<SkillDefinition> getLearnedSkills(String language) {
		return learnedSkills.get(language);
	}

	public boolean isNaturalSkill(String skillName, String language) {
		for (SkillDefinition availableSkill : getNaturalSkills(language)) {
			if (availableSkill.getName().equals(skillName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<SkillDefinition> getElements(String language) throws InvalidXmlElementException {
		if (elements.get(language) == null) {
			elements.put(language, new ArrayList<SkillDefinition>());
			for (String skillId : translatorSkill.getAllTranslatedElements()) {
				SkillDefinition skill = createElement(translatorSkill, skillId, language);
				elements.get(language).add(skill);
				if (skill.isNatural()) {
					if (naturalSkills.get(language) == null) {
						naturalSkills.put(language, new ArrayList<SkillDefinition>());
					}
					naturalSkills.get(language).add(skill);
					Collections.sort(naturalSkills.get(language));
				} else {
					if (learnedSkills.get(language) == null) {
						learnedSkills.put(language, new ArrayList<SkillDefinition>());
					}
					learnedSkills.get(language).add(skill);
					Collections.sort(learnedSkills.get(language));
				}
			}
			Collections.sort(elements.get(language));
		}
		return elements.get(language);
	}

	@Override
	protected SkillDefinition createElement(ITranslator translator, String skillId, String language) throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(skillId, NAME, language);
			SkillDefinition skill = new SkillDefinition(skillId, name);
			try {
				Set<Specialization> specializations = new HashSet<>();
				for (String specializationId : translator.getAllChildrenTags(skillId, SPECIALIZABLE_SKILL_TAG)) {
					String specizalizationName = translator.getNodeValue(specializationId, language);
					specializations.add(new Specialization(specializationId, specizalizationName));
				}
				skill.setSpecializations(specializations);
			} catch (NumberFormatException nfe) {
				throw new InvalidSkillException("Invalid specialization value for skill '" + skillId + "'.");
			}
			try {
				String numberToShow = translator.getNodeValue(skillId, NUMBER_TO_SHOW_TAG);
				if (numberToShow != null) {
					skill.setNumberToShow(Integer.parseInt(numberToShow));
				}
			} catch (NumberFormatException nfe) {
				throw new InvalidSkillException("Invalid number value for skill '" + skillId + "'.");
			}
			String factionSkill = translator.getNodeValue(skillId, FACTION_SKILL_TAG);
			if (factionSkill != null) {
				StringTokenizer factionsOfSkill = new StringTokenizer(factionSkill, ",");
				while (factionsOfSkill.hasMoreTokens())
					skill.addFaction(FactionsFactory.getInstance().getElement(factionsOfSkill.nextToken(), language));
			}

			String group = translator.getNodeValue(skillId, GROUP_SKILL_TAG);
			skill.setSkillGroup(SkillGroup.getSkillGroup(group));

			String natural = translator.getNodeValue(skillId, NATURAL_SKILL_TAG);
			skill.setNatural(Boolean.parseBoolean(natural));

			return skill;
		} catch (Exception e) {
			throw new InvalidBlessingException("Invalid name in skill '" + skillId + "'.");
		}
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorSkill;
	}
}
