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
import com.softwaremagico.tm.character.blessings.BlessingClassification;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.blessings.BlessingGroup;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.BlessingPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.CurseNumberPreferences;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;

public class RandomCursesDefinition extends RandomSelector<Blessing> {
	private final static int MAX_PROBABILITY = 100000;

	protected RandomCursesDefinition(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences)
			throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignAvailableCurse() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		IGaussianDistribution cursesDistribution = CurseNumberPreferences.getSelected(getPreferences());
		// Select a blessing
		for (int i = 0; i < cursesDistribution.randomGaussian(); i++) {
			Blessing selectedCurse = selectElementByWeight();
			try {
				getCharacterPlayer().addBlessing(selectedCurse);
				RandomGenerationLog.info(this.getClass().getName(), "Added curse '" + selectedCurse + "'.");
			} catch (TooManyBlessingsException e) {
				// No more possible.
				break;
			}
			removeElementWeight(selectedCurse);
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
	protected int getWeight(Blessing curse) {
		if (curse == null) {
			return 0;
		}
		if (curse.getBlessingGroup() == BlessingGroup.RESTRICTED) {
			return 0;
		}
		// Only curses.
		if (curse.getBlessingClassification() == BlessingClassification.BLESSING) {
			return 0;
		}
		BlessingPreferences blessingPreferences = BlessingPreferences.getSelected(getPreferences());
		if (blessingPreferences != null && curse.getBlessingGroup() == BlessingGroup.get(blessingPreferences.name())) {
			return MAX_PROBABILITY;
		}
		// No injuries for a combat character.
		CombatPreferences selectedCombat = CombatPreferences.getSelected(getPreferences());
		if (selectedCombat != null && selectedCombat.minimum() >= CombatPreferences.FAIR.minimum()) {
			if (curse.getBlessingGroup() == BlessingGroup.INJURIES) {
				return 0;
			}
		}
		// If specialization is set, not curses that affects the skills with ranks.
		SpecializationPreferences specializationPreferences = SpecializationPreferences.getSelected(getPreferences());
		if (specializationPreferences.mean() >= SpecializationPreferences.FAIR.mean()) {
			for (AvailableSkill skill : curse.getAffectedSkill(getCharacterPlayer().getLanguage())) {
				// More specialized, less ranks required to skip the curse.
				if (getCharacterPlayer().getSkillAssignedRanks(skill) >= (10 - specializationPreferences.maximum())) {
					return 0;
				}
			}
		}
		return 1;
	}
}
