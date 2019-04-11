package com.softwaremagico.tm.character.equipment.weapons;

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

import java.util.Collection;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.EquipmentSelector;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public abstract class RandomWeapon extends EquipmentSelector<Weapon> {

	protected RandomWeapon(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidRandomElementSelectedException {
		Weapon selectedWeapon = selectElementByWeight();
		if (!getCharacterPlayer().getAllWeapons().contains(selectedWeapon)) {
			getCharacterPlayer().addWeapon(selectedWeapon);
			RandomGenerationLog.info(this.getClass().getName(), "Selected weapon: " + selectedWeapon);
		}
	}

	@Override
	protected Collection<Weapon> getAllElements() throws InvalidXmlElementException {
		return WeaponFactory.getInstance().getElements(getCharacterPlayer().getLanguage());
	}

	protected abstract Set<WeaponType> weaponTypesFilter();

	/**
	 * Similar tech level weapons preferred.
	 * 
	 * @param weapon
	 * @return
	 */
	protected abstract int getWeightTechModificator(Weapon weapon);

	@Override
	protected int getWeight(Weapon weapon) {
		// Only ranged weapons.
		if (!weaponTypesFilter().contains(weapon.getType())) {
			return NO_PROBABILITY;
		}

		int weight = super.getWeight(weapon);
		// Similar tech level preferred.
		int weightTech = getWeightTechModificator(weapon);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight value by tech level for '" + weapon + "' is '" + weightTech + "'.");
		weight += weightTech;

		// Weapons depending on the purchasing power of the character.
		int costModificator = getWeightCostModificator(weapon);
		RandomGenerationLog.debug(this.getClass().getName(), "Cost multiplication for weight for '" + weapon + "' is '" + costModificator + "'.");
		weight /= costModificator;

		// Skill modifications.
		int skillMultiplier = getCharacterPlayer().getSkillTotalRanks(weapon.getSkill());
		if (skillMultiplier > 0) {
			RandomGenerationLog.debug(this.getClass().getName(), "Skill multiplication for weight for '" + weapon + "' is '" + skillMultiplier + "'.");
			weight = (weight + skillMultiplier) * skillMultiplier;
		}

		RandomGenerationLog.debug(this.getClass().getName(), "Total weight for '" + weapon + "' is '" + weight + "'.");
		return weight;
	}

	@Override
	protected void assignIfMandatory(Weapon weapon) throws InvalidXmlElementException {
		return;
	}

	@Override
	protected void assignMandatoryValues(Set<Weapon> mandatoryValues) throws InvalidXmlElementException {
		return;
	}
}
