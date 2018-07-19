package com.softwaremagico.tm.random;

import java.util.Random;
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

public class RandomSkills {
	private CharacterPlayer characterPlayer;
	private final Set<IRandomPreferences> preferences;
	private Random rand = new Random();

	// Weight -> Characteristic.
	private final TreeMap<Integer, AvailableSkill> weightedSkills;
	private final int totalWeight;

	public RandomSkills(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) {
		this.characterPlayer = characterPlayer;
		this.preferences = preferences;
		weightedSkills = assignSkillsWeight();
		totalWeight = assignTotalWeight();
		spendSkillsPoints();
	}

	public void spendSkillsPoints() {
		// Set minimum values of characteristics.
		assignMinimumValuesOfSkills();

		// Assign random values by weight
		try {
			while (characterPlayer.getSkillsTotalPoints() < FreeStyleCharacterCreation.SKILLS_POINTS) {
				AvailableSkill selectedSkill = selectByWeight();
				MachineLog.debug(this.getClass().getName(), "Selected skill is '" + selectedSkill.getName() + "'.");
				int value = 0;
				if (characterPlayer.getSkillRanks(selectedSkill) != null) {
					value = characterPlayer.getSkillRanks(selectedSkill);
				}
				if (value < FreeStyleCharacterCreation.MAX_INITIAL_SKILL_VALUE) {
					try {
						characterPlayer.setSkillRank(selectedSkill, value);
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
		for (IRandomPreferences preference : preferences) {
			if (preference instanceof TechnologicalPreferences) {
				characterPlayer.getCharacteristic(CharacteristicName.TECH).setValue(((TechnologicalPreferences) preference).minimumValue());
			}
		}
	}

	private TreeMap<Integer, AvailableSkill> assignSkillsWeight() {
		TreeMap<Integer, AvailableSkill> weightedSkills = new TreeMap<>();
		int count = 0;

		for (SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getLearnedSkills(characterPlayer.getLanguage())) {
			try {
				for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getAvailableSkills(skillDefinition, characterPlayer.getLanguage())) {
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

	private Integer assignTotalWeight() {
		int totalWeight = 0;
		for (Integer value : weightedSkills.keySet()) {
			totalWeight += value;
		}
		return totalWeight;
	}

	/**
	 * Assign a weight to each characteristic depending on the preferences
	 * selected.
	 * 
	 * @param characteristicType
	 * @return
	 */
	private int getWeight(AvailableSkill skill) {
		if (skill == null) {
			return 0;
		}
		int weight = 1;
		return weight;
	}

	/**
	 * Selects a characteristic depending on its weight.
	 */
	private AvailableSkill selectByWeight() {
		Integer value = new Integer((int) (rand.nextDouble() * totalWeight));
		return weightedSkills.get(weightedSkills.floorKey(value));
	}

}
