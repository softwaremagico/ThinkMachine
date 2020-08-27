package com.softwaremagico.tm.character.equipment.weapons;

import java.util.Random;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.WeaponsPreferences;

public class RandomMeleeWeapon extends RandomWeapon {

	public RandomMeleeWeapon(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences,
			Set<Weapon> mandatoryWeapons) throws InvalidXmlElementException {
		super(characterPlayer, preferences, mandatoryWeapons);
	}

	@Override
	public void assign() throws InvalidRandomElementSelectedException {
		final Random random = new Random();

		final WeaponsPreferences weaponPreferences = WeaponsPreferences.getSelected(getPreferences());
		float probabilityOfMeleeWeapon = weaponPreferences.getMeleeWeaponProbability();
		while (probabilityOfMeleeWeapon > 0) {
			if (probabilityOfMeleeWeapon > 0 && random.nextFloat() < probabilityOfMeleeWeapon) {
				try {
					super.assign();
				} catch (InvalidRandomElementSelectedException ires) {
					RandomGenerationLog.warning(this.getClass().getName(),
							"No melee weapons available for '" + getCharacterPlayer() + "'.");
				}
			}
			probabilityOfMeleeWeapon -= 0.4f;
		}
	}

	@Override
	protected Set<WeaponType> weaponTypesFilter() {
		return WeaponType.getMeleeTypes();
	}

	@Override
	protected int getWeightCostModificator(Weapon weapon) {
		if (weapon.getCost() > getCurrentMoney() / (double) 1.1) {
			return 10;
		} else if (weapon.getCost() > getCurrentMoney() / (double) 2) {
			return 7;
		} else if (weapon.getCost() > getCurrentMoney() / (double) 3) {
			return 5;
		} else if (weapon.getCost() > getCurrentMoney() / (double) 4) {
			return 4;
		} else if (weapon.getCost() > getCurrentMoney() / (double) 5) {
			return 3;
		} else if (weapon.getCost() > getCurrentMoney() / (double) 10) {
			return 2;
		} else {
			// Melee weapons are usually cheap.
			return 1;
		}
	}

	@Override
	protected int getWeightTechModificator(Weapon weapon) {
		int weight = 0;

		// Shields only if already has a weapon.
		if (weapon.getType().equals(WeaponType.MELEE_SHIELD)) {
			boolean alreadyWeapon = false;
			for (final Weapon weaponEquiped : getCharacterPlayer().getAllWeapons()) {
				// Already a different weapon
				if (weaponEquiped.getType() != WeaponType.MELEE_SHIELD
						&& weaponTypesFilter().contains(weaponEquiped.getType())) {
					alreadyWeapon = true;
					break;
				}
			}
			if (!alreadyWeapon) {
				return 0;
			}
		}

		// Similar tech level preferred.
		weight += MAX_PROBABILITY / Math.pow(10,
				2 * (getCharacterPlayer().getCharacteristicValue(CharacteristicName.TECH) - weapon.getTechLevel()));
		RandomGenerationLog.debug(this.getClass().getName(), "Weight tech bonus for '" + weapon + "' is '"
				+ MAX_PROBABILITY
						/ Math.pow(10, 2 * (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue()
								- weapon.getTechLevel()))
				+ "'.");
		if (weight <= 0) {
			// Melee weapons usually has very low tech level.
			weight = 1;
		}

		return weight;
	}

}
