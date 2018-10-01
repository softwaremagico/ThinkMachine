package com.softwaremagico.tm.character.equipment.weapons;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.skills.SkillDefinition;

public class Weapon extends Element<Weapon> {
	private final String goal;
	private final String damage;
	private final int strength;
	private final String range;
	private final Integer shots;
	private final String rate;
	private final Size size;
	private final int techLevel;
	private final boolean techLevelSpecial;

	private final String special;
	private final Set<DamageType> damageTypes;
	private final int cost;
	private final SkillDefinition skill;
	private final CharacteristicDefinition characteristic;
	private final WeaponType type;

	private final Set<Ammunition> ammunitions;
	private final Set<Accessory> accesories;

	private final Faction faction;

	public WeaponType getType() {
		return type;
	}

	public Weapon(String id, String name, WeaponType type, String goal, CharacteristicDefinition characteristic, SkillDefinition skill, String damage,
			int strength, String range, Integer shots, String rate, int tech, boolean techLevelSpecial, Size size, String special, Set<DamageType> damageTypes,
			int cost, Set<Ammunition> ammunitions, Set<Accessory> accesories, Faction faction) {
		super(id, name);
		this.characteristic = characteristic;
		this.skill = skill;
		this.goal = goal;
		this.damage = damage;
		this.strength = strength;
		this.range = range;
		this.shots = shots;
		this.rate = rate;
		this.size = size;
		this.techLevel = tech;
		this.techLevelSpecial = techLevelSpecial;
		this.type = type;
		this.special = special;
		this.damageTypes = damageTypes;
		this.cost = cost;
		this.ammunitions = ammunitions;
		this.faction = faction;
		this.accesories = accesories;
	}

	public String getRoll() {
		try {
			return characteristic.getAbbreviature() + "+" + skill.getName();
		} catch (Exception e) {
			return "";
		}
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

	public Integer getShots() {
		return shots;
	}

	public String getRate() {
		return rate;
	}

	public Size getSize() {
		return size;
	}

	public String getSpecial() {
		return special;
	}

	public int getTechLevel() {
		return techLevel;
	}

	public int getStrength() {
		return strength;
	}

	public String getRange() {
		return range;
	}

	public int getCost() {
		return cost;
	}

	public SkillDefinition getSkill() {
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

	public Faction getFaction() {
		return faction;
	}

	public Set<Accessory> getAccesories() {
		return accesories;
	}

}
