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

import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;

public class RandomBlessingDefinition extends RandomSelector<Blessing> {

	protected RandomBlessingDefinition(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences)
			throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignAvailableBlessings() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		// Later, the others.
		while (CostCalculator.getBeneficesCosts(getCharacterPlayer()) < FreeStyleCharacterCreation.TRAITS_POINTS
				&& !getWeightedElements().isEmpty()) {
			// Select a blessing
			Blessing selectedBlessing = selectElementByWeight();
			try {
				getCharacterPlayer().addBlessing(selectedBlessing);
			} catch (TooManyBlessingsException e) {
				// No more possible.
				break;
			}
			removeElementWeight(selectedBlessing);
		}
	}

	@Override
	protected TreeMap<Integer, Blessing> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, Blessing> weightedBlessings = new TreeMap<>();
		int count = 1;
		for (Blessing blessing : BlessingFactory.getInstance().getElements(getCharacterPlayer().getLanguage())) {
			int weight = getWeight(blessing);
			if (weight > 0) {
				weightedBlessings.put(count, blessing);
				count += weight;
			}
		}
		return weightedBlessings;
	}

	@Override
	protected int getWeight(Blessing blessing) {
		return 1;
	}
}
