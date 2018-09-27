package com.softwaremagico.tm.random.selectors;

import java.util.Set;

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

public enum CombatPreferences implements IRandomPreferences {
	PEACEFUL(0, 1, 0.2f, 0.1f),

	FAIR(2, 5, 0.4f, 0.6f),

	BELLIGERENT(4, 10, 1f, 1f);

	private final int minimum;
	private final int maximum;

	private final float meleeWeaponProbability;
	private final float rangeWeaponProbability;

	private CombatPreferences(int minimum, int maximum, float meleeWeaponProbability, float rangeWeaponProbability) {
		this.maximum = maximum;
		this.minimum = minimum;
		this.meleeWeaponProbability = meleeWeaponProbability;
		this.rangeWeaponProbability = rangeWeaponProbability;
	}

	@Override
	public int maximum() {
		return maximum;
	}

	@Override
	public int minimum() {
		return minimum;
	}

	public static CombatPreferences getSelected(Set<IRandomPreferences> preferences) {
		for (IRandomPreferences preference : preferences) {
			if (preference instanceof CombatPreferences) {
				return (CombatPreferences) preference;
			}
		}
		return FAIR;
	}

	public float getMeleeWeaponProbability() {
		return meleeWeaponProbability;
	}

	public float getRangeWeaponProbability() {
		return rangeWeaponProbability;
	}
}
