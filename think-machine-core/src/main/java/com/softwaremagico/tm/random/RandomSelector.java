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

import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;

public abstract class RandomSelector<Element extends com.softwaremagico.tm.Element<?>> {
	private CharacterPlayer characterPlayer;
	private final Set<IRandomPreferences> preferences;
	private Random rand = new Random();

	// Weight -> Characteristic.
	private final TreeMap<Integer, Element> weightedElements;
	private final int totalWeight;

	protected RandomSelector(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		this.characterPlayer = characterPlayer;
		this.preferences = preferences;
		weightedElements = assignElementsWeight();
		totalWeight = assignTotalWeight();
	}

	private Integer assignTotalWeight() {
		int totalWeight = 0;
		for (Integer value : weightedElements.keySet()) {
			totalWeight += value;
		}
		return totalWeight;
	}

	protected CharacterPlayer getCharacterPlayer() {
		return characterPlayer;
	}

	protected Set<IRandomPreferences> getPreferences() {
		return preferences;
	}

	protected abstract TreeMap<Integer, Element> assignElementsWeight() throws InvalidXmlElementException;

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
		Integer value = new Integer((int) (rand.nextDouble() * (totalWeight + 1)));
		if (weightedElements == null || weightedElements.isEmpty()) {
			throw new InvalidRandomElementSelectedException("No elements to select");
		}
		Element selectedElement = weightedElements.values().iterator().next();
		for (Integer key : weightedElements.keySet()) {
			value -= key;
			if (value <= 0) {
				MachineLog.info(this.getClass().getName(), "Selected element is '" + selectedElement + "'.");
				return selectedElement;
			}
			selectedElement = weightedElements.get(key);
		}
		return selectedElement;
	}

}
