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

public enum CombatPreferences implements IRandomPreference {
	PEACEFUL(0, 1, WeaponsPreferences.LOW),

	FAIR(2, 5, WeaponsPreferences.MEDIUM),

	BELLIGERENT(4, 10, WeaponsPreferences.HIGH);

	private final int minimum;
	private final int maximum;

	private final WeaponsPreferences defaultWeaponPreferences;

	private CombatPreferences(int minimum, int maximum, WeaponsPreferences defaultWeaponPreferences) {
		this.maximum = maximum;
		this.minimum = minimum;
		this.defaultWeaponPreferences = defaultWeaponPreferences;
	}

	@Override
	public int maximum() {
		return maximum;
	}

	@Override
	public int minimum() {
		return minimum;
	}

	public static CombatPreferences getSelected(Set<IRandomPreference> preferences) {
		for (IRandomPreference preference : preferences) {
			if (preference instanceof CombatPreferences) {
				return (CombatPreferences) preference;
			}
		}
		return FAIR;
	}

	public WeaponsPreferences getDefaultWeaponPreferences() {
		return defaultWeaponPreferences;
	}
}
