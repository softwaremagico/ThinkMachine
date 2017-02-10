package com.softwaremagico.tm.character;

import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicValue;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.traits.Benefit;
import com.softwaremagico.tm.character.traits.Blessing;

public class CostCalculator {

	public static int getCost(CharacterPlayer characterPlayer) {
		int cost = 0;
		if (characterPlayer.getRace() != null) {
			cost += characterPlayer.getRace().getCost();
		}
		cost += getCharacteristicsCost(characterPlayer);
		cost += getSkillCosts(characterPlayer);
		cost += getTraitsCosts(characterPlayer);
		cost += getPsiPowersCosts(characterPlayer);
		cost += getCyberneticsCost(characterPlayer);
		//cost += getCombatStylesCost(characterPlayer);
		return cost;
	}

	private static int getCharacteristicsCost(CharacterPlayer characterPlayer) {
		int characteristicCost = 0;
		for (CharacteristicName characteristicName : CharacteristicName.getBasicCharacteristics()) {
			characteristicCost += characterPlayer.getValue(characteristicName)
					- getRaceCharacteristicStartingValue(characterPlayer, characteristicName);
		}
		return characteristicCost - 20;
	}

	private static int getSkillCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (AvailableSkill skill : SkillFactory.getNaturalSkills(characterPlayer.getLanguage())) {
			cost += characterPlayer.getSkillValue(skill.getName()) - 3;
		}
		for (AvailableSkill skill : SkillFactory.getLearnedSkills(characterPlayer.getLanguage())) {
			if (characterPlayer.getSkillValue(skill.getName()) != null) {
				cost += characterPlayer.getSkillValue(skill.getName());
			}
		}
		return cost - 30;
	}

	private static int getRaceCharacteristicStartingValue(CharacterPlayer characterPlayer, CharacteristicName characteristicName) {
		if (characterPlayer.getRace() != null) {
			CharacteristicValue value = characterPlayer.getRace().getValue(characteristicName);
			if (value != null) {
				return value.getValue();
			}
		}
		return 0;
	}

	private static int getTraitsCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		cost += getBlessingCosts(characterPlayer);
		cost += getBenefitsCosts(characterPlayer);
		return cost - 10;
	}

	private static int getBlessingCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (Blessing blessing : characterPlayer.getBlessings()) {
			cost += blessing.getCost();
		}
		return cost;
	}

	private static int getBenefitsCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (Benefit benefit : characterPlayer.getBenefits()) {
			cost += benefit.getCost();
		}
		for (Benefit affliction : characterPlayer.getAfflictions()) {
			cost += affliction.getCost();
		}
		return cost;
	}

	private static int getPsiPowersCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (OccultismPower occultismPower : characterPlayer.getOccultism().getElements()) {
			cost += occultismPower.getLevel();
		}
		cost += characterPlayer.getOccultism().getExtraWyrd() * 2;
		cost += (characterPlayer.getOccultism().getPsiValue() - characterPlayer.getRace().getPsi()) * 3;
		cost += (characterPlayer.getOccultism().getTeurgyValue() - characterPlayer.getRace().getTeurgy()) * 3;
		return cost;
	}

	private static int getCyberneticsCost(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (Device device : characterPlayer.getCybernetics().getElements()) {
			cost += device.getPoints();
		}
		return cost;
	}
}
