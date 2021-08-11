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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.equipment.EquipmentSelector;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.Collection;
import java.util.Set;

public abstract class RandomWeapon extends EquipmentSelector<Weapon> {

    protected RandomWeapon(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences,
                           Set<Weapon> mandatoryWeapons) throws InvalidXmlElementException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        super(characterPlayer, preferences, mandatoryWeapons);
    }

    @Override
    public void assign() throws InvalidRandomElementSelectedException, UnofficialElementNotAllowedException {
        final Weapon selectedWeapon = selectElementByWeight();
        if (!getCharacterPlayer().getAllWeapons().contains(selectedWeapon)) {
            getCharacterPlayer().addWeapon(selectedWeapon);
            RandomGenerationLog.info(this.getClass().getName(), "Selected weapon '{}'.", selectedWeapon);
        }
    }

    @Override
    protected Collection<Weapon> getAllElements() throws InvalidXmlElementException {
        return WeaponFactory.getInstance().getElements(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName());
    }

    protected abstract Set<WeaponType> weaponTypesFilter();

    /**
     * Similar tech level weapons preferred.
     *
     * @param weapon the weapon
     * @return the weight
     */
    protected abstract int getWeightTechModificator(Weapon weapon);

    @Override
    protected int getWeight(Weapon weapon) throws InvalidRandomElementSelectedException {
        super.getWeight(weapon);

        // Preferences

        // Only ranged weapons.
        if (!weaponTypesFilter().contains(weapon.getType())) {
            throw new InvalidRandomElementSelectedException("Only weapons of type '" + weaponTypesFilter()
                    + "' are accepted. Weapon '" + weapon + "' has not this type.");
        }

        int weight = 0;
        // Similar tech level preferred.
        final int weightTech = getWeightTechModificator(weapon);
        RandomGenerationLog.debug(this.getClass().getName(),
                "Weight value by tech level for '{}}' is '{}'.", weapon, weightTech);
        weight += weightTech;

        // Weapons depending on the purchasing power of the character.
        final int costModificator = getWeightCostModificator(weapon);
        RandomGenerationLog.debug(this.getClass().getName(),
                "Cost multiplication for weight for '{}' is '{}'.", weapon, costModificator);
        weight /= costModificator;

        // Skill modifications.
        if (!weapon.getWeaponDamages().isEmpty()) {
            final int skillMultiplier = getCharacterPlayer().getSkillTotalRanks(weapon.getWeaponDamages().get(0).getSkill());
            if (skillMultiplier > 0) {
                RandomGenerationLog.debug(this.getClass().getName(),
                        "Skill multiplication for weight for '{}' is '{}'.", weapon, skillMultiplier);
                weight = (weight + skillMultiplier) * skillMultiplier;
            }
        }

        //Grandes and big guns appear too often for normal characters.
        final CombatPreferences combatPreferences = CombatPreferences.getSelected(getPreferences());
        if (combatPreferences != CombatPreferences.BELLIGERENT && (weapon.getType() == WeaponType.GRENADE ||
                weapon.getType() == WeaponType.GRENADE_LAUNCHER || weapon.getType() == WeaponType.ROCKETEER ||
                weapon.getType() == WeaponType.HEAVY || weapon.getType() == WeaponType.MINE)) {
            weight /= 10;
        }

        RandomGenerationLog.debug(this.getClass().getName(), "Total weight for '{}' is '{}'.", weapon, weight);
        return weight;
    }

    @Override
    public void validateElement(Weapon weapon) throws InvalidRandomElementSelectedException {
        super.validateElement(weapon);

        // Difficulty modification
        final DifficultLevelPreferences preference = DifficultLevelPreferences.getSelected(getPreferences());
        if (weapon.getWeaponDamages().isEmpty()) {
            throw new InvalidRandomElementSelectedException("Weapons without damage '" + weapon
                    + "' is not allowed for random characters.");
        }
        switch (preference) {
            case VERY_EASY:
                if (weapon.getWeaponDamages().get(0).getMainDamage() >= 3) {
                    throw new InvalidRandomElementSelectedException("Weapons with a damage '" + weapon.getWeaponDamages().get(0).getDamage()
                            + "' are not allowed by selected preference '" + preference + "'.");
                }
                break;
            case EASY:
                if (weapon.getWeaponDamages().get(0).getMainDamage() >= 5) {
                    throw new InvalidRandomElementSelectedException("Weapons with a damage '" + weapon.getWeaponDamages().get(0).getDamage()
                            + "' are not allowed by selected preference '" + preference + "'.");
                }
                break;
            case MEDIUM:
                if (weapon.getWeaponDamages().get(0).getMainDamage() >= 8) {
                    throw new InvalidRandomElementSelectedException("Weapons with a damage '" + weapon.getWeaponDamages().get(0).getDamage()
                            + "' are not allowed by selected preference '" + preference + "'.");
                }
                break;
            case HARD:
                if (weapon.getWeaponDamages().get(0).getMainDamage() <= 3) {
                    throw new InvalidRandomElementSelectedException("Weapons with a damage '" + weapon.getWeaponDamages().get(0).getDamage()
                            + "' are not allowed by selected preference '" + preference + "'.");
                }
                break;
            case VERY_HARD:
                if (weapon.getWeaponDamages().get(0).getMainDamage() <= 4 || weapon.getDamageTypes().isEmpty()) {
                    throw new InvalidRandomElementSelectedException("Weapons with a damage '" + weapon.getWeaponDamages().get(0).getDamage()
                            + "' or without damage perforation are not allowed by selected preference '" + preference
                            + "'.");
                }
                break;
        }
    }

    @Override
    protected void assignIfMandatory(Weapon weapon) throws InvalidXmlElementException {
        // Ignored
    }

    @Override
    protected void assignMandatoryValues(Set<Weapon> mandatoryValues) throws InvalidXmlElementException, UnofficialElementNotAllowedException {
        for (final Weapon weapon : mandatoryValues) {
            if (weaponTypesFilter().contains(weapon.getType())) {
                getCharacterPlayer().addWeapon(weapon);
            }
        }
    }
}
