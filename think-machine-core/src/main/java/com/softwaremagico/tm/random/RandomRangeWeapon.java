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
import com.softwaremagico.tm.character.race.InvalidRaceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;

public class RandomRangeWeapon extends RandomSelector<Weapon> {

	protected RandomRangeWeapon(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
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

	@Override
	protected int getWeight(Weapon weapon) {
		// Weapons only if technology is enough.
		if (weapon.getTechLevel() > getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue()) {
			return 0;
		}
		
		//It is possible

		return 1;
	}
}
