package com.softwaremagico.tm.character.equipment.shields;

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
import com.softwaremagico.tm.character.equipment.shields.InvalidShieldException;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class RandomShield extends RandomSelector<Shield> {
	private Integer currentMoney = null;

	public RandomShield(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidRandomElementSelectedException, InvalidShieldException {
		Shield selectedShield = selectElementByWeight();
		if (getCharacterPlayer().getShield() == null) {
			getCharacterPlayer().setShield(selectedShield);
			RandomGenerationLog.info(this.getClass().getName(), "Selected shield: " + selectedShield);
		}
	}

	@Override
	protected Collection<Shield> getAllElements() throws InvalidXmlElementException {
		return ShieldFactory.getInstance().getElements(getCharacterPlayer().getLanguage());
	}

	protected int getCurrentMoney() {
		if (currentMoney == null) {
			currentMoney = getCharacterPlayer().getMoney();
		}
		return currentMoney;
	}

	/**
	 * Not so expensive shields.
	 * 
	 * @param shield
	 * @return
	 */
	protected int getWeightCostModificator(Shield shield) {
		if (shield.getCost() > getCurrentMoney() / 2) {
			return 5;
		} else {
			return 1;
		}
	}

	@Override
	protected int getWeight(Shield shield) {
		// Shields only if technology is enough.
		if (shield.getTechLevel() > getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue()) {
			return NO_PROBABILITY;
		}

		// I can afford it.
		if (shield.getCost() > getCurrentMoney()) {
			return NO_PROBABILITY;
		}

		int weight = 1;

		// More protection is better.
		if (getPreferences().contains(CombatPreferences.BELLIGERENT)) {
			weight = weight * shield.getHits();
			RandomGenerationLog.debug(this.getClass().getName(), "Protection multiplicator for '" + shield + "' is '" + shield.getHits() + "'.");
		}

		// shields depending on the purchasing power of the character.
		int costModificator = getWeightCostModificator(shield);
		RandomGenerationLog.debug(this.getClass().getName(), "Cost multiplication for weight for '" + shield + "' is '" + costModificator + "'.");
		weight = weight / costModificator;

		RandomGenerationLog.debug(this.getClass().getName(), "Total weight for '" + shield + "' is '" + weight + "'.");
		return weight;
	}

	@Override
	protected void assignIfMandatory(Shield element) throws InvalidXmlElementException {
		return;
	}

	@Override
	protected void assignMandatoryValues(Set<Shield> mandatoryValues) throws InvalidXmlElementException {
		return;
	}
}
