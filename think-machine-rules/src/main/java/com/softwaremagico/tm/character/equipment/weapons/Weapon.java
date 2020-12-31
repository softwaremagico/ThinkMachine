package com.softwaremagico.tm.character.equipment.weapons;

import java.util.HashSet;

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

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.log.MachineLog;

public class Weapon extends Equipment<Weapon> {
    private static final String NUMBER_EXTRACTOR_PATTERN = "^[^\\d]*(\\d+)";
    private static final Pattern FIRST_NUMBER_PATTERN = Pattern.compile(NUMBER_EXTRACTOR_PATTERN);
    private static final String AREA_DAMAGE = "\\((\\d+)\\s*m\\)$";
    private static final Pattern AREA_DAMAGE_PATTERN = Pattern.compile(AREA_DAMAGE);
    private static final String DAMAGE_WITHOUT_AREA = "^(.*?)\\(";
    private static final Pattern DAMAGE_WITHOUT_AREA_PATTERN = Pattern.compile(DAMAGE_WITHOUT_AREA);
    private static final int SPECIAL_DAMAGE_THREAT = 5;

    private final String goal;
    private final String damage;
    private final int strength;
    private final String range;
    private final Integer shots;
    private final String rate;
    private final Size size;
    private final boolean techLevelSpecial;

    private final String special;
    private final Set<DamageType> damageTypes;
    private final AvailableSkill skill;
    private final CharacteristicDefinition characteristic;
    private final WeaponType type;

    private final Set<Ammunition> ammunitions;
    private final Set<Accessory> accesories;

    private transient Integer mainDamage = null;
    private transient Integer areaDamage = null;
    private transient String areaWithoutDamage = null;

    /**
     * For creating empty elements.
     */
    public Weapon() {
        super();
        this.ammunitions = new HashSet<>();
        this.accesories = new HashSet<>();
        this.damageTypes = new HashSet<>();
        this.type = null;
        this.techLevelSpecial = false;
        this.goal = "";
        this.strength = 0;
        this.range = "";
        this.shots = 0;
        this.rate = "";
        this.special = "";
        this.skill = null;
        this.characteristic = null;
        this.size = null;
        this.damage = "";
    }

    public Weapon(String id, String name, String description, String language, String moduleName, WeaponType type, String goal,
                  CharacteristicDefinition characteristic, AvailableSkill skill, String damage, int strength, String range,
                  Integer shots, String rate, int techLevel, boolean techLevelSpecial, Size size, String special, Set<DamageType> damageTypes,
                  float cost, Set<Ammunition> ammunitions, Set<Accessory> accesories) {
        super(id, name, description, cost, techLevel, language, moduleName);
        this.characteristic = characteristic;
        this.skill = skill;
        this.goal = goal;
        this.damage = damage;
        this.strength = strength;
        this.range = range;
        this.shots = shots;
        this.rate = rate;
        this.size = size;
        this.techLevelSpecial = techLevelSpecial;
        this.type = type;
        this.special = special;
        this.damageTypes = damageTypes;
        this.ammunitions = ammunitions;
        this.accesories = accesories;
    }

    public String getRoll() {
        try {
            return characteristic.getAbbreviature() + "+" + skill.getName();
        } catch (Exception e) {
            return "";
        }
    }

    public WeaponType getType() {
        return type;
    }

    public String getGoal() {
        if (goal == null) {
            return "";
        }
        return goal;
    }

    public String getDamage() {
        return damage;
    }

    public String getDamageWithoutArea() {
        if (areaWithoutDamage == null) {
            try {
                final Matcher matcher = DAMAGE_WITHOUT_AREA_PATTERN.matcher(getDamage());
                if (matcher.find()) {
                    areaWithoutDamage = matcher.group(1);
                } else {
                    areaWithoutDamage = getDamage();
                }
            } catch (NullPointerException e) {
                // No area
                areaWithoutDamage = getDamage();
            }
            areaWithoutDamage = areaWithoutDamage.trim();
        }
        return areaWithoutDamage;
    }

    public int getMainDamage() {
        if (mainDamage == null) {
            try {
                final Matcher matcher = FIRST_NUMBER_PATTERN.matcher(getDamage());
                if (matcher.find()) {
                    mainDamage = Integer.parseInt(matcher.group());
                } else {
                    mainDamage = 0;
                }
            } catch (NullPointerException e) {
                // No damage
                mainDamage = 0;
            } catch (NumberFormatException e) {
                if (getDamage().contains("*")) {
                    // Special damage!
                    mainDamage = SPECIAL_DAMAGE_THREAT;
                } else {
                    MachineLog.severe(this.getClass().getName(), "Invalid main damage in '{}' for '{}'.", getDamage(), this);
                }
            }
        }
        return mainDamage;
    }

    public int getAreaMeters() {
        if (areaDamage == null) {
            try {
                final Matcher matcher = AREA_DAMAGE_PATTERN.matcher(getDamage());
                if (matcher.find()) {
                    areaDamage = Integer.parseInt(matcher.group(1));
                } else {
                    areaDamage = 0;
                }
            } catch (NullPointerException e) {
                // No area
                areaDamage = 0;
            } catch (NumberFormatException e) {
                MachineLog.severe(this.getClass().getName(), "Invalid area damage in '{}' for '{}'.", getDamage(), this);
            }
        }
        return areaDamage;
    }

    public Integer getShots() {
        return shots;
    }

    public String getRate() {
        return rate;
    }

    public int getMainRate() {
        try {
            final Matcher matcher = FIRST_NUMBER_PATTERN.matcher(getRate());
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            }
        } catch (NullPointerException e) {
            // Melee weapon.
        }
        return 0;
    }

    public boolean isAutomaticWeapon() {
        if (getRate() != null) {
            return getRate().toLowerCase().contains("a");
        }
        return false;
    }

    public Size getSize() {
        return size;
    }

    public String getSpecial() {
        return special;
    }

    public int getStrength() {
        return strength;
    }

    public String getRange() {
        return range;
    }

    public int getMainRange() {
        try {
            final Matcher matcher = FIRST_NUMBER_PATTERN.matcher(getRange());
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            }
        } catch (NullPointerException e) {
            // Melee weapon.
        }
        return 0;
    }

    public AvailableSkill getSkill() {
        return skill;
    }

    public CharacteristicDefinition getCharacteristic() {
        return characteristic;
    }

    public String getStrengthOrRange() {
        if (range == null) {
            return strength + "";
        }
        return range;
    }

    public Set<DamageType> getDamageTypes() {
        return damageTypes;
    }

    public boolean isTechLevelSpecial() {
        return techLevelSpecial;
    }

    public Set<Ammunition> getAmmunitions() {
        return ammunitions;
    }

    public Set<Accessory> getAccesories() {
        return accesories;
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
}
