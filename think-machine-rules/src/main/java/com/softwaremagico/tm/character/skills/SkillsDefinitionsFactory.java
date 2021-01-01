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
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import com.softwaremagico.tm.log.SuppressFBWarnings;

import java.util.*;

public class SkillsDefinitionsFactory extends XmlFactory<SkillDefinition> {
    private static final String TRANSLATOR_FILE = "skills.xml";

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    private static final String FACTION_SKILL_TAG = "factionSkill";
    private static final String SPECIALIZABLE_SKILL_TAG = "specializations";
    private static final String GROUP_SKILL_TAG = "group";
    private static final String NATURAL_SKILL_TAG = "natural";
    private static final String NUMBER_TO_SHOW_TAG = "numberToShow";

    private static Map<String, Map<String, List<SkillDefinition>>> naturalSkills = new HashMap<>();
    private static Map<String, Map<String, List<SkillDefinition>>> learnedSkills = new HashMap<>();
    private static Map<String, Map<String, Map<SkillGroup, Set<SkillDefinition>>>> skillsByGroup = new HashMap<>();

    private static class SkillsDefinitionsFactoryInit {
        public static final SkillsDefinitionsFactory INSTANCE = new SkillsDefinitionsFactory();
    }

    public static SkillsDefinitionsFactory getInstance() {
        return SkillsDefinitionsFactoryInit.INSTANCE;
    }

    private SkillsDefinitionsFactory() {
        super();
    }

    private static void initializeMaps() {
        naturalSkills = new HashMap<>();
        learnedSkills = new HashMap<>();
        skillsByGroup = new HashMap<>();
    }

    @Override
    public void clearCache() {
        initializeMaps();
        super.clearCache();
    }

    public List<SkillDefinition> getNaturalSkills(String language, String moduleName) {
        if (naturalSkills.get(language) == null || naturalSkills.get(language).get(moduleName) == null) {
            try {
                getElements(language, moduleName);
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return naturalSkills.get(language).get(moduleName);
    }

    public List<SkillDefinition> getLearnedSkills(String language, String moduleName) {
        if (learnedSkills.get(language) == null || learnedSkills.get(language).get(moduleName) == null) {
            try {
                getElements(language, moduleName);
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return learnedSkills.get(language).get(moduleName);
    }

    public Set<SkillDefinition> getSkills(SkillGroup skillGroup, String language, String moduleName) {
        return skillsByGroup.get(language).get(moduleName).get(skillGroup);
    }

    public boolean isNaturalSkill(String skillName, String language, String moduleName) {
        for (final SkillDefinition availableSkill : getNaturalSkills(language, moduleName)) {
            if (Objects.equals(availableSkill.getName(), skillName)) {
                return true;
            }
        }
        return false;
    }

    public SkillDefinition get(String skillName, String language, String moduleName) {
        try {
            for (final SkillDefinition availableSkill : getElements(language, moduleName)) {
                if (Objects.equals(availableSkill.getId(), skillName)) {
                    return availableSkill;
                }
            }
        } catch (InvalidXmlElementException ixee) {
            MachineXmlReaderLog.errorMessage(this.getClass().getName(), ixee);
        }
        return null;
    }

    @Override
    public List<SkillDefinition> getElements(String language, String moduleName) throws InvalidXmlElementException {
        if (elements.get(language) == null || elements.get(language).get(moduleName) == null) {
            elements.computeIfAbsent(language, k -> new HashMap<>());
            elements.get(language).computeIfAbsent(moduleName, k -> new ArrayList<>());
            for (final String skillId : getTranslator(moduleName).getAllTranslatedElements()) {
                String name = null;
                try {
                    name = getTranslator(moduleName).getNodeValue(skillId, NAME, language);
                } catch (Exception e) {
                    //Name is optional?
                }

                String description = null;
                try {
                    description = getTranslator(moduleName).getNodeValue(skillId, DESCRIPTION, language);
                } catch (Exception e) {
                    //Description is not mandatory.
                }
                final SkillDefinition skill = createElement(getTranslator(moduleName), skillId, name, description, language, moduleName);
                elements.get(language).get(moduleName).add(skill);
                setRandomConfiguration(skill, getTranslator(moduleName), language, moduleName);
                if (skill.isNatural()) {
                    naturalSkills.computeIfAbsent(language, k -> new HashMap<>());
                    naturalSkills.get(language).computeIfAbsent(moduleName, k -> new ArrayList<>());
                    naturalSkills.get(language).get(moduleName).add(skill);
                    Collections.sort(naturalSkills.get(language).get(moduleName));
                } else {
                    learnedSkills.computeIfAbsent(language, k -> new HashMap<>());
                    learnedSkills.get(language).computeIfAbsent(moduleName, k -> new ArrayList<>());
                    learnedSkills.get(language).get(moduleName).add(skill);
                    Collections.sort(learnedSkills.get(language).get(moduleName));
                }
            }
            Collections.sort(elements.get(language).get(moduleName));
        }
        return elements.get(language).get(moduleName);
    }

    @Override
    @SuppressFBWarnings("REC_CATCH_EXCEPTION")
    protected SkillDefinition createElement(ITranslator translator, String skillId, String name, String description,
                                            String language, String moduleName)
            throws InvalidXmlElementException {
        try {
            final SkillDefinition skill = new SkillDefinition(skillId, name, description, language, moduleName);
            try {
                final Set<Specialization> specializations = new HashSet<>();
                for (final String specializationId : translator.getAllChildrenTags(skillId, SPECIALIZABLE_SKILL_TAG)) {
                    final String specializationName = translator.getNodeValue(specializationId, language);
                    final Specialization specialization = new Specialization(specializationId, specializationName, description,
                            language, moduleName);
                    setRandomConfiguration(specialization, translator, language, moduleName);
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
                                language, moduleName));
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

            classifySkillByGroup(skill, language, moduleName);

            return skill;
        } catch (Exception e) {
            throw new InvalidSkillException("Invalid structure in skill '" + skillId + "'.", e);
        }
    }

    private synchronized void classifySkillByGroup(SkillDefinition skillDefintion, String language, String moduleName) {
        skillsByGroup.computeIfAbsent(language, k -> new HashMap<>());
        skillsByGroup.get(language).computeIfAbsent(moduleName, k -> new HashMap<>());
        skillsByGroup.get(language).get(moduleName)
                .computeIfAbsent(skillDefintion.getSkillGroup(), k -> new HashSet<>());
        skillsByGroup.get(language).get(moduleName).get(skillDefintion.getSkillGroup()).add(skillDefintion);
    }

    @Override
    protected String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }
}
