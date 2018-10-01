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

import com.softwaremagico.tm.Element;

public class Ammunition extends Element<Ammunition> {
	private final String goal;
	private final String damage;
	private final int strength;
	private final String range;
	private final Integer cost;

	public Ammunition(String id, String name, String goal, String damage, int strength, String range, Integer cost) {
		super(id, name);
		this.goal = goal;
		this.damage = damage;
		this.strength = strength;
		this.range = range;
		this.cost = cost;
	}

	public String getGoal() {
		return goal;
	}

	public String getDamage() {
		return damage;
	}

	public int getStrength() {
		return strength;
	}

	public String getRange() {
		return range;
	}

	public String getStrengthOrRange() {
		if (range == null) {
			return strength + "";
		}
		return range;
	}

	public Integer getCost() {
		return cost;
	}

}
