package com.softwaremagico.tm.character.equipment.weapons;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2021 Softwaremagico
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

import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.log.MachineLog;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeaponDamage {
    private static final String NUMBER_EXTRACTOR_PATTERN = "^[^\\d]*(\\d+)";
    private static final String AREA_DAMAGE = "\\((\\d+)\\s*m\\)$";
    private static final Pattern AREA_DAMAGE_PATTERN = Pattern.compile(AREA_DAMAGE);
    private static final String DAMAGE_WITHOUT_AREA = "^(.*?)\\(";
    private static final int SPECIAL_DAMAGE_THREAT = 5;
    private static final Pattern DAMAGE_WITHOUT_AREA_PATTERN = Pattern.compile(DAMAGE_WITHOUT_AREA);
    private static final Pattern FIRST_NUMBER_PATTERN = Pattern.compile(NUMBER_EXTRACTOR_PATTERN);

    private final String name;
    private final String goal;
    private final Integer damageTechLevel;
    private final String damage;
    private final int strength;
    private final String range;
    private final Integer shots;
    private final String rate;

    private final AvailableSkill skill;
    private final CharacteristicDefinition characteristic;

    private transient Integer mainDamage = null;
    private transient Integer areaDamage = null;
    private transient String areaWithoutDamage = null;

    protected WeaponDamage() {
        this(null, null, "", "", 0, "", 0, "", null, null);
    }

    public WeaponDamage(String goal, String damage, Integer strength, String range, Integer shots, String rate,
                        CharacteristicDefinition characteristic, AvailableSkill skill) {
        this(null, null, goal, damage, strength, range, shots, rate, characteristic, skill);
    }

    public WeaponDamage(String name, Integer damageTechLevel, String goal, String damage, Integer strength, String range, Integer shots, String rate,
                        CharacteristicDefinition characteristic, AvailableSkill skill) {
        this.name = name;
        this.goal = goal;
        this.damageTechLevel = damageTechLevel;
        if (strength == null) {
            this.strength = 0;
        } else {
            this.strength = strength;
        }
        this.range = range;
        this.shots = shots;
        this.rate = rate;
        this.damage = damage;
        this.characteristic = characteristic;
        this.skill = skill;
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


    public int getStrength() {
        return strength;
    }

    public String getRange() {
        return range;
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

    public String getStrengthOrRange() {
        if (range == null || Objects.equals(range, "")) {
            return strength + "";
        }
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

    public Integer getDamageTechLevel() {
        return damageTechLevel;
    }

    public String getName() {
        return name;
    }

    public AvailableSkill getSkill() {
        return skill;
    }

    public CharacteristicDefinition getCharacteristic() {
        return characteristic;
    }

    public String getRoll() {
        try {
            return characteristic.getAbbreviature() + "+" + skill.getName();
        } catch (Exception e) {
            return "";
        }
    }
}
