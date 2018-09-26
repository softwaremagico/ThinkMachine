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

import com.softwaremagico.tm.InvalidXmlElementException;

public class AvailableSkillsFactory {
	protected Map<String, List<AvailableSkill>> elements = new HashMap<>();
	private static Map<String, List<AvailableSkill>> naturalSkills = new HashMap<>();
	private static Map<String, List<AvailableSkill>> learnedSkills = new HashMap<>();

	private static Map<String, Map<SkillGroup, Set<AvailableSkill>>> skillsByGroup = new HashMap<>();

	private static AvailableSkillsFactory instance;

	private int maximumNumberOfSpecializations = 0;

	private static void createInstance() {
		if (instance == null) {
			synchronized (AvailableSkillsFactory.class) {
				if (instance == null) {
					instance = new AvailableSkillsFactory();
				}
			}
		}
	}

	public static AvailableSkillsFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	public void clearCache() {
		elements = new HashMap<>();
		naturalSkills = new HashMap<>();
		learnedSkills = new HashMap<>();
		skillsByGroup = new HashMap<>();
	}

	public Set<AvailableSkill> getSkillsByGroup(SkillGroup skillGroup, String language) throws InvalidXmlElementException {
		if (skillsByGroup == null || skillsByGroup.isEmpty() || skillsByGroup.get(language) == null || skillsByGroup.get(language).isEmpty()) {
			for (AvailableSkill availableNaturalSkill : getNaturalSkills(language)) {
				classifySkillByGroup(availableNaturalSkill, language);
			}
			for (AvailableSkill availableLearnedSkill : getLearnedSkills(language)) {
				classifySkillByGroup(availableLearnedSkill, language);
			}
		}
		return skillsByGroup.get(language).get(skillGroup);
	}

	public List<AvailableSkill> getNaturalSkills(String language) throws InvalidXmlElementException {
		if (naturalSkills.get(language) == null) {
			naturalSkills.put(language, new ArrayList<AvailableSkill>());
		}
		if (naturalSkills.get(language).isEmpty()) {
			if (elements.get(language) == null) {
				elements.put(language, new ArrayList<AvailableSkill>());
			}
			for (SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getNaturalSkills(language)) {
				Set<AvailableSkill> skills = createElement(skillDefinition);

				naturalSkills.get(language).addAll(skills);
				elements.get(language).addAll(skills);
			}
			Collections.sort(naturalSkills.get(language));
			Collections.sort(elements.get(language));
		}
		return naturalSkills.get(language);
	}

	public List<AvailableSkill> getLearnedSkills(String language) throws InvalidXmlElementException {
		if (learnedSkills.get(language) == null) {
			learnedSkills.put(language, new ArrayList<AvailableSkill>());
		}
		if (learnedSkills.get(language).isEmpty()) {
			if (elements.get(language) == null) {
				elements.put(language, new ArrayList<AvailableSkill>());
			}
			for (SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getLearnedSkills(language)) {
				Set<AvailableSkill> skills = createElement(skillDefinition);
				learnedSkills.get(language).addAll(skills);
				elements.get(language).addAll(skills);
			}
			Collections.sort(learnedSkills.get(language));
			Collections.sort(elements.get(language));
		}
		return learnedSkills.get(language);
	}

	private Set<AvailableSkill> createElement(SkillDefinition skillDefinition) {
		Set<AvailableSkill> availableSkills = new HashSet<>();
		if (skillDefinition.isSpecializable()) {
			for (Specialization specialization : skillDefinition.getSpecializations()) {
				availableSkills.add(new AvailableSkill(skillDefinition, specialization));
			}
			if (maximumNumberOfSpecializations < skillDefinition.getSpecializations().size()) {
				maximumNumberOfSpecializations = skillDefinition.getSpecializations().size();
			}
		} else {
			availableSkills.add(new AvailableSkill(skillDefinition));
		}
		return availableSkills;
	}

	public AvailableSkill getElement(String elementId, String language) throws InvalidXmlElementException {
		return getElement(elementId, null, language);
	}

	public AvailableSkill getElement(String elementId, String specializationId, String language) throws InvalidXmlElementException {
		for (AvailableSkill element : getElements(language)) {
			if (element.getId() != null) {
				if (Objects.equals(element.getId().toLowerCase(), elementId.toLowerCase())) {
					if (element.getSpecialization() == null) {
						return element;
					} else {
						if (Objects.equals(element.getSpecialization().getId().toLowerCase(), specializationId.toLowerCase())) {
							return element;
						}
					}
				}
			}
		}
		if (specializationId == null) {
			throw new InvalidXmlElementException("Element '" + elementId + "' does not exists.");
		} else {
			throw new InvalidXmlElementException("Element '" + elementId + " [" + specializationId + "]' does not exists.");
		}
	}

	private List<AvailableSkill> getElements(String language) throws InvalidXmlElementException {
		if (elements.get(language) == null) {
			elements.put(language, new ArrayList<AvailableSkill>());
			// Initialize values
			getNaturalSkills(language);
			getLearnedSkills(language);
		}
		return elements.get(language);
	}

	public List<AvailableSkill> getAvailableSkills(SkillDefinition skillDefinition, String language) throws InvalidXmlElementException {
		List<AvailableSkill> availableSkills = new ArrayList<>();
		for (AvailableSkill availableSkill : getNaturalSkills(language)) {
			if (Objects.equals(availableSkill.getSkillDefinition(), skillDefinition)) {
				availableSkills.add(availableSkill);
			}
		}
		for (AvailableSkill availableSkill : getLearnedSkills(language)) {
			if (Objects.equals(availableSkill.getSkillDefinition(), skillDefinition)) {
				availableSkills.add(availableSkill);
			}
		}
		return availableSkills;
	}

	private void classifySkillByGroup(AvailableSkill availableSkill, String language) {
		if (skillsByGroup.get(language) == null) {
			skillsByGroup.put(language, new HashMap<SkillGroup, Set<AvailableSkill>>());
		}
		if (skillsByGroup.get(language).get(availableSkill.getSkillDefinition().getSkillGroup()) == null) {
			skillsByGroup.get(language).put(availableSkill.getSkillDefinition().getSkillGroup(), new HashSet<AvailableSkill>());
		}
		skillsByGroup.get(language).get(availableSkill.getSkillDefinition().getSkillGroup()).add(availableSkill);
	}

	public int getMaximumNumberOfSpecializations() {
		return maximumNumberOfSpecializations;
	}

}
