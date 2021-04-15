package com.softwaremagico.tm.random.predefined;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2021 Softwaremagico
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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.predefined.profile.InvalidRandomPredefinedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.RandomPreferenceUtils;

import java.util.*;

public abstract class RandomPredefinedFactory<Predefined extends Element<Predefined> & IRandomPredefined> extends XmlFactory<Predefined> {
    private static final String GROUP = "group";
    private static final String PREFERENCES = "preferences";
    private static final String CHARACTERISTICS_MINIMUM_VALUES = "characteristicsMinimumValues";
    private static final String REQUIRED_SKILLS = "requiredSkills";
    private static final String SUGGESTED_SKILLS = "suggestedSkills";
    private static final String REQUIRED_SKILL = "skill";
    private static final String REQUIRED_SKILLS_ID = "id";
    private static final String REQUIRED_SKILLS_SPECIALIZATION = "speciality";
    private static final String SUGGESTED_BENEFICES = "suggestedBenefices";
    private static final String MANDATORY_BENEFICES = "mandatoryBenefices";
    private static final String MANDATORY_BENEFICES_SPECIALIZATIONS = "mandatoryBeneficesSpecialization";
    private static final String PARENT = "parent";
    private static final String FACTION = "faction";
    private static final String RACE = "race";

    private final Map<String, Set<Predefined>> predefinedByGroup = new HashMap<>();

    protected void setParent(Predefined predefined, String language, String moduleName)
            throws InvalidXmlElementException {
        final String parentName = getTranslator(moduleName).getNodeValue(predefined.getId(), PARENT);
        if (parentName != null && !parentName.isEmpty()) {
            try {
                final Predefined parent = getElement(parentName, language, moduleName);
                predefined.setParent(parent);
            } catch (Exception e) {
                throw new InvalidRandomPredefinedException("Invalid parent in element '" + predefined + "'.");
            }
        }
    }

    @Override
    public Predefined getElement(String elementId, String language, String moduleName)
            throws InvalidXmlElementException {
        final Predefined randomProfile = super.getElement(elementId, language, moduleName);
        if (!randomProfile.isParentMerged()) {
            setParent(randomProfile, language, moduleName);
        }
        return randomProfile;
    }

    protected abstract Predefined createNew(String id, String name, String description, String language, String moduleName,
                                            Set<IRandomPreference> randomPreferences, Set<Characteristic> characteristicsMinimumValues,
                                            Set<AvailableSkill> requiredSkills, Set<AvailableSkill> suggestedSkills,
                                            Set<BeneficeDefinition> mandatoryBenefices, Set<BeneficeDefinition> suggestedBenefices,
                                            Set<AvailableBenefice> mandatoryBeneficeSpecializations, Faction faction, Race race);

    private void classify(Predefined predefined, String groupName) {
        predefinedByGroup.computeIfAbsent(groupName, k -> new HashSet<>());
        predefinedByGroup.get(groupName).add(predefined);
    }

    public Set<String> getGroups(String language, String moduleName) {
        if (predefinedByGroup.isEmpty()) {
            try {
                getElements(language, moduleName);
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return predefinedByGroup.keySet();
    }

    public Set<Predefined> getByGroup(String groupName) {
        return predefinedByGroup.get(groupName);
    }


    @Override
    protected Predefined createElement(ITranslator translator, String predefinedId, String name, String description,
                                       String language, String moduleName)
            throws InvalidXmlElementException {

        final Set<IRandomPreference> preferencesSelected = new HashSet<>();
        final String preferencesSelectedNames = translator.getNodeValue(predefinedId, PREFERENCES);
        if (preferencesSelectedNames != null) {
            final StringTokenizer preferencesSelectedTokenizer = new StringTokenizer(preferencesSelectedNames, ",");
            while (preferencesSelectedTokenizer.hasMoreTokens()) {
                try {
                    preferencesSelected.add(RandomPreferenceUtils.getSelectedPreference(preferencesSelectedTokenizer
                            .nextToken().trim()));
                } catch (StringIndexOutOfBoundsException e) {
                    throw new InvalidRandomPredefinedException("Invalid preferences '"
                            + preferencesSelectedNames + "' on predefined '" + predefinedId + "'.");
                }
            }
        }

        final Set<Characteristic> characteristicsMinimumValues = new HashSet<>();
        for (final CharacteristicName characteristicName : CharacteristicName.values()) {
            final String characteristicValue = translator.getNodeValue(predefinedId, CHARACTERISTICS_MINIMUM_VALUES,
                    characteristicName.name().toLowerCase());
            if (characteristicValue != null) {
                try {
                    final Characteristic characteristicOption = new Characteristic(CharacteristicsDefinitionFactory
                            .getInstance().get(characteristicName, language, moduleName));
                    characteristicOption.setValue(Integer.parseInt(characteristicValue));
                    characteristicsMinimumValues.add(characteristicOption);
                } catch (NumberFormatException e) {
                    throw new InvalidRandomPredefinedException("Invalid min value in characteristic '"
                            + characteristicName.name().toLowerCase() + "' of predefined '" + predefinedId + "'.");
                }
            }
        }

        final Set<AvailableSkill> requiredSkills = new HashSet<>();
        int node = 0;
        while (true) {
            try {
                String requiredSkillId;
                try {
                    requiredSkillId = translator.getNodeValue(predefinedId, REQUIRED_SKILLS, REQUIRED_SKILL,
                            node, REQUIRED_SKILLS_ID);
                    if (requiredSkillId == null) {
                        break;
                    }
                } catch (NullPointerException e) {
                    // Not more.
                    break;
                }
                String skillSpeciality = null;
                try {
                    skillSpeciality = translator.getNodeValue(predefinedId, REQUIRED_SKILLS, REQUIRED_SKILL,
                            node, REQUIRED_SKILLS_SPECIALIZATION);
                } catch (NullPointerException e) {
                    // Not mandatory
                }
                try {
                    if (skillSpeciality == null) {
                        requiredSkills.add(AvailableSkillsFactory.getInstance().getElement(requiredSkillId, language,
                                moduleName));
                    } else {
                        requiredSkills.add(AvailableSkillsFactory.getInstance().getElement(requiredSkillId,
                                skillSpeciality, language, moduleName));
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidRandomPredefinedException("Invalid required skill '" + requiredSkillId + "' for  predefined '"
                            + predefinedId + "'.", e);
                }
                node++;
            } catch (NumberFormatException e) {
                break;
            }
        }

        final Set<AvailableSkill> suggestedSkills = new HashSet<>();
        node = 0;
        while (true) {
            try {
                String suggestedSkillId;
                try {
                    suggestedSkillId = translator.getNodeValue(predefinedId, SUGGESTED_SKILLS, REQUIRED_SKILL,
                            node, REQUIRED_SKILLS_ID);
                    if (suggestedSkillId == null) {
                        break;
                    }
                } catch (NullPointerException e) {
                    // Not more.
                    break;
                }
                String skillSpeciality = null;
                try {
                    skillSpeciality = translator.getNodeValue(predefinedId, SUGGESTED_SKILLS, REQUIRED_SKILL,
                            node, REQUIRED_SKILLS_SPECIALIZATION);
                } catch (NullPointerException e) {
                    // Not mandatory
                }
                try {
                    if (skillSpeciality == null) {
                        suggestedSkills.add(AvailableSkillsFactory.getInstance().getElement(suggestedSkillId, language,
                                moduleName));
                    } else {
                        suggestedSkills.add(AvailableSkillsFactory.getInstance().getElement(suggestedSkillId,
                                skillSpeciality, language, moduleName));
                    }
                } catch (InvalidXmlElementException e) {
                    throw new InvalidRandomPredefinedException("Invalid suggested skill '" + suggestedSkillId
                            + "' for  predefined '" + predefinedId + "'.", e);
                }
                node++;
            } catch (NumberFormatException e) {
                break;
            }
        }

        final Set<BeneficeDefinition> mandatoryBenefices = getCommaSeparatedValues(predefinedId, MANDATORY_BENEFICES,
                language, moduleName, BeneficeDefinitionFactory.getInstance());

        final Set<AvailableBenefice> mandatoryBeneficeSpecializations = getCommaSeparatedValues(predefinedId, MANDATORY_BENEFICES_SPECIALIZATIONS,
                language, moduleName, AvailableBeneficeFactory.getInstance());

        final Set<BeneficeDefinition> suggestedBenefices = getCommaSeparatedValues(predefinedId, SUGGESTED_BENEFICES,
                language, moduleName, BeneficeDefinitionFactory.getInstance());


        final String group = translator.getNodeValue(predefinedId, GROUP);
        if (group == null) {
            throw new InvalidRandomPredefinedException("Invalid group for '" + predefinedId + "'.");
        }

        final Faction faction = getElement(predefinedId, FACTION, language, moduleName, FactionsFactory.getInstance());

        final Race race = getElement(predefinedId, RACE, language, moduleName, RaceFactory.getInstance());

        final Predefined predefined = createNew(predefinedId, name, description, language, moduleName, preferencesSelected,
                characteristicsMinimumValues, requiredSkills, suggestedSkills, mandatoryBenefices, suggestedBenefices, mandatoryBeneficeSpecializations,
                faction, race);

        classify(predefined, group);

        return predefined;
    }


}
