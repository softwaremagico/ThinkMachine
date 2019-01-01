package com.softwaremagico.tm.character.occultism;

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
import java.util.Objects;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;

public class RandomPsique extends RandomSelector<OccultismType> {

	public RandomPsique(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidRandomElementSelectedException, InvalidXmlElementException {
		// Select which type of psique.
		OccultismType selectedOccultismType = selectElementByWeight();
		RandomGenerationLog.info(this.getClass().getName(), "Assinged psique '" + selectedOccultismType + "'.");
		// Select a level of psique.
		int level = assignLevelOfPsique(selectedOccultismType);
		RandomGenerationLog.info(this.getClass().getName(), "Assinged psique level of '" + level + "'.");
		// Assign to the character.
		getCharacterPlayer().setPsiqueLevel(selectedOccultismType, level);
	}

	@Override
	protected Collection<OccultismType> getAllElements() throws InvalidXmlElementException {
		return OccultismTypeFactory.getInstance().getElements(getCharacterPlayer().getLanguage());
	}

	@Override
	protected int getWeight(OccultismType element) {
		// Church factions must have always theurgy.
		if (Objects.equals(element, OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage()))) {
			if (getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.CHURCH) {
				return 0;
			}
			// No church factions have psi.
		} else if (Objects.equals(element, OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage()))) {
			if (getCharacterPlayer().getFaction().getFactionGroup() != FactionGroup.CHURCH) {
				return 0;
			}
		}
		return 1;
	}

	private int assignLevelOfPsique(OccultismType psique) throws InvalidXmlElementException {
		// A curse does not allow occultism.
		try {
			if (getCharacterPlayer().getAfflictions().contains(
					AvailableBeneficeFactory.getInstance().getElement("noOccult", getCharacterPlayer().getLanguage()))) {
				return 0;
			}
		} catch (InvalidXmlElementException e) {
			RandomGenerationLog.errorMessage(this.getClass().getName(), e);
		}
		IGaussianDistribution psiqueLevelSelector = PsiqueLevelPreferences.getSelected(getPreferences());
		int maxLevelSelected = psiqueLevelSelector.randomGaussian();
		if (maxLevelSelected > psiqueLevelSelector.maximum()) {
			maxLevelSelected = psiqueLevelSelector.maximum();
		}
		return maxLevelSelected;
	}

	@Override
	protected void assignIfMandatory(OccultismType element) throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException {

	}
}
