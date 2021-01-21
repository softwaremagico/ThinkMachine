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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.*;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.BlessingClassification;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.*;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.character.combat.CombatStyleGroup;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.cybernetics.*;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.armours.InvalidArmourException;
import com.softwaremagico.tm.character.equipment.shields.InvalidShieldException;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponType;
import com.softwaremagico.tm.character.equipment.weapons.Weapons;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.InvalidFactionException;
import com.softwaremagico.tm.character.occultism.*;
import com.softwaremagico.tm.character.races.InvalidRaceException;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.races.RaceCharacteristic;
import com.softwaremagico.tm.character.skills.*;
import com.softwaremagico.tm.character.values.*;
import com.softwaremagico.tm.character.xp.ElementCannotBeUpgradeWithExperienceException;
import com.softwaremagico.tm.character.xp.Experience;
import com.softwaremagico.tm.character.xp.ExperienceIncrease;
import com.softwaremagico.tm.character.xp.NotEnoughExperienceException;
import com.softwaremagico.tm.json.CharacterJsonManager;
import com.softwaremagico.tm.json.InvalidJsonException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.txt.CharacterSheet;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CharacterPlayer {
    private final String language;

    private final String moduleName;

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

    private List<Blessing> blessings;

    private List<AvailableBenefice> benefices;

    private Cybernetics cybernetics;

    private Weapons weapons;

    private Armour armour;

    private Shield shield;

    private Experience experience;

    private String comparisonId;

    private transient CharacterModificationHandler characterModificationHandler;

    private transient Integer initialMoney;

    public CharacterPlayer(String language, String moduleName) {
        comparisonId = IdGenerator.createId();
        this.language = language;
        this.moduleName = moduleName;
        reset();
    }

    private void reset() {
        info = new CharacterInfo();
        initializeCharacteristics();
        occultism = new Occultism();
        skills = new HashMap<>();
        blessings = new ArrayList<>();
        benefices = new ArrayList<>();
        cybernetics = new Cybernetics();
        weapons = new Weapons();
        experience = new Experience();
        try {
            setArmour(null);
        } catch (InvalidArmourException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
        try {
            setShield(null);
        } catch (InvalidShieldException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
        }
    }

    public CharacterInfo getInfo() {
        return info;
    }

    public void setInfo(CharacterInfo info) {
        this.info = info;
    }

    private void initializeCharacteristics() {
        characteristics = new HashMap<>();
        for (final CharacteristicDefinition characteristicDefinition : CharacteristicsDefinitionFactory.getInstance()
                .getAll(getLanguage(), getModuleName())) {
            characteristics.put(characteristicDefinition.getId(), new Characteristic(characteristicDefinition));
        }
    }

    /**
     * Gets the starting value for a characteristic depending on the race and age.
     *
     * @param characteristicName the characteristic to update.
     * @return the initial value of a characteristic.
     */
    public Integer getStartingValue(CharacteristicName characteristicName) {
        if (CharacteristicName.DEFENSE.equals(characteristicName)) {
            return 1;
        }
        if (CharacteristicName.INITIATIVE.equals(characteristicName)) {
            return getStartingValue(CharacteristicName.DEXTERITY) + getStartingValue(CharacteristicName.WITS);
        }
        if (getInfo() != null && getInfo().getAge() != null) {
            return FreeStyleCharacterCreation.getMinInitialCharacteristicsValues(characteristicName, getInfo().getAge(),
                    getRace());
        }
        return getRaceCharacteristicStartingValue(characteristicName);
    }

    public Integer getStartingValue(AvailableSkill skill) {
        if (skill.getSkillDefinition().isNatural()) {
            return FreeStyleCharacterCreation.getMinInitialNaturalSkillsValues(getInfo().getAge());
        }
        return 0;
    }

    public Integer getRawValue(CharacteristicName characteristicName) {
        if (CharacteristicName.INITIATIVE.equals(characteristicName)) {
            return getValue(CharacteristicName.DEXTERITY) + getValue(CharacteristicName.WITS);
        }
        if (CharacteristicName.DEFENSE.equals(characteristicName)) {
            return getStartingValue(characteristicName);
        }
        if (CharacteristicName.MOVEMENT.equals(characteristicName)) {
            return getStartingValue(characteristicName);
        }
        if (characteristics.get(characteristicName.getId()) == null) {
            return 0;
        }
        return characteristics.get(characteristicName.getId()).getValue();
    }

    public Integer getValue(CharacteristicName characteristicName) {
        Integer value = getRawValue(characteristicName);

        // Add XP modifications
        value += getExperienceIncrease(characteristics.get(characteristicName.getId())).size();

        // Add cybernetics modifications
        value += getCyberneticsModificationAlways(getCharacteristic(characteristicName));

        // Add modifications always applied.
        value += getBlessingModificationAlways(
                CharacteristicsDefinitionFactory.getInstance().get(characteristicName, getLanguage(), getModuleName()));

        return value;
    }

    public Integer getVitalityValue() throws InvalidXmlElementException {
        return getValue(CharacteristicName.ENDURANCE) + 5 + getBlessingModificationAlways(
                SpecialValuesFactory.getInstance().getElement(SpecialValue.VITALITY, getLanguage(), getModuleName()));
    }

    public Integer getBasicWyrdValue() {
        return Math.max(getValue(CharacteristicName.WILL), getValue(CharacteristicName.FAITH));
    }

    public Integer getWyrdValue() throws InvalidXmlElementException {
        return getBasicWyrdValue() + getExtraWyrd() + getExperienceExtraWyrd().size() + getBlessingModificationAlways(
                SpecialValuesFactory.getInstance().getElement(SpecialValue.WYRD, getLanguage(), getModuleName()));
    }

    public void setSkillRank(AvailableSkill availableSkill, int value) throws InvalidSkillException {
        if (availableSkill == null) {
            throw new InvalidSkillException("Null skill is not allowed here.");
        }

        int previousValue = 0;
        if (skills.get(availableSkill.getUniqueId()) != null) {
            previousValue = skills.get(availableSkill.getUniqueId()).getValue();
        }
        if (availableSkill.getSkillDefinition().isNatural() && value <= FreeStyleCharacterCreation.getMinInitialNaturalSkillsValues(getInfo().getAge())) {
            value = 0;
        }
        if (previousValue != value) {
            getCharacterModificationHandler().launchSkillUpdatedListener(availableSkill,
                    previousValue, value, availableSkill.getSkillDefinition().isNatural() ?
                            FreeStyleCharacterCreation.getMinInitialNaturalSkillsValues(getInfo().getAge()) : 0);
        }
        final SelectedSkill skillWithRank = new SelectedSkill(availableSkill, value, false);
        if (value == 0) {
            skills.remove(availableSkill.getUniqueId());
        } else {
            skills.put(availableSkill.getUniqueId(), skillWithRank);
        }
    }

    private Integer getSkillAssignedRanks(Skill<?> skill) {
        if (skills.get(skill.getUniqueId()) == null) {
            if (SkillsDefinitionsFactory.getInstance().isNaturalSkill(skill.getName(), getLanguage(),
                    getModuleName())) {
                return FreeStyleCharacterCreation.getMinInitialNaturalSkillsValues(getInfo().getAge());
            }
            return 0;
        }
        return skills.get(skill.getUniqueId()).getValue();
    }

    /**
     * All ranks assigned to an skill, avoiding any blessing or XP modification.
     *
     * @param skill Skill to check.
     * @return ranks of the skill.
     */
    public Integer getSkillAssignedRanks(AvailableSkill skill) {
        final SelectedSkill selectedSkill = getSelectedSkill(skill);
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
     * All ranks assigned to an skill plus blessing, cybernetics and XP
     * modification.
     *
     * @param skill Skill to check.
     * @return ranks of the skill.
     */
    public Integer getSkillTotalRanks(AvailableSkill skill) {
        final Integer cyberneticBonus = getCyberneticsValue(skill);
        Integer skillValue = getSkillAssignedRanks(skill);
        // Set the modifications of blessings.
        if (skills.get(skill.getUniqueId()) != null) {
            skillValue += getBlessingModificationAlways(
                    skills.get(skill.getUniqueId()).getAvailableSkill().getSkillDefinition());
        }
        // XP
        skillValue += getExperienceIncrease(skill).size();
        // Cybernetics only if better.
        if (cyberneticBonus != null) {
            return Math.max(skillValue, cyberneticBonus);
        }
        return skillValue;
    }

    private Integer getCyberneticsValue(Skill<?> skill) {
        int maxValue = 0;
        for (final ICyberneticDevice device : cybernetics.getElements()) {
            for (final StaticValue staticValue : device.getStaticValues()) {
                if (Objects.equals(staticValue.getAffects().getId(), skill.getId())) {
                    if (maxValue < staticValue.getValue()) {
                        maxValue = staticValue.getValue();
                    }
                }
            }
        }
        return maxValue;
    }

    public boolean isSkillSpecial(AvailableSkill availableSkill) {
        return getSelectedSkill(availableSkill) != null && getSelectedSkill(availableSkill).hasCost();
    }

    /**
     * Gets the selected skill by specialization.
     *
     * @param skill the skill to select
     * @return a selectedSkill
     */
    public SelectedSkill getSelectedSkill(AvailableSkill skill) {
        for (final SelectedSkill selectedSkill : skills.values()) {
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

    public void setBlessings(Collection<Blessing> blessings) throws TooManyBlessingsException {
        while (blessings.remove(null)) {
            ;
        }
        //Get all blessings that will be removed.
        final Set<Blessing> blessingsToRemove = new HashSet<>(this.blessings);
        blessingsToRemove.removeAll(blessings);
        blessingsToRemove.forEach(blessingToRemove -> removeBlessing(blessingToRemove));

        for (final Blessing blessing : blessings) {
            try {
                if (!this.blessings.contains(blessing)) {
                    addBlessing(blessing);
                }
            } catch (BlessingAlreadyAddedException e) {
                //Nothing to do.
            }
        }
    }

    public void addBlessing(Blessing blessing) throws TooManyBlessingsException, BlessingAlreadyAddedException {
        if (getAllBlessings().contains(blessing)) {
            throw new BlessingAlreadyAddedException("Character already has blessing '" + blessing + "'!");
        }
        if (blessing.getBlessingClassification() == BlessingClassification.CURSE) {
            if (CostCalculator.getBlessingCosts(getCurses()) + blessing.getCost() >= FreeStyleCharacterCreation
                    .getMaxCursePoints(getInfo().getAge())) {
                throw new TooManyBlessingsException(
                        "Only a total of '" + FreeStyleCharacterCreation.getMaxCursePoints(getInfo().getAge())
                                + "' points are allowed for curses.");
            }
        }
        // Only 7 values can be modified by blessings.
        if (getBlessingModificationsNumber() + blessing.getBonifications().size() > FreeStyleCharacterCreation
                .getMaxBlessingModifications(getInfo().getAge())) {
            throw new TooManyBlessingsException(
                    "Only a total of '" + FreeStyleCharacterCreation.getMaxBlessingModifications(getInfo().getAge())
                            + "' modifications are allowed for blessings. Now exists '" + getAllBlessings()
                            + "' and adding '" + blessing + "'.");
        }
        // Only 7 blessings as max.
        if (getAllBlessings().size() > FreeStyleCharacterCreation.getMaxBlessingModifications(getInfo().getAge())) {
            throw new TooManyBlessingsException(
                    "Only a total of '" + FreeStyleCharacterCreation.getMaxBlessingModifications(getInfo().getAge())
                            + "' modifications are allowed for blessings. Now exists '" + getAllBlessings()
                            + "' and adding '" + blessing + "'.");
        }
        blessings.add(blessing);
        getCharacterModificationHandler().launchBlessingUpdatedListener(blessing, false);
        Collections.sort(blessings);
    }

    public void removeBlessing(Blessing blessing) {
        if (blessings.remove(blessing)) {
            getCharacterModificationHandler().launchBlessingUpdatedListener(blessing, true);
        }
    }

    private int getBlessingModificationsNumber() {
        int counter = 0;
        for (final Blessing blessing : getAllBlessings()) {
            counter += blessing.getBonifications().size();
        }
        return counter;
    }

    public List<Blessing> getCurses() {
        final List<Blessing> curses = new ArrayList<>();

        for (final Blessing blessing : getAllBlessings()) {
            if (blessing.getBlessingClassification() == BlessingClassification.CURSE) {
                curses.add(blessing);
            }
        }

        Collections.sort(curses);
        return Collections.unmodifiableList(curses);
    }

    public List<Blessing> getBlessings() {
        final List<Blessing> blessings = new ArrayList<>();

        for (final Blessing blessing : getAllBlessings()) {
            if (blessing.getBlessingClassification() == BlessingClassification.BLESSING) {
                blessings.add(blessing);
            }
        }

        Collections.sort(blessings);
        return Collections.unmodifiableList(blessings);
    }

    /**
     * Returns all selected blessings and curses not including faction and race
     * mandatories.
     *
     * @return the list of blessings
     */
    public List<Blessing> getSelectedBlessings() {
        return blessings;
    }

    /**
     * Return all blessings include the factions blessings and curses.
     *
     * @return a list of blessings
     */
    public List<Blessing> getAllBlessings() {
        final List<Blessing> allBlessings = new ArrayList<>(blessings);
        // Add faction blessings
        if (getFaction() != null) {
            allBlessings.addAll(getFaction().getBlessings());
        }
        Collections.sort(allBlessings);
        return Collections.unmodifiableList(allBlessings);
    }

    public void setBenefices(Collection<AvailableBenefice> benefices) throws InvalidBeneficeException {
        while (benefices.remove(null)) {
            ;
        }
        //Get all benefices that will be removed.
        final Set<AvailableBenefice> beneficesToRemove = new HashSet<>(this.benefices);
        beneficesToRemove.removeAll(benefices);
        beneficesToRemove.forEach(this::removeBenefice);

        for (final AvailableBenefice benefice : benefices) {
            try {
                if (!this.benefices.contains(benefice)) {
                    addBenefice(benefice);
                }
            } catch (BeneficeAlreadyAddedException e) {
                // Nothing to do.
            }
        }
    }

    public void addBenefice(AvailableBenefice benefice) throws InvalidBeneficeException, BeneficeAlreadyAddedException {
        if (benefice.getBeneficeDefinition().getGroup() == BeneficeGroup.RESTRICTED) {
            throw new InvalidBeneficeException("Benefice '" + benefice + "' is restricted and cannot be added.");
        }
        //Check if is incompatible with others.
        for (final AvailableBenefice existingBenefice : benefices) {
            if (benefice.getBeneficeDefinition().getIncompatibleWith().contains(existingBenefice.getId())) {
                throw new IncompatibleBeneficeException("Benefice '" + benefice + "' is incompatible with '" + existingBenefice + "'.",
                        benefice, existingBenefice);
            }
            if (existingBenefice.getBeneficeDefinition().getIncompatibleWith().contains(benefice.getId())) {
                throw new IncompatibleBeneficeException("Benefice '" + benefice + "' is incompatible with '" + existingBenefice + "'.",
                        benefice, existingBenefice);
            }
            if (existingBenefice.getBeneficeDefinition().getIncompatibleWith().contains(benefice.getSpecialization().getId())) {
                throw new IncompatibleBeneficeException("Benefice '" + benefice + "' is incompatible with '" + existingBenefice + "'.",
                        benefice, existingBenefice);
            }
            if (benefice.getBeneficeDefinition().getIncompatibleWith().contains(existingBenefice.getSpecialization().getId())) {
                throw new IncompatibleBeneficeException("Benefice '" + benefice + "' is incompatible with '" + existingBenefice + "'.",
                        benefice, existingBenefice);
            }
            if (benefice.getSpecialization() != null &&
                    benefice.getSpecialization().getIncompatibleWith().contains(existingBenefice.getBeneficeDefinition().getId())) {
                throw new IncompatibleBeneficeException("Benefice '" + benefice + "' is incompatible with '" + existingBenefice + "'.",
                        benefice, existingBenefice);
            }
            if (existingBenefice.getSpecialization() != null &&
                    existingBenefice.getSpecialization().getIncompatibleWith().contains(benefice.getBeneficeDefinition().getId())) {
                throw new IncompatibleBeneficeException("Benefice '" + benefice + "' is incompatible with '" + existingBenefice + "'.",
                        benefice, existingBenefice);
            }
        }
        if (getBenefice(benefice.getBeneficeDefinition().getId()) != null) {
            throw new BeneficeAlreadyAddedException("Character already has benefice '" + benefice + "'!");
        }
        if (getFaction() != null) {
            for (final RestrictedBenefice restrictedBenefice : getFaction().getRestrictedBenefices()) {
                if (Objects.equals(restrictedBenefice.getBeneficeDefinition(), benefice.getBeneficeDefinition())) {
                    if (benefice.getCost() > restrictedBenefice.getMaxValue()) {
                        throw new InvalidBeneficeException("Faction '" + getFaction()
                                + "' limits the cost of benefit to '" + restrictedBenefice.getMaxValue() + "'");
                    }
                }
            }
        }
        benefices.add(benefice);
        getCharacterModificationHandler().launchBeneficesUpdatedListener(benefice, false);
        Collections.sort(benefices);
        if ((benefice.getId().startsWith("cash") || (benefice.getId().startsWith("assets")))) {
            initialMoney = null;
            getCharacterModificationHandler().launchInitialFirebirdsUpdatedListener(getInitialMoney());
        }
    }

    /**
     * Returns all selected benefices and afflictions not including faction and race
     * mandatories.
     *
     * @return all selected benefices
     */
    public List<AvailableBenefice> getSelectedBenefices() {
        return benefices;
    }

    /**
     * Return all benefices include the factions benefices.
     *
     * @return a list of available benefices
     */
    public List<AvailableBenefice> getAllBenefices() {
        final List<AvailableBenefice> positiveBenefices = new ArrayList<>();
        for (final AvailableBenefice benefice : benefices) {
            if (benefice.getBeneficeClassification() == BeneficeClassification.BENEFICE) {
                positiveBenefices.add(benefice);
            }
        }
        // Add faction benefices
        if (getFaction() != null && getFaction().getBenefices() != null) {
            for (final AvailableBenefice benefice : getFaction().getBenefices()) {
                if (benefice.getBeneficeClassification() == BeneficeClassification.BENEFICE) {
                    positiveBenefices.add(benefice);
                }
            }
        }
        Collections.sort(positiveBenefices);
        return Collections.unmodifiableList(positiveBenefices);
    }

    public void removeBenefice(AvailableBenefice benefice) {
        if (benefices.remove(benefice)) {
            getCharacterModificationHandler().launchBeneficesUpdatedListener(benefice, true);
        }
        if ((benefice.getId().startsWith("cash"))) {
            initialMoney = null;
            getCharacterModificationHandler().launchInitialFirebirdsUpdatedListener(getInitialMoney());
        }
    }

    public AvailableBenefice getBenefice(String beneficeDefinitionId) {
        for (final AvailableBenefice benefice : benefices) {
            if (benefice.getBeneficeDefinition().getId().equalsIgnoreCase(beneficeDefinitionId)) {
                return benefice;
            }
        }
        return null;
    }

    public List<AvailableBenefice> getAfflictions() {
        final List<AvailableBenefice> afflictions = new ArrayList<>();
        for (final AvailableBenefice affliction : benefices) {
            if (affliction.getBeneficeClassification() == BeneficeClassification.AFFLICTION) {
                afflictions.add(affliction);
            }
        }
        // Add faction afflictions
        if (getFaction() != null && getFaction().getBenefices() != null) {
            for (final AvailableBenefice affliction : getFaction().getBenefices()) {
                if (affliction.getBeneficeClassification() == BeneficeClassification.AFFLICTION) {
                    afflictions.add(affliction);
                }
            }
        }
        return Collections.unmodifiableList(afflictions);
    }

    private Cybernetics getCyberneticList() {
        return cybernetics;
    }

    public List<SelectedCyberneticDevice> getCybernetics() {
        return cybernetics.getElements();
    }

    public boolean hasCyberneticDevice(CyberneticDevice cyberneticDevice) {
        for (final SelectedCyberneticDevice device : getCybernetics()) {
            if (Objects.equals(device.getCyberneticDevice(), cyberneticDevice)) {
                return true;
            }
        }
        return false;
    }

    public SelectedCyberneticDevice addCybernetics(CyberneticDevice cyberneticDevice)
            throws TooManyCyberneticDevicesException, RequiredCyberneticDevicesException {
        if (getCyberneticsIncompatibility() + cyberneticDevice.getIncompatibility() > Cybernetics
                .getMaxCyberneticIncompatibility(this)) {
            throw new TooManyCyberneticDevicesException(
                    "Cybernetic device cannot be added due to incompatibility requirements. Current incompatibility '"
                            + getCyberneticsIncompatibility() + "', device incompatibility '"
                            + cyberneticDevice.getIncompatibility()
                            + "', maximum incompatibility for this character is '"
                            + Cybernetics.getMaxCyberneticIncompatibility(this) + "'.");
        }
        if (cyberneticDevice.getRequirement() != null) {
            if (!hasCyberneticDevice(cyberneticDevice.getRequirement())) {
                throw new RequiredCyberneticDevicesException("Cybernetic device '" + cyberneticDevice + "' requires '"
                        + cyberneticDevice.getRequirement() + "' to be added to the character.");
            }
        }
        final SelectedCyberneticDevice selectedCyberneticDevice = new SelectedCyberneticDevice(cyberneticDevice);
        if (getCyberneticList().addElement(selectedCyberneticDevice)) {
            getCharacterModificationHandler().launchCyberneticDeviceUpdatedListener(selectedCyberneticDevice, false);
        }
        return selectedCyberneticDevice;
    }

    public void removeCybernetics(CyberneticDevice cyberneticDevice) {
        SelectedCyberneticDevice existingDevice = null;
        for (final SelectedCyberneticDevice selectedCyberneticDevice : getCyberneticList().getElements()) {
            if (Objects.equals(selectedCyberneticDevice.getCyberneticDevice(), cyberneticDevice)) {
                existingDevice = selectedCyberneticDevice;
                break;
            }
        }
        if (existingDevice != null) {
            getCyberneticList().removeElement(existingDevice);
            getCharacterModificationHandler().launchCyberneticDeviceUpdatedListener(existingDevice, true);
        }
    }

    public int getCyberneticsIncompatibility() {
        int incompatibility = 0;
        for (final SelectedCyberneticDevice device : getCybernetics()) {
            incompatibility += device.getIncompatibility();
        }
        return incompatibility;
    }

    public void addWeapon(Weapon weapon) {
        if (weapons.addElement(weapon)) {
            getCharacterModificationHandler().launchEquipmentUpdatedListener(weapon, false);
        }
    }

    public void removeWeapon(Weapon weapon) {
        if (weapons.removeElement(weapon)) {
            getCharacterModificationHandler().launchEquipmentUpdatedListener(weapon, true);
        }
    }

    public void setMeleeWeapons(Collection<Weapon> weapons) {
        //Get all benefices that will be removed.
        final Set<Weapon> weaponsToRemove = new HashSet<>(this.weapons.getElements()).stream().
                filter(weapon -> weapon.isMeleeWeapon()).collect(Collectors.toSet());
        weaponsToRemove.removeAll(weapons);
        weaponsToRemove.forEach(this::removeWeapon);

        for (final Weapon weapon : weapons) {
            if (!this.weapons.getElements().contains(weapon)) {
                addWeapon(weapon);
            }
        }
    }

    public void setRangeWeapons(Collection<Weapon> weapons) {
        //Get all benefices that will be removed.
        final Set<Weapon> weaponsToRemove = new HashSet<>(this.weapons.getElements()).stream().
                filter(weapon -> weapon.isRangeWeapon()).collect(Collectors.toSet());
        weaponsToRemove.removeAll(weapons);
        weaponsToRemove.forEach(this::removeWeapon);

        for (final Weapon weapon : weapons) {
            if (!this.weapons.getElements().contains(weapon)) {
                addWeapon(weapon);
            }
        }
    }

    public void setWeapons(Collection<Weapon> weapons) {
        //Get all benefices that will be removed.
        final Set<Weapon> weaponsToRemove = new HashSet<>(this.weapons.getElements());
        weaponsToRemove.removeAll(weapons);
        weaponsToRemove.forEach(this::removeWeapon);

        for (final Weapon weapon : weapons) {
            if (!this.weapons.getElements().contains(weapon)) {
                addWeapon(weapon);
            }
        }
    }

    public List<Weapon> getAllWeapons(WeaponType type) {
        final List<Weapon> allWeapons = new ArrayList<>(getAllWeapons());
        final List<Weapon> selectedWeapons = new ArrayList<>();
        for (final Weapon weapon : allWeapons) {
            if (Objects.equals(weapon.getType(), type)) {
                selectedWeapons.add(weapon);
            }
        }
        Collections.sort(selectedWeapons);
        return Collections.unmodifiableList(selectedWeapons);
    }

    /**
     * Gets all weapons purchased and acquired with benefices.
     *
     * @return all weapons of the character.
     */
    public List<Weapon> getAllWeapons() {
        final List<Weapon> allWeapons = new ArrayList<>(weapons.getElements());
        // Weapons from benefices.
        for (final AvailableBenefice benefice : getAllBenefices()) {
            try {
                allWeapons.add(
                        WeaponFactory.getInstance().getElement(benefice.getId(), getLanguage(), getModuleName()));
            } catch (InvalidXmlElementException ixmle) {
                // Benefice is not a weapon.
            }
        }
        // Weapons from cybernetics.
        for (final ICyberneticDevice cyberneticDevice : getCyberneticList().getElements()) {
            if (cyberneticDevice.getWeapon() != null) {
                allWeapons.add(cyberneticDevice.getWeapon());
            }
        }
        Collections.sort(allWeapons);
        return Collections.unmodifiableList(allWeapons);
    }

    /**
     * Gets only all weapons purchased.
     *
     * @return only purchased weapons.
     */
    public List<Weapon> getSelectedWeapons() {
        return weapons.getElements();
    }

    public Armour getSelectedArmour() {
        return armour;
    }

    public Armour getArmour() {
        // Armour from benefices.
        for (final AvailableBenefice benefice : getAllBenefices()) {
            try {
                final Armour armour = ArmourFactory.getInstance().getElement(benefice.getId(), getLanguage(),
                        getModuleName());
                if (armour != null) {
                    return armour;
                }
            } catch (InvalidXmlElementException ixmle) {
                // Benefice is not an armour.
            }
        }
        return armour;
    }

    public void setArmour(Armour armour) throws InvalidArmourException {
        if (getShield() != null && armour != null && !armour.getAllowedShields().contains(getShield())) {
            throw new InvalidArmourException(
                    "Armour '" + armour + "' is not compatible with shield '" + getShield() + "'.");
        }
        if (this.armour != armour) {
            if (armour == null) {
                //Remove costs of previous armour.
                getCharacterModificationHandler().launchEquipmentUpdatedListener(this.armour, true);
            } else {
                getCharacterModificationHandler().launchEquipmentUpdatedListener(armour, false);
            }
        }
        this.armour = armour;
    }

    public Shield getSelectedShield() {
        return shield;
    }

    public Shield getShield() {
        // Shields from benefices.
        for (final AvailableBenefice benefice : getAllBenefices()) {
            try {
                final Shield shield = ShieldFactory.getInstance().getElement(benefice.getId(), getLanguage(),
                        getModuleName());
                if (shield != null) {
                    return shield;
                }
            } catch (InvalidXmlElementException ixmle) {
                // Benefice is not a shield.
            }
        }
        return shield;
    }

    public void setShield(Shield shield) throws InvalidShieldException {
        if (getArmour() != null && shield != null && !getArmour().getAllowedShields().contains(shield)) {
            throw new InvalidShieldException(
                    "Shield '" + shield + "' is not compatible with armour '" + getArmour() + "'.");
        }
        if (this.shield != shield) {
            if (shield == null) {
                //Remove costs of previous shield.
                getCharacterModificationHandler().launchEquipmentUpdatedListener(this.shield, true);
            } else {
                getCharacterModificationHandler().launchEquipmentUpdatedListener(shield, false);
            }
        }
        this.shield = shield;
    }

    public List<CombatStyle> getMeleeCombatStyles() throws InvalidXmlElementException {
        final List<CombatStyle> meleeCombatActions = new ArrayList<>();
        for (final AvailableBenefice beneficeDefinition : getAllBenefices()) {
            if (beneficeDefinition.getBeneficeDefinition().getGroup() == BeneficeGroup.FIGHTING) {
                final CombatStyle combatStyle = CombatStyleFactory.getInstance().getElement(beneficeDefinition.getId(),
                        getLanguage(), getModuleName());
                if (combatStyle.getGroup() == CombatStyleGroup.MELEE
                        || combatStyle.getGroup() == CombatStyleGroup.FIGHT) {
                    meleeCombatActions.add(combatStyle);
                }
            }
        }
        Collections.sort(meleeCombatActions);
        return Collections.unmodifiableList(meleeCombatActions);
    }

    public List<CombatStyle> getRangedCombatStyles() throws InvalidXmlElementException {
        final List<CombatStyle> rangedCombatActions = new ArrayList<>();
        for (final AvailableBenefice beneficeDefinition : getAllBenefices()) {
            if (beneficeDefinition.getBeneficeDefinition().getGroup() == BeneficeGroup.FIGHTING) {
                final CombatStyle combatStyle = CombatStyleFactory.getInstance().getElement(beneficeDefinition.getId(),
                        getLanguage(), getModuleName());
                if (combatStyle.getGroup() == CombatStyleGroup.RANGED) {
                    rangedCombatActions.add(combatStyle);
                }
            }
        }
        Collections.sort(rangedCombatActions);
        return Collections.unmodifiableList(rangedCombatActions);
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) throws InvalidRaceException {
        if (race == null) {
            throw new InvalidRaceException("Race is null!");
        }
        if (!Objects.equals(this.race, race)) {
            MachineLog.debug(this.getClass().getName(), "Race set to '{}'.", race);
            this.race = race;
            for (final Characteristic characteristic : characteristics.values()) {
                final int raceInitialValue = race
                        .get(characteristic.getCharacteristicDefinition().getCharacteristicName()).getInitialValue();
                if (getRawValue(
                        characteristic.getCharacteristicDefinition().getCharacteristicName()) < raceInitialValue) {
                    this.characteristics
                            .get(characteristic.getCharacteristicDefinition().getCharacteristicName().getId())
                            .setValue(raceInitialValue);
                }
                final int raceMaxValue = race.get(characteristic.getCharacteristicDefinition().getCharacteristicName())
                        .getMaximumInitialValue();
                if (getRawValue(characteristic.getCharacteristicDefinition().getCharacteristicName()) > raceMaxValue) {
                    this.characteristics
                            .get(characteristic.getCharacteristicDefinition().getCharacteristicName().getId())
                            .setValue(raceMaxValue);
                }
            }
        }
    }

    private int getRaceCharacteristicStartingValue(CharacteristicName characteristicName) {
        if (getRace() != null) {
            final RaceCharacteristic value = getRace().getParameter(characteristicName);
            if (value != null) {
                return value.getInitialValue();
            }
            return Characteristic.DEFAULT_INITIAL_VALUE;
        }
        return 0;
    }

    public List<AvailableSkill> getNaturalSkills() throws InvalidXmlElementException {
        return new ArrayList<>(AvailableSkillsFactory.getInstance().getNaturalSkills(getLanguage(),
                getModuleName()));
    }

    public List<AvailableSkill> getLearnedSkills() throws InvalidXmlElementException {
        final List<AvailableSkill> learnedSkills = new ArrayList<>();
        for (final AvailableSkill skill : AvailableSkillsFactory.getInstance().getLearnedSkills(getLanguage(),
                getModuleName())) {
            if (getSkillTotalRanks(skill) != null) {
                learnedSkills.add(skill);
            }
        }
        return learnedSkills;
    }

    @Override
    public String toString() {
        final String name = getCompleteNameRepresentation();
        if (name.length() > 0) {
            return name;
        }
        return super.toString();
    }

    public String getRepresentation() {
        final CharacterSheet characterSheet = new CharacterSheet(this);
        return characterSheet.toString();
    }

    public int getStrengthDamangeModification() {
        try {
            final int strength = characteristics.get(CharacteristicName.STRENGTH.getId()).getValue();

            if (strength > 5) {
                return strength / 3 - 1;
            }
        } catch (NullPointerException npe) {
            //Nothing to do.
        }
        return 0;
    }

    /**
     * Total points spent in characteristics.
     *
     * @return the number of points spent in characteristics
     */
    public int getCharacteristicsTotalPoints() {
        int characteristicPoints = 0;
        for (final CharacteristicName characteristicName : CharacteristicName.getBasicCharacteristics()) {
            characteristicPoints += getRawValue(characteristicName) - getStartingValue(characteristicName);
        }
        return characteristicPoints;
    }

    /**
     * Total points spent in skills.
     *
     * @return the number of points spent in skills
     * @throws InvalidXmlElementException if skills cannot be readed.
     */
    public int getSkillsTotalPoints() throws InvalidXmlElementException {
        int skillPoints = 0;
        for (final AvailableSkill skill : AvailableSkillsFactory.getInstance().getNaturalSkills(getLanguage(),
                getModuleName())) {
            skillPoints += getSkillAssignedRanks(skill) - getStartingValue(skill);
        }

        for (final AvailableSkill skill : AvailableSkillsFactory.getInstance().getLearnedSkills(getLanguage(),
                getModuleName())) {
            if (isSkillSpecial(skill)) {
                continue;
            }
            if (getSkillAssignedRanks(skill) != null) {
                skillPoints += getSkillAssignedRanks(skill);
            }
        }

        return skillPoints;
    }

    public int getExperienceEarned() {
        return experience.getTotalExperience();
    }

    public void setExperienceEarned(int totalExperience) {
        experience.setTotalExperience(totalExperience);
    }

    public void setExperienceExtraWyrd(int addedValues)
            throws NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
        setExperienceIncreasedRanks(new Wyrd(getLanguage(), getModuleName()), addedValues);
    }

    public Set<ExperienceIncrease> getExperienceExtraWyrd() {
        return getExperienceIncrease(new Wyrd(getLanguage(), getModuleName()));
    }

    public void removeExperienceExtraWyrd(int ranks) {
        removeExperienceIncreasedRanks(new Wyrd(getLanguage(), getModuleName()), ranks);
    }

    public void setExperienceInOccultism(OccultismPath occultismPath, OccultismPower occultismPower)
            throws NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
        final ExperienceIncrease increase = experience.setExperienceIncrease(occultismPath, occultismPower,
                occultismPower.getLevel(), Experience.getExperienceCostFor(occultismPower, occultismPower.getLevel()));
        if (getExperienceExpended() > getExperienceEarned()) {
            experience.remove(occultismPath, increase);
            throw new NotEnoughExperienceException(
                    "Not enough experience to add occultism power '" + occultismPower + "'.");
        }
    }

    public void removeExperienceInOccultismPower(OccultismPath occultismPath, OccultismPower occultismPower)
            throws InvalidPowerLevelException {
        experience.remove(occultismPath, occultismPower);
    }

    public Set<ExperienceIncrease> getExperienceInOccultismPower(OccultismPower occultismPower) {
        return getExperienceIncrease(occultismPower);
    }

    public void setExperienceIncreasedRanks(Element<?> element, int addedValues)
            throws NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
        final int previousRanks;
        if (element instanceof AvailableSkill) {
            previousRanks = getSkillAssignedRanks((AvailableSkill) element)
                    + getExperienceIncrease(element).size();
        } else if (element instanceof Characteristic) {
            previousRanks = getRawValue(
                    ((Characteristic) element).getCharacteristicDefinition().getCharacteristicName())
                    + getExperienceIncrease(element).size();
        } else if (element instanceof OccultismType) {
            previousRanks = getBasicPsiqueLevel((OccultismType) element)
                    + getExperienceIncrease(element).size();
        } else if (element instanceof Wyrd) {
            previousRanks = getBasicWyrdValue() + getExtraWyrd();
        } else {
            previousRanks = 0;
        }

        for (int addedValue = 1; addedValue <= addedValues; addedValue++) {
            final ExperienceIncrease increase = experience.setExperienceIncrease(element, previousRanks + addedValue,
                    Experience.getExperienceCostFor(element, previousRanks + addedValue));
            if (getExperienceExpended() > getExperienceEarned()) {
                experience.remove(element, increase);
                throw new NotEnoughExperienceException(
                        "Not enough experience to increase '" + addedValue + "' ranks to element '" + element + "'.");
            }
        }
    }

    public void removeExperienceIncreasedRanks(Element<?> element, int ranks) {
        experience.remove(element, ranks);
    }

    public Set<ExperienceIncrease> getExperienceIncrease(Element<?> element) {
        return experience.getExperienceIncreased(element);
    }

    public void setExperiencePsiLevel(OccultismType occultismType, int addedValues)
            throws NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
        setExperienceIncreasedRanks(occultismType, addedValues);
    }

    public Set<ExperienceIncrease> getExperiencePsiLevel(OccultismType occultismType) {
        return getExperienceIncrease(occultismType);
    }

    public void removeExperiencePsiLevel(OccultismType occultismType, int ranks) {
        removeExperienceIncreasedRanks(occultismType, ranks);
    }

    public int getExperienceExpended() {
        int expendedExperience = 0;
        // Experience spent on skills.
        for (final Entry<String, Set<ExperienceIncrease>> elementsImproved : experience.getRanksIncreased()
                .entrySet()) {
            for (final ExperienceIncrease experienceIncrease : elementsImproved.getValue()) {
                expendedExperience += experienceIncrease.getCost();
            }
        }
        return expendedExperience;
    }

    public Characteristic getCharacteristic(String characteristicId) {
        return characteristics.get(characteristicId);
    }

    public Characteristic getCharacteristic(CharacteristicName characteristicName) {
        return getCharacteristic(characteristicName.getId());
    }

    public int getCharacteristicValue(CharacteristicName characteristicName) {
        return getCharacteristic(characteristicName.getId()).getValue();
    }

    public void setCharacteristic(CharacteristicName characteristicName, int value) {
        if (value < getRaceCharacteristicStartingValue(characteristicName)) {
            value = getRaceCharacteristicStartingValue(characteristicName);
        }
        final int previousValue = getCharacteristic(characteristicName.getId()).getValue();
        getCharacteristic(characteristicName.getId()).setValue(value);
        getCharacterModificationHandler().launchCharacteristicUpdatedListener(
                getCharacteristic(characteristicName.getId()),
                previousValue, value, getRaceCharacteristicStartingValue(characteristicName));
    }

    public Set<Characteristic> getCharacteristics() {
        return new HashSet<>(characteristics.values());
    }

    public Set<Characteristic> getCharacteristics(CharacteristicType characteristicType) {
        if (characteristicsByType == null || characteristicsByType.isEmpty()) {
            characteristicsByType = new HashMap<>();
            for (final Characteristic characteristic : characteristics.values()) {
                characteristicsByType.computeIfAbsent(characteristic.getCharacteristicDefinition().getType(),
                        k -> new HashSet<>());
                characteristicsByType.get(characteristic.getCharacteristicDefinition().getType()).add(characteristic);
            }
        }
        return characteristicsByType.get(characteristicType);
    }

    public boolean isSkillTrained(AvailableSkill skill) {
        final int skillRanks = getSkillTotalRanks(skill);
        return ((skillRanks > FreeStyleCharacterCreation.getMinInitialNaturalSkillsValues(getInfo().getAge())
                && skill.getSkillDefinition().isNatural())
                // check ranks and if is natural.
                || (skillRanks > 0 && skill.getSkillDefinition().isNatural()));
    }

    public boolean isCharacteristicTrained(Characteristic characteristic) {
        return characteristic
                .getValue() > getStartingValue(characteristic.getCharacteristicDefinition().getCharacteristicName());
    }

    /**
     * Return which characteristic type has higher values in its characteristics.
     *
     * @return a list of characteristicTypes
     */
    public List<Entry<CharacteristicType, Integer>> getPreferredCharacteristicsTypeSorted() {
        final Map<CharacteristicType, Integer> totalRanksByCharacteristicType = new TreeMap<>();
        for (final CharacteristicType characteristicType : CharacteristicType.values()) {
            if (characteristicType != CharacteristicType.OTHERS && getCharacteristics(characteristicType) != null) {
                for (final Characteristic characteristic : getCharacteristics(characteristicType)) {
                    totalRanksByCharacteristicType.putIfAbsent(characteristicType, 0);
                    totalRanksByCharacteristicType.put(characteristicType,
                            totalRanksByCharacteristicType.get(characteristicType)
                                    + getValue(characteristic.getCharacteristicDefinition().getCharacteristicName()));
                }
            }
        }
        if (totalRanksByCharacteristicType.isEmpty()) {
            return null;
        }
        // Sort result.
        final List<Entry<CharacteristicType, Integer>> sortedList = new LinkedList<>(
                totalRanksByCharacteristicType.entrySet());
        sortedList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        return sortedList;
    }

    public BeneficeSpecialization getStatus() {
        for (final AvailableBenefice benefice : getAllBenefices()) {
            if (benefice.getBeneficeDefinition().getGroup() == BeneficeGroup.STATUS) {
                // Must have an specialization.
                if (benefice.getSpecialization() != null) {
                    return benefice.getSpecialization();
                }
            }
        }
        return null;
    }

    /**
     * Gets the starting value of money in firebirds. The value cames from "cash"
     * benefices.
     *
     * @return an integer represented the starting ammount of firebirds.
     */
    public int getInitialMoney() {
        if (initialMoney == null) {
            initialMoney = 0;
            boolean defined = false;
            for (final AvailableBenefice benefice : getAllBenefices()) {
                if ((benefice.getId().startsWith("cash"))) {
                    // Must have an specialization.
                    if (benefice.getSpecialization() != null) {
                        initialMoney += Integer.parseInt(benefice.getId().replaceAll("[^\\d.]", ""));
                        defined = true;
                    }
                } else if ((benefice.getId().startsWith("assets"))) {
                    if (benefice.getSpecialization() != null) {
                        initialMoney += (int) (Integer.parseInt(benefice.getId().replaceAll("[^\\d.]", "")) * 0.1);
                        defined = true;
                    }
                }
            }
            if (defined) {
                return initialMoney;
            }
            try {
                initialMoney = Integer.parseInt(AvailableBeneficeFactory.getInstance()
                        .getElement("cash [firebirds250]", getLanguage(), getModuleName()).getId()
                        .replaceAll("[^\\d.]", ""));
                return initialMoney;
            } catch (InvalidXmlElementException e) {
                return 0;
            }
        }
        return initialMoney;
    }

    /**
     * Returns the total cost of money spent in equipment.
     *
     * @return the summatory of the costs of the equipment.
     */
    public int getSpentMoney() {
        int total = 0;
        for (final Weapon weapon : weapons.getElements()) {
            // Skip weapons payed by benefices.
            try {
                if (!getAllBenefices().contains(AvailableBeneficeFactory.getInstance().getElement(weapon.getId(),
                        getLanguage(), getModuleName()))) {
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

    /**
     * Gets the actual remaining money of the character.
     *
     * @return the difference between the starting money and the spent one.
     */
    public int getMoney() {
        return getInitialMoney() - getSpentMoney();
    }

    /**
     * Gets the current rank of the status of a character.
     *
     * @return the status of the character.
     */
    public String getRank() {
        final BeneficeSpecialization status = getStatus();
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

    public void setFaction(Faction faction) throws InvalidFactionException {
        if (faction == null) {
            throw new InvalidFactionException("Faction is null!");
        }
        if (!Objects.equals(this.faction, faction)) {
            MachineLog.debug(this.getClass().getName(), "Faction set to '{}'.", faction);
            this.faction = faction;
        }
    }

    public int getBlessingModificationSituation(IValue value) {
        int modification = 0;
        for (final Blessing blessing : getAllBlessings()) {
            modification += getModification(blessing, value, false);
        }
        return modification;
    }

    public int getCyberneticsModificationSituation(IValue value) {
        int modification = 0;
        for (final ICyberneticDevice cyberneticDevice : getCybernetics()) {
            modification += getModification(cyberneticDevice, value, false);
        }
        return modification;
    }

    public int getBlessingModificationAlways(IValue value) {
        int modification = 0;
        for (final Blessing blessing : getAllBlessings()) {
            modification += getModification(blessing, value, true);
        }
        return modification;
    }

    public int getCyberneticsModificationAlways(IValue value) {
        int modification = 0;
        for (final ICyberneticDevice cyberneticDevice : getCyberneticList().getElements()) {
            modification += getModification(cyberneticDevice, value, true);
        }
        return modification;
    }

    private int getModification(IElementWithBonification element, IValue value, boolean always) {
        int bonus = 0;
        for (final Bonification bonification : element.getBonifications()) {
            if ((always && (bonification.getSituation() == null || bonification.getSituation().isEmpty()))
                    || (!always && bonification.getSituation() != null && !bonification.getSituation().isEmpty())) {
                if (bonification.getAffects() instanceof SpecialValue) {
                    final SpecialValue specialValue = (SpecialValue) bonification.getAffects();
                    // Has a list of values defined.
                    for (final IValue specialValueSkill : specialValue.getAffects()) {
                        if (Objects.equals(specialValueSkill, value)) {
                            bonus += bonification.getBonification();
                            break;
                        }
                    }
                }
                if (value instanceof Characteristic) {
                    if (Objects.equals(bonification.getAffects(),
                            ((Characteristic) value).getCharacteristicDefinition())) {
                        bonus += bonification.getBonification();
                    }
                }
                if (Objects.equals(bonification.getAffects(), value)) {
                    bonus += bonification.getBonification();
                }
            }
        }
        return bonus;
    }

    public boolean hasSkillTemporalModificator(AvailableSkill availableSkill) {
        if (getBlessingModificationSituation(availableSkill.getSkillDefinition()) != 0) {
            return true;
        }
        return getCyberneticsModificationSituation(availableSkill.getSkillDefinition()) != 0;
    }

    public boolean hasSkillModificator(AvailableSkill availableSkill) {
        if (getBlessingModificationAlways(availableSkill.getSkillDefinition()) != 0) {
            return true;
        }
        return getCyberneticsModificationAlways(availableSkill.getSkillDefinition()) != 0;
    }

    public boolean hasCharacteristicTemporalModificator(CharacteristicName characteristicName) {
        if (getBlessingModificationSituation(CharacteristicsDefinitionFactory.getInstance().get(characteristicName,
                getLanguage(), getModuleName())) != 0) {
            return true;
        }
        return getCyberneticsModificationSituation(CharacteristicsDefinitionFactory.getInstance().get(characteristicName,
                getLanguage(), getModuleName())) != 0;
    }

    public boolean hasCharacteristicModificator(CharacteristicName characteristicName) {
        if (getBlessingModificationAlways(CharacteristicsDefinitionFactory.getInstance().get(characteristicName,
                getLanguage(), getModuleName())) != 0) {
            return true;
        }
        return getCyberneticsModificationAlways(CharacteristicsDefinitionFactory.getInstance().get(characteristicName,
                getLanguage(), getModuleName())) != 0;
    }

    public int getExtraWyrd() {
        return getOccultism().getExtraWyrd() != null ? getOccultism().getExtraWyrd().getValue() : 0;
    }

    public void setExtraWyrd(int extraWyrd) {
        getOccultism().setExtraWyrd(extraWyrd, getLanguage(), getModuleName());
        getCharacterModificationHandler().launchWyrdUpdatedListener(extraWyrd);
    }

    public int getBasicPsiqueLevel(OccultismType occultismType) {
        if (getRace() != null) {
            if (occultismType.getId().equals(OccultismTypeFactory.PSI_TAG)) {
                return Math.max(getRace().getPsi(), getOccultism().getPsiqueLevel(occultismType));
            }
            if (occultismType.getId().equals(OccultismTypeFactory.THEURGY_TAG)) {
                return Math.max(getRace().getTheurgy(), getOccultism().getPsiqueLevel(occultismType));
            }
        }
        return getOccultism().getPsiqueLevel(occultismType);
    }

    public int getPsiqueLevel(OccultismType occultismType) {
        return getBasicPsiqueLevel(occultismType) + getExperiencePsiLevel(occultismType).size();
    }

    public void setPsiqueLevel(OccultismType occultismType, int psyValue) throws InvalidPsiqueLevelException {
        int defaultValue = 0;
        if (getRace() != null) {
            if (Objects.equals(occultismType.getId(), OccultismTypeFactory.PSI_TAG) ||
                    Objects.equals(occultismType.getId(), OccultismTypeFactory.THEURGY_TAG)) {
                defaultValue = 1;
            }
        }
        if (getOccultism().getPsiqueLevel(occultismType) != psyValue) {
            getCharacterModificationHandler().launchOccultismLevelUpdatedListener(occultismType,
                    getOccultism().getPsiqueLevel(occultismType), psyValue, defaultValue);
        }
        getOccultism().setPsiqueLevel(occultismType, psyValue, getLanguage(), getModuleName(), getFaction());
    }

    public int getDarkSideLevel(OccultismType occultismType) {
        return getOccultism().getDarkSideLevel(occultismType);
    }

    public void setDarkSideLevel(OccultismType occultismType, int darkSideValue) {
        getOccultism().setDarkSideLevel(occultismType, darkSideValue);
    }

    public Map<String, List<OccultismPower>> getSelectedPowers() {
        return getOccultism().getSelectedPowers();
    }

    public int getTotalSelectedPowers() {
        return getOccultism().getTotalSelectedPowers();
    }

    public void addOccultismPower(OccultismPower power) throws InvalidOccultismPowerException {
        final OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power);
        if (getOccultism().addPower(path, power, getLanguage(), getFaction())) {
            getCharacterModificationHandler().launchOccultismPowerUpdatedListener(power, false);
        }
    }

    public String getCompleteNameRepresentation() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getInfo().getNameRepresentation());
        stringBuilder.append(" ");
        if (getInfo() != null && getInfo().getSurname() != null) {
            stringBuilder.append(getInfo().getSurname().getName());
        }
        return stringBuilder.toString().trim();
    }

    /**
     * Gets the total ranks spent in a group of characteristics.
     *
     * @param skillGroup group to check.
     * @return the number of ranks
     * @throws InvalidXmlElementException if malformed file in translations.
     */
    public int getRanksAssigned(SkillGroup skillGroup) throws InvalidXmlElementException {
        int ranks = 0;
        for (final AvailableSkill skill : AvailableSkillsFactory.getInstance().getSkillsByGroup(skillGroup,
                getLanguage(), getModuleName())) {
            ranks += getSkillAssignedRanks(skill);
            if (skill.getSkillDefinition().isNatural()) {
                ranks -= FreeStyleCharacterCreation.getMinInitialNaturalSkillsValues(getInfo().getAge());
            }
        }
        return ranks;
    }

    /**
     * Check if exists a weapon that needs that skill.
     *
     * @param skill skill to check.
     * @return a weapon that needs this skill
     */
    public Weapon hasWeaponWithSkill(AvailableSkill skill) {
        for (final Weapon weapon : getAllWeapons()) {
            if (Objects.equals(weapon.getSkill(), skill)) {
                return weapon;
            }
        }
        return null;
    }

    /**
     * Gets the weapon with higher ranks.
     *
     * @return a weapon
     */
    public Weapon getMainWeapon() {
        Weapon mainWeapon = null;
        int totalValue = 0;
        for (final Weapon weapon : getAllWeapons()) {
            if (getSkillTotalRanks(weapon.getSkill())
                    + getCharacteristicValue(weapon.getCharacteristic().getCharacteristicName()) > totalValue) {
                totalValue = getSkillTotalRanks(weapon.getSkill())
                        + getCharacteristicValue(weapon.getCharacteristic().getCharacteristicName());
                mainWeapon = weapon;
            }
        }
        return mainWeapon;
    }

    public int getEquipmentMaxTechnologicalLevel() {
        int techLevel = 1;
        for (final Weapon weapon : getAllWeapons()) {
            if (weapon.getTechLevel() > techLevel) {
                techLevel = weapon.getTechLevel();
            }
        }
        if (getArmour() != null && getArmour().getTechLevel() > techLevel) {
            techLevel = getArmour().getTechLevel();
        }
        if (getShield() != null && getShield().getTechLevel() > techLevel) {
            techLevel = getShield().getTechLevel();
        }
        return techLevel;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getComparisonId() {
        return comparisonId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((comparisonId == null) ? 0 : comparisonId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CharacterPlayer other = (CharacterPlayer) obj;
        if (comparisonId == null) {
            return other.comparisonId == null;
        } else {
            return comparisonId.equals(other.comparisonId);
        }
    }

    public CharacterModificationHandler getCharacterModificationHandler() {
        if (characterModificationHandler == null) {
            characterModificationHandler = new CharacterModificationHandler();
        }
        return characterModificationHandler;
    }

    public CharacterPlayer duplicate() throws InvalidGeneratedCharacter {
        try {
            final CharacterPlayer characterPlayer = CharacterJsonManager.fromJson(CharacterJsonManager.toJson(this));
            characterPlayer.comparisonId = IdGenerator.createId();
            return characterPlayer;
        } catch (InvalidJsonException e) {
            throw new InvalidGeneratedCharacter("Error duplicating character", e);
        }
    }
}
