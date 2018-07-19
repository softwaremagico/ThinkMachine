package com.softwaremagico.tm.random;

import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;

public abstract class RandomSelector<Element extends com.softwaremagico.tm.Element<?>> {
	private CharacterPlayer characterPlayer;
	private final Set<IRandomPreferences> preferences;
	private Random rand = new Random();

	// Weight -> Characteristic.
	private final TreeMap<Integer, Element> weightedElements;
	private final int totalWeight;

	protected RandomSelector(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) {
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

	protected abstract TreeMap<Integer, Element> assignElementsWeight();

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
	 */
	protected Element selectElementByWeight() {
		Integer value = new Integer((int) (rand.nextDouble() * totalWeight));
		Element selectedElement = weightedElements.get(weightedElements.floorKey(value));
		MachineLog.debug(this.getClass().getName(), "Selected element is '" + selectedElement.getName() + "'.");
		return selectedElement;
	}

}
