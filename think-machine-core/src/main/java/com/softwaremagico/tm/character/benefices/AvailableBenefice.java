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

import com.softwaremagico.tm.Element;

public class AvailableBenefice extends Element<AvailableBenefice> {
	private BeneficeSpecialization specialization = null;
	private final BeneficeDefinition benefitDefinition;
	private final int cost;
	private final BeneficeClassification beneficeClassification;

	public AvailableBenefice(String id, String name, BeneficeDefinition benefitDefinition, BeneficeClassification beneficeClassification, int cost) {
		super(id, name);
		this.benefitDefinition = benefitDefinition;
		this.beneficeClassification = beneficeClassification;
		this.cost = cost;
	}

	public BeneficeDefinition getBenefitDefinition() {
		return benefitDefinition;
	}

	public int getCost() {
		if (getBeneficeClassification().equals(BeneficeClassification.AFFLICTION)) {
			return -cost;
		}
		return cost;
	}

	@Override
	public String toString() {
		return getName() + " (" + (benefitDefinition != null && benefitDefinition.getBeneficeClassification() == BeneficeClassification.AFFLICTION ? "+" : "") + cost
				+ ")";
	}

	public BeneficeSpecialization getSpecialization() {
		return specialization;
	}

	public void setSpecialization(BeneficeSpecialization specialization) {
		this.specialization = specialization;
	}

	public BeneficeClassification getBeneficeClassification() {
		return beneficeClassification;
	}

}
