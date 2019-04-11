package com.softwaremagico.tm.character.equipment;

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

import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public abstract class EquipmentSelector<E extends Equipment<?>> extends RandomSelector<E> {
	private Integer currentMoney = null;

	protected EquipmentSelector(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	protected int getCurrentMoney() {
		if (currentMoney == null) {
			currentMoney = getCharacterPlayer().getMoney();
		}
		return currentMoney;
	}

	/**
	 * Not so expensive weapons.
	 * 
	 * @param weapon
	 * @return
	 */
	protected abstract int getWeightCostModificator(E equipment);

	@Override
	protected int getWeight(E equipment) throws InvalidRandomElementSelectedException {
		// Weapons only if technology is enough.
		if (equipment.getTechLevel() > getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue()) {
			throw new InvalidRandomElementSelectedException("Technology requirements not met for '" + equipment + "'.");
		}

		// I can afford it.
		if (equipment.getCost() > getCurrentMoney()) {
			throw new InvalidRandomElementSelectedException("Cost not affordable for '" + equipment + "'.");
		}

		return 1;
	}
}
