package com.softwaremagico.tm.random.profile;

import java.util.Map;
import java.util.Set;

import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public interface IRandomProfile {

	int getExperiencePoints();

	Set<IRandomPreference> getPreferences();

	Map<CharacteristicName, Integer> getCharacteristicsMinimumValues();

	Map<AvailableSkill, Integer> getSkillsMinimumValues();

	Set<Blessing> getBlessings();

	Set<AvailableBenefice> getBenefices();

}
