package com.softwaremagico.tm.character.benefices;

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
import java.util.List;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.factions.FactionGroup;

public class BeneficeDefinition extends Element<BeneficeDefinition> {
	private final List<Integer> costs;
	private final Set<BeneficeSpecialization> specializations = new HashSet<>();
	private final BeneficeGroup group;
	private final BeneficeClassification beneficeClassification;
	private final FactionGroup restrictedFactionGroup;

	public BeneficeDefinition(String id, String name, String language, String moduleName, List<Integer> costs, BeneficeGroup group,
			BeneficeClassification beneficeClassification, FactionGroup restricted) {
		super(id, name, language, moduleName);
		this.costs = costs;
		this.group = group;
		this.beneficeClassification = beneficeClassification;
		this.restrictedFactionGroup = restricted;
	}

	public List<Integer> getCosts() {
		return costs;
	}

	public Set<BeneficeSpecialization> getSpecializations() {
		return specializations;
	}

	public BeneficeGroup getGroup() {
		return group;
	}

	public void addSpecializations(Set<BeneficeSpecialization> specializations) {
		this.specializations.addAll(specializations);
	}

	protected BeneficeClassification getBeneficeClassification() {
		return beneficeClassification;
	}

	public FactionGroup getRestrictedFactionGroup() {
		return restrictedFactionGroup;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

}
