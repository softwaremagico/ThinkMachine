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

import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.Size;

public class Ammunition extends Element<Ammunition> {
	private final String goal;
	private final String damage;
	private final Integer strength;
	private final String range;
	private final Integer cost;
	private final Size size;

	private final Set<DamageType> damageTypes;
	private final Set<Accessory> accesories;

	public Ammunition(String id, String name, String language, String goal, String damage, Integer strength, String range, Size size, Integer cost,
			Set<DamageType> damageTypes, Set<Accessory> accesories) {
		super(id, name, language);
		this.goal = goal;
		this.damage = damage;
		this.strength = strength;
		this.range = range;
		this.cost = cost;
		this.size = size;
		this.damageTypes = damageTypes;
		this.accesories = accesories;
	}

	public String getGoal() {
		return goal;
	}

	public String getDamage() {
		return damage;
	}

	public Integer getStrength() {
		return strength;
	}

	public String getRange() {
		return range;
	}

	public String getStrengthOrRange() {
		if (range == null) {
			if (strength != null) {
				return strength + "";
			}
			return "--";
		}
		return range;
	}

	public Integer getCost() {
		return cost;
	}

	public Set<Accessory> getAccesories() {
		return accesories;
	}

	public Set<DamageType> getDamageTypes() {
		return damageTypes;
	}

	public Size getSize() {
		return size;
	}

}
