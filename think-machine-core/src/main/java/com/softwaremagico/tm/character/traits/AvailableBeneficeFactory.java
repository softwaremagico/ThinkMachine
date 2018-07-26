package com.softwaremagico.tm.character.traits;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.softwaremagico.tm.InvalidXmlElementException;

public class AvailableBeneficeFactory {
	private Map<String, Map<String, AvailableBenefice>> availableBenefices;

	private static AvailableBeneficeFactory instance;

	private AvailableBeneficeFactory() {
		clearCache();
	}

	private static void createInstance() {
		if (instance == null) {
			synchronized (AvailableBeneficeFactory.class) {
				if (instance == null) {
					instance = new AvailableBeneficeFactory();
				}
			}
		}
	}

	public static AvailableBeneficeFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	public void clearCache() {
		availableBenefices = new HashMap<>();
	}

	public Collection<AvailableBenefice> getElements(String language) throws InvalidXmlElementException {
		if (availableBenefices.get(language) == null) {
			availableBenefices.put(language, new HashMap<String, AvailableBenefice>());
			for (BeneficeDefinition benefitDefinition : BeneficeDefinitionFactory.getInstance().getElements(language)) {
				if (benefitDefinition.getSpecializations().isEmpty()) {
					for (Integer cost : benefitDefinition.getCosts()) {
						String id = benefitDefinition.getId() + (benefitDefinition.getCosts().size() == 1 ? "" : "_" + cost);
						AvailableBenefice availableBenefice = new AvailableBenefice(id, benefitDefinition.getName(), benefitDefinition, cost);
						availableBenefices.get(language).put(id, availableBenefice);
					}
				} else {
					for (Specialization specialization : benefitDefinition.getSpecializations()) {
						// Cost in specialization
						if (specialization.getCost() != null) {
							String id = benefitDefinition.getId() + " [" + specialization.getId() + "]";
							AvailableBenefice availableBenefice = new AvailableBenefice(id, benefitDefinition.getName(), benefitDefinition,
									specialization.getCost());
							availableBenefice.setSpecialization(specialization);
							availableBenefices.get(language).put(id, availableBenefice);
						} else {
							for (Integer cost : benefitDefinition.getCosts()) {
								String id = benefitDefinition.getId() + (benefitDefinition.getCosts().size() == 1 ? "" : "_" + cost) + " ["
										+ specialization.getId() + "]";
								AvailableBenefice availableBenefice = new AvailableBenefice(id, benefitDefinition.getName(), benefitDefinition, cost);
								availableBenefice.setSpecialization(specialization);
								availableBenefices.get(language).put(id, availableBenefice);
							}
						}
					}
				}
			}
		}
		return availableBenefices.get(language).values();
	}

	public AvailableBenefice getElement(String beneficeId, String language) throws InvalidXmlElementException {
		if (availableBenefices.get(language) == null) {
			getElements(language);
		}
		AvailableBenefice avilableBenefice = availableBenefices.get(language).get(beneficeId);
		if (avilableBenefice == null) {
			throw new InvalidBlessingException("The benefice '" + beneficeId + "' does not exists.");
		}
		return avilableBenefice;
	}
}
