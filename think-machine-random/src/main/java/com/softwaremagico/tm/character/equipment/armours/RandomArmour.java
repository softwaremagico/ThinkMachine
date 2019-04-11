package com.softwaremagico.tm.character.equipment.armours;

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
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class RandomArmour extends EquipmentSelector<Armour> {

	public RandomArmour(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidRandomElementSelectedException, InvalidArmourException {
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

	/**
	 * Not so expensive armours.
	 * 
	 * @param armour
	 * @return
	 */
	@Override
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
	protected int getWeight(Armour armour) throws InvalidRandomElementSelectedException {
		super.getWeight(armour);

		// Heavy armours only for real warriors.
		if (!getPreferences().contains(CombatPreferences.BELLIGERENT)) {
			if (armour.isHeavy()) {
				throw new InvalidRandomElementSelectedException("Heavy armour '" + armour + "' not accepted for not combat characters.");
			}
		}

		DifficultLevelPreferences preference = DifficultLevelPreferences.getSelected(getPreferences());
		switch (preference) {
		case VERY_EASY:
			break;
		case EASY:
			if (armour.isHeavy()) {
				throw new InvalidRandomElementSelectedException("Heavy armour '" + armour + "' are not allowed by selected preference '" + preference + "'.");
			}
			break;
		case MEDIUM:
			break;
		case HARD:
			if (armour.getProtection() < 3) {
				throw new InvalidRandomElementSelectedException("Basic armour '" + armour + "' are not allowed by selected preference '" + preference + "'.");
			}
			break;
		case VERY_HARD:
			if (armour.getProtection() < 5 || armour.getDamageTypes().isEmpty()) {
				throw new InvalidRandomElementSelectedException("Basic armour '" + armour + "' are not allowed by selected preference '" + preference + "'.");
			}
			break;
		}

		int weight = 1;
		// Similar tech level preferred.
		int weightTech = getWeightTechModificator(armour);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight value by tech level for '" + armour + "' is '" + weightTech + "'.");
		weight += weightTech;

		// armours depending on the purchasing power of the character.
		int costModificator = getWeightCostModificator(armour);
		RandomGenerationLog.debug(this.getClass().getName(), "Cost multiplication for weight for '" + armour + "' is '" + costModificator + "'.");
		weight /= costModificator;

		// More protection is better.
		weight *= armour.getProtection();
		RandomGenerationLog.debug(this.getClass().getName(), "Protection multiplicator for '" + armour + "' is '" + armour.getProtection() + "'.");

		RandomGenerationLog.debug(this.getClass().getName(), "Total weight for '" + armour + "' is '" + weight + "'.");
		return weight;
	}

	@Override
	protected void assignIfMandatory(Armour element) throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException {
		return;
	}

	@Override
	protected void assignMandatoryValues(Set<Armour> mandatoryValues) throws InvalidXmlElementException {
		return;
	}
}
