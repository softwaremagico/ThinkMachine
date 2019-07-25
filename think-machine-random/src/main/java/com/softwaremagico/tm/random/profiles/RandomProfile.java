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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.json.ExcludeFromJson;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class RandomProfile extends Element<RandomProfile> implements IRandomProfile {
	private final Set<IRandomPreference> randomPreferences;
	private final Set<Characteristic> characteristicsMinimumValues;
	private final Set<AvailableSkill> requiredSkills;
	private final Set<AvailableSkill> suggestedSkills;
	private final Set<BeneficeDefinition> suggestedBenefices;
	private final Set<BeneficeDefinition> mandatoryBenefices;
	private final Set<Weapon> mandatoryWeapons;
	private final Set<Armour> mandatoryArmours;
	private final Set<Shield> mandatoryShields;

	@ExcludeFromJson
	public boolean parentMerged = false;

	public RandomProfile(String id, String name, String language, Set<IRandomPreference> randomPreferences,
			Set<Characteristic> characteristicsMinimumValues, Set<AvailableSkill> requiredSkills,
			Set<AvailableSkill> suggestedSkills, Set<BeneficeDefinition> mandatoryBenefices,
			Set<BeneficeDefinition> suggestedBenefices, Set<Weapon> mandatoryWeapons, Set<Armour> mandatoryArmours,
			Set<Shield> mandatoryShields) {
		super(id, name, language);
		this.randomPreferences = randomPreferences;
		this.characteristicsMinimumValues = characteristicsMinimumValues;
		this.requiredSkills = requiredSkills;
		this.suggestedSkills = suggestedSkills;
		this.suggestedBenefices = suggestedBenefices;
		this.mandatoryBenefices = mandatoryBenefices;
		this.mandatoryWeapons = mandatoryWeapons;
		this.mandatoryArmours = mandatoryArmours;
		this.mandatoryShields = mandatoryShields;
	}

	public RandomProfile(String id, String name, String language) {
		this(id, name, language, new HashSet<IRandomPreference>(), new HashSet<Characteristic>(),
				new HashSet<AvailableSkill>(), new HashSet<AvailableSkill>(), new HashSet<BeneficeDefinition>(),
				new HashSet<BeneficeDefinition>(), new HashSet<Weapon>(), new HashSet<Armour>(), new HashSet<Shield>());
	}

	@Override
	public void setParent(IRandomProfile parentProfile) throws InvalidXmlElementException {
		if (!parentMerged && parentProfile != null) {
			// Merge preferences. This has preference over parent profile.
			final RandomProfile mergedProfile = ProfileMerger.merge(parentProfile.getLanguage(), parentProfile, this);

			randomPreferences.clear();
			randomPreferences.addAll(mergedProfile.getPreferences());

			characteristicsMinimumValues.clear();
			characteristicsMinimumValues.addAll(mergedProfile.getCharacteristicsMinimumValues());

			parentMerged = true;
		}
	}

	@Override
	public Set<Characteristic> getCharacteristicsMinimumValues() {
		return characteristicsMinimumValues;
	}

	@Override
	public Characteristic getCharacteristicMinimumValues(CharacteristicName characteristicName) {
		for (final Characteristic characteristic : getCharacteristicsMinimumValues()) {
			if (Objects.equals(characteristic.getCharacteristicDefinition().getCharacteristicName(),
					characteristicName)) {
				return characteristic;
			}
		}
		return null;
	}

	@Override
	public Map<AvailableSkill, Integer> getSkillsMinimumValues() {
		return new HashMap<>();
	}

	@Override
	public Set<Blessing> getBlessings() {
		return new HashSet<>();
	}

	@Override
	public Set<AvailableBenefice> getBenefices() {
		return new HashSet<>();
	}

	@Override
	public int getExperiencePoints() {
		return 0;
	}

	@Override
	public Set<IRandomPreference> getPreferences() {
		return randomPreferences;
	}

	public boolean isParentMerged() {
		return parentMerged;
	}

	public void setParentMerged(boolean parentMerged) {
		this.parentMerged = parentMerged;
	}

	@Override
	public Set<AvailableSkill> getRequiredSkills() {
		return requiredSkills;
	}

	@Override
	public Set<AvailableSkill> getSuggestedSkills() {
		return suggestedSkills;
	}

	@Override
	public Set<BeneficeDefinition> getSuggestedBenefices() {
		return suggestedBenefices;
	}

	@Override
	public Set<BeneficeDefinition> getMandatoryBenefices() {
		return mandatoryBenefices;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public Set<Weapon> getMandatoryWeapons() {
		return mandatoryWeapons;
	}

	@Override
	public Set<Armour> getMandatoryArmours() {
		return mandatoryArmours;
	}

	@Override
	public Set<Shield> getMandatoryShields() {
		return mandatoryShields;
	}
}
