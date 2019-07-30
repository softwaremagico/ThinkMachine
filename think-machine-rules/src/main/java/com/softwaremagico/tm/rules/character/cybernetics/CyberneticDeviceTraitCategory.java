package com.softwaremagico.tm.rules.character.cybernetics;

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

public enum CyberneticDeviceTraitCategory {
	PROSCRIBED(10), ATTACHED(4), POWER(5), VISIBILITY(9), MATERIAL(3), QUALITY(8), USABILITY(6), TRAIT(7);
	private final int preference;

	private CyberneticDeviceTraitCategory(int preference) {
		this.preference = preference;
	}

	public static CyberneticDeviceTraitCategory get(String tag) {
		for (final CyberneticDeviceTraitCategory category : CyberneticDeviceTraitCategory.values()) {
			if (category.name().equalsIgnoreCase(tag)) {
				return category;
			}
		}
		return null;
	}

	public int getPreference() {
		return preference;
	}
}
