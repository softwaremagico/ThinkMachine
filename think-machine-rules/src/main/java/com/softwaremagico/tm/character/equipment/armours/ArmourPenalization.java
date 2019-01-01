package com.softwaremagico.tm.character.equipment.armours;

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

public class ArmourPenalization {
	private final int dexterityModification;
	private final int strengthModification;
	private final int initiativeModification;
	private final int enduranceModification;

	public ArmourPenalization(int dexterityModification, int strengthModification, int initiativeModification, int enduranceModification) {
		this.dexterityModification = dexterityModification;
		this.strengthModification = strengthModification;
		this.initiativeModification = initiativeModification;
		this.enduranceModification = enduranceModification;
	}

	public int getEnduranceModification() {
		return enduranceModification;
	}

	public int getDexterityModification() {
		return dexterityModification;
	}

	public int getStrengthModification() {
		return strengthModification;
	}

	public int getInitiativeModification() {
		return initiativeModification;
	}

}
