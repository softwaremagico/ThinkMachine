package com.softwaremagico.tm.random.profiles;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.RandomPreferenceUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public abstract class RandomPredefinedFactory<Predefined extends Element<Predefined> & IRandomProfile> extends XmlFactory<Predefined> {
    private static final String PREFERENCES = "preferences";
    private static final String CHARACTERISTICS_MINIMUM_VALUES = "characteristicsMinimumValues";
    private static final String REQUIRED_SKILLS = "requiredSkills";
    private static final String SUGGESTED_SKILLS = "suggestedSkills";
    private static final String REQUIRED_SKILL = "skill";
    private static final String REQUIRED_SKILLS_ID = "id";
    private static final String REQUIRED_SKILLS_SPECIALIZATION = "speciality";
    private static final String SUGGESTED_BENEFICES = "suggestedBenefices";
    private static final String MANDATORY_BENEFICES = "mandatoryBenefices";
    private static final String PARENT = "parent";

    protected void setParent(Predefined predefined, String language, String moduleName)
            throws InvalidXmlElementException {
        final String parentName = getTranslator(moduleName).getNodeValue(predefined.getId(), PARENT);
        if (parentName != null && !parentName.isEmpty()) {
            try {
                final Predefined parent = getElement(parentName, language, moduleName);
                predefined.setParent(parent);
            } catch (Exception e) {
                throw new InvalidProfileException("Invalid parent in element '" + predefined + "'.");
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
                                            Set<BeneficeDefinition> mandatoryBenefices, Set<BeneficeDefinition> suggestedBenefices);


    @Override
    protected Predefined createElement(ITranslator translator, String profileId, String name, String description,
                                       String language, String moduleName)
            throws InvalidXmlElementException {

        final Set<IRandomPreference> preferencesSelected = new HashSet<>();
        final String preferencesSelectedNames = translator.getNodeValue(profileId, PREFERENCES);
        if (preferencesSelectedNames != null) {
            final StringTokenizer preferencesSelectedTokenizer = new StringTokenizer(preferencesSelectedNames, ",");
            while (preferencesSelectedTokenizer.hasMoreTokens()) {
                try {
                    preferencesSelected.add(RandomPreferenceUtils.getSelectedPreference(preferencesSelectedTokenizer
                            .nextToken().trim()));
                } catch (StringIndexOutOfBoundsException e) {
                    throw new InvalidProfileException("Invalid preferences '"
                            + preferencesSelectedNames + "' on profile '" + profileId + "'.");
                }
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
                    skillSpeciality = translator.getNodeValue(profileId, REQUIRED_SKILLS, REQUIRED_SKILL,
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
                    skillSpeciality = translator.getNodeValue(profileId, SUGGESTED_SKILLS, REQUIRED_SKILL,
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

        return createNew(profileId, name, description, language, moduleName, preferencesSelected,
                characteristicsMinimumValues, requiredSkills, suggestedSkills, mandatoryBenefices, suggestedBenefices);
    }


}
