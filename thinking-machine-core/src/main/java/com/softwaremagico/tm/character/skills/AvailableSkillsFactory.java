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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;

public class AvailableSkillsFactory {
	protected Map<String, List<AvailableSkill>> elements = new HashMap<>();
	private static List<AvailableSkill> naturalSkills = new ArrayList<>();
	private static List<AvailableSkill> learnedSkills = new ArrayList<>();

	private static AvailableSkillsFactory instance;

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
		naturalSkills = new ArrayList<>();
		learnedSkills = new ArrayList<>();
	}

	public List<AvailableSkill> getNaturalSkills(String language) throws InvalidXmlElementException {
		if (naturalSkills.isEmpty()) {
			if (elements.get(language) == null) {
				elements.put(language, new ArrayList<AvailableSkill>());
			}
			for (SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getNaturalSkills(language)) {
				Set<AvailableSkill> skills = createElement(skillDefinition);
				naturalSkills.addAll(skills);
				elements.get(language).addAll(skills);
			}
			Collections.sort(naturalSkills);
			Collections.sort(elements.get(language));
		}
		return naturalSkills;
	}

	public List<AvailableSkill> getLearnedSkills(String language) throws InvalidXmlElementException {
		if (learnedSkills.isEmpty()) {
			if (elements.get(language) == null) {
				elements.put(language, new ArrayList<AvailableSkill>());
			}
			for (SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getLearnedSkills(language)) {
				Set<AvailableSkill> skills = createElement(skillDefinition);
				learnedSkills.addAll(skills);
				elements.get(language).addAll(skills);
			}
			Collections.sort(learnedSkills);
			Collections.sort(elements.get(language));
		}
		return learnedSkills;
	}

	private Set<AvailableSkill> createElement(SkillDefinition skillDefinition) {
		Set<AvailableSkill> availableSkills = new HashSet<>();
		if (skillDefinition.isSpecializable()) {
			for (String specialization : skillDefinition.getSpecializations()) {
				availableSkills.add(new AvailableSkill(skillDefinition, specialization.trim()));
			}
		} else {
			availableSkills.add(new AvailableSkill(skillDefinition));
		}
		return availableSkills;
	}

	public AvailableSkill getElement(String elementName, String language) throws InvalidXmlElementException {
		return getElement(elementName, null, language);
	}

	public AvailableSkill getElement(String elementName, String specialization, String language) throws InvalidXmlElementException {
		for (AvailableSkill element : getElements(language)) {
			if (element.getName() != null) {
				if (Objects.equals(element.getCompleteName().toLowerCase(), AvailableSkill.getCompleteName(elementName, specialization).toLowerCase())) {
					return element;
				}
			}
		}
		if (specialization == null) {
			throw new InvalidXmlElementException("Element '" + elementName + "' does not exists.");
		} else {
			throw new InvalidXmlElementException("Element '" + elementName + " [" + specialization + "]' does not exists.");
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

}
