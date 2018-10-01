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

import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponType;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;

public abstract class RandomWeapon extends RandomSelector<Weapon> {
	protected final static int TECH_LEVEL_BONUS = 1000000;
	private Integer currentMoney = null;

	protected RandomWeapon(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignWeapon() throws InvalidRandomElementSelectedException {
		Weapon selectedWeapon = selectElementByWeight();
		if (!getCharacterPlayer().getWeapons().getElements().contains(selectedWeapon)) {
			getCharacterPlayer().getWeapons().addElement(selectedWeapon);
			RandomGenerationLog.info(this.getClass().getName(), "Selected weapon: " + selectedWeapon);
		}
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

	protected int getCurrentMoney() {
		if (currentMoney == null) {
			currentMoney = getCharacterPlayer().getMoney();
		}
		return currentMoney;
	}

	protected abstract Set<WeaponType> weaponTypesFilter();

	/**
	 * Not so expensive weapons.
	 * 
	 * @param weapon
	 * @return
	 */
	protected abstract int getWeightCostModificator(Weapon weapon);

	/**
	 * Similar tech level weapons preferred.
	 * 
	 * @param weapon
	 * @return
	 */
	protected abstract int getWeightTechModificator(Weapon weapon);

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
		if (weapon.getCost() > getCurrentMoney()) {
			return 0;
		}

		// Faction restriction.
		if (weapon.getFaction() != null && Objects.equals(weapon.getFaction(), getCharacterPlayer().getFaction())) {
			return 0;
		}

		int weight = 0;
		// Similar tech level preferred.
		int weightTech = getWeightTechModificator(weapon);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight value by tech level for '" + weapon + "' is '" + weightTech + "'.");
		weight += weightTech;

		// Weapons depending on the purchasing power of the character.

		int costModificator = getWeightCostModificator(weapon);
		RandomGenerationLog.debug(this.getClass().getName(), "Cost multiplication for weight for '" + weapon + "' is '" + costModificator + "'.");
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
			RandomGenerationLog.debug(this.getClass().getName(), "Skill multiplication for weight for '" + weapon + "' is '" + skillMultiplier + "'.");
			weight = weight * skillMultiplier;
		} catch (InvalidXmlElementException e) {
			RandomGenerationLog.errorMessage(this.getClass().getName(), e);
		}

		RandomGenerationLog.debug(this.getClass().getName(), "Total weight for '" + weapon + "' is '" + weight + "'.");
		return weight;
	}
}
