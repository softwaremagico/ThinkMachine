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

import com.softwaremagico.tm.InvalidXmlElementException;

import java.util.*;

public class AvailableSkillsFactory {
    protected Map<String, Map<String, List<AvailableSkill>>> elements = new HashMap<>();
    private Map<String, Map<String, List<AvailableSkill>>> naturalSkills = new HashMap<>();
    private Map<String, Map<String, List<AvailableSkill>>> learnedSkills = new HashMap<>();
    private Map<String, Map<SkillGroup, Set<AvailableSkill>>> skillsByGroup = new HashMap<>();

    private int maximumNumberOfSpecializations = 0;

    private static class AvailableSkillsFactoryInit {
        public static final AvailableSkillsFactory INSTANCE = new AvailableSkillsFactory();
    }

    public static AvailableSkillsFactory getInstance() {
        return AvailableSkillsFactoryInit.INSTANCE;
    }

    public void clearCache() {
        elements = new HashMap<>();
        naturalSkills = new HashMap<>();
        learnedSkills = new HashMap<>();
        skillsByGroup = new HashMap<>();
    }

    public Set<AvailableSkill> getSkillsByGroup(SkillGroup skillGroup, String language, String moduleName) {
        if (skillsByGroup == null || skillsByGroup.isEmpty() || skillsByGroup.get(language) == null
                || skillsByGroup.get(language).isEmpty()) {
            for (final AvailableSkill availableNaturalSkill : getNaturalSkills(language, moduleName)) {
                classifySkillByGroup(availableNaturalSkill, language);
            }
            for (final AvailableSkill availableLearnedSkill : getLearnedSkills(language, moduleName)) {
                classifySkillByGroup(availableLearnedSkill, language);
            }
        }
        if (skillsByGroup == null || skillsByGroup.get(language) == null
                || skillsByGroup.get(language).get(skillGroup) == null) {
            return new HashSet<>();
        }
        return skillsByGroup.get(language).get(skillGroup);
    }

    public List<AvailableSkill> getNaturalSkills(String language, String moduleName) {
        if (naturalSkills.get(language) == null || naturalSkills.get(language).get(moduleName) == null) {
            naturalSkills.computeIfAbsent(language, k -> new HashMap<>());
            naturalSkills.get(language).computeIfAbsent(moduleName, k -> new ArrayList<>());
            elements.computeIfAbsent(language, k -> new HashMap<>());
            elements.get(language).computeIfAbsent(moduleName, k -> new ArrayList<>());
            for (final SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getNaturalSkills(
                    language, moduleName)) {
                final Set<AvailableSkill> skills = createElement(skillDefinition);

                naturalSkills.get(language).get(moduleName).addAll(skills);
                elements.get(language).get(moduleName).addAll(skills);
            }
            Collections.sort(naturalSkills.get(language).get(moduleName));
            Collections.sort(elements.get(language).get(moduleName));
        }
        return naturalSkills.get(language).get(moduleName);
    }

    public List<AvailableSkill> getLearnedSkills(String language, String moduleName) {
        if (learnedSkills.get(language) == null || learnedSkills.get(language).get(moduleName) == null) {
            learnedSkills.computeIfAbsent(language, k -> new HashMap<>());
            learnedSkills.get(language).computeIfAbsent(moduleName, k -> new ArrayList<>());
            elements.computeIfAbsent(language, k -> new HashMap<>());
            elements.get(language).computeIfAbsent(moduleName, k -> new ArrayList<>());
            for (final SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getLearnedSkills(
                    language, moduleName)) {
                final Set<AvailableSkill> skills = createElement(skillDefinition);
                learnedSkills.get(language).get(moduleName).addAll(skills);
                elements.get(language).get(moduleName).addAll(skills);
            }
            Collections.sort(learnedSkills.get(language).get(moduleName));
            Collections.sort(elements.get(language).get(moduleName));
        }
        return learnedSkills.get(language).get(moduleName);
    }

    private Set<AvailableSkill> createElement(SkillDefinition skillDefinition) {
        final Set<AvailableSkill> availableSkills = new HashSet<>();
        if (skillDefinition.isSpecializable()) {
            for (final Specialization specialization : skillDefinition.getSpecializations()) {
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

    public AvailableSkill getElement(String elementId, String language, String moduleName)
            throws InvalidXmlElementException {
        return getElement(elementId, null, language, moduleName);
    }

    public AvailableSkill getElement(String elementId, String specializationId, String language, String moduleName)
            throws InvalidXmlElementException {
        for (final AvailableSkill element : getElements(language, moduleName)) {
            if (element.getId() != null) {
                if (Objects.equals(element.getId().toLowerCase(), elementId.toLowerCase())) {
                    if (element.getSpecialization() == null) {
                        return element;
                    } else {
                        if (specializationId != null
                                && Objects.equals(element.getSpecialization().getId().toLowerCase(),
                                specializationId.toLowerCase())) {
                            return element;
                        }
                    }
                }
            }
        }
        if (specializationId == null) {
            throw new InvalidXmlElementException("Element '" + elementId + "' does not exists.");
        } else {
            throw new InvalidXmlElementException("Element '" + elementId + " [" + specializationId
                    + "]' does not exists.");
        }
    }

    private List<AvailableSkill> getElements(String language, String moduleName) {
        if (elements.get(language) == null || elements.get(language).get(moduleName) == null) {
            elements.computeIfAbsent(language, k -> new HashMap<>());
            elements.get(language).computeIfAbsent(moduleName, k -> new ArrayList<>());
            // Initialize values
            getNaturalSkills(language, moduleName);
            getLearnedSkills(language, moduleName);
        }
        return elements.get(language).get(moduleName);
    }

    public List<AvailableSkill> getAvailableSkills(SkillDefinition skillDefinition, String language, String moduleName) {
        final List<AvailableSkill> availableSkills = new ArrayList<>();
        for (final AvailableSkill availableSkill : getNaturalSkills(language, moduleName)) {
            if (Objects.equals(availableSkill.getSkillDefinition(), skillDefinition)) {
                availableSkills.add(availableSkill);
            }
        }
        for (final AvailableSkill availableSkill : getLearnedSkills(language, moduleName)) {
            if (Objects.equals(availableSkill.getSkillDefinition(), skillDefinition)) {
                availableSkills.add(availableSkill);
            }
        }
        return availableSkills;
    }

    private void classifySkillByGroup(AvailableSkill availableSkill, String language) {
        skillsByGroup.computeIfAbsent(language, k -> new HashMap<>());
        skillsByGroup.get(language).computeIfAbsent(availableSkill.getSkillDefinition().getSkillGroup(),
                k -> new HashSet<>());
        skillsByGroup.get(language).get(availableSkill.getSkillDefinition().getSkillGroup()).add(availableSkill);
    }

    public int getMaximumNumberOfSpecializations() {
        return maximumNumberOfSpecializations;
    }

}
