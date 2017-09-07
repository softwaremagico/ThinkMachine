package com.softwaremagico.tm.character.equipment;

/*-
 * #%L
 * The Thinking Machine (Core)
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

import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.skills.SkillDefinition;

public class Weapon extends Element<Weapon> {
	private String goal;
	private String damage;
	private int strength;
	private String range;
	private Integer shots;
	private String rate;
	private Size size;
	private int techLevel;
	private String special;
	private Set<DamageType> damageTypes;
	private int cost;
	private SkillDefinition skill;
	private CharacteristicDefinition characteristic;

	public Weapon(String id, String name) {
		super(id, name);
		damageTypes = new HashSet<>();
	}

	public Weapon(String id, String name, String goal, String damage, int strength, String range, Integer shots, String rate, int tech, Size size) {
		this(id, name);
		this.goal = goal;
		this.damage = damage;
		this.strength = strength;
		this.range = range;
		this.shots = shots;
		this.rate = rate;
		this.size = size;
		this.techLevel = tech;
	}

	public Weapon(String id, String name, String goal, String damage, int strength, String range, Integer shots, String rate, int tech, Size size, String special) {
		this(id, name, goal, damage, strength, range, shots, rate, tech, size);
		setSpecial(special);
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

	public void setSpecial(String special) {
		this.special = special;
	}

	public int getTechLevel() {
		return techLevel;
	}

	protected void setGoal(String goal) {
		this.goal = goal;
	}

	protected void setDamage(String damage) {
		this.damage = damage;
	}

	protected void setShots(Integer shots) {
		this.shots = shots;
	}

	protected void setRate(String rate) {
		this.rate = rate;
	}

	protected void setSize(Size size) {
		this.size = size;
	}

	protected void setTechLevel(int techLevel) {
		this.techLevel = techLevel;
	}

	public int getStrength() {
		return strength;
	}

	protected void setStrength(int strength) {
		this.strength = strength;
	}

	public String getRange() {
		return range;
	}

	protected void setRange(String range) {
		this.range = range;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public SkillDefinition getSkill() {
		return skill;
	}

	public void setSkill(SkillDefinition skill) {
		this.skill = skill;
	}

	public CharacteristicDefinition getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(CharacteristicDefinition characteristic) {
		this.characteristic = characteristic;
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

	public void setDamageTypes(Set<DamageType> damageTypes) {
		this.damageTypes = damageTypes;
	}
}
