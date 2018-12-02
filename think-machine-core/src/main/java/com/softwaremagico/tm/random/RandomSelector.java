package com.softwaremagico.tm.random;

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
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.definition.RandomElementDefinition;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public abstract class RandomSelector<Element extends com.softwaremagico.tm.Element<?>> {
	protected final static int MAX_PROBABILITY = 1000000;
	protected final static int NO_PROBABILITY = -10 * MAX_PROBABILITY;

	protected final static int BAD_PROBABILITY = -20;
	protected final static int DIFFICULT_PROBABILITY = -10;
	protected final static int BASIC_PROBABILITY = 1;
	protected final static int LITTLE_PROBABILITY = 6;
	protected final static int FAIR_PROBABILITY = 11;
	protected final static int GOOD_PROBABILITY = 21;

	private final static int BASIC_MULTIPLICATOR = 5;
	private final static int HIGH_MULTIPLICATOR = 10;

	private CharacterPlayer characterPlayer;
	private final Set<IRandomPreference> preferences;
	private Random rand = new Random();

	// Weight -> Characteristic.
	private final TreeMap<Integer, Element> weightedElements;
	private final int totalWeight;

	protected RandomSelector(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		this.characterPlayer = characterPlayer;
		this.preferences = preferences;
		weightedElements = assignElementsWeight();
		totalWeight = assignTotalWeight();
		assignMandatories();
	}

	private Integer assignTotalWeight() {
		try {
			return weightedElements.lastKey();
		} catch (NoSuchElementException nse) {
			return 0;
		}
	}

	protected CharacterPlayer getCharacterPlayer() {
		return characterPlayer;
	}

	protected Set<IRandomPreference> getPreferences() {
		if (preferences == null) {
			return new HashSet<IRandomPreference>();
		}
		return preferences;
	}

	protected abstract Collection<Element> getAllElements() throws InvalidXmlElementException;

	protected abstract void assign() throws InvalidXmlElementException, InvalidRandomElementSelectedException;

	protected abstract void assignIfMandatory(Element element) throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException;

	private void assignMandatories() throws InvalidXmlElementException {
		for (Element element : getAllElements()) {
			try {
				assignIfMandatory(element);
			} catch (ImpossibleToAssignMandatoryElementException e) {
				throw new InvalidXmlElementException("Mandatory element cannot be assigned.", e);
			}
		}
	}

	protected TreeMap<Integer, Element> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, Element> weightedElements = new TreeMap<>();
		int count = 1;
		for (Element element : getAllElements()) {
			try {
				validateElement(element);
			} catch (InvalidRandomElementSelectedException e) {
				// Element not valid. Ignore it.
				continue;
			}

			int weight = getTotalWeight(element);
			if (weight > 0) {
				weightedElements.put(count, element);
				count += weight;
			}
		}
		return weightedElements;
	}

	public int getTotalWeight(Element element) {
		int weight = getWeight(element);
		weight = (int) ((weight) * getRandomDefinitionBonus(element));
		if (weight > 0) {
			// Some probabilities are defined directly.
			if (element.getRandomDefinition().getStaticProbability() != null) {
				weight = element.getRandomDefinition().getStaticProbability();
			}
		}
		return weight;
	}

	private double getRandomDefinitionBonus(Element element) {
		return getRandomDefinitionBonus(element.getRandomDefinition());
	}

	protected double getRandomDefinitionBonus(RandomElementDefinition randomDefinition) {
		double multiplier = 1d;

		if (randomDefinition == null) {
			return multiplier;
		}

		if (randomDefinition.getProbabilityMultiplier() != null) {
			RandomGenerationLog.debug(this.getClass().getName(), "Random definition multiplicator is '" + randomDefinition.getProbabilityMultiplier() + "'.");
			multiplier *= randomDefinition.getProbabilityMultiplier();
		}

		// Recommended to race.
		if (getCharacterPlayer().getRace() != null && randomDefinition.getRecommendedRaces().contains(getCharacterPlayer().getRace())) {
			RandomGenerationLog.debug(this.getClass().getName(), "Random definition as recommended for '" + getCharacterPlayer().getRace() + "'.");
			multiplier *= BASIC_MULTIPLICATOR;
		}

		// Recommended to my faction group.
		if (getCharacterPlayer().getFaction() != null
				&& randomDefinition.getRecommendedFactionsGroups().contains(getCharacterPlayer().getFaction().getFactionGroup())) {
			RandomGenerationLog.debug(this.getClass().getName(), "Random definition as recommended for '" + getCharacterPlayer().getFaction().getFactionGroup()
					+ "'.");
			multiplier *= BASIC_MULTIPLICATOR;
		}

		// Recommended to my faction.
		if (getCharacterPlayer().getFaction() != null && randomDefinition.getRecommendedFactions().contains(getCharacterPlayer().getFaction())) {
			RandomGenerationLog.debug(this.getClass().getName(), "Random definition as recommended for '" + getCharacterPlayer().getFaction() + "'.");
			multiplier *= HIGH_MULTIPLICATOR;
		}

		// Probability definition by preference.
		if (randomDefinition.getProbability() != null) {
			multiplier *= randomDefinition.getProbability().getProbabilityMultiplicator();
			RandomGenerationLog.debug(this.getClass().getName(), "Random definition defines with bonus probability of '"
					+ randomDefinition.getProbability().getProbabilityMultiplicator() + "'.");
		}

		RandomGenerationLog.debug(this.getClass().getName(), "Random definitions bonus multiplier is '" + multiplier + "'.");
		return multiplier;
	}

	protected void validateElement(Element element) throws InvalidRandomElementSelectedException {
		if (element == null) {
			throw new InvalidRandomElementSelectedException("Null elements not allowed.");
		}

		try {
			validateElement(element.getRandomDefinition());
		} catch (InvalidRandomElementSelectedException e) {
			throw new InvalidRandomElementSelectedException("Invalid element  '" + element + "'.", e);
		}
	}

	protected void validateElement(RandomElementDefinition randomDefinition) throws InvalidRandomElementSelectedException {
		if (randomDefinition == null) {
			return;
		}

		// Check technology limitations.
		if (randomDefinition.getMinimumTechLevel() != null
				&& randomDefinition.getMinimumTechLevel() > getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue()) {
			throw new InvalidRandomElementSelectedException("The tech level of the character is insufficient.");
		}

		if (randomDefinition.getMaximumTechLevel() != null
				&& randomDefinition.getMaximumTechLevel() < getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue()) {
			throw new InvalidRandomElementSelectedException("The tech level of the character is too high.");
		}

		// Race limitation
		if (randomDefinition.getRestrictedRaces() != null && !randomDefinition.getRestrictedRaces().isEmpty()
				&& !randomDefinition.getRestrictedRaces().contains(getCharacterPlayer().getRace())) {
			throw new InvalidRandomElementSelectedException("Element restricted to races '" + randomDefinition.getRestrictedRaces() + "'.");
		}

		if (randomDefinition.getForbiddenRaces() != null && randomDefinition.getForbiddenRaces().contains(getCharacterPlayer().getRace())) {
			throw new InvalidRandomElementSelectedException("Element forbidden to races '" + randomDefinition.getForbiddenRaces() + "'.");
		}

		// Faction restriction.
		if (getCharacterPlayer().getFaction() != null && !randomDefinition.getRestrictedFactions().isEmpty()
				&& !randomDefinition.getRestrictedFactions().contains(getCharacterPlayer().getFaction())) {
			throw new InvalidRandomElementSelectedException("Element restricted to factions '" + randomDefinition.getRestrictedFactions() + "'.");
		}

		if (getCharacterPlayer().getFaction() != null && !randomDefinition.getRecommendedFactionsGroups().isEmpty()
				&& !randomDefinition.getRecommendedFactionsGroups().contains(getCharacterPlayer().getFaction().getFactionGroup())) {
			throw new InvalidRandomElementSelectedException("Element restricted to factions '" + randomDefinition.getRecommendedFactionsGroups() + "'.");
		}

		// Faction groups restriction.
		if (getCharacterPlayer().getFaction() != null
				&& !randomDefinition.getRestrictedFactions().isEmpty()
				&& (getCharacterPlayer().getFaction().getFactionGroup() == null || !randomDefinition.getRestrictedFactions().contains(
						getCharacterPlayer().getFaction().getFactionGroup()))) {
			throw new InvalidRandomElementSelectedException("Element restricted to factions '" + randomDefinition.getRestrictedFactions() + "'.");
		}
	}

	/**
	 * Assign a weight to an element depending on the preferences selected.
	 * 
	 * @param Element
	 *            element to get the weight
	 * @return weight as integer
	 */
	protected abstract int getWeight(Element element);

	/**
	 * Selects a characteristic depending on its weight.
	 * 
	 * @throws InvalidRandomElementSelectedException
	 */
	protected Element selectElementByWeight() throws InvalidRandomElementSelectedException {
		if (weightedElements == null || weightedElements.isEmpty() || totalWeight == 0) {
			throw new InvalidRandomElementSelectedException("No elements to select");
		}
		int value = rand.nextInt(totalWeight) + 1;
		Element selectedElement = weightedElements.values().iterator().next();
		SortedMap<Integer, Element> view = weightedElements.headMap(value, true);
		try {
			selectedElement = view.get(view.lastKey());
		} catch (NoSuchElementException nse) {
			// If weight of first element is greater than 1, it is possible that
			// the value is less that the first element weight. That means that
			// 'view' would be empty launching a NoSuchElementException. Select
			// the first one by default.
		}
		return selectedElement;
	}

	protected void removeElementWeight(Element element) {
		Integer keyToDelete = null;
		for (Entry<Integer, Element> entry : weightedElements.entrySet()) {
			if (entry.getValue().equals(element)) {
				keyToDelete = entry.getKey();
			}
		}
		if (keyToDelete != null) {
			weightedElements.remove(keyToDelete);
		}
	}

	protected void updateWeight(Element element, int newWeight) {
		removeElementWeight(element);
		weightedElements.put(newWeight, element);
	}

	public TreeMap<Integer, Element> getWeightedElements() {
		return weightedElements;
	}
}
