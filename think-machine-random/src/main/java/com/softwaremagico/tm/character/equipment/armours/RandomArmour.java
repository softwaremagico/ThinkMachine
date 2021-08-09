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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.EquipmentSelector;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.ArmourPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

public class RandomArmour extends EquipmentSelector<Armour> {

    public RandomArmour(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences,
                        Set<Armour> mandatoryArmours) throws InvalidXmlElementException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        super(characterPlayer, preferences, mandatoryArmours);
    }

    @Override
    public void assign() throws InvalidRandomElementSelectedException, InvalidArmourException, UnofficialElementNotAllowedException {
        final Random random = new Random();
        final ArmourPreferences armourPreferences = ArmourPreferences.getSelected(getPreferences());
        if (armourPreferences != null && random.nextFloat() < armourPreferences.getArmourProbability()) {
            final Armour selectedArmour = selectElementByWeight();
            if (getCharacterPlayer().getArmour() == null) {
                getCharacterPlayer().setArmour(selectedArmour);
                RandomGenerationLog.info(this.getClass().getName(), "Selected armour '{}'.", selectedArmour);
            }
        }
    }

    @Override
    protected Collection<Armour> getAllElements() throws InvalidXmlElementException {
        return ArmourFactory.getInstance().getElements(getCharacterPlayer().getLanguage(),
                getCharacterPlayer().getModuleName());
    }

    /**
     * Not so expensive armours.
     *
     * @param armour the armour
     * @return the weight
     */
    @Override
    protected int getWeightCostModificator(Armour armour) throws InvalidRandomElementSelectedException {
        if (armour.getCost() > getCurrentMoney()) {
            throw new InvalidRandomElementSelectedException("Not enough money!");
        } else if (armour.getCost() > getCurrentMoney() / 2d) {
            return 100;
        } else if (armour.getCost() > getCurrentMoney() / 3d) {
            return 50;
        } else if (armour.getCost() > getCurrentMoney() / 4d) {
            return 25;
        } else if (armour.getCost() > getCurrentMoney() / 5d) {
            return 5;
        } else if (armour.getCost() > getCurrentMoney() / 10d) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * Similar tech level armours preferred.
     *
     * @param armour the armour
     * @return the weight
     */
    protected int getWeightTechModificator(Armour armour) {
        int weight = 0;
        // Similar tech level preferred.
        weight += MAX_PROBABILITY / Math.pow(10,
                (getCharacterPlayer().getCharacteristicValue(CharacteristicName.TECH) - armour.getTechLevel()));
        RandomGenerationLog.debug(this.getClass().getName(),
                "Weight tech bonus for '{}' is '{}'.", armour, MAX_PROBABILITY
                        / Math.pow(10, 2 * (getCharacterPlayer().getCharacteristicValue(CharacteristicName.TECH)
                        - armour.getTechLevel())));
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
        int weight = super.getWeight(armour);

        // Heavy armours only for real warriors.
        if (!getPreferences().contains(CombatPreferences.BELLIGERENT)) {
            if (armour.isHeavy()) {
                throw new InvalidRandomElementSelectedException(
                        "Heavy armour '" + armour + "' not accepted for not combat characters.");
            }
        }

        // Similar tech level preferred.
        final int weightTech = getWeightTechModificator(armour);
        RandomGenerationLog.debug(this.getClass().getName(),
                "Weight value by tech level for '{}' is '{}'.", armour, weightTech);
        weight += weightTech;

        // armours depending on the purchasing power of the character.
        final int costModificator = getWeightCostModificator(armour);
        RandomGenerationLog.debug(this.getClass().getName(),
                "Cost multiplication for weight for '{}' is '{}'.", armour, costModificator);
        weight /= costModificator;

        // More protection is better.
        weight *= armour.getProtection();
        RandomGenerationLog.debug(this.getClass().getName(),
                "Protection multiplicator for '{}' is '{}'.", armour, armour.getProtection());

        RandomGenerationLog.debug(this.getClass().getName(), "Total weight for '{}' is '{}'.", armour, weight);
        return weight;
    }

    @Override
    public void validateElement(Armour armour) throws InvalidRandomElementSelectedException {
        super.validateElement(armour);

        final DifficultLevelPreferences preference = DifficultLevelPreferences.getSelected(getPreferences());
        switch (preference) {
            case VERY_EASY:
            case MEDIUM:
                break;
            case EASY:
                if (armour.isHeavy()) {
                    throw new InvalidRandomElementSelectedException(
                            "Heavy armour '" + armour + "' are not allowed by selected preference '" + preference + "'.");
                }
                break;
            case HARD:
                if (armour.getProtection() < 3) {
                    throw new InvalidRandomElementSelectedException(
                            "Basic armour '" + armour + "' are not allowed by selected preference '" + preference + "'.");
                }
                break;
            case VERY_HARD:
                if (armour.getProtection() < 5 || armour.getDamageTypes().isEmpty()) {
                    throw new InvalidRandomElementSelectedException(
                            "Basic armour '" + armour + "' are not allowed by selected preference '" + preference + "'.");
                }
                break;
        }
    }

    @Override
    protected void assignIfMandatory(Armour element)
            throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException {
        //Ignored
    }

    @Override
    protected void assignMandatoryValues(Set<Armour> mandatoryValues) throws InvalidXmlElementException, UnofficialElementNotAllowedException {
        // We only assign one armour of the mandatory list.
        if (!mandatoryValues.isEmpty()) {
            getCharacterPlayer().setArmour(mandatoryValues.iterator().next());
        }
    }
}
