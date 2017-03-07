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

import com.softwaremagico.tm.Element;

public class Weapon extends Element<Weapon> {
	private final String roll;
	private final Integer goal;
	private final int damage;
	private final String strengthOrRange;
	private final Integer shots;
	private final String rate;
	private final Size size;
	private final int tech;
	private String others;

	public Weapon(String name, String roll, Integer goal, int damage, String strengthOrRange, Integer shots, String rate, int tech,
			Size size) {
		super(name);
		this.roll = roll;
		this.goal = goal;
		this.damage = damage;
		this.strengthOrRange = strengthOrRange;
		this.shots = shots;
		this.rate = rate;
		this.size = size;
		this.tech = tech;
	}

	public Weapon(String name, String roll, Integer goal, int damage, String strengthOrRange, Integer shots, String rate, int tech,
			Size size, String others) {
		this(name, roll, goal, damage, strengthOrRange, shots, rate, tech, size);
		setOthers(others);
	}

	public String getRoll() {
		return roll;
	}

	public Integer getGoal() {
		return goal;
	}

	public int getDamage() {
		return damage;
	}

	public String getStrengthOrRange() {
		return strengthOrRange;
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

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public int getTech() {
		return tech;
	}
}
