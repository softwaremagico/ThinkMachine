package com.softwaremagico.tm.random;

import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
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

		int weight = 1;
		return weight;
	}

}
