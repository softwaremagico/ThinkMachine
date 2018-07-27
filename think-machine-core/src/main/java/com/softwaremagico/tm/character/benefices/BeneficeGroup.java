package com.softwaremagico.tm.character.benefices;

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

public enum BeneficeGroup {
	BACKGROUND, COMMUNITY, TECHNOLOGY, RELICS, POSSESSIONS, RICHES, STATUS, FIGHTING;

	public static BeneficeGroup get(String tag) {
		if (tag != null) {
			for (BeneficeGroup benefitGroup : BeneficeGroup.values()) {
				if (benefitGroup.name().equalsIgnoreCase(tag)) {
					return benefitGroup;
				}
			}
		}
		return null;
	}
}
