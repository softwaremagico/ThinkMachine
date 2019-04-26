package com.softwaremagico.tm.character.blessings;

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
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.BlessingPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.CurseNumberPreferences;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;

public class RandomCursesDefinition extends RandomSelector<Blessing> {

	public RandomCursesDefinition(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences)
			throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		IGaussianDistribution cursesDistribution = CurseNumberPreferences.getSelected(getPreferences());
		// Select a curse
		int totalSelectedCurses = cursesDistribution.randomGaussian();
		while (getCharacterPlayer().getCurses().size() < totalSelectedCurses
				+ getCharacterPlayer().getFaction().getBlessings(BlessingClassification.CURSE).size()) {
			Blessing selectedCurse = selectElementByWeight();
			try {
				getCharacterPlayer().addBlessing(selectedCurse);
				RandomGenerationLog.info(this.getClass().getName(), "Added curse '" + selectedCurse + "'.");
				removeElementWeight(selectedCurse);
			} catch (TooManyBlessingsException e) {
				// No more possible.
				break;
			} catch (BlessingAlreadyAddedException e) {
				removeElementWeight(selectedCurse);
				continue;
			}
		}
	}

	@Override
	protected Collection<Blessing> getAllElements() throws InvalidXmlElementException {
		return BlessingFactory.getInstance().getElements(getCharacterPlayer().getLanguage());
	}

	@Override
	protected int getWeight(Blessing curse) throws InvalidRandomElementSelectedException {
		if (curse == null) {
			return 0;
		}
		if (curse.getBlessingGroup() == BlessingGroup.RESTRICTED) {
			throw new InvalidRandomElementSelectedException("Curse '" + curse + "' is restricted.");
		}
		// Only curses.
		if (curse.getBlessingClassification() == BlessingClassification.BLESSING) {
			throw new InvalidRandomElementSelectedException("Benefice '" + curse + "' is not a curse.");
		}
		BlessingPreferences blessingPreferences = BlessingPreferences.getSelected(getPreferences());
		if (blessingPreferences != null && curse.getBlessingGroup() == BlessingGroup.get(blessingPreferences.name())) {
			return MAX_PROBABILITY;
		}
		// No injuries for a combat character.
		CombatPreferences selectedCombat = CombatPreferences.getSelected(getPreferences());
		if (selectedCombat != null && selectedCombat.minimum() >= CombatPreferences.FAIR.minimum()) {
			if (curse.getBlessingGroup() == BlessingGroup.INJURIES) {
				throw new InvalidRandomElementSelectedException("No injuries '" + curse + "' for a fighter.");
			}
		}
		// If specialization is set, not curses that affects the skills with
		// ranks.
		SpecializationPreferences specializationPreferences = SpecializationPreferences.getSelected(getPreferences());
		if (specializationPreferences.mean() >= SpecializationPreferences.FAIR.mean()) {
			for (AvailableSkill skill : curse.getAffectedSkill(getCharacterPlayer().getLanguage())) {
				// More specialized, less ranks required to skip the curse.
				if (getCharacterPlayer().getSkillAssignedRanks(skill) >= (10 - specializationPreferences.maximum())) {
					throw new InvalidRandomElementSelectedException("Curse '" + curse + "' affects the skill '" + skill
							+ "' with ranks '" + getCharacterPlayer().getSkillAssignedRanks(skill) + "'.");
				}
			}
		}
		return 1;
	}

	@Override
	protected void assignIfMandatory(Blessing element) throws InvalidXmlElementException,
			ImpossibleToAssignMandatoryElementException {
		BlessingPreferences blessingPreferences = BlessingPreferences.getSelected(getPreferences());
		if (blessingPreferences != null && element.getBlessingGroup() == BlessingGroup.get(blessingPreferences.name())) {
			try {
				getCharacterPlayer().addBlessing(element);
			} catch (TooManyBlessingsException | BlessingAlreadyAddedException e) {
				throw new ImpossibleToAssignMandatoryElementException("Impossible to assign mandatory blessing '"
						+ element + "'.", e);
			}
			RandomGenerationLog.info(this.getClass().getName(), "Added blessing '" + element + "'.");
		}
	}

	@Override
	protected void assignMandatoryValues(Set<Blessing> mandatoryValues) throws InvalidXmlElementException {
		return;
	}
}
