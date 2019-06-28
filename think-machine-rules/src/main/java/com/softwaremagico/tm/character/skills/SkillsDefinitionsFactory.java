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
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;

public class SkillsDefinitionsFactory extends XmlFactory<SkillDefinition> {
	private static final ITranslator translatorSkill = LanguagePool.getTranslator("skills.xml");

	private static final String NAME = "name";
	private static final String FACTION_SKILL_TAG = "factionSkill";
	private static final String SPECIALIZABLE_SKILL_TAG = "specializations";
	private static final String GROUP_SKILL_TAG = "group";
	private static final String NATURAL_SKILL_TAG = "natural";
	private static final String NUMBER_TO_SHOW_TAG = "numberToShow";

	private static Map<String, List<SkillDefinition>> naturalSkills = new HashMap<>();
	private static Map<String, List<SkillDefinition>> learnedSkills = new HashMap<>();
	private static Map<String, Map<SkillGroup, Set<SkillDefinition>>> skillsByGroup = new HashMap<>();

	private static class SkillsDefinitionsFactoryInit {
		public static final SkillsDefinitionsFactory INSTANCE = new SkillsDefinitionsFactory();
	}

	public static SkillsDefinitionsFactory getInstance() {
		return SkillsDefinitionsFactoryInit.INSTANCE;
	}

	@Override
	public void clearCache() {
		naturalSkills.clear();
		learnedSkills.clear();
		skillsByGroup.clear();
		super.clearCache();
	}

	public List<SkillDefinition> getNaturalSkills(String language) {
		if (naturalSkills.get(language) == null) {
			try {
				getElements(language);
			} catch (InvalidXmlElementException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
		return naturalSkills.get(language);
	}

	public List<SkillDefinition> getLearnedSkills(String language) {
		if (learnedSkills.get(language) == null) {
			try {
				getElements(language);
			} catch (InvalidXmlElementException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
		return learnedSkills.get(language);
	}

	public Set<SkillDefinition> getSkills(SkillGroup skillGroup, String language) {
		if (skillsByGroup.get(language) == null) {
			try {
				getElements(language);
			} catch (InvalidXmlElementException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
		return skillsByGroup.get(language).get(skillGroup);
	}

	public boolean isNaturalSkill(String skillName, String language) {
		for (final SkillDefinition availableSkill : getNaturalSkills(language)) {
			if (Objects.equals(availableSkill.getName(), skillName)) {
				return true;
			}
		}
		return false;
	}

	public SkillDefinition get(String skillName, String language) {
		try {
			for (final SkillDefinition availableSkill : getElements(language)) {
				if (Objects.equals(availableSkill.getId(), skillName)) {
					return availableSkill;
				}
			}
		} catch (InvalidXmlElementException ixee) {
			MachineLog.errorMessage(this.getClass().getName(), ixee);
		}
		return null;
	}

	@Override
	public List<SkillDefinition> getElements(String language) throws InvalidXmlElementException {
		if (elements.get(language) == null) {
			elements.put(language, new ArrayList<SkillDefinition>());
			for (final String skillId : translatorSkill.getAllTranslatedElements()) {
				final SkillDefinition skill = createElement(translatorSkill, skillId, language);
				elements.get(language).add(skill);
				setRandomConfiguration(skill, getTranslator(), language);
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
	protected SkillDefinition createElement(ITranslator translator, String skillId, String language)
			throws InvalidXmlElementException {
		try {
			final String name = translator.getNodeValue(skillId, NAME, language);
			final SkillDefinition skill = new SkillDefinition(skillId, name, language);
			try {
				final Set<Specialization> specializations = new HashSet<>();
				for (final String specializationId : translator.getAllChildrenTags(skillId, SPECIALIZABLE_SKILL_TAG)) {
					final String specizalizationName = translator.getNodeValue(specializationId, language);
					final Specialization specialization = new Specialization(specializationId, specizalizationName,
							language);
					setRandomConfiguration(specialization, translator, language);
					specializations.add(specialization);
				}
				skill.setSpecializations(specializations);
			} catch (NumberFormatException nfe) {
				throw new InvalidSkillException("Invalid specialization value for skill '" + skillId + "'.");
			}
			try {
				final String numberToShow = translator.getNodeValue(skillId, NUMBER_TO_SHOW_TAG);
				if (numberToShow != null) {
					skill.setNumberToShow(Integer.parseInt(numberToShow));
				}
			} catch (NumberFormatException nfe) {
				throw new InvalidSkillException("Invalid number value for skill '" + skillId + "'.");
			}
			final String factionSkill = translator.getNodeValue(skillId, FACTION_SKILL_TAG);
			if (factionSkill != null) {
				final StringTokenizer factionsOfSkill = new StringTokenizer(factionSkill, ",");
				while (factionsOfSkill.hasMoreTokens()) {
					try {
						skill.addFaction(FactionsFactory.getInstance().getElement(factionsOfSkill.nextToken().trim(),
								language));
					} catch (InvalidXmlElementException ixe) {
						throw new InvalidSkillException("Error in skill '" + skillId
								+ "' structure. Invalid faction defintion. ", ixe);
					}
				}
			}

			final String group = translator.getNodeValue(skillId, GROUP_SKILL_TAG);
			skill.setSkillGroup(SkillGroup.getSkillGroup(group));

			final String natural = translator.getNodeValue(skillId, NATURAL_SKILL_TAG);
			skill.setNatural(Boolean.parseBoolean(natural));

			classifySkillByGroup(skill, language);

			return skill;
		} catch (Exception e) {
			throw new InvalidSkillException("Invalid structure in skill '" + skillId + "'.", e);
		}
	}

	private void classifySkillByGroup(SkillDefinition skillDefintion, String language) {
		if (skillsByGroup.get(language) == null) {
			skillsByGroup.put(language, new HashMap<SkillGroup, Set<SkillDefinition>>());
		}
		if (skillsByGroup.get(language).get(skillDefintion.getSkillGroup()) == null) {
			skillsByGroup.get(language).put(skillDefintion.getSkillGroup(), new HashSet<SkillDefinition>());
		}
		skillsByGroup.get(language).get(skillDefintion.getSkillGroup()).add(skillDefintion);
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorSkill;
	}
}
