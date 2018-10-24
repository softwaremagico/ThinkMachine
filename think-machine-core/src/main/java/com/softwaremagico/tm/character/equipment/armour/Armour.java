package com.softwaremagico.tm.character.equipment.armour;

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

import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.shield.Shield;

public class Armour extends Element<Armour> {
	private final int protection;
	private final Set<DamageType> damageTypes;
	private final int techLevel;
	private final ArmourPenalization standardPenalizations;
	private final ArmourPenalization specialPenalizations;
	private final float cost;
	private final Set<Shield> allowedShields;

	public Armour(String id, String name, String language, int techLevel, int protection, Set<DamageType> damageTypes, float cost) {
		super(id, name, language);
		this.protection = protection;
		this.damageTypes = damageTypes;
		this.techLevel = techLevel;
		this.standardPenalizations = new ArmourPenalization(0, 0, 0, 0);
		this.specialPenalizations = new ArmourPenalization(0, 0, 0, 0);
		this.allowedShields = new HashSet<>();
		this.cost = cost;
	}

	public Armour(String id, String name, String language, int techLevel, int protection, Set<DamageType> damageTypes, ArmourPenalization specialPenalizations,
			ArmourPenalization otherPenalizations, Set<Shield> allowedShields, float cost) {
		super(id, name, language);
		this.protection = protection;
		this.damageTypes = damageTypes;
		this.techLevel = techLevel;
		this.standardPenalizations = specialPenalizations;
		this.specialPenalizations = otherPenalizations;
		this.allowedShields = allowedShields;
		this.cost = cost;
	}

	public int getProtection() {
		return protection;
	}

	public int getTechLevel() {
		return techLevel;
	}

	public float getCost() {
		return cost;
	}

	public Set<DamageType> getDamageTypes() {
		return damageTypes;
	}

	public ArmourPenalization getStandardPenalizations() {
		return standardPenalizations;
	}

	public ArmourPenalization getSpecialPenalizations() {
		return specialPenalizations;
	}

	public Set<Shield> getAllowedShields() {
		return allowedShields;
	}

	public boolean isHeavy() {
		return standardPenalizations.getDexterityModification() > 0 || standardPenalizations.getStrengthModification() > 0
				|| standardPenalizations.getEnduranceModification() > 0;
	}
}
