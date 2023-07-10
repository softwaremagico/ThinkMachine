package com.softwaremagico.tm.character.equipment.weapons;

import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.equipment.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

public class Weapon extends Equipment<Weapon> {

    private final List<WeaponDamage> weaponDamages;


    private final Size size;
    private final boolean techLevelSpecial;

    private final String special;
    private final Set<DamageType> damageTypes;
    private final WeaponType type;

    private final Set<Ammunition> ammunition;
    private final Set<Accessory> accessories;


    /**
     * For creating empty elements.
     */
    public Weapon() {
        super();
        this.ammunition = new HashSet<>();
        this.accessories = new HashSet<>();
        this.damageTypes = new HashSet<>();
        this.weaponDamages = new ArrayList<>();
        this.type = null;
        this.techLevelSpecial = false;
        this.special = "";
        this.size = null;
    }

    public Weapon(String id, String name, String description, String language, String moduleName, WeaponType type,
                  List<WeaponDamage> damages, int techLevel, boolean techLevelSpecial, Size size, String special,
                  Set<DamageType> damageTypes, float cost, Set<Ammunition> ammunition, Set<Accessory> accessories) {
        super(id, name, description, cost, techLevel, language, moduleName);
        this.weaponDamages = damages;
        this.size = size;
        this.techLevelSpecial = techLevelSpecial;
        this.type = type;
        this.special = special;
        this.damageTypes = damageTypes;
        this.ammunition = ammunition;
        this.accessories = accessories;
    }

    public WeaponType getType() {
        return type;
    }


    @SuppressWarnings({"java:S3655"})
    public boolean isMeleeWeapon() {
        return getType() == WeaponType.MELEE || getType() == WeaponType.MELEE_ARTIFACT || getType() == WeaponType.MELEE_SHIELD ||
                (getWeaponDamages().stream().findFirst().isPresent() && getWeaponDamages().stream().findFirst().get().getRange() == null);
    }

    public boolean isRangedWeapon() {
        return !isMeleeWeapon();
    }

    public boolean isAutomaticWeapon() {
        if (!weaponDamages.isEmpty()) {
            if (weaponDamages.get(0).getRate() != null) {
                return weaponDamages.get(0).getRate().toLowerCase().contains("a");
            }
        }
        return false;
    }

    public Size getSize() {
        return size;
    }

    public String getSpecial() {
        return special;
    }

    public Set<DamageType> getDamageTypes() {
        return damageTypes;
    }

    public boolean isTechLevelSpecial() {
        return techLevelSpecial;
    }

    public Set<Ammunition> getAmmunition() {
        return ammunition;
    }

    public Set<Accessory> getAccessories() {
        return accessories;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String getWeaponOthersText() {
        // Damage types
        final StringBuilder stringBuilder = new StringBuilder();
        for (final DamageType damageType : getDamageTypes()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(damageType.getName());
        }

        // Others
        if (getSpecial() != null && !getSpecial().isEmpty()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(" / ");
            }
            stringBuilder.append(getSpecial());
        }
        return stringBuilder.toString();
    }

    public List<WeaponDamage> getWeaponDamages() {
        return weaponDamages;
    }
}
