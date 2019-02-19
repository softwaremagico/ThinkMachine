package com.softwaremagico.tm.random.profiles;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

import org.reflections.Reflections;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.random.selectors.AgePreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

@SuppressWarnings("rawtypes")
public class RandomProfileFactory extends XmlFactory<RandomProfile> {
	private final static ITranslator translator = LanguagePool.getTranslator("profiles.xml");

	private final static String NAME = "name";
	private final static String PREFERENCES = "preferences";
	private final static String CHARACTERISTICS_MINIMUM_VALUES = "characteristicsMinimumValues";
	private final static String REQUIRED_SKILLS = "requiredSkills";
	private final static String REQUIRED_SKILL = "skill";
	private final static String REQUIRED_SKILLS_ID = "id";
	private final static String REQUIRED_SKILLS_SPECIALIZATION = "speciality";
	private final static String PARENT = "parent";

	private static RandomProfileFactory instance;

	private static Set<Class<? extends Enum>> availablePreferences;

	private static void createInstance() {
		if (instance == null) {
			synchronized (RandomProfileFactory.class) {
				if (instance == null) {
					instance = new RandomProfileFactory();
				}
			}
		}
	}

	public static RandomProfileFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	private Set<Class<? extends Enum>> getAvailablePreferences() {
		if (availablePreferences == null) {
			Reflections reflections = new Reflections(AgePreferences.class.getPackage().getName());
			availablePreferences = reflections.getSubTypesOf(Enum.class);
		}
		return availablePreferences;
	}

	public static <T extends Enum<T>> T getEnum(final String value, final Class<T> enumClass) {
		return Enum.valueOf(enumClass, value);
	}

	@SuppressWarnings("unchecked")
	private IRandomPreference getSelectedPreference(String preferenceName) {
		for (Class<? extends Enum> classPreference : getAvailablePreferences()) {
			if (Objects.equals(classPreference.getSimpleName(), preferenceName.substring(0, preferenceName.indexOf('.')))) {
				return (IRandomPreference) Enum.valueOf(classPreference, preferenceName.substring(preferenceName.indexOf('.') + 1));
			}
		}
		return null;
	}

	@Override
	protected ITranslator getTranslator() {
		return translator;
	}

	@Override
	public RandomProfile getElement(String elementId, String language) throws InvalidXmlElementException {
		RandomProfile randomProfile = super.getElement(elementId, language);
		if (!randomProfile.isParentMerged()) {
			setParent(randomProfile, translator, language);
		}
		return randomProfile;
	}

	protected void setParent(RandomProfile profile, ITranslator translator, String language) throws InvalidXmlElementException {
		String parentName = translator.getNodeValue(profile.getId(), PARENT);
		if (parentName != null && !parentName.isEmpty()) {
			try {
				RandomProfile parent = RandomProfileFactory.getInstance().getElement(parentName, language);
				profile.setParent(parent);
			} catch (Exception e) {
				throw new InvalidProfileException("Invalid parent in profile '" + profile + "'.");
			}
		}
	}

	@Override
	protected RandomProfile createElement(ITranslator translator, String profileId, String language) throws InvalidXmlElementException {
		String name = null;
		try {
			name = translator.getNodeValue(profileId, NAME, language);
		} catch (Exception e) {
			throw new InvalidProfileException("Invalid name in profile '" + profileId + "'.");
		}

		Set<IRandomPreference> preferencesSelected = new HashSet<>();
		String preferencesSelectedNames = translator.getNodeValue(profileId, PREFERENCES);
		if (preferencesSelectedNames != null) {
			StringTokenizer preferencesSelectedTokenizer = new StringTokenizer(preferencesSelectedNames, ",");
			while (preferencesSelectedTokenizer.hasMoreTokens()) {
				preferencesSelected.add(getSelectedPreference(preferencesSelectedTokenizer.nextToken().trim()));
			}
		}

		Map<CharacteristicName, Integer> characteristicsMinimumValues = new HashMap<>();
		for (CharacteristicName characteristicName : CharacteristicName.values()) {
			String characteristicValue = translator.getNodeValue(profileId, CHARACTERISTICS_MINIMUM_VALUES, characteristicName.name().toLowerCase());
			if (characteristicValue != null) {
				try {
					characteristicsMinimumValues.put(characteristicName, Integer.parseInt(characteristicValue));
				} catch (NumberFormatException e) {
					throw new InvalidProfileException("Invalid min value in characteristic '" + characteristicName.name().toLowerCase() + "' of profile '"
							+ profileId + "'.");
				}
			}
		}

		Set<AvailableSkill> requiredSkills = new HashSet<>();
		String requiredSkillsNames = translator.getNodeValue(profileId, REQUIRED_SKILLS);
		try {
			StringTokenizer requiredSkillsTokenizer = new StringTokenizer(requiredSkillsNames, ",");
			while (requiredSkillsTokenizer.hasMoreTokens()) {
				String preferredSkillId = requiredSkillsTokenizer.nextToken().trim();
				try {
					requiredSkills.add(AvailableSkillsFactory.getInstance().getElement(preferredSkillId, language));
				} catch (InvalidXmlElementException e) {
					throw new InvalidProfileException("Invalid skill '" + preferredSkillId + "' for  profile '" + profileId + "'.", e);
				}
			}
		} catch (NullPointerException e) {
			// No skills defined.
		}

		int node = 0;
		while (true) {
			try {
				String preferredSkillId = null;
				try {
					preferredSkillId = translator.getNodeValue(profileId, REQUIRED_SKILL, REQUIRED_SKILLS_ID, node);
					if (preferredSkillId == null) {
						break;
					}
				} catch (NullPointerException e) {
					// Not more.
					break;
				}
				String skillSpeciality = null;
				try {
					skillSpeciality = translator.getNodeValue(profileId, REQUIRED_SKILL, REQUIRED_SKILLS_SPECIALIZATION, node);
				} catch (NullPointerException e) {
					// Not mandatory
				}
				try {
					if (skillSpeciality == null) {
						requiredSkills.add(AvailableSkillsFactory.getInstance().getElement(preferredSkillId, language));
					} else {
						requiredSkills.add(AvailableSkillsFactory.getInstance().getElement(preferredSkillId, skillSpeciality, language));
					}
				} catch (InvalidXmlElementException e) {
					throw new InvalidProfileException("Invalid skill '" + preferredSkillId + "' for  profile '" + profileId + "'.", e);
				}
				node++;
			} catch (NumberFormatException e) {
				break;
			}
		}

		RandomProfile profile = new RandomProfile(profileId, name, language, preferencesSelected, characteristicsMinimumValues, requiredSkills);
		return profile;
	}
}
