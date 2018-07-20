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

public enum SpecializationPreferences implements IRandomPreferences {

	VERY_GENERALIZED(1, 3),

	GENERALIZED(1, 5),

	FAIR(2, 6),

	SPECIALIZED(4, 8),

	VERY_SPECIALIZED(5, 10);

	private final int minimumValue;
	private final int maximumValue;

	private SpecializationPreferences(int minimumValue, int maximumValue) {
		this.maximumValue = maximumValue;
		this.minimumValue = minimumValue;
	}

	@Override
	public int maximumValue() {
		return maximumValue;
	}

	@Override
	public int minimumValue() {
		return minimumValue;
	}

	public static SpecializationPreferences getSelected(Set<IRandomPreferences> preferences) {
		for (IRandomPreferences preference : preferences) {
			if (preference instanceof SpecializationPreferences) {
				return (SpecializationPreferences) preference;
			}
		}
		return null;
	}

}
