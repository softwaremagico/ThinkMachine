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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;

public class RandomSkillRanks {

	private final CharacterPlayer characterPlayer;
	private final AvailableSkill availableSkill;
	private final Set<IRandomPreferences> preferences;

	public RandomSkillRanks(CharacterPlayer characterPlayer, AvailableSkill selectedSkill, Set<IRandomPreferences> preferences)
			throws InvalidXmlElementException {
		this.characterPlayer = characterPlayer;
		this.preferences = preferences;
		this.availableSkill = selectedSkill;
	}

	public int assignRandomRanks() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		int finalRanks = getRankValue();
		// Final ranks cannot be greater that the total points remaining.
		if (characterPlayer.getSkillsTotalPoints() + (finalRanks - characterPlayer.getStartingValue(availableSkill)) > FreeStyleCharacterCreation.SKILLS_POINTS) {
			finalRanks = FreeStyleCharacterCreation.SKILLS_POINTS - characterPlayer.getSkillsTotalPoints() + characterPlayer.getStartingValue(availableSkill);
		}
		characterPlayer.setSkillRank(availableSkill, finalRanks);
		characterPlayer.setDesiredSkillRanks(availableSkill, finalRanks);
		return finalRanks;
	}

	private int getRankValue() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		int skillValue = 0;
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
		int minimumValue = selectedSpecialization.minimum();
		if (availableSkill.getSkillDefinition().isNatural()) {
			minimumValue = SkillDefinition.NATURAL_SKILLS_MINIMUM_VALUE;
		}
		// Gaussian distribution.
		do {
			skillValue = selectedSpecialization.randomGaussian();
		} while (skillValue < minimumValue || skillValue > selectedSpecialization.maximum());
		return skillValue;
	}

	private Set<IRandomPreferences> getPreferences() {
		return preferences;
	}
}
