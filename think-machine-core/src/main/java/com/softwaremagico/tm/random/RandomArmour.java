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

import java.util.Collection;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.armour.Armour;
import com.softwaremagico.tm.character.equipment.armour.ArmourFactory;
import com.softwaremagico.tm.character.equipment.armour.InvalidArmourException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class RandomArmour extends RandomSelector<Armour> {
	private Integer currentMoney = null;

	protected RandomArmour(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignArmour() throws InvalidRandomElementSelectedException, InvalidArmourException {
		Armour selectedArmour = selectElementByWeight();
		if (getCharacterPlayer().getArmour() == null) {
			getCharacterPlayer().setArmour(selectedArmour);
			RandomGenerationLog.info(this.getClass().getName(), "Selected armour: " + selectedArmour);
		}
	}

	@Override
	protected Collection<Armour> getAllElements() throws InvalidXmlElementException {
		return ArmourFactory.getInstance().getElements(getCharacterPlayer().getLanguage());
	}

	protected int getCurrentMoney() {
		if (currentMoney == null) {
			currentMoney = getCharacterPlayer().getMoney();
		}
		return currentMoney;
	}

	/**
	 * Not so expensive armours.
	 * 
	 * @param armour
	 * @return
	 */
	protected int getWeightCostModificator(Armour armour) {
		if (armour.getCost() > getCurrentMoney() / 2) {
			return 100;
		} else if (armour.getCost() > getCurrentMoney() / 3) {
			return 50;
		} else if (armour.getCost() > getCurrentMoney() / 4) {
			return 25;
		} else if (armour.getCost() > getCurrentMoney() / 5) {
			return 5;
		} else if (armour.getCost() > getCurrentMoney() / 10) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * Similar tech level armours preferred.
	 * 
	 * @param armour
	 * @return
	 */
	protected int getWeightTechModificator(Armour armour) {
		int weight = 0;
		// Similar tech level preferred.
		weight += MAX_PROBABILITY / Math.pow(10, (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() - armour.getTechLevel()));
		RandomGenerationLog.debug(
				this.getClass().getName(),
				"Weight tech bonus for '" + armour + "' is '" + MAX_PROBABILITY
						/ Math.pow(10, 2 * (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() - armour.getTechLevel())) + "'.");
		if (weight <= 0) {
			if (armour.getTechLevel() < 3) {
				weight = 0;
			} else {
				weight = 1;
			}
		}
		return weight;
	}

	@Override
	protected int getWeight(Armour armour) {
		// armours only if technology is enough.
		if (armour.getTechLevel() > getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue()) {
			return NO_PROBABILITY;
		}

		// I can afford it.
		if (armour.getCost() > getCurrentMoney()) {
			return NO_PROBABILITY;
		}

		// Heavy armours only for real warriors.
		if (!getPreferences().contains(CombatPreferences.BELLIGERENT)) {
			if (armour.isHeavy()) {
				return 0;
			}
		}

		int weight = 0;
		// Similar tech level preferred.
		int weightTech = getWeightTechModificator(armour);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight value by tech level for '" + armour + "' is '" + weightTech + "'.");
		weight += weightTech;

		// armours depending on the purchasing power of the character.
		int costModificator = getWeightCostModificator(armour);
		RandomGenerationLog.debug(this.getClass().getName(), "Cost multiplication for weight for '" + armour + "' is '" + costModificator + "'.");
		weight = weight / costModificator;

		// More protection is better.
		weight = weight * armour.getProtection();
		RandomGenerationLog.debug(this.getClass().getName(), "Protection multiplicator for '" + armour + "' is '" + armour.getProtection() + "'.");

		RandomGenerationLog.debug(this.getClass().getName(), "Total weight for '" + armour + "' is '" + weight + "'.");
		return weight;
	}
}