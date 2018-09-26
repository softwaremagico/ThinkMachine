package com.softwaremagico.tm.character.skills;

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
/**
 * A Skill that has been chosen by a player. It has rank values and in some
 * cases a specific generalization.
 */
public class SelectedSkill extends Skill<SelectedSkill> {
	private final int value;
	// Special are represented with a '*' in the character sheet. Does not count
	// for cost calculation.
	private final boolean cost;
	private final AvailableSkill availableSkill;

	public SelectedSkill(AvailableSkill availableSkill, int value, boolean cost) {
		super(availableSkill.getId(), availableSkill.getCompleteName());
		this.availableSkill = availableSkill;
		this.value = value;
		this.cost = cost;
	}

	public int getValue() {
		return value;
	}

	public AvailableSkill getAvailableSkill() {
		return availableSkill;
	}

	public boolean hasCost() {
		return cost;
	}

	@Override
	public String toString() {
		return super.toString() + " (" + value + ")";
	}
}
