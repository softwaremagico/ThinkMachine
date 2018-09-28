package com.softwaremagico.tm.random.selectors;

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

public enum WeaponsPreferences implements IRandomPreferences {

	NONE(0f, 0f),

	LOW(0.2f, 0.1f),

	MEDIUM(0.4f, 0.6f),

	HIGH(1f, 1f),

	WARLORD(2f, 2f);

	private final float meleeWeaponProbability;
	private final float rangeWeaponProbability;

	private WeaponsPreferences(float meleeWeaponProbability, float rangeWeaponProbability) {
		this.meleeWeaponProbability = meleeWeaponProbability;
		this.rangeWeaponProbability = rangeWeaponProbability;
	}

	public static WeaponsPreferences getSelected(Set<IRandomPreferences> preferences) {
		for (IRandomPreferences preference : preferences) {
			if (preference instanceof WeaponsPreferences) {
				return (WeaponsPreferences) preference;
			}
		}
		return null;
	}

	public float getMeleeWeaponProbability() {
		return meleeWeaponProbability;
	}

	public float getRangeWeaponProbability() {
		return rangeWeaponProbability;
	}

	@Override
	public int maximum() {
		return 0;
	}

	@Override
	public int minimum() {
		return 0;
	}
}
