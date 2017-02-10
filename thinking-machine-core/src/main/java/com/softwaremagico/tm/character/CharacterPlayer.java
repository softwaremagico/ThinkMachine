package com.softwaremagico.tm.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicValue;
import com.softwaremagico.tm.character.characteristics.Characteristics;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.cybernetics.Cybernetics;
import com.softwaremagico.tm.character.equipment.Armour;
import com.softwaremagico.tm.character.equipment.Shield;
import com.softwaremagico.tm.character.equipment.Weapons;
import com.softwaremagico.tm.character.occultism.Occultism;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SelectedSkill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.traits.Benefit;
import com.softwaremagico.tm.character.traits.Blessing;

public class CharacterPlayer {
	private String language;

	// Basic description of the character.
	private CharacterInfo info;

	private Race race;

	// Characteristics.
	private Characteristics characteristics;

	// All Psi/Teurgy powers
	private Occultism occultism;

	// Skills
	private Map<String, SelectedSkill> skills;

	private List<Blessing> blessings;
	private List<Benefit> benefits;

	private Cybernetics cybernetics;

	private Weapons weapons;

	private Armour armour;

	private Shield shield;

	private List<CombatStyle> meleeCombatActions;

	private List<CombatStyle> rangedCombatActions;

	public CharacterPlayer(String language) {
		this.language = language;
		reset();
	}

	private void reset() {
		info = new CharacterInfo();
		characteristics = new Characteristics();
		occultism = new Occultism();
		skills = new HashMap<>();
		blessings = new ArrayList<>();
		benefits = new ArrayList<>();
		cybernetics = new Cybernetics();
		weapons = new Weapons();
		meleeCombatActions = new ArrayList<>();
		rangedCombatActions = new ArrayList<>();
		setArmour(null);
		setShield(null);
	}

	public CharacterInfo getInfo() {
		return info;
	}

	public void setInfo(CharacterInfo info) {
		this.info = info;
	}

	public Characteristics getCharacteristics() {
		return characteristics;
	}

	/**
	 * Gets the starting value for a characteristic depending on the race.
	 * 
	 * @param characteristicName
	 * @return
	 */
	public Integer getStartingValue(CharacteristicName characteristicName) {
		if (CharacteristicName.DEFENSE.equals(characteristicName)) {
			return 1;
		}
		if (CharacteristicName.INITIATIVE.equals(characteristicName)) {
			return getStartingValue(CharacteristicName.DEXTERITY) + getStartingValue(CharacteristicName.WITS);
		}
		return getRaceCharacteristicStartingValue(characteristicName);
	}

	public Integer getValue(CharacteristicName characteristicName) {
		if (CharacteristicName.INITIATIVE.equals(characteristicName)) {
			return getValue(CharacteristicName.DEXTERITY) + getValue(CharacteristicName.WITS);
		}
		if (CharacteristicName.DEFENSE.equals(characteristicName)) {
			return getStartingValue(characteristicName);
		}
		if (CharacteristicName.MOVEMENT.equals(characteristicName)) {
			return getStartingValue(characteristicName);
		}
		return getCharacteristics().getCharacteristic(characteristicName).getValue();
	}

	public Integer getVitalityValue() {
		return getValue(CharacteristicName.ENDURANCE) + 5;
	}

	public Integer getWyrdValue() {
		return Math.max(getValue(CharacteristicName.WILL), getValue(CharacteristicName.FAITH)) + occultism.getExtraWyrd();
	}

	public void addSkill(String skillName, int value) {
		skills.put(skillName, new SelectedSkill(skillName, value));
	}

	public Integer getSkillValue(String skillName) {
		if (skills.get(skillName) == null) {
			if (SkillFactory.isNaturalSkill(skillName, language)) {
				return 3;
			}
			return null;
		}
		return skills.get(skillName).getValue();
	}

	public String getLanguage() {
		return language;
	}

	public Occultism getOccultism() {
		return occultism;
	}

	public void addBlessing(Blessing blessing) {
		blessings.add(blessing);
		Collections.sort(blessings);
	}

	public List<Blessing> getBlessings() {
		return Collections.unmodifiableList(blessings);
	}

	public void addBenefit(Benefit benefit) {
		benefits.add(benefit);
		Collections.sort(benefits);
	}

	public List<Benefit> getBenefits() {
		List<Benefit> positiveBenefits = new ArrayList<>();
		for (Benefit benefit : benefits) {
			if (benefit.getCost() >= 0) {
				positiveBenefits.add(benefit);
			}
		}
		return Collections.unmodifiableList(positiveBenefits);
	}

	public List<Benefit> getAfflictions() {
		List<Benefit> afflictions = new ArrayList<>();
		for (Benefit benefit : benefits) {
			if (benefit.getCost() < 0) {
				afflictions.add(benefit);
			}
		}
		return Collections.unmodifiableList(afflictions);
	}

	public Cybernetics getCybernetics() {
		return cybernetics;
	}

	public Weapons getWeapons() {
		return weapons;
	}

	public Armour getArmour() {
		return armour;
	}

	public void setArmour(Armour armour) {
		this.armour = armour;
	}

	public Shield getShield() {
		return shield;
	}

	public void setShield(Shield shield) {
		this.shield = shield;
	}

	public List<CombatStyle> getMeleeCombatStyles() {
		return meleeCombatActions;
	}

	public List<CombatStyle> getRangedCombatStyles() {
		return rangedCombatActions;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public int getCost() {
		int cost = 0;
		if (getRace() != null) {
			cost += getRace().getCost();
		}
		cost += getCharacteristicsCost();
		cost += getSkillCosts();
		return cost;
	}

	private int getCharacteristicsCost() {
		int characteristicCost = 0;
		for (CharacteristicName characteristicName : CharacteristicName.getBasicCharacteristics()) {
			characteristicCost += getValue(characteristicName) - getRaceCharacteristicStartingValue(characteristicName);
		}
		return characteristicCost;
	}

	private int getSkillCosts() {
		int cost = 0;
		for (AvailableSkill skill : SkillFactory.getNaturalSkills(language)) {
			cost += getSkillValue(skill.getName()) - 3;
		}
		for (AvailableSkill skill : SkillFactory.getLearnedSkills(language)) {
			cost += getSkillValue(skill.getName());
		}
		return cost;
	}

	private int getRaceCharacteristicStartingValue(CharacteristicName characteristicName) {
		if (getRace() != null) {
			CharacteristicValue value = getRace().getValue(characteristicName);
			if (value != null) {
				return value.getValue();
			}
		}
		return 0;
	}
}
