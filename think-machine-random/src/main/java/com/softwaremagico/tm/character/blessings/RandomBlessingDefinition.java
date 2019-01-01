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
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingClassification;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.blessings.BlessingGroup;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.BlessingNumberPreferences;
import com.softwaremagico.tm.random.selectors.BlessingPreferences;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;

public class RandomBlessingDefinition extends RandomSelector<Blessing> {

	public RandomBlessingDefinition(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		IGaussianDistribution blessingDistribution = BlessingNumberPreferences.getSelected(getPreferences());
		// Select a blessing
		for (int i = 0; i < blessingDistribution.randomGaussian(); i++) {
			Blessing selectedBlessing = selectElementByWeight();
			try {
				getCharacterPlayer().addBlessing(selectedBlessing);
				RandomGenerationLog.info(this.getClass().getName(), "Added blessing '" + selectedBlessing + "'.");
			} catch (TooManyBlessingsException e) {
				RandomGenerationLog.debug(this.getClass().getName(), e.getMessage());
				// No more possible.
				break;
			}
			removeElementWeight(selectedBlessing);
		}
	}

	@Override
	protected Collection<Blessing> getAllElements() throws InvalidXmlElementException {
		return BlessingFactory.getInstance().getElements(getCharacterPlayer().getLanguage());
	}

	@Override
	protected int getWeight(Blessing blessing) {
		if (blessing == null) {
			return 0;
		}
		if (blessing.getBlessingGroup() == BlessingGroup.RESTRICTED) {
			return 0;
		}
		// Only curses.
		if (blessing.getBlessingClassification() == BlessingClassification.CURSE) {
			return 0;
		}
		// If specialization is set, add blessings that affects the skills with
		// ranks.
		SpecializationPreferences specializationPreferences = SpecializationPreferences.getSelected(getPreferences());
		for (AvailableSkill skill : blessing.getAffectedSkill(getCharacterPlayer().getLanguage())) {
			// More specialized, less ranks required to skip the curse.
			if (specializationPreferences.mean() >= SpecializationPreferences.FAIR.mean())
				if (getCharacterPlayer().getSkillAssignedRanks(skill) >= specializationPreferences.mean()) {
					return GOOD_PROBABILITY;
				}
		}
		for (AvailableSkill skill : blessing.getAffectedSkill(getCharacterPlayer().getLanguage())) {
			// More specialized, less ranks required to skip the curse.
			if (getCharacterPlayer().getSkillAssignedRanks(skill) >= (10 - specializationPreferences.maximum())) {
				return FAIR_PROBABILITY;
			}
		}
		return 1;
	}

	@Override
	protected void assignIfMandatory(Blessing blessing) throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException {
		BlessingPreferences blessingPreferences = BlessingPreferences.getSelected(getPreferences());
		if (blessingPreferences != null && blessing.getBlessingGroup() == BlessingGroup.get(blessingPreferences.name())) {
			try {
				getCharacterPlayer().addBlessing(blessing);
				RandomGenerationLog.info(this.getClass().getName(), "Added blessing '" + blessing + "'.");
			} catch (TooManyBlessingsException e) {
				throw new ImpossibleToAssignMandatoryElementException("Impossible to assign mandatory blessing '" + blessing + "'.", e);
			}
		}
	}
}
