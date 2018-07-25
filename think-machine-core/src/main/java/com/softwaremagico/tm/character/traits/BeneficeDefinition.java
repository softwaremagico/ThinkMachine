package com.softwaremagico.tm.character.traits;

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

public class BeneficeDefinition extends Element<BeneficeDefinition> {
	private final List<Integer> costs;
	private final Set<Specialization> specializations = new HashSet<>();
	private final BeneficeGroup group;
	private final BeneficeClassification classification;

	public BeneficeDefinition(String name, List<Integer> costs, BeneficeGroup group, BeneficeClassification classification) {
		this(null, name, costs, group, classification);
	}

	public BeneficeDefinition(String id, String name, List<Integer> costs, BeneficeGroup group, BeneficeClassification classification) {
		super(id, name);
		this.costs = costs;
		this.group = group;
		this.classification = classification;
	}

	public List<Integer> getCosts() {
		return costs;
	}

	public Set<Specialization> getSpecializations() {
		return specializations;
	}

	public BeneficeGroup getGroup() {
		return group;
	}

	public void addSpecializations(Set<Specialization> specializations) {
		this.specializations.addAll(specializations);
	}

	public BeneficeClassification getClassification() {
		return classification;
	}

}
