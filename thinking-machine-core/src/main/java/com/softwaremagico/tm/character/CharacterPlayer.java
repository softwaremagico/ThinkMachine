package com.softwaremagico.tm.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.character.traits.Benefit;
import com.softwaremagico.tm.character.traits.Blessing;

public class CharacterPlayer {
	private final static int COMBAT_STYLE_COST = 5;

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
	private List<String> skillNameOrdered;

	private List<Blessing> blessings;
	private List<Benefit> benefits;

	private Cybernetics cybernetics;

	private Weapons weapons;

	private Armour armour;

	private Shield shield;

	private List<CombatStyle> meleeCombatActions;

	private List<CombatStyle> rangedCombatActions;

	public CharacterPlayer() {
		reset();
	}

	public CharacterPlayer(String language) {
		this.language = language;
		reset();
	}

	private void reset() {
		info = new CharacterInfo();
		characteristics = new Characteristics();
		occultism = new Occultism();
		skills = new HashMap<>();
		skillNameOrdered = new ArrayList<>();
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
		Integer value = getCharacteristics().getCharacteristic(characteristicName).getValue();
		if (value != null) {
			return value;
		}
		return 0;
	}

	public Integer getVitalityValue() {
		return getValue(CharacteristicName.ENDURANCE) + 5;
	}

	public Integer getWyrdValue() {
		return Math.max(getValue(CharacteristicName.WILL), getValue(CharacteristicName.FAITH)) + occultism.getExtraWyrd();
	}

	public void addSkill(String skillName, int value) {
		addSkill(skillName, value, false);
	}

	public void addSkill(String skillName, int value, boolean special) {
		skills.put(skillName, new SelectedSkill(skillName, value, special));
		skillNameOrdered.add(skillName);
		Collections.sort(skillNameOrdered);
	}

	private Integer getSkillValue(Skill skill) {
		if (skills.get(skill.getName()) == null) {
			if (SkillFactory.isNaturalSkill(skill.getName(), language)) {
				return 3;
			}
			return null;
		}
		return skills.get(skill.getName()).getValue();
	}

	public Integer getSkillValue(AvailableSkill skill) {
		SelectedSkill selectedSkill = getSelectedSkill(skill);
		// Use the skill with generalization.
		if (selectedSkill != null) {
			return getSkillValue(selectedSkill);
		} else {
			// Use a simple skill if not generalization.
			if (!skill.isGeneralizable() || skill.isNatural()) {
				return getSkillValue((Skill) skill);
			} else {
				return null;
			}
		}
	}

	public boolean isSkillSpecial(AvailableSkill skill) {
		if (getSelectedSkill(skill) != null && getSelectedSkill(skill).isSpecial()) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the selected skill by specialization.
	 * 
	 * @param skill
	 * @return
	 */
	public SelectedSkill getSelectedSkill(AvailableSkill skill) {
		if (skill.isGeneralizable() && skill.getGeneralization() == null) {
			// Check for specializations.
			int order = 0;
			for (String skillName : skillNameOrdered) {
				if (skillName.contains("[")) {
					String skillPrefix = skillName.substring(0, skillName.indexOf("[")).trim();
					if (Objects.equals(skillPrefix, skill.getName())) {
						if (order == skill.getIndexOfGeneralization()) {
							return skills.get(skillName);
						} else {
							order++;
						}
					} else {
						order = 0;
					}
				}
			}
		}
		return null;
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
		for (CombatStyle style : getMeleeCombatStyles()) {
			positiveBenefits.add(new Benefit(style.getName(), COMBAT_STYLE_COST));
		}
		for (CombatStyle style : getRangedCombatStyles()) {
			positiveBenefits.add(new Benefit(style.getName(), COMBAT_STYLE_COST));
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

	private int getRaceCharacteristicStartingValue(CharacteristicName characteristicName) {
		if (getRace() != null) {
			CharacteristicValue value = getRace().getValue(characteristicName);
			if (value != null) {
				return value.getValue();
			}
		}
		return 0;
	}

	public List<AvailableSkill> getNaturalSkills() {
		List<AvailableSkill> naturalSkills = new ArrayList<>();
		boolean planet = false;
		for (AvailableSkill skill : SkillFactory.getNaturalSkills(language)) {
			if (skill.isGeneralizable()) {
				if (!planet) {
					skill.setGeneralization(getInfo().getPlanet());
					planet = true;
				} else {
					skill.setGeneralization(getInfo().getAlliance());
				}
			}
			naturalSkills.add(skill);
		}
		return naturalSkills;
	}

	@Override
	public String toString() {
		if (getInfo() != null && getInfo().getName() != null) {
			return getInfo().getName();
		}
		return super.toString();
	}

	public int getStrengthDamangeModification() {
		try {
			int strength = getCharacteristics().getCharacteristic(CharacteristicName.STRENGTH).getValue();
			if (strength > 5) {
				return strength / 3 - 1;
			}
		} catch (NullPointerException npe) {

		}
		return 0;
	}

	public int getCharacteristicsTotalPoints() {
		int characteristicCost = 0;
		for (CharacteristicName characteristicName : CharacteristicName.getBasicCharacteristics()) {
			characteristicCost += getValue(characteristicName) - getStartingValue(characteristicName);
		}
		return characteristicCost;
	}
}
