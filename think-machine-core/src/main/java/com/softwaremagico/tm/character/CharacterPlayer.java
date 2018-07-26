package com.softwaremagico.tm.character;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.combat.LearnedStance;
import com.softwaremagico.tm.character.cybernetics.Cybernetics;
import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.character.equipment.Armour;
import com.softwaremagico.tm.character.equipment.Shield;
import com.softwaremagico.tm.character.equipment.Weapons;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.occultism.Occultism;
import com.softwaremagico.tm.character.race.InvalidRaceException;
import com.softwaremagico.tm.character.race.Race;
import com.softwaremagico.tm.character.race.RaceCharacteristic;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.InvalidSkillException;
import com.softwaremagico.tm.character.skills.SelectedSkill;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.character.traits.AvailableBenefice;
import com.softwaremagico.tm.character.traits.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.traits.BeneficeClassification;
import com.softwaremagico.tm.character.traits.BeneficeGroup;
import com.softwaremagico.tm.character.traits.Blessing;
import com.softwaremagico.tm.character.traits.RankSpecialization;
import com.softwaremagico.tm.log.MachineLog;

public class CharacterPlayer {
	private String language;

	// Basic description of the character.
	private CharacterInfo info;

	private Race race;

	private Faction faction;

	// Characteristics.
	private Map<String, Characteristic> characteristics;

	private transient Map<CharacteristicType, Set<Characteristic>> characteristicsByType;

	// All Psi/Teurgy powers
	private Occultism occultism;

	// Skills
	private Map<String, SelectedSkill> skills;

	private List<String> skillNameOrdered;

	private List<Blessing> blessings;

	private List<AvailableBenefice> benefices;

	private Cybernetics cybernetics;

	private Weapons weapons;

	private Armour armour;

	private Shield shield;

	private List<CombatStyle> meleeCombatActions;

	private List<CombatStyle> rangedCombatActions;

	private List<LearnedStance> learnedStances;

	private int experience = 0;

	private FreeStyleCharacterCreation freeStyleCharacterCreation;

	public CharacterPlayer() {
		this("en");
	}

	public CharacterPlayer(String language) {
		this.language = language;
		reset();
	}

	private void reset() {
		info = new CharacterInfo();
		initializeCharacteristics();
		occultism = new Occultism();
		skills = new HashMap<>();
		skillNameOrdered = new ArrayList<>();
		blessings = new ArrayList<>();
		benefices = new ArrayList<>();
		cybernetics = new Cybernetics();
		weapons = new Weapons();
		meleeCombatActions = new ArrayList<>();
		rangedCombatActions = new ArrayList<>();
		learnedStances = new ArrayList<>();
		setArmour(null);
		setShield(null);
		freeStyleCharacterCreation = null;
	}

	public CharacterInfo getInfo() {
		return info;
	}

	public void setInfo(CharacterInfo info) {
		this.info = info;
	}

	private void initializeCharacteristics() {
		characteristics = new HashMap<String, Characteristic>();
		for (CharacteristicDefinition characteristicDefinition : CharacteristicsDefinitionFactory.getInstance().getAll(language)) {
			characteristics.put(characteristicDefinition.getId(), new Characteristic(characteristicDefinition));
		}
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

	public Integer getStartingValue(AvailableSkill skill) {
		if (skill.getSkillDefinition().isNatural()) {
			return 3;
		}
		return 0;
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
		Integer value = characteristics.get(characteristicName.getId()).getValue();

		if (value == null) {
			return 0;
		}
		// Add cibernetics modifications
		value += getCyberneticBonus(characteristicName);

		return value;
	}

	public Integer getCyberneticCharacteristicsBonus() {
		Integer value = 0;
		for (CharacteristicName characteristicName : CharacteristicName.getBasicCharacteristics()) {
			value += getCyberneticBonus(characteristicName);
		}
		return value;
	}

	public Integer getCyberneticBonus(CharacteristicName characteristicName) {
		Integer value = 0;
		// Add cibernetics modifications
		for (Device device : cybernetics.getElements()) {
			if (device.getCharacteristicImprovement(characteristicName) != null && device.getCharacteristicImprovement(characteristicName).isAlways()
					&& device.getCharacteristicImprovement(characteristicName).getBonus() != 0) {
				value += device.getCharacteristicImprovement(characteristicName).getBonus();
			}
		}
		return value;
	}

	public Integer getOptionalValue(CharacteristicName characteristicName) {
		Integer value = 0;
		// Add cibernetics modifications
		for (Device device : cybernetics.getElements()) {
			if (device.getCharacteristicImprovement(characteristicName) != null && !device.getCharacteristicImprovement(characteristicName).isAlways()
					&& device.getCharacteristicImprovement(characteristicName).getBonus() != 0) {
				value += device.getCharacteristicImprovement(characteristicName).getBonus();
			}
		}
		return value;
	}

	public Integer getVitalityValue() {
		return getValue(CharacteristicName.ENDURANCE) + 5;
	}

	public Integer getWyrdValue() {
		return Math.max(getValue(CharacteristicName.WILL), getValue(CharacteristicName.FAITH)) + occultism.getExtraWyrd();
	}

	public void setSkillRank(AvailableSkill availableSkill, int value) throws InvalidSkillException {
		if (availableSkill == null) {
			throw new InvalidSkillException("Null skill is not allowed here.");
		}
		SelectedSkill skillWithRank = new SelectedSkill(availableSkill, value, false);
		skills.put(availableSkill.getCompleteName(), skillWithRank);
		skillNameOrdered.add(availableSkill.getName());
		Collections.sort(skillNameOrdered);
	}

	public void setDesiredSkillRanks(AvailableSkill availableSkill, int value) throws InvalidSkillException {
		if (availableSkill == null) {
			throw new InvalidSkillException("Null skill is not allowed here.");
		}
		getFreeStyleCharacterCreation().getDesiredSkillRanks().put(availableSkill, value);
	}

	private Integer getSkillRanks(Skill<?> skill) {
		Integer cyberneticBonus = getCyberneticsValue(skill.getName());
		if (skills.get(skill.getName()) == null) {
			if (SkillsDefinitionsFactory.getInstance().isNaturalSkill(skill.getName(), language)) {
				if (cyberneticBonus != 0) {
					return Math.max(3, cyberneticBonus);
				}
				return 3;
			}
			// No ranks. Maybe some cybernetic...
			return cyberneticBonus;
		}
		if (cyberneticBonus != null) {
			return Math.max(skills.get(skill.getName()).getValue(), cyberneticBonus);
		}
		return skills.get(skill.getName()).getValue();
	}

	public Integer getSkillRanks(AvailableSkill skill) {
		SelectedSkill selectedSkill = getSelectedSkill(skill);
		// Use the skill with generalization.
		if (selectedSkill != null) {
			return getSkillRanks(selectedSkill);
		} else {
			// Use a simple skill if not generalization.
			if (!skill.getSkillDefinition().isSpecializable() || skill.getSkillDefinition().isNatural()) {
				return getSkillRanks((Skill<AvailableSkill>) skill);
			} else {
				return 0;
			}
		}
	}

	private Integer getCyberneticsValue(String skillName) {
		int maxValue = 0;
		for (Device device : cybernetics.getElements()) {
			if (device.getSkillImprovement(skillName) != null) {
				if (maxValue < device.getSkillImprovement(skillName).getValue()) {
					maxValue = device.getSkillImprovement(skillName).getValue();
				}
			}
		}
		return maxValue;
	}

	public boolean isSkillSpecial(AvailableSkill availableSkill) {
		if (getSelectedSkill(availableSkill) != null && getSelectedSkill(availableSkill).hasCost()) {
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
		for (SelectedSkill selectedSkill : skills.values()) {
			if (Objects.equals(selectedSkill.getAvailableSkill(), skill)) {
				return selectedSkill;
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

	public void addBenefice(AvailableBenefice benefice) {
		benefices.add(benefice);
		Collections.sort(benefices);
	}

	public List<AvailableBenefice> getBenefices() throws InvalidXmlElementException {
		List<AvailableBenefice> positiveBenefices = new ArrayList<>();
		for (AvailableBenefice benefice : benefices) {
			if (benefice.getBenefitDefinition().getClassification() == BeneficeClassification.BENEFICE) {
				positiveBenefices.add(benefice);
			}
		}
		for (CombatStyle style : getMeleeCombatStyles()) {
			positiveBenefices.add(AvailableBeneficeFactory.getInstance().getElement(style.getName(), getLanguage()));
		}
		for (CombatStyle style : getRangedCombatStyles()) {
			positiveBenefices.add(AvailableBeneficeFactory.getInstance().getElement(style.getName(), getLanguage()));
		}
		return Collections.unmodifiableList(positiveBenefices);
	}

	public List<AvailableBenefice> getAfflictions() {
		List<AvailableBenefice> afflictions = new ArrayList<>();
		for (AvailableBenefice benefice : benefices) {
			if (benefice.getBenefitDefinition().getClassification() == BeneficeClassification.AFFLICTION) {
				afflictions.add(benefice);
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

	public void setRace(Race race) throws InvalidRaceException {
		if (race == null) {
			throw new InvalidRaceException("Race '" + race + "' does not exists!");
		}
		this.race = race;
	}

	private int getRaceCharacteristicStartingValue(CharacteristicName characteristicName) {
		if (getRace() != null) {
			RaceCharacteristic value = getRace().getParameter(characteristicName);
			if (value != null) {
				return value.getInitialValue();
			}
			return Characteristic.DEFAULT_INITIAL_VALUE;
		}
		return 0;
	}

	public List<AvailableSkill> getNaturalSkills() throws InvalidXmlElementException {
		List<AvailableSkill> naturalSkills = new ArrayList<>();
		// Adds default planet and alliance.
		for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getNaturalSkills(language)) {
			if (skill.getSkillDefinition().getId().equals(SkillDefinition.PLANETARY_LORE_ID)) {
				skill.setSpecialization(new Specialization(getInfo().getPlanet(), getInfo().getPlanet()));
			} else if (skill.getSkillDefinition().getId().equals(SkillDefinition.FACTORION_LORE_ID)) {
				skill.setSpecialization(new Specialization(getFaction().getName(), getFaction().getName()));
			}
			naturalSkills.add(skill);
		}
		return naturalSkills;
	}

	public List<AvailableSkill> getLearnedSkills() throws InvalidXmlElementException {
		List<AvailableSkill> learnedSkills = new ArrayList<>();
		for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getLearnedSkills(language)) {
			if (getSkillRanks(skill) != null) {
				learnedSkills.add(skill);
			}
		}
		return learnedSkills;
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
			int strength = characteristics.get(CharacteristicName.STRENGTH.getId()).getValue();

			if (strength > 5) {
				return strength / 3 - 1;
			}
		} catch (NullPointerException npe) {

		}
		return 0;
	}

	/**
	 * Total points spent in characteristics.
	 * 
	 * @return
	 */
	public int getCharacteristicsTotalPoints() {
		int characteristicPoints = 0;
		for (CharacteristicName characteristicName : CharacteristicName.getBasicCharacteristics()) {
			characteristicPoints += getValue(characteristicName) - getStartingValue(characteristicName);
		}
		return characteristicPoints;
	}

	/**
	 * Total points spent in skills.
	 * 
	 * @return
	 * @throws InvalidXmlElementException
	 */
	public int getSkillsTotalPoints() throws InvalidXmlElementException {
		int skillPoints = 0;
		for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getNaturalSkills(getLanguage())) {
			skillPoints += getSkillRanks(skill) - getStartingValue(skill);
		}

		for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getLearnedSkills((getLanguage()))) {
			if (isSkillSpecial(skill)) {
				continue;
			}
			if (getSkillRanks(skill) != null) {
				skillPoints += getSkillRanks(skill);
			}
		}

		return skillPoints;
	}

	public int getRemainginExperience() {
		return experience;
	}

	public List<LearnedStance> getLearnedStances() {
		return learnedStances;
	}

	public void setLearnedStances(List<LearnedStance> learnedStances) {
		this.learnedStances = learnedStances;
	}

	public Characteristic getCharacteristic(String characteristicId) {
		return characteristics.get(characteristicId);
	}

	public Characteristic getCharacteristic(CharacteristicName characteristicName) {
		return getCharacteristic(characteristicName.getId());
	}

	public Set<Characteristic> getCharacteristics() {
		return new HashSet<Characteristic>(characteristics.values());
	}

	public Set<Characteristic> getCharacteristics(CharacteristicType characteristicType) {
		if (characteristicsByType == null || characteristicsByType.isEmpty()) {
			characteristicsByType = new HashMap<>();
			for (Characteristic characteristic : characteristics.values()) {
				if (characteristicsByType.get(characteristic.getType()) == null) {
					characteristicsByType.put(characteristic.getType(), new HashSet<Characteristic>());
				}
				characteristicsByType.get(characteristic.getType()).add(characteristic);
			}
		}
		return characteristicsByType.get(characteristicType);
	}

	public boolean isSkillTrained(AvailableSkill skill) {
		int skillRanks = getSkillRanks(skill);
		try {
			boolean isNatural = getNaturalSkills().contains(skill);
			return ((skillRanks > 3 && isNatural) || (skillRanks > 0 && isNatural));
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		}
		return false;
	}

	public boolean isCharacteristicTrained(Characteristic characteristic) {
		return characteristic.getValue() > getStartingValue(characteristic.getCharacteristicName());
	}

	public FreeStyleCharacterCreation getFreeStyleCharacterCreation() {
		return freeStyleCharacterCreation;
	}

	public void setFreeStyleCharacterCreation(FreeStyleCharacterCreation freeStyleCharacterCreation) {
		this.freeStyleCharacterCreation = freeStyleCharacterCreation;
	}

	/**
	 * Return which characteristic type has higher values in its
	 * characteristics.
	 * 
	 * @return CharacteristicType
	 */
	public List<Entry<CharacteristicType, Integer>> getPreferredCharacteristicsTypeSorted() {
		Map<CharacteristicType, Integer> totalRanksByCharacteristicType = new TreeMap<>();
		for (CharacteristicType characteristicType : CharacteristicType.values()) {
			if (characteristicType != CharacteristicType.OTHERS && getCharacteristics(characteristicType) != null) {
				for (Characteristic characteristic : getCharacteristics(characteristicType)) {
					if (totalRanksByCharacteristicType.get(characteristicType) == null) {
						totalRanksByCharacteristicType.put(characteristicType, 0);
					}
					totalRanksByCharacteristicType.put(characteristicType,
							totalRanksByCharacteristicType.get(characteristicType) + getValue(characteristic.getCharacteristicName()));
				}
			}
		}
		if (totalRanksByCharacteristicType.isEmpty()) {
			return null;
		}
		// Sort result.
		List<Entry<CharacteristicType, Integer>> sortedList = new LinkedList<>(totalRanksByCharacteristicType.entrySet());
		Collections.sort(sortedList, new Comparator<Entry<CharacteristicType, Integer>>() {
			public int compare(Entry<CharacteristicType, Integer> o1, Entry<CharacteristicType, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		return sortedList;
	}

	public String getRank() throws InvalidXmlElementException {
		for (AvailableBenefice benefice : getBenefices()) {
			if (benefice.getBenefitDefinition().getGroup() == BeneficeGroup.STATUS) {
				// Must have an specialization.
				if (benefice.getSpecialization() != null) {
					RankSpecialization rankSpecialization = benefice.getSpecialization();
					// Some factions have different names.
					if (getFaction() != null && getFaction().getRankTranslation(rankSpecialization.getId()) != null) {
						return getFaction().getRankTranslation(rankSpecialization.getId()).getName();
					} else {
						return rankSpecialization.getName();
					}
				}
			}
		}
		return null;
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}
}
