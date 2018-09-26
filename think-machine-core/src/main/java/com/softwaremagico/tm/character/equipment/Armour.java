package com.softwaremagico.tm.character.equipment;

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

import com.softwaremagico.tm.Element;

public class Armour extends Element<Element<Armour>> {
	private final int protection;
	private final boolean hard;
	private final boolean fire;
	private final boolean laser;
	private final boolean plasma;
	private final boolean impact;
	private final boolean shock;
	private final boolean electrical;
	private final int techLevel;

	private final int dexterityBonus;
	private final int strengthBonus;
	private final int initiativeBonus;
	private final int enduranceBonus;

	private final int cost;

	public Armour(String name, int protection, boolean hard, boolean fire, boolean laser, boolean plasma, boolean impact, boolean shock, boolean electrical,
			int techLevel, int dexterityBonus, int strengthBonus, int initiativeBonus, int enduranceBonus, int cost) {
		super(null, name);
		this.protection = protection;
		this.hard = hard;
		this.fire = fire;
		this.laser = laser;
		this.plasma = plasma;
		this.impact = impact;
		this.shock = shock;
		this.electrical = electrical;
		this.techLevel = techLevel;
		this.dexterityBonus = dexterityBonus;
		this.strengthBonus = strengthBonus;
		this.initiativeBonus = initiativeBonus;
		this.enduranceBonus = enduranceBonus;
		this.cost = cost;
	}

	public int getProtection() {
		return protection;
	}

	public boolean isHard() {
		return hard;
	}

	public boolean isFire() {
		return fire;
	}

	public boolean isPlasma() {
		return plasma;
	}

	public boolean isImpact() {
		return impact;
	}

	public boolean isShock() {
		return shock;
	}

	public int getTechLevel() {
		return techLevel;
	}

	public boolean isElectrical() {
		return electrical;
	}

	public int getDexterityBonus() {
		return dexterityBonus;
	}

	public int getStrengthBonus() {
		return strengthBonus;
	}

	public int getInitiativeBonus() {
		return initiativeBonus;
	}

	public int getEnduranceBonus() {
		return enduranceBonus;
	}

	public boolean isLaser() {
		return laser;
	}

	public int getCost() {
		return cost;
	}

}
