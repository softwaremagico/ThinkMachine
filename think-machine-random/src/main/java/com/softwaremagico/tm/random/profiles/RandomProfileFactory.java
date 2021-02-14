package com.softwaremagico.tm.random.profiles;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.RandomPreferenceUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class RandomProfileFactory extends XmlFactory<RandomProfile> {
    private static final String TRANSLATOR_FILE = "profiles.xml";

    private static final String PREFERENCES = "preferences";
    private static final String CHARACTERISTICS_MINIMUM_VALUES = "characteristicsMinimumValues";
    private static final String REQUIRED_SKILLS = "requiredSkills";
    private static final String SUGGESTED_SKILLS = "suggestedSkills";
    private static final String REQUIRED_SKILL = "skill";
    private static final String REQUIRED_SKILLS_ID = "id";
    private static final String REQUIRED_SKILLS_SPECIALIZATION = "speciality";
    private static final String SUGGESTED_BENEFICES = "suggestedBenefices";
    private static final String MANDATORY_BENEFICES = "mandatoryBenefices";
    private static final String MANDATORY_WEAPONS = "weapons";
    private static final String MANDATORY_ARMOURS = "armours";
    private static final String MANDATORY_SHIELDS = "shields";
    private static final String PARENT = "parent";

    private static class RandomProfileFactoryInit {
        public static final RandomProfileFactory INSTANCE = new RandomProfileFactory();
    }

    public static RandomProfileFactory getInstance() {
        return RandomProfileFactoryInit.INSTANCE;
    }

    public static <T extends Enum<T>> T getEnum(final String value, final Class<T> enumClass) {
        return Enum.valueOf(enumClass, value);
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    public FactoryCacheLoader<RandomProfile> getFactoryCacheLoader() {
        return null;
    }

    @Override
    public RandomProfile getElement(String elementId, String language, String moduleName)
            throws InvalidXmlElementException {
        final RandomProfile randomProfile = super.getElement(elementId, language, moduleName);
        if (!randomProfile.isParentMerged()) {
            setParent(randomProfile, language, moduleName);
        }
        return randomProfile;
    }

    protected void setParent(RandomProfile profile, String language, String moduleName)
            throws InvalidXmlElementException {
        final String parentName = getTranslator(moduleName).getNodeValue(profile.getId(), PARENT);
        if (parentName != null && !parentName.isEmpty()) {
            try {
                final RandomProfile parent = RandomProfileFactory.getInstance().getElement(parentName, language,
                        moduleName);
                profile.setParent(parent);
            } catch (Exception e) {
                throw new InvalidProfileException("Invalid parent in profile '" + profile + "'.");
            }
        }
    }

    @Override
    protected RandomProfile createElement(ITranslator translator, String profileId, String name, String description,
                                          String language, String moduleName)
            throws InvalidXmlElementException {

        final Set<IRandomPreference> preferencesSelected = new HashSet<>();
        final String preferencesSelectedNames = translator.getNodeValue(profileId, PREFERENCES);
        if (preferencesSelectedNames != null) {
            final StringTokenizer preferencesSelectedTokenizer = new StringTokenizer(preferencesSelectedNames, ",");
            while (preferencesSelectedTokenizer.hasMoreTokens()) {
                preferencesSelected.add(RandomPreferenceUtils.getSelectedPreference(preferencesSelectedTokenizer
                        .nextToken().trim()));
            }
        }

        final Set<Characteristic> characteristicsMinimumValues = new HashSet<>();
        for (final CharacteristicName characteristicName : CharacteristicName.values()) {
            final String characteristicValue = translator.getNodeValue(profileId, CHARACTERISTICS_MINIMUM_VALUES,
                    characteristicName.name().toLowerCase());
            if (characteristicValue != null) {
                try {
                    final Characteristic characteristicOption = new Characteristic(CharacteristicsDefinitionFactory
                            .getInstance().get(characteristicName, language, moduleName));
                    characteristicOption.setValue(Integer.parseInt(characteristicValue));
                    characteristicsMinimumValues.add(characteristicOption);
                } catch (NumberFormatException e) {
                    throw new InvalidProfileException("Invalid min value in characteristic '"
                            + characteristicName.name().toLowerCase() + "' of profile '" + profileId + "'.");
                }
            }
        }

        final Set<AvailableSkill> requiredSkills = new HashSet<>();
        int node = 0;
        while (true) {
            try {
                String requiredSkillId;
                try {
                    requiredSkillId = translator.getNodeValue(profileId, REQUIRED_SKILLS, REQUIRED_SKILL,
                            REQUIRED_SKILLS_ID, node);
                    if (requiredSkillId == null) {
                        break;
                    }
                } catch (NullPointerException e) {
                    // Not more.
                    break;
                }
                String skillSpeciality = null;
                try {
                    skillSpeciality = translator.getNodeValue(profileId, REQUIRED_SKILLS, REQUIRED_SKILL,
                            REQUIRED_SKILLS_SPECIALIZATION, node);
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
                    throw new InvalidProfileException("Invalid required skill '" + requiredSkillId + "' for  profile '"
                            + profileId + "'.", e);
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
                    suggestedSkillId = translator.getNodeValue(profileId, SUGGESTED_SKILLS, REQUIRED_SKILL,
                            REQUIRED_SKILLS_ID, node);
                    if (suggestedSkillId == null) {
                        break;
                    }
                } catch (NullPointerException e) {
                    // Not more.
                    break;
                }
                String skillSpeciality = null;
                try {
                    skillSpeciality = translator.getNodeValue(profileId, SUGGESTED_SKILLS, REQUIRED_SKILL,
                            REQUIRED_SKILLS_SPECIALIZATION, node);
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
                    throw new InvalidProfileException("Invalid suggested skill '" + suggestedSkillId
                            + "' for  profile '" + profileId + "'.", e);
                }
                node++;
            } catch (NumberFormatException e) {
                break;
            }
        }

        final Set<BeneficeDefinition> mandatoryBenefices = getCommaSeparatedValues(profileId, MANDATORY_BENEFICES,
                language, moduleName, BeneficeDefinitionFactory.getInstance());

        final Set<BeneficeDefinition> suggestedBenefices = getCommaSeparatedValues(profileId, SUGGESTED_BENEFICES,
                language, moduleName, BeneficeDefinitionFactory.getInstance());

        final Set<Weapon> mandatoryWeapons = getCommaSeparatedValues(profileId, MANDATORY_WEAPONS, language,
                moduleName, WeaponFactory.getInstance());

        final Set<Armour> mandatoryArmours = getCommaSeparatedValues(profileId, MANDATORY_ARMOURS, language,
                moduleName, ArmourFactory.getInstance());

        final Set<Shield> mandatoryShields = getCommaSeparatedValues(profileId, MANDATORY_SHIELDS, language,
                moduleName, ShieldFactory.getInstance());

        return new RandomProfile(profileId, name, description, language, moduleName, preferencesSelected,
                characteristicsMinimumValues, requiredSkills, suggestedSkills, mandatoryBenefices, suggestedBenefices,
                mandatoryWeapons, mandatoryArmours, mandatoryShields);
    }
}
