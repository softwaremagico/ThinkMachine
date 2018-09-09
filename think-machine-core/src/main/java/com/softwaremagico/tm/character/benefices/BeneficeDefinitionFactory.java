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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class BeneficeDefinitionFactory extends XmlFactory<BeneficeDefinition> {
	private final static ITranslator translatorBenefit = LanguagePool.getTranslator("benefices.xml");

	private final static String NAME = "name";
	private final static String COST = "cost";
	private final static String GROUP = "group";
	private final static String AFFLICTION = "affliction";
	private final static String SPECIALIZATION_AFFLICTION = "specializationIsAffliction";
	private final static String SPECIALIZABLE_BENEFICE_TAG = "specializations";
	private final static String RESTRICTED_TAG = "restricted";

	private static BeneficeDefinitionFactory instance;

	private Map<BeneficeGroup, Set<BeneficeDefinition>> beneficesByGroup = new HashMap<>();

	private static void createInstance() {
		if (instance == null) {
			synchronized (BeneficeDefinitionFactory.class) {
				if (instance == null) {
					instance = new BeneficeDefinitionFactory();
				}
			}
		}
	}

	@Override
	public void clearCache() {
		beneficesByGroup = new HashMap<>();
		super.clearCache();
	}

	public static BeneficeDefinitionFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorBenefit;
	}

	@Override
	protected BeneficeDefinition createElement(ITranslator translator, String benefitId, String language) throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(benefitId, NAME, language);
			String costRange = translator.getNodeValue(benefitId, COST);

			BeneficeGroup benefitGroup = null;
			String groupName = translator.getNodeValue(benefitId, GROUP);
			if (groupName != null) {
				benefitGroup = BeneficeGroup.get(groupName);
			}

			String afflictionTag = translator.getNodeValue(benefitId, AFFLICTION);
			BeneficeClassification classification = BeneficeClassification.BENEFICE;
			if (afflictionTag != null) {
				if (Boolean.parseBoolean(afflictionTag)) {
					classification = BeneficeClassification.AFFLICTION;
				}
			}

			Set<BeneficeSpecialization> specializations = new HashSet<>();
			for (String specializationId : translator.getAllChildrenTags(benefitId, SPECIALIZABLE_BENEFICE_TAG)) {
				String specizalizationName = translator.getNodeValue(specializationId, NAME, language);
				BeneficeSpecialization specialization = new BeneficeSpecialization(specializationId, specizalizationName);
				specializations.add(specialization);
				// Set specific cost.
				String specizalizationCost = translator.getNodeValue(specializationId, COST);
				if (specizalizationCost != null) {
					try {
						specialization.setCost(Integer.parseInt(specizalizationCost));
					} catch (NumberFormatException e) {
						throw new InvalidBeneficeException("Invalid cost in benefit '" + benefitId + "' and specialization '" + specializationId + "'.", e);
					}
				}
				// Set specific classification.
				String specizalizationClassification = translator.getNodeValue(specializationId, SPECIALIZATION_AFFLICTION);
				BeneficeClassification specializationClassification;
				if (specizalizationClassification != null) {
					specializationClassification = BeneficeClassification.get(specizalizationClassification);
				} else {
					// Copy classfication from main benefice
					specializationClassification = classification;
				}
				specialization.setClassification(specializationClassification);
			}

			List<Integer> costs = new ArrayList<>();
			if (costRange.contains("-")) {
				int minValue = Integer.parseInt(costRange.substring(0, costRange.indexOf('-')));
				int maxValue = Integer.parseInt(costRange.substring(costRange.indexOf('-') + 1, costRange.length()));
				for (int i = minValue; i <= maxValue; i++) {
					costs.add(i);
				}
			} else if (costRange.contains(",")) {
				StringTokenizer costsOfBenefice = new StringTokenizer(costRange, ",");
				while (costsOfBenefice.hasMoreTokens()) {
					costs.add(Integer.parseInt(costsOfBenefice.nextToken().trim()));
				}
			} else {
				costs.add(Integer.parseInt(costRange));
			}

			String restriction = translator.getNodeValue(benefitId, RESTRICTED_TAG);
			FactionGroup restrictedToGroup = null;
			if (restriction != null) {
				restrictedToGroup = FactionGroup.get(restriction);
			}

			BeneficeDefinition benefit = new BeneficeDefinition(benefitId, name, costs, benefitGroup, classification, restrictedToGroup);
			benefit.addSpecializations(specializations);
			return benefit;

		} catch (Exception e) {
			throw new InvalidBeneficeException("Invalid structure in benefit '" + benefitId + "'.", e);
		}
	}

	public Map<BeneficeGroup, Set<BeneficeDefinition>> getBeneficesByGroup(String language) throws InvalidXmlElementException {
		if (beneficesByGroup.isEmpty()) {
			for (BeneficeDefinition benefice : getElements(language)) {
				if (beneficesByGroup.get(benefice.getGroup()) == null) {
					beneficesByGroup.put(benefice.getGroup(), new HashSet<BeneficeDefinition>());
				}
				beneficesByGroup.get(benefice.getGroup()).add(benefice);
			}
		}
		return beneficesByGroup;
	}

	public Set<BeneficeDefinition> getBenefices(BeneficeGroup group, String language) throws InvalidXmlElementException {
		if (group == null) {
			return null;
		}
		return getBeneficesByGroup(language).get(group);
	}
}
