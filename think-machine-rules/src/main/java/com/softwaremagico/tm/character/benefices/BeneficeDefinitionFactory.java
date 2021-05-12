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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.log.SuppressFBWarnings;

import java.util.*;

public class BeneficeDefinitionFactory extends XmlFactory<BeneficeDefinition> {
    private static final String TRANSLATOR_FILE = "benefices.xml";

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String COST = "cost";
    private static final String GROUP = "group";
    private static final String AFFLICTION = "affliction";
    private static final String SPECIALIZATION_AFFLICTION = "specializationIsAffliction";
    private static final String SPECIALIZABLE_BENEFICE_TAG = "specializations";
    private static final String INCOMPATIBLE_WITH = "incompatibleWith";

    private Map<BeneficeGroup, Set<BeneficeDefinition>> beneficesByGroup = new HashMap<>();

    private static class BeneficeDefinitionFactoryInit {
        public static final BeneficeDefinitionFactory INSTANCE = new BeneficeDefinitionFactory();
    }

    public static BeneficeDefinitionFactory getInstance() {
        return BeneficeDefinitionFactoryInit.INSTANCE;
    }

    @Override
    public void refreshCache() {
        beneficesByGroup = new HashMap<>();
        super.refreshCache();
    }

    @Override
    @SuppressFBWarnings("REC_CATCH_EXCEPTION")
    protected BeneficeDefinition createElement(ITranslator translator, String benefitId, String name, String description,
                                               String language, String moduleName) throws InvalidXmlElementException {
        try {
            final String costRange = translator.getNodeValue(benefitId, COST);

            BeneficeGroup benefitGroup = null;
            final String groupName = translator.getNodeValue(benefitId, GROUP);
            if (groupName != null) {
                benefitGroup = BeneficeGroup.get(groupName);
            }

            final String afflictionTag = translator.getNodeValue(benefitId, AFFLICTION);
            BeneficeClassification classification = BeneficeClassification.BENEFICE;
            if (afflictionTag != null) {
                if (Boolean.parseBoolean(afflictionTag)) {
                    classification = BeneficeClassification.AFFLICTION;
                }
            }

            final Set<BeneficeSpecialization> specializations = new HashSet<>();
            for (final String specializationId : translator.getAllChildrenTags(benefitId, SPECIALIZABLE_BENEFICE_TAG)) {
                final String specializationName = translator.getNodeValue(specializationId, NAME, language);
                final String specializationDescription = translator.getNodeValue(specializationId, DESCRIPTION, language);

                final Set<String> incompatibleWith = new HashSet<>();
                try {
                    final String incompatibleWithValues = translator.getNodeValue(specializationId, INCOMPATIBLE_WITH);
                    if (incompatibleWithValues != null) {
                        final StringTokenizer incompatibleWithValuesTokenizer = new StringTokenizer(incompatibleWithValues, ",");
                        while (incompatibleWithValuesTokenizer.hasMoreTokens()) {
                            incompatibleWith.add(incompatibleWithValuesTokenizer.nextToken().trim());
                        }
                    }
                } catch (NullPointerException npe) {
                    // Optional
                }

                final BeneficeSpecialization specialization = new BeneficeSpecialization(specializationId,
                        specializationName, specializationDescription != null ? specializationDescription : description,
                        language, moduleName, incompatibleWith);
                specializations.add(specialization);
                // Set random option.
                setRandomConfiguration(specialization, translator, language, moduleName);

                // Set specific cost.
                final String specizalizationCost = translator.getNodeValue(specializationId, COST);
                if (specizalizationCost != null) {
                    try {
                        specialization.setCost(Integer.parseInt(specizalizationCost));
                    } catch (NumberFormatException e) {
                        throw new InvalidBeneficeException("Invalid cost in benefit '" + benefitId
                                + "' and specialization '" + specializationId + "'.", e);
                    }
                }
                // Set specific classification.
                final String specializationClassificationValue = translator.getNodeValue(specializationId,
                        SPECIALIZATION_AFFLICTION);
                BeneficeClassification specializationClassification = BeneficeClassification.BENEFICE;
                if (specializationClassificationValue != null) {
                    if (Boolean.parseBoolean(specializationClassificationValue)) {
                        specializationClassification = BeneficeClassification.AFFLICTION;
                    }
                } else {
                    // Copy classification from main benefice
                    specializationClassification = classification;
                }
                specialization.setClassification(specializationClassification);
            }

            final List<Integer> costs = new ArrayList<>();
            if (costRange.contains("-")) {
                final int minValue = Integer.parseInt(costRange.substring(0, costRange.indexOf('-')));
                final int maxValue = Integer.parseInt(costRange.substring(costRange.indexOf('-') + 1));
                for (int i = minValue; i <= maxValue; i++) {
                    costs.add(i);
                }
            } else if (costRange.contains(",")) {
                final StringTokenizer costsOfBenefice = new StringTokenizer(costRange, ",");
                while (costsOfBenefice.hasMoreTokens()) {
                    costs.add(Integer.parseInt(costsOfBenefice.nextToken().trim()));
                }
            } else {
                costs.add(Integer.parseInt(costRange));
            }

            final Set<String> incompatibleWith = new HashSet<>();
            try {
                final String incompatibleWithValues = translator.getNodeValue(benefitId, INCOMPATIBLE_WITH);
                if (incompatibleWithValues != null) {
                    final StringTokenizer incompatibleWithValuesTokenizer = new StringTokenizer(incompatibleWithValues, ",");
                    while (incompatibleWithValuesTokenizer.hasMoreTokens()) {
                        incompatibleWith.add(incompatibleWithValuesTokenizer.nextToken().trim());
                    }
                }
            } catch (NullPointerException npe) {
                // Optional
            }

            final BeneficeDefinition benefice = new BeneficeDefinition(benefitId, name, description, language, moduleName, costs,
                    benefitGroup, classification, incompatibleWith);
            benefice.addSpecializations(specializations);
            return benefice;

        } catch (Exception e) {
            throw new InvalidBeneficeException("Invalid structure in benefit '" + benefitId + "'.", e);
        }
    }

    public Map<BeneficeGroup, Set<BeneficeDefinition>> getBeneficesByGroup(String language, String moduleName)
            throws InvalidXmlElementException {
        if (beneficesByGroup.isEmpty()) {
            for (final BeneficeDefinition benefice : getElements(language, moduleName)) {
                beneficesByGroup.computeIfAbsent(benefice.getGroup(), k -> new HashSet<>());
                beneficesByGroup.get(benefice.getGroup()).add(benefice);
            }
        }
        return beneficesByGroup;
    }

    public Set<BeneficeDefinition> getBenefices(BeneficeGroup group, String language, String moduleName)
            throws InvalidXmlElementException {
        if (group == null) {
            return null;
        }
        return getBeneficesByGroup(language, moduleName).get(group);
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    public FactoryCacheLoader<BeneficeDefinition> getFactoryCacheLoader() {
        return null;
    }
}
