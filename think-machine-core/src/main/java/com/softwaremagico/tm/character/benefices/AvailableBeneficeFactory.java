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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;

public class AvailableBeneficeFactory {
	private Map<String, Map<String, AvailableBenefice>> availableBenefices;
	private Map<String, Map<BeneficeDefinition, Set<AvailableBenefice>>> availableBeneficesByDefinition;

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
		availableBeneficesByDefinition = new HashMap<>();
	}

	public Collection<AvailableBenefice> getElements(String language) throws InvalidXmlElementException {
		if (availableBenefices.get(language) == null) {
			for (BeneficeDefinition benefitDefinition : BeneficeDefinitionFactory.getInstance().getElements(language)) {
				if (benefitDefinition.getSpecializations().isEmpty()) {
					for (Integer cost : benefitDefinition.getCosts()) {
						String id = benefitDefinition.getId() + (benefitDefinition.getCosts().size() == 1 ? "" : "_" + cost);
						AvailableBenefice availableBenefice = new AvailableBenefice(id, benefitDefinition.getName(), benefitDefinition,
								benefitDefinition.getBeneficeClassification(), cost);
						addAvailableBenefice(language, id, benefitDefinition, availableBenefice);
					}
				} else {
					for (BeneficeSpecialization specialization : benefitDefinition.getSpecializations()) {
						// Cost in specialization
						if (specialization.getCost() != null) {
							String id = benefitDefinition.getId() + " [" + specialization.getId() + "]";
							AvailableBenefice availableBenefice = new AvailableBenefice(id, specialization.getName(), benefitDefinition,
									specialization.getClassification(), specialization.getCost());
							availableBenefice.setSpecialization(specialization);
							addAvailableBenefice(language, id, benefitDefinition, availableBenefice);
						} else {
							for (Integer cost : benefitDefinition.getCosts()) {
								String id = benefitDefinition.getId() + (benefitDefinition.getCosts().size() == 1 ? "" : "_" + cost) + " ["
										+ specialization.getId() + "]";
								AvailableBenefice availableBenefice = new AvailableBenefice(id, specialization.getName(), benefitDefinition,
										specialization.getClassification(), cost);
								availableBenefice.setSpecialization(specialization);
								addAvailableBenefice(language, id, benefitDefinition, availableBenefice);
							}
						}
					}
				}
			}
		}
		return availableBenefices.get(language).values();
	}

	private void addAvailableBenefice(String language, String id, BeneficeDefinition beneficeDefinition, AvailableBenefice availableBenefice) {
		if (availableBenefices.get(language) == null) {
			availableBenefices.put(language, new HashMap<String, AvailableBenefice>());
		}
		availableBenefices.get(language).put(id, availableBenefice);

		if (availableBeneficesByDefinition.get(language) == null) {
			availableBeneficesByDefinition.put(language, new HashMap<BeneficeDefinition, Set<AvailableBenefice>>());
		}
		if (availableBeneficesByDefinition.get(language).get(beneficeDefinition) == null) {
			availableBeneficesByDefinition.get(language).put(beneficeDefinition, new HashSet<AvailableBenefice>());
		}
		availableBeneficesByDefinition.get(language).get(beneficeDefinition).add(availableBenefice);
	}

	public AvailableBenefice getElement(String beneficeId, String language) throws InvalidXmlElementException {
		if (availableBenefices.get(language) == null) {
			getElements(language);
		}
		AvailableBenefice avilableBenefice = availableBenefices.get(language).get(beneficeId);
		if (avilableBenefice == null) {
			throw new InvalidBeneficeException("The benefice '" + beneficeId + "' does not exists.");
		}
		return avilableBenefice;
	}

	public Set<AvailableBenefice> getAvailableBeneficesByDefinition(String language, BeneficeDefinition beneficeDefinition) throws InvalidXmlElementException {
		if (language == null) {
			return null;
		}
		// Force the load.
		if (availableBeneficesByDefinition.get(language) == null) {
			getElements(language);
		}
		return availableBeneficesByDefinition.get(language).get(beneficeDefinition);
	}

	public Map<BeneficeDefinition, Set<AvailableBenefice>> getAvailableBeneficesByDefinition(String language) throws InvalidXmlElementException {
		if (availableBeneficesByDefinition == null || availableBeneficesByDefinition.get(language) == null) {
			getElements(language);
		}
		return availableBeneficesByDefinition.get(language);
	}
}
