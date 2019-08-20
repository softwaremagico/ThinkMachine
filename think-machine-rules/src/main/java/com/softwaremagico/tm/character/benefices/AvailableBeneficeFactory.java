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
import com.softwaremagico.tm.random.definition.RandomElementDefinition;

public class AvailableBeneficeFactory {
	private Map<String, Map<String, Map<String, AvailableBenefice>>> availableBenefices;
	private Map<String, Map<String, Map<BeneficeDefinition, Set<AvailableBenefice>>>> availableBeneficesByDefinition;

	private AvailableBeneficeFactory() {
		clearCache();
	}

	private static class AvailableBeneficeFactoryInit {
		public static final AvailableBeneficeFactory INSTANCE = new AvailableBeneficeFactory();
	}

	public static AvailableBeneficeFactory getInstance() {
		return AvailableBeneficeFactoryInit.INSTANCE;
	}

	public void clearCache() {
		availableBenefices = new HashMap<>();
		availableBeneficesByDefinition = new HashMap<>();
	}

	public Collection<AvailableBenefice> getElements(String language, String moduleName)
			throws InvalidXmlElementException {
		if (availableBenefices.get(language) == null) {
			for (final BeneficeDefinition benefitDefinition : BeneficeDefinitionFactory.getInstance().getElements(
					language, moduleName)) {
				if (benefitDefinition.getSpecializations().isEmpty()) {
					for (final Integer cost : benefitDefinition.getCosts()) {
						final String id = benefitDefinition.getId()
								+ (benefitDefinition.getCosts().size() == 1 ? "" : "_" + cost);
						final AvailableBenefice availableBenefice = new AvailableBenefice(id,
								benefitDefinition.getName(), language, benefitDefinition,
								benefitDefinition.getBeneficeClassification(), cost,
								benefitDefinition.getRandomDefinition());
						addAvailableBenefice(language, moduleName, id, benefitDefinition, availableBenefice);
					}
				} else {
					for (final BeneficeSpecialization specialization : benefitDefinition.getSpecializations()) {
						// Cost in specialization
						if (specialization.getCost() != null) {
							final String id = benefitDefinition.getId() + " [" + specialization.getId() + "]";
							final AvailableBenefice availableBenefice = new AvailableBenefice(id,
									specialization.getName(), language, benefitDefinition,
									specialization.getClassification(), specialization.getCost(),
									new RandomElementDefinition(benefitDefinition.getRandomDefinition(), specialization
											.getRandomDefinition()));
							availableBenefice.setSpecialization(specialization);
							addAvailableBenefice(language, moduleName, id, benefitDefinition, availableBenefice);
						} else {
							for (final Integer cost : benefitDefinition.getCosts()) {
								final String id = benefitDefinition.getId()
										+ (benefitDefinition.getCosts().size() == 1 ? "" : "_" + cost) + " ["
										+ specialization.getId() + "]";
								final AvailableBenefice availableBenefice = new AvailableBenefice(id,
										specialization.getName(), language, benefitDefinition,
										specialization.getClassification(), cost, new RandomElementDefinition(
												benefitDefinition.getRandomDefinition(),
												specialization.getRandomDefinition()));
								availableBenefice.setSpecialization(specialization);
								addAvailableBenefice(language, moduleName, id, benefitDefinition, availableBenefice);
							}
						}
					}
				}
			}
		}
		return availableBenefices.get(language).get(moduleName).values();
	}

	private void addAvailableBenefice(String language, String moduleName, String id,
			BeneficeDefinition beneficeDefinition, AvailableBenefice availableBenefice) {
		if (availableBenefices.get(language) == null) {
			availableBenefices.put(language, new HashMap<String, Map<String, AvailableBenefice>>());
		}

		if (availableBenefices.get(language).get(moduleName) == null) {
			availableBenefices.get(language).put(moduleName, new HashMap<String, AvailableBenefice>());
		}

		availableBenefices.get(language).get(moduleName).put(id, availableBenefice);

		if (availableBeneficesByDefinition.get(language) == null) {
			availableBeneficesByDefinition.put(language,
					new HashMap<String, Map<BeneficeDefinition, Set<AvailableBenefice>>>());
		}
		if (availableBeneficesByDefinition.get(language).get(moduleName) == null) {
			availableBeneficesByDefinition.get(language).put(moduleName,
					new HashMap<BeneficeDefinition, Set<AvailableBenefice>>());
		}
		if (availableBeneficesByDefinition.get(language).get(moduleName).get(beneficeDefinition) == null) {
			availableBeneficesByDefinition.get(language).get(moduleName)
					.put(beneficeDefinition, new HashSet<AvailableBenefice>());
		}
		availableBeneficesByDefinition.get(language).get(moduleName).get(beneficeDefinition).add(availableBenefice);
	}

	public AvailableBenefice getElement(String beneficeId, String language, String moduleName)
			throws InvalidXmlElementException {
		if (availableBenefices.get(language) == null || availableBenefices.get(language).isEmpty()) {
			getElements(language, moduleName);
		}
		final AvailableBenefice avilableBenefice = availableBenefices.get(language).get(moduleName).get(beneficeId);
		if (avilableBenefice == null) {
			throw new InvalidBeneficeException("The benefice '" + beneficeId + "' does not exists.");
		}
		return avilableBenefice;
	}

	public Set<AvailableBenefice> getAvailableBeneficesByDefinition(String language, String moduleName,
			BeneficeDefinition beneficeDefinition) throws InvalidXmlElementException {
		if (language == null) {
			return null;
		}
		// Force the load.
		if (availableBeneficesByDefinition.get(language) == null
				|| availableBeneficesByDefinition.get(language).isEmpty()) {
			getElements(language, moduleName);
		}
		return availableBeneficesByDefinition.get(language).get(moduleName).get(beneficeDefinition);
	}

	public Map<BeneficeDefinition, Set<AvailableBenefice>> getAvailableBeneficesByDefinition(String language,
			String moduleName) throws InvalidXmlElementException {
		if (availableBeneficesByDefinition == null || availableBeneficesByDefinition.get(language) == null) {
			getElements(language, moduleName);
		}
		return availableBeneficesByDefinition.get(language).get(moduleName);
	}
}
