package com.softwaremagico.tm.character.benefices;

import com.softwaremagico.tm.IElementRetriever;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.random.definition.RandomElementDefinition;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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

public class AvailableBeneficeFactory implements IElementRetriever<AvailableBenefice> {
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

    public Collection<AvailableBenefice> getElements(String language, String moduleName) throws InvalidXmlElementException {
        if (availableBenefices.get(language) == null) {
            for (final BeneficeDefinition benefitDefinition : BeneficeDefinitionFactory.getInstance().getElements(language, moduleName)) {
                if (benefitDefinition.getSpecializations().isEmpty()) {
                    for (final Integer cost : benefitDefinition.getCosts()) {
                        final String id = benefitDefinition.getId() + (benefitDefinition.getCosts().size() == 1 ? "" : "_" + cost);
                        final AvailableBenefice availableBenefice = new AvailableBenefice(id, benefitDefinition.getName(), benefitDefinition.getDescription(),
                                language, benefitDefinition, benefitDefinition.getBeneficeClassification(), cost, benefitDefinition.getRandomDefinition());
                        addAvailableBenefice(language, moduleName, id, benefitDefinition, availableBenefice);
                    }
                } else {
                    for (final BeneficeSpecialization specialization : benefitDefinition.getSpecializations()) {
                        // Cost in specialization
                        if (specialization.getCost() != null) {
                            final String id = benefitDefinition.getId() + " [" + specialization.getId() + "]";
                            final AvailableBenefice availableBenefice = new AvailableBenefice(id, benefitDefinition.getName(),
                                    benefitDefinition.getDescription(), language, benefitDefinition, specialization.getClassification(),
                                    specialization.getCost(),
                                    new RandomElementDefinition(benefitDefinition.getRandomDefinition(), specialization.getRandomDefinition()));
                            availableBenefice.setSpecialization(specialization);
                            addAvailableBenefice(language, moduleName, id, benefitDefinition, availableBenefice);
                        } else {
                            for (final Integer cost : benefitDefinition.getCosts()) {
                                final String id = benefitDefinition.getId() + (benefitDefinition.getCosts().size() == 1 ? "" : "_" + cost) + " ["
                                        + specialization.getId() + "]";
                                final AvailableBenefice availableBenefice = new AvailableBenefice(id, benefitDefinition.getName(),
                                        benefitDefinition.getDescription(), language, benefitDefinition, specialization.getClassification(), cost,
                                        new RandomElementDefinition(benefitDefinition.getRandomDefinition(), specialization.getRandomDefinition()));
                                availableBenefice.setSpecialization(specialization);
                                addAvailableBenefice(language, moduleName, id, benefitDefinition, availableBenefice);
                            }
                        }
                    }
                }
            }
            final Map<String, AvailableBenefice> sortedElements = availableBenefices.get(language).get(moduleName).entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(new AvailableBeneficesComparator()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            availableBenefices.get(language).put(moduleName, sortedElements);
        }
        return availableBenefices.get(language).get(moduleName).values();
    }

    private void addAvailableBenefice(String language, String moduleName, String id, BeneficeDefinition beneficeDefinition,
                                      AvailableBenefice availableBenefice) {
        availableBenefices.computeIfAbsent(language, k -> new HashMap<>());

        availableBenefices.get(language).computeIfAbsent(moduleName, k -> new LinkedHashMap<>());

        availableBenefices.get(language).get(moduleName).put(id, availableBenefice);

        availableBeneficesByDefinition.computeIfAbsent(language, k -> new HashMap<>());
        availableBeneficesByDefinition.get(language).computeIfAbsent(moduleName, k -> new HashMap<>());
        availableBeneficesByDefinition.get(language).get(moduleName).computeIfAbsent(beneficeDefinition, k -> new HashSet<>());
        availableBeneficesByDefinition.get(language).get(moduleName).get(beneficeDefinition).add(availableBenefice);
    }

    @Override
    public AvailableBenefice getElement(String beneficeId, String language, String moduleName) throws InvalidXmlElementException {
        if (availableBenefices.get(language) == null || availableBenefices.get(language).isEmpty()) {
            getElements(language, moduleName);
        }
        final AvailableBenefice availableBenefice = availableBenefices.get(language).get(moduleName).get(beneficeId);
        if (availableBenefice == null) {
            throw new InvalidBeneficeException("The benefice '" + beneficeId + "' does not exists.");
        }
        return availableBenefice;
    }

    public Set<AvailableBenefice> getAvailableBeneficesByDefinition(String language, String moduleName, BeneficeDefinition beneficeDefinition)
            throws InvalidXmlElementException {
        if (language == null) {
            return null;
        }
        // Force the load.
        if (availableBeneficesByDefinition.get(language) == null || availableBeneficesByDefinition.get(language).isEmpty()) {
            getElements(language, moduleName);
        }
        return availableBeneficesByDefinition.get(language).get(moduleName).get(beneficeDefinition);
    }

    public Map<BeneficeDefinition, Set<AvailableBenefice>> getAvailableBeneficesByDefinition(String language, String moduleName)
            throws InvalidXmlElementException {
        if (availableBeneficesByDefinition == null || availableBeneficesByDefinition.get(language) == null) {
            getElements(language, moduleName);
        }
        return availableBeneficesByDefinition.get(language).get(moduleName);
    }

    private static class AvailableBeneficesComparator implements Comparator<AvailableBenefice>, Serializable {
        private static final long serialVersionUID = -6197075436977682790L;

        @Override
        public int compare(AvailableBenefice o1, AvailableBenefice o2) {
            if (!Objects.deepEquals(o1.getBeneficeDefinition().getName(), o2.getBeneficeDefinition().getName())) {
                return o1.getName().compareTo(o2.getName());
            }
            return o1.getCost() - o2.getCost();
        }

    }
}
