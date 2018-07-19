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
import com.softwaremagico.tm.character.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.InvalidSkillException;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;

public class RandomSkills extends RandomSelector<AvailableSkill> {

	public RandomSkills(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) {
		super(characterPlayer, preferences);
		spendSkillsPoints();
	}

	public void spendSkillsPoints() {
		// Set minimum values of characteristics.
		assignMinimumValuesOfSkills();

		// Assign random values by weight
		try {
			while (getCharacterPlayer().getSkillsTotalPoints() < FreeStyleCharacterCreation.SKILLS_POINTS) {
				AvailableSkill selectedSkill = selectElementByWeight();

				int value = 0;
				if (getCharacterPlayer().getSkillRanks(selectedSkill) != null) {
					value = getCharacterPlayer().getSkillRanks(selectedSkill);
				}
				if (value < FreeStyleCharacterCreation.MAX_INITIAL_SKILL_VALUE) {
					try {
						getCharacterPlayer().setSkillRank(selectedSkill, value);
					} catch (InvalidSkillException e) {
						MachineLog.errorMessage(this.getClass().getName(), e);
					}
				}
			}
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		}
	}

	private void assignMinimumValuesOfSkills() {
		for (IRandomPreferences preference : getPreferences()) {
			if (preference instanceof TechnologicalPreferences) {
				getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).setValue(((TechnologicalPreferences) preference).minimumValue());
			}
		}
	}

	@Override
	protected TreeMap<Integer, AvailableSkill> assignElementsWeight() {
		TreeMap<Integer, AvailableSkill> weightedSkills = new TreeMap<>();
		int count = 0;

		for (SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getLearnedSkills(getCharacterPlayer().getLanguage())) {
			try {
				for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getAvailableSkills(skillDefinition, getCharacterPlayer().getLanguage())) {
					int weight = getWeight(skill);
					if (weight > 0) {
						weightedSkills.put(count, skill);
						count += weight;
					}
				}
			} catch (InvalidXmlElementException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}

		return weightedSkills;
	}

	@Override
	protected int getWeight(AvailableSkill skill) {
		if (skill == null) {
			return 0;
		}
		// Weapons only if technology is enough.
		if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() < skill.getSkillDefinition().getRandomDefinition().getMinimumTechLevel()) {
			return 0;
		}

		if (getCharacterPlayer().getInfo().getFaction().getFactionGroup() != FactionGroup.GUILD && skill.getSkillDefinition().isFromGuild()) {
			return 0;
		}

		int weight = 1;
		return weight;
	}

}
