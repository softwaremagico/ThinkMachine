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
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeClassification;
import com.softwaremagico.tm.character.benefices.BeneficeGroup;
import com.softwaremagico.tm.character.benefices.BeneficeSpecialization;
import com.softwaremagico.tm.character.benefices.InvalidBeneficeException;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingClassification;
import com.softwaremagico.tm.character.blessings.Bonification;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.combat.LearnedStance;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.cybernetics.Cybernetics;
import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.character.equipment.Armour;
import com.softwaremagico.tm.character.equipment.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapons;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.occultism.InvalidOccultismPowerException;
import com.softwaremagico.tm.character.occultism.InvalidPsiqueLevelException;
import com.softwaremagico.tm.character.occultism.Occultism;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.race.InvalidRaceException;
import com.softwaremagico.tm.character.race.Race;
import com.softwaremagico.tm.character.race.RaceCharacteristic;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.InvalidSkillException;
import com.softwaremagico.tm.character.skills.SelectedSkill;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillGroup;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.character.values.SpecialValue;
import com.softwaremagico.tm.character.values.SpecialValuesFactory;
import com.softwaremagico.tm.log.CostCalculatorLog;
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

	private Integer getRawValue(CharacteristicName characteristicName) {
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

		return value;
	}

	public Integer getValue(CharacteristicName characteristicName) {
		if (CharacteristicName.INITIATIVE.equals(characteristicName)) {
			return getValue(CharacteristicName.DEXTERITY) + getValue(CharacteristicName.WITS)
					+ getBlessingModificationAlways(CharacteristicsDefinitionFactory.getInstance().get(characteristicName, language));
		}
		if (CharacteristicName.DEFENSE.equals(characteristicName)) {
			return getStartingValue(characteristicName)
					+ getBlessingModificationAlways(CharacteristicsDefinitionFactory.getInstance().get(characteristicName, language));
		}
		if (CharacteristicName.MOVEMENT.equals(characteristicName)) {
			return getStartingValue(characteristicName)
					+ getBlessingModificationAlways(CharacteristicsDefinitionFactory.getInstance().get(characteristicName, language));
		}
		Integer value = characteristics.get(characteristicName.getId()).getValue();

		if (value == null) {
			return 0;
		}
		// Add cybernetics modifications
		value += getCyberneticBonus(characteristicName);

		// Add modifications always applied.
		value += getBlessingModificationAlways(CharacteristicsDefinitionFactory.getInstance().get(characteristicName, language));

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

	public Integer getCyberneticsImprovement(CharacteristicName characteristicName) {
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

	public Integer getVitalityValue() throws InvalidXmlElementException {
		return getValue(CharacteristicName.ENDURANCE) + 5
				+ getBlessingModificationAlways(SpecialValuesFactory.getInstance().getElement(SpecialValue.VITALITY, getLanguage()));
	}

	public Integer getBasicWyrdValue() {
		return Math.max(getValue(CharacteristicName.WILL), getValue(CharacteristicName.FAITH));
	}

	public Integer getWyrdValue() throws InvalidXmlElementException {
		return getBasicWyrdValue() + occultism.getExtraWyrd()
				+ getBlessingModificationAlways(SpecialValuesFactory.getInstance().getElement(SpecialValue.WYRD, getLanguage()));
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

	/**
	 * For random characers, set the desired values.
	 * 
	 * @param availableSkill
	 *            skill selected
	 * @param value
	 *            desired ranks
	 * @throws InvalidSkillException
	 *             if skill does not exists.
	 */
	public void setDesiredSkillRanks(AvailableSkill availableSkill, int value) throws InvalidSkillException {
		if (availableSkill == null) {
			throw new InvalidSkillException("Null skill is not allowed here.");
		}
		getFreeStyleCharacterCreation().getDesiredSkillRanks().put(availableSkill, value);
	}

	private Integer getSkillAssignedRanks(Skill<?> skill) {
		if (skills.get(skill.getName()) == null) {
			if (SkillsDefinitionsFactory.getInstance().isNaturalSkill(skill.getName(), language)) {
				return 3;
			}
			return 0;
		}
		return skills.get(skill.getName()).getValue();
	}

	/**
	 * All ranks assigned to an skill, avoiding any blessing modification.
	 * 
	 * @param skill
	 *            Skill to check.
	 * @return ranks of the skill.
	 */
	public Integer getSkillAssignedRanks(AvailableSkill skill) {
		SelectedSkill selectedSkill = getSelectedSkill(skill);
		// Use the skill with generalization.
		if (selectedSkill != null) {
			return getSkillAssignedRanks(selectedSkill);
		} else {
			// Use a simple skill if not generalization.
			if (!skill.getSkillDefinition().isSpecializable() || skill.getSkillDefinition().isNatural()) {
				return getSkillAssignedRanks((Skill<AvailableSkill>) skill);
			} else {
				return 0;
			}
		}
	}

	/**
	 * All ranks assigned to an skill plus blessing modification.
	 * 
	 * @param skill
	 *            Skill to check.
	 * @return ranks of the skill.
	 */
	private Integer getSkillTotalRanks(Skill<?> skill) {
		Integer cyberneticBonus = getCyberneticsValue(skill.getName());
		Integer skillValue = getSkillAssignedRanks(skill);
		// Set the modifications of blessings.
		if (skills.get(skill.getName()) != null) {
			skillValue += getBlessingModificationAlways(skills.get(skill.getName()).getAvailableSkill().getSkillDefinition());
		}
		// Cybernetics only if better.
		if (cyberneticBonus != null) {
			return Math.max(skillValue, cyberneticBonus);
		}
		return skillValue;
	}

	public Integer getSkillTotalRanks(AvailableSkill skill) {
		SelectedSkill selectedSkill = getSelectedSkill(skill);
		// Use the skill with generalization.
		if (selectedSkill != null) {
			return getSkillTotalRanks(selectedSkill);
		} else {
			// Use a simple skill if not generalization.
			if (!skill.getSkillDefinition().isSpecializable() || skill.getSkillDefinition().isNatural()) {
				return getSkillTotalRanks((Skill<AvailableSkill>) skill);
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

	private Occultism getOccultism() {
		return occultism;
	}

	public void addBlessing(Blessing blessing) throws TooManyBlessingsException {
		if (blessing.getBlessingClassification() == BlessingClassification.CURSE) {
			if (CostCalculator.getBlessingCosts(getCurses()) + blessing.getCost() >= FreeStyleCharacterCreation.MAX_CURSE_POINTS) {
				throw new TooManyBlessingsException("Only a total of '" + FreeStyleCharacterCreation.MAX_CURSE_POINTS + "' points are allowed for curses.");
			}
		}
		// Only 7 values can be modified by blessings.
		if (getBlessingModificationsNumber() + blessing.getBonifications().size() > FreeStyleCharacterCreation.MAX_BLESSING_MODIFICATIONS) {
			throw new TooManyBlessingsException("Only a total of '" + FreeStyleCharacterCreation.MAX_BLESSING_MODIFICATIONS
					+ "' modifications are allowed for blessings. Now exists '" + getAllBlessings() + "' and adding '" + blessing + "'.");
		}
		// Only 7 blessings as max.
		if (getAllBlessings().size() > FreeStyleCharacterCreation.MAX_BLESSING_MODIFICATIONS) {
			throw new TooManyBlessingsException("Only a total of '" + FreeStyleCharacterCreation.MAX_BLESSING_MODIFICATIONS
					+ "' modifications are allowed for blessings. Now exists '" + getAllBlessings() + "' and adding '" + blessing + "'.");
		}
		blessings.add(blessing);
		Collections.sort(blessings);
	}

	private int getBlessingModificationsNumber() {
		int counter = 0;
		for (Blessing blessing : getAllBlessings()) {
			counter += blessing.getBonifications().size();
		}
		return counter;
	}

	public List<Blessing> getCurses() {
		List<Blessing> curses = new ArrayList<>();

		for (Blessing blessing : blessings) {
			if (blessing.getBlessingClassification() == BlessingClassification.CURSE) {
				curses.add(blessing);
			}
		}
		// Faction curses
		if (getFaction() != null) {
			for (Blessing blessing : getFaction().getBlessings()) {
				if (blessing.getBlessingClassification() == BlessingClassification.CURSE) {
					curses.add(blessing);
				}
			}
		}

		return Collections.unmodifiableList(curses);
	}

	/**
	 * Return all blessings include the factions blessings.
	 * 
	 * @return
	 */
	public List<Blessing> getAllBlessings() {
		List<Blessing> allBlessings = new ArrayList<>(blessings);
		// Add faction blessings
		if (getFaction() != null) {
			for (Blessing blessing : getFaction().getBlessings()) {
				allBlessings.add(blessing);
			}
		}
		return Collections.unmodifiableList(allBlessings);
	}

	public void addBenefice(AvailableBenefice benefice) throws InvalidBeneficeException {
		if (benefice.getBenefitDefinition().getGroup() == BeneficeGroup.RESTRICTED) {
			throw new InvalidBeneficeException("Benefice '" + benefice + "' is restricted and cannot be added.");
		}
		benefices.add(benefice);
		Collections.sort(benefices);
	}

	/**
	 * Return all benefices include the factions benfices.
	 * 
	 * @return
	 */
	public List<AvailableBenefice> getAllBenefices() throws InvalidXmlElementException {
		List<AvailableBenefice> positiveBenefices = new ArrayList<>();
		for (AvailableBenefice benefice : benefices) {
			if (benefice.getBeneficeClassification() == BeneficeClassification.BENEFICE) {
				positiveBenefices.add(benefice);
			}
		}
		// Add faction benefices
		if (getFaction() != null && getFaction().getBenefices() != null) {
			for (AvailableBenefice benefice : getFaction().getBenefices()) {
				if (benefice.getBeneficeClassification() == BeneficeClassification.BENEFICE) {
					positiveBenefices.add(benefice);
				}
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
			if (benefice.getBeneficeClassification() == BeneficeClassification.AFFLICTION) {
				afflictions.add(benefice);
			}
		}
		// Add faction afflictions
		if (getFaction() != null && getFaction().getBenefices() != null) {
			for (AvailableBenefice benefice : getFaction().getBenefices()) {
				if (benefice.getBeneficeClassification() == BeneficeClassification.AFFLICTION) {
					afflictions.add(benefice);
				}
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

	public List<Weapon> getAllWeapons() {
		List<Weapon> allWeapons = new ArrayList<>();
		allWeapons.addAll(weapons.getElements());
		try {
			for (AvailableBenefice benefice : getAllBenefices()) {
				try {
					allWeapons.add(WeaponFactory.getInstance().getElement(benefice.getId(), getLanguage()));
				} catch (InvalidXmlElementException ixmle) {
					// Benefice is not a weapon.
				}
			}
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		}
		Collections.sort(allWeapons);
		return Collections.unmodifiableList(allWeapons);
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
		// Adds default planet and faction.
		for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getNaturalSkills(language)) {
			if (skill.getSkillDefinition().getId().equals(SkillDefinition.PLANETARY_LORE_ID)) {
				if (getInfo().getPlanet() != null) {
					skill.setSpecialization(new Specialization(getInfo().getPlanet().getName(), getInfo().getPlanet().getName()));
				}
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
			if (getSkillTotalRanks(skill) != null) {
				learnedSkills.add(skill);
			}
		}
		return learnedSkills;
	}

	@Override
	public String toString() {
		String name = getNameRepresentation();
		if (name.length() > 0) {
			return name;
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
			characteristicPoints += getRawValue(characteristicName) - getStartingValue(characteristicName);
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
			skillPoints += getSkillAssignedRanks(skill) - getStartingValue(skill);
		}

		for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getLearnedSkills((getLanguage()))) {
			if (isSkillSpecial(skill)) {
				continue;
			}
			if (getSkillTotalRanks(skill) != null) {
				skillPoints += getSkillAssignedRanks(skill);
			}
		}

		CostCalculatorLog.debug(this.getClass().getName(), skills.toString());
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
		int skillRanks = getSkillTotalRanks(skill);
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

	public BeneficeSpecialization getStatus() throws InvalidXmlElementException {
		for (AvailableBenefice benefice : getAllBenefices()) {
			if (benefice.getBenefitDefinition().getGroup() == BeneficeGroup.STATUS) {
				// Must have an specialization.
				if (benefice.getSpecialization() != null) {
					return benefice.getSpecialization();
				}
			}
		}
		return null;
	}

	public int getInitialMoney() {
		try {
			for (AvailableBenefice benefice : getAllBenefices()) {
				if ((benefice.getId().startsWith("cash"))) {
					// Must have an specialization.
					if (benefice.getSpecialization() != null) {
						return Integer.parseInt(benefice.getId().replaceAll("[^\\d.]", ""));
					}
				}
			}
			return Integer.parseInt(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds250]", getLanguage()).getId().replaceAll("[^\\d.]", ""));
		} catch (InvalidXmlElementException e) {
			return 0;
		}
	}

	public int getSpentMoney() {
		int total = 0;
		for (Weapon weapon : weapons.getElements()) {
			// Skip weapons payed by benefices.
			try {
				if (!getAllBenefices().contains(AvailableBeneficeFactory.getInstance().getElement(weapon.getId(), getLanguage()))) {
					total += weapon.getCost();
				}
			} catch (InvalidXmlElementException ibe) {
				total += weapon.getCost();
			}
		}
		if (shield != null) {
			total += shield.getCost();
		}
		if (armour != null) {
			total += armour.getCost();
		}

		return total;
	}

	public int getMoney() {
		return getInitialMoney() - getSpentMoney();
	}

	public String getRank() throws InvalidXmlElementException {
		BeneficeSpecialization status = getStatus();
		if (status == null) {
			return null;
		}
		// Some factions have different names.
		if (getFaction() != null && getFaction().getRankTranslation(status.getId()) != null) {
			return getFaction().getRankTranslation(status.getId()).getName();
		} else {
			return status.getName();
		}
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	public int getBlessingModification(IValue value) {
		int modification = 0;
		for (Blessing blessing : getAllBlessings()) {
			for (Bonification bonification : blessing.getBonifications()) {
				if (bonification.getSituation() != null && !bonification.getSituation().isEmpty()) {
					if (bonification.getAffects() instanceof SpecialValue) {
						SpecialValue specialValue = (SpecialValue) bonification.getAffects();
						// Has a list of values defined.
						for (IValue specialValueSkill : specialValue.getAffects()) {
							if (Objects.equals(specialValueSkill, value)) {
								modification += bonification.getBonification();
								break;
							}
						}
					}
					if (Objects.equals(bonification.getAffects(), value)) {
						modification += bonification.getBonification();
					}
				}
			}
		}
		return modification;
	}

	public int getBlessingModificationAlways(IValue value) {
		int modification = 0;
		for (Blessing blessing : getAllBlessings()) {
			for (Bonification bonification : blessing.getBonifications()) {
				if (bonification.getSituation() == null || bonification.getSituation().isEmpty()) {
					if (bonification.getAffects() instanceof SpecialValue) {
						SpecialValue specialValue = (SpecialValue) bonification.getAffects();
						// Has a list of values defined.
						for (IValue specialValueSkill : specialValue.getAffects()) {
							if (Objects.equals(specialValueSkill, value)) {
								modification += bonification.getBonification();
								break;
							}
						}
					}
					if (Objects.equals(bonification.getAffects(), value)) {
						modification += bonification.getBonification();
					}
				}

			}
		}
		return modification;
	}

	public boolean hasSkillTemporalModificator(AvailableSkill availableSkill) {
		if (getBlessingModification(availableSkill.getSkillDefinition()) != 0) {
			return true;
		}
		return false;
	}

	public boolean hasSkillModificator(AvailableSkill availableSkill) {
		if (getBlessingModificationAlways(availableSkill.getSkillDefinition()) != 0) {
			return true;
		}
		return false;
	}

	public boolean hasCharacteristicTemporalModificator(CharacteristicName characteristicName) {
		if (getBlessingModification(CharacteristicsDefinitionFactory.getInstance().get(characteristicName, getLanguage())) != 0) {
			return true;
		}
		return false;
	}

	public boolean hasCharacteristicModificator(CharacteristicName characteristicName) {
		if (getBlessingModificationAlways(CharacteristicsDefinitionFactory.getInstance().get(characteristicName, getLanguage())) != 0) {
			return true;
		}
		return false;
	}

	public int getExtraWyrd() {
		return getOccultism().getExtraWyrd();
	}

	public void setExtraWyrd(int extraWyrd) {
		getOccultism().setExtraWyrd(extraWyrd);
	}

	public int getPsiqueLevel(OccultismType occultismType) {
		return getOccultism().getPsiqueLevel(occultismType);
	}

	public void setPsiqueLevel(OccultismType occultismType, int psyValue) throws InvalidPsiqueLevelException {
		getOccultism().setPsiqueLevel(occultismType, psyValue, getLanguage(), getFaction());
	}

	public int getDarkSideLevel(OccultismType occultismType) {
		return getOccultism().getDarkSideLevel(occultismType);
	}

	public void setDarkSideLevel(OccultismType occultismType, int darkSideValue) {
		getOccultism().setDarkSideLevel(occultismType, darkSideValue);
	}

	public Map<String, List<String>> getSelectedPowers() {
		return getOccultism().getSelectedPowers();
	}

	public void addOccultismPower(OccultismPower power) throws InvalidOccultismPowerException {
		getOccultism().addPower(power, getLanguage(), getFaction());
	}

	public String getNameRepresentation() {
		StringBuilder stringBuilder = new StringBuilder("");
		if (getInfo() != null && getInfo().getNames() != null && !getInfo().getNames().isEmpty()) {
			for (Name name : getInfo().getNames()) {
				stringBuilder.append(name.getName());
				stringBuilder.append(" ");
			}
		}
		if (getInfo() != null && getInfo().getSurname() != null) {
			stringBuilder.append(getInfo().getSurname().getName());
		}
		return stringBuilder.toString().trim();
	}

	/**
	 * Gets the total ranks spent in a group of characteristics.
	 * 
	 * @param skillGroup
	 *            group to check.
	 * @return the number of ranks
	 * @throws InvalidXmlElementException
	 *             if malformed file in translations.
	 */
	public int getRanksAssigned(SkillGroup skillGroup) throws InvalidXmlElementException {
		int ranks = 0;
		for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getSkillsByGroup(skillGroup, getLanguage())) {
			ranks += getSkillAssignedRanks(skill);
			if (skill.getSkillDefinition().isNatural()) {
				ranks -= 3;
			}
		}
		return ranks;
	}
}
