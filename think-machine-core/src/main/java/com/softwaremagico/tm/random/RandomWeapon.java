package com.softwaremagico.tm.random;

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

import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponType;
import com.softwaremagico.tm.character.race.InvalidRaceException;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;

public abstract class RandomWeapon extends RandomSelector<Weapon> {
	private final static int TECH_LEVEL_BONUS = 10000000;

	protected RandomWeapon(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignWeapon() throws InvalidRaceException, InvalidRandomElementSelectedException {
		getCharacterPlayer().getWeapons().addElement(selectElementByWeight());
	}

	@Override
	protected TreeMap<Integer, Weapon> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, Weapon> weightedWeapons = new TreeMap<>();
		int count = 1;
		for (Weapon weapon : WeaponFactory.getInstance().getElements(getCharacterPlayer().getLanguage())) {
			int weight = getWeight(weapon);
			if (weight > 0) {
				weightedWeapons.put(count, weapon);
				count += weight;
			}
		}
		return weightedWeapons;
	}

	protected abstract Set<WeaponType> weaponTypesFilter();

	@Override
	protected int getWeight(Weapon weapon) {
		// Weapons only if technology is enough.
		if (weapon.getTechLevel() > getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue()) {
			return 0;
		}

		// Only ranged weapons.
		if (!weaponTypesFilter().contains(weapon.getType())) {
			return 0;
		}

		// I can afford it.
		if (weapon.getCost() > getCharacterPlayer().getMoney()) {
			return 0;
		}

		int weight = 0;
		// Similar tech level preferred.
		weight += TECH_LEVEL_BONUS - Math.pow(10, 2 * getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() - weapon.getTechLevel());
		if (weight <= 0) {
			weight = 0;
		}

		// Cheapest weapons are more common.
		int costModificator = 1;
		if (weapon.getCost() > 5000) {
			costModificator = 10;
		} else if (weapon.getCost() > 3000) {
			costModificator = 5;
		} else if (weapon.getCost() > 2000) {
			costModificator = 4;
		} else if (weapon.getCost() > 1000) {
			costModificator = 3;
		} else if (weapon.getCost() > 500) {
			costModificator = 2;
		} else {
			costModificator = 1;
		}

		weight = weight / costModificator;

		// Skill modifications.
		try {
			int skillMultiplier = 0;
			for (AvailableSkill availableSkill : AvailableSkillsFactory.getInstance().getAvailableSkills(weapon.getSkill(), getCharacterPlayer().getLanguage())) {
				int totalRanks = getCharacterPlayer().getSkillTotalRanks(availableSkill);
				if (totalRanks > 0) {
					if (totalRanks > skillMultiplier) {
						skillMultiplier = totalRanks;
					}
				}
			}
			weight = weight * skillMultiplier;
		} catch (InvalidXmlElementException e) {
			RandomGenerationLog.errorMessage(this.getClass().getName(), e);
		}

		return weight;
	}
}
