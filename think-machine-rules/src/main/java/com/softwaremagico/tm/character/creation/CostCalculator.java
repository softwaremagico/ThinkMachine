package com.softwaremagico.tm.character.creation;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.ICyberneticDevice;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.log.CostCalculatorLog;
import com.softwaremagico.tm.log.MachineLog;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class CostCalculator {
    public static final int CHARACTERISTIC_EXTRA_POINTS_COST = 3;
    public static final int SKILL_EXTRA_POINTS_COST = 1;
    public static final int TRAITS_EXTRA_POINTS_COST = 1;
    public static final int PSIQUE_LEVEL_COST = 3;
    public static final int PATH_LEVEL_COST = 1;
    public static final int EXTRA_WYRD_COST = 2;
    public static final int OCCULTISM_POWER_LEVEL_COST = 1;

    public static final int CYBERNETIC_DEVICE_COST = 1;

    private CostCalculatorModificationHandler costCalculatorModificationHandler;

    private AtomicInteger currentCharacteristicPoints;
    private AtomicInteger currentCharacteristicExtraPoints;
    private AtomicInteger currentSkillsPoints;
    private AtomicInteger currentSkillsExtraPoints;
    private AtomicInteger currentTraitsPoints;
    private AtomicInteger currentTraitsExtraPoints;
    private AtomicInteger currentOccultismLevelExtraPoints;
    private AtomicInteger currentOccultismPowersExtraPoints;
    private AtomicInteger currentWyrdExtraPoints;
    private AtomicInteger currentCyberneticsExtraPoints;
    private float fireBirdsExpend;
    private final CharacterPlayer characterPlayer;

    public interface CharacteristicCostUpdated {
        void updated(int currentCharacteristicPoints);
    }

    public interface ExtraCostUpdated {
        void updated(int extraPoints);
    }

    public CostCalculator(CharacterPlayer characterPlayer) {
        this.characterPlayer = characterPlayer;
        setCostListeners();
        updateCost();
    }

    private interface ICurrentPointsChanged {
        void updated(int value);
    }

    private interface ICurrentExtraPointsChanged {
        void updated(int value);
    }

    public CharacterProgressionStatus getStatus() {
        if (getTotalExtraCost() < 0) {
            return CharacterProgressionStatus.INVALID;
        }
        if (characterPlayer.getRace() == null || characterPlayer.getInfo() == null || characterPlayer.getFaction() == null) {
            return CharacterProgressionStatus.UNDEFINED;
        }
        if (currentCharacteristicPoints.get() == 0) {
            return CharacterProgressionStatus.NOT_STARTED;
        }
        if (currentSkillsPoints.get() == 0) {
            return CharacterProgressionStatus.DRAFT;
        }
        if (getTotalExtraCost() < FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge())) {
            return CharacterProgressionStatus.IN_PROGRESS;
        }
        if (characterPlayer.getExperienceExpended() > 0) {
            return CharacterProgressionStatus.EXTENDED;
        }
        if (fireBirdsExpend > 0) {
            return CharacterProgressionStatus.EQUIPPED;
        }
        return CharacterProgressionStatus.FINISHED;
    }

    public void updateCost() {
        if (characterPlayer != null && characterPlayer.getInfo() != null) {
            currentCharacteristicPoints.set(Math.min(characterPlayer.getCharacteristicsTotalPoints(),
                    FreeStyleCharacterCreation.getCharacteristicsPoints(characterPlayer.getInfo().getAge())));
            currentCharacteristicExtraPoints.set(Math.max(characterPlayer.getCharacteristicsTotalPoints() -
                            (FreeStyleCharacterCreation.getCharacteristicsPoints(characterPlayer.getInfo().getAge()))
                    , 0));
            try {
                currentSkillsPoints.set(Math.min(characterPlayer.getSkillsTotalPoints(),
                        FreeStyleCharacterCreation.getSkillsPoints(characterPlayer.getInfo().getAge())));
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
            try {
                currentSkillsExtraPoints.set(Math.max(characterPlayer.getSkillsTotalPoints() -
                        FreeStyleCharacterCreation.getSkillsPoints(characterPlayer.getInfo().getAge()), 0));
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
            try {
                currentTraitsPoints.set(Math.min(getBlessingCosts(characterPlayer) + getBeneficesCosts(characterPlayer),
                        FreeStyleCharacterCreation.getTraitsPoints(characterPlayer.getInfo().getAge())));
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
            try {
                currentTraitsExtraPoints.set(Math.max(getTraitsCosts(characterPlayer), 0));
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
            currentOccultismLevelExtraPoints.set(characterPlayer.getBasicPsiqueLevel(
                    OccultismTypeFactory.getPsi(characterPlayer.getLanguage(), characterPlayer.getModuleName()))
                    - (characterPlayer.getRace() != null ? characterPlayer.getRace().getPsi() : 0) +
                    characterPlayer.getBasicPsiqueLevel(
                            OccultismTypeFactory.getTheurgy(characterPlayer.getLanguage(), characterPlayer.getModuleName()))
                    - (characterPlayer.getRace() != null ? characterPlayer.getRace().getTheurgy() : 0));
            currentOccultismPowersExtraPoints.set(getPsiPathsCosts(characterPlayer));
            currentWyrdExtraPoints.set(Math.max(characterPlayer.getExtraWyrd(), 0));
            currentCyberneticsExtraPoints.set(Math.max(getCyberneticsCost(characterPlayer), 0));
            fireBirdsExpend = characterPlayer.getSpentMoney();
        }
    }

    private void setCostListeners() {
        currentCharacteristicPoints = new AtomicInteger(0);
        currentCharacteristicExtraPoints = new AtomicInteger(0);
        currentSkillsPoints = new AtomicInteger(0);
        currentSkillsExtraPoints = new AtomicInteger(0);
        currentTraitsPoints = new AtomicInteger(0);
        currentTraitsExtraPoints = new AtomicInteger(0);
        currentOccultismLevelExtraPoints = new AtomicInteger(0);
        currentOccultismPowersExtraPoints = new AtomicInteger(0);
        currentWyrdExtraPoints = new AtomicInteger(0);
        currentCyberneticsExtraPoints = new AtomicInteger(0);
        fireBirdsExpend = 0f;

        characterPlayer.getCharacterModificationHandler().addCharacteristicUpdatedListener(
                (characteristic, previousRank, newRank, minimumRank) -> {
                    updateCost(currentCharacteristicPoints, FreeStyleCharacterCreation.getCharacteristicsPoints(characterPlayer.getInfo().getAge()),
                            currentCharacteristicExtraPoints, previousRank, newRank, minimumRank,
                            value -> getCostCharacterModificationHandler().launchCharacteristicPointsUpdatedListeners(value),
                            value -> getCostCharacterModificationHandler().launchCharacteristicExtraPointsUpdatedListeners(value));
                });
        characterPlayer.getCharacterModificationHandler().addSkillUpdateListener((skill, previousRank, newRank, minimumRank) -> {
            updateCost(currentSkillsPoints, FreeStyleCharacterCreation.getSkillsPoints(characterPlayer.getInfo().getAge()),
                    currentSkillsExtraPoints, previousRank, newRank, minimumRank,
                    value -> getCostCharacterModificationHandler().launchSkillsPointsUpdatedListeners(value),
                    value -> getCostCharacterModificationHandler().launchSkillsExtraPointsUpdatedListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addBeneficesUpdatedListener((benefice, removed) -> {
            updateCost(currentTraitsPoints, FreeStyleCharacterCreation.getTraitsPoints(characterPlayer.getInfo().getAge()),
                    currentTraitsExtraPoints, removed ? benefice.getCost() : 0, removed ? 0 : benefice.getCost(), null,
                    value -> getCostCharacterModificationHandler().launchTraitsPointsUpdatedListeners(value),
                    value -> getCostCharacterModificationHandler().launchTraitsExtraPointsUpdatedListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addBlessingUpdatedListener((blessing, removed) -> {
            updateCost(currentTraitsPoints, FreeStyleCharacterCreation.getTraitsPoints(characterPlayer.getInfo().getAge()),
                    currentTraitsExtraPoints, removed ? blessing.getCost() : 0, removed ? 0 : blessing.getCost(), null,
                    value -> getCostCharacterModificationHandler().launchTraitsPointsUpdatedListeners(value),
                    value -> getCostCharacterModificationHandler().launchTraitsExtraPointsUpdatedListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addOccultismLevelUpdatedListener(
                (occultismType, previousPsyValue, newPsyValue, minimumPsyValue) -> {
                    updateCost(new AtomicInteger(0), 0,
                            currentOccultismLevelExtraPoints, previousPsyValue, newPsyValue, minimumPsyValue,
                            null,
                            value -> getCostCharacterModificationHandler().launchOccultismLevelExtraPointUpdatedListeners(value));
                });
        characterPlayer.getCharacterModificationHandler().addOccultismPowerUpdatedListener((power, removed) -> {
            updateCost(new AtomicInteger(0), 0,
                    currentOccultismPowersExtraPoints, removed ? power.getCost() : 0, removed ? 0 : power.getCost(), 0,
                    null,
                    value -> getCostCharacterModificationHandler().launchOccultismPowerExtraPointUpdatedListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addWyrdUpdatedListener(wyrdValue -> {
            if (currentWyrdExtraPoints.get() != wyrdValue) {
                currentWyrdExtraPoints.set(wyrdValue);
                getCostCharacterModificationHandler().launchWyrdExtraPointUpdatedListeners(wyrdValue);
            }
        });
        characterPlayer.getCharacterModificationHandler().addCyberneticDeviceUpdatedListener((device, removed) -> {
            updateCost(new AtomicInteger(0), 0,
                    currentCyberneticsExtraPoints, removed ? device.getCost() : 0, removed ? 0 : device.getCost(), 0,
                    null,
                    value -> getCostCharacterModificationHandler().launchCyberneticExtraPointsListeners(value));
        });
        characterPlayer.getCharacterModificationHandler().addEquipmentUpdatedListener((equipment, removed) -> {
            if (equipment != null) {
                fireBirdsExpend += (removed ? -equipment.getCost() : equipment.getCost());
                getCostCharacterModificationHandler().launchFirebirdSpendListeners((removed ? -equipment.getCost() : equipment.getCost()));
            }
        });
        characterPlayer.getCharacterModificationHandler().addFirebirdsUpdatedListener(initialMoney -> {
            getCostCharacterModificationHandler().launchInitialFirebirdListeners(initialMoney);
        });
    }

    /**
     * Calculates the cost variation for a points category (skill, characteristics, traits).
     *
     * @param mainPoints                points by default assigned to a category (skill, characteristics, traits)
     * @param maximumMainPoints         total maximum points available to the category
     * @param extraPoints               extra points assigned after all main points are consumed.
     * @param previousValue             already existing value.
     * @param newValue                  value to change.
     * @param defaultValue              initial starting value.
     * @param currentPointsChanged      callback when basic points are changed.
     * @param currentExtraPointsChanged callback when extra points are changed.
     */
    private synchronized void updateCost(AtomicInteger mainPoints, int maximumMainPoints, AtomicInteger extraPoints, int previousValue,
                                         int newValue, Integer defaultValue, ICurrentPointsChanged currentPointsChanged,
                                         ICurrentExtraPointsChanged currentExtraPointsChanged) {
        final int increment = defaultValue != null ? Math.max(newValue, defaultValue) - Math.max(previousValue, defaultValue) : newValue - previousValue;
        final int previousExtraPoints = extraPoints.get();

        if (mainPoints.get() + increment + extraPoints.get() <= maximumMainPoints) {
            if (extraPoints.get() > 0) {
                //increment must be negative.
                mainPoints.addAndGet(extraPoints.get() + increment);
                if (currentPointsChanged != null) {
                    currentPointsChanged.updated(extraPoints.get() - increment);
                }
                if (currentExtraPointsChanged != null) {
                    currentExtraPointsChanged.updated(-extraPoints.get());
                }
                extraPoints.set(0);
            } else {
                mainPoints.addAndGet(increment);
                if (currentPointsChanged != null) {
                    currentPointsChanged.updated(increment);
                }
            }
        } else {
            if (extraPoints.get() > 0) {
                if (increment > 0) {
                    extraPoints.addAndGet(increment);
                    if (currentExtraPointsChanged != null) {
                        currentExtraPointsChanged.updated(increment);
                    }
                } else {
                    final int extraPointIncrement = Math.min(Math.abs(increment), extraPoints.get());
                    extraPoints.addAndGet(-extraPointIncrement);
                    currentExtraPointsChanged.updated(-extraPointIncrement);
                    if (extraPointIncrement < Math.abs(increment)) {
                        mainPoints.set(-(Math.abs(increment) - extraPointIncrement));
                        currentPointsChanged.updated(-(Math.abs(increment) - extraPointIncrement));
                    }
                }
                // Not extraPoints spent yet.
            } else {
                if (increment > 0) {
                    extraPoints.addAndGet(increment - (maximumMainPoints - mainPoints.get()));
                    if (currentExtraPointsChanged != null) {
                        currentExtraPointsChanged.updated(increment - (maximumMainPoints - mainPoints.get()));
                    }
                    if (mainPoints.get() != maximumMainPoints) {
                        mainPoints.set(maximumMainPoints);
                        if (currentPointsChanged != null) {
                            currentPointsChanged.updated(maximumMainPoints);
                        }
                    }
                } else {
                    mainPoints.set(increment);
                    if (currentPointsChanged != null) {
                        currentPointsChanged.updated(increment);
                    }
                }
            }
        }
        if (previousExtraPoints != extraPoints.get()) {
            getCostCharacterModificationHandler().launchExtraPointsUpdatedListeners();
        }
    }

    public int getTotalExtraCost() {
        return currentCharacteristicExtraPoints.get() * CHARACTERISTIC_EXTRA_POINTS_COST +
                currentSkillsExtraPoints.get() * SKILL_EXTRA_POINTS_COST +
                currentTraitsExtraPoints.get() * TRAITS_EXTRA_POINTS_COST +
                currentOccultismLevelExtraPoints.get() * PSIQUE_LEVEL_COST +
                currentOccultismPowersExtraPoints.get() * PATH_LEVEL_COST +
                currentCyberneticsExtraPoints.get() * CYBERNETIC_DEVICE_COST +
                currentWyrdExtraPoints.get() * EXTRA_WYRD_COST;
    }

    public int getCost() throws InvalidXmlElementException {
        return getCost(characterPlayer);
    }


    public static int getCost(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        return getCost(characterPlayer, 0, 0);
    }

    public static int getCost(CharacterPlayer characterPlayer, int extraSkillPoints, int extraCharacteristicsPoints)
            throws InvalidXmlElementException {
        int cost = 0;
        if (characterPlayer.getRace() != null) {
            cost += characterPlayer.getRace().getCost();
        }
        cost += getCharacteristicsCost(characterPlayer, extraCharacteristicsPoints);
        cost += getSkillCosts(characterPlayer, extraSkillPoints);
        cost += getTraitsCosts(characterPlayer);
        cost += getPsiPowersCosts(characterPlayer);
        cost += getCyberneticsCost(characterPlayer);
        CostCalculatorLog.debug(CostCalculator.class.getName(),
                "Character '{}' total cost '{}'.\n", characterPlayer.getCompleteNameRepresentation(), cost);
        return cost;
    }

    public static int logCost(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        return logCost(characterPlayer, 0, 0);
    }

    public static int logCost(CharacterPlayer characterPlayer, int extraSkillPoints, int extraCharacteristicsPoints)
            throws InvalidXmlElementException {
        int cost = 0;
        CostCalculatorLog.info(CostCalculator.class.getName(), "####################### ");
        CostCalculatorLog.info(CostCalculator.class.getName(), "\t{}", characterPlayer.getCompleteNameRepresentation());
        CostCalculatorLog.info(CostCalculator.class.getName(), "####################### ");
        if (characterPlayer.getRace() != null) {
            cost += characterPlayer.getRace().getCost();
            CostCalculatorLog.info(CostCalculator.class.getName(), "Race cost '{}'.", characterPlayer.getRace().getCost());
        }
        cost += getCharacteristicsCost(characterPlayer, extraCharacteristicsPoints);
        CostCalculatorLog.info(CostCalculator.class.getName(),
                "Characteristics cost: " + getCharacteristicsCost(characterPlayer, extraCharacteristicsPoints));
        cost += getSkillCosts(characterPlayer, extraSkillPoints);
        CostCalculatorLog.info(CostCalculator.class.getName(),
                "Skills cost: " + getSkillCosts(characterPlayer, extraSkillPoints));
        cost += getTraitsCosts(characterPlayer);
        CostCalculatorLog.info(CostCalculator.class.getName(), "Traits cost '{}'.", getTraitsCosts(characterPlayer));
        cost += getPsiPowersCosts(characterPlayer);
        CostCalculatorLog.info(CostCalculator.class.getName(),
                "Psi powers cost: " + getPsiPowersCosts(characterPlayer));
        cost += getCyberneticsCost(characterPlayer);
        CostCalculatorLog.info(CostCalculator.class.getName(),
                "Cybernetics cost: " + getCyberneticsCost(characterPlayer));
        CostCalculatorLog.info(CostCalculator.class.getName(), "Total cost '{}'.\n", cost);
        return cost;
    }

    private static int getCharacteristicsCost(CharacterPlayer characterPlayer, int extraCharacteristicsPoints) {
        return (characterPlayer.getCharacteristicsTotalPoints() - Math.max(CharacteristicName.values().length,
                (FreeStyleCharacterCreation.getCharacteristicsPoints(characterPlayer.getInfo().getAge()))
                        + extraCharacteristicsPoints))
                * CHARACTERISTIC_EXTRA_POINTS_COST;
    }

    private static int getSkillCosts(CharacterPlayer characterPlayer, int extraSkillPoints)
            throws InvalidXmlElementException {
        return (characterPlayer.getSkillsTotalPoints() - Math.max(0,
                (FreeStyleCharacterCreation.getSkillsPoints(characterPlayer.getInfo().getAge())) + extraSkillPoints))
                * SKILL_EXTRA_POINTS_COST;
    }

    private static int getTraitsCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        int cost = 0;
        cost += getBlessingCosts(characterPlayer);
        cost += getBeneficesCosts(characterPlayer);
        return cost - FreeStyleCharacterCreation.getTraitsPoints(characterPlayer.getInfo().getAge());
    }

    private static int getBlessingCosts(CharacterPlayer characterPlayer) {
        int cost = 0;
        for (final Blessing blessing : characterPlayer.getAllBlessings()) {
            cost += blessing.getCost();
        }
        return cost;
    }

    public static int getBlessingCosts(List<Blessing> blessings) {
        int cost = 0;
        for (final Blessing blessing : blessings) {
            cost += blessing.getCost();
        }
        return cost;
    }

    public int getBeneficesCosts() throws InvalidXmlElementException {
        return getBeneficesCosts(characterPlayer);
    }

    public static int getBeneficesCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        int cost = 0;
        for (final AvailableBenefice benefit : characterPlayer.getAllBenefices()) {
            cost += benefit.getCost();
        }
        for (final AvailableBenefice affliction : characterPlayer.getAfflictions()) {
            cost += affliction.getCost();
        }
        return cost;
    }

    private int getPsiPathsCosts() {
        return getPsiPathsCosts(characterPlayer);
    }

    private static int getPsiPathsCosts(CharacterPlayer characterPlayer) {
        int cost = 0;
        for (final Entry<String, List<OccultismPower>> occultismPathEntry : characterPlayer.getSelectedPowers()
                .entrySet()) {
            for (final OccultismPower occultismPower : occultismPathEntry.getValue()) {
                cost += occultismPower.getLevel() * OCCULTISM_POWER_LEVEL_COST;
            }
        }
        return cost;
    }

    private static int getPsiPowersCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
        int cost = getPsiPathsCosts(characterPlayer);
        cost += characterPlayer.getExtraWyrd() * EXTRA_WYRD_COST;
        cost += Math.max(0,
                (characterPlayer.getBasicPsiqueLevel(
                        OccultismTypeFactory.getPsi(characterPlayer.getLanguage(), characterPlayer.getModuleName()))
                        - (characterPlayer.getRace() != null ? characterPlayer.getRace().getPsi() : 0))
                        * PSIQUE_LEVEL_COST);
        cost += Math.max(0,
                (characterPlayer.getBasicPsiqueLevel(
                        OccultismTypeFactory.getTheurgy(characterPlayer.getLanguage(), characterPlayer.getModuleName()))
                        - (characterPlayer.getRace() != null ? characterPlayer.getRace().getTheurgy() : 0))
                        * PSIQUE_LEVEL_COST);
        return cost;
    }

    private static int getCyberneticsCost(CharacterPlayer characterPlayer) {
        int cost = 0;
        for (final ICyberneticDevice device : characterPlayer.getCybernetics()) {
            cost += device.getPoints() * CYBERNETIC_DEVICE_COST;
        }
        return cost;
    }

    public CostCalculatorModificationHandler getCostCharacterModificationHandler() {
        if (costCalculatorModificationHandler == null) {
            costCalculatorModificationHandler = new CostCalculatorModificationHandler();
        }
        return costCalculatorModificationHandler;
    }

    public int getCurrentCharacteristicPoints() {
        return currentCharacteristicPoints.get();
    }

    public int getCurrentCharacteristicExtraPoints() {
        return currentCharacteristicExtraPoints.get();
    }

    public int getCurrentSkillsPoints() {
        return currentSkillsPoints.get();
    }

    public int getCurrentSkillsExtraPoints() {
        return currentSkillsExtraPoints.get();
    }

    public int getCurrentTraitsPoints() {
        return currentTraitsPoints.get();
    }

    public int getCurrentTraitsExtraPoints() {
        return currentTraitsExtraPoints.get();
    }

    public int getCurrentOccultismLevelExtraPoints() {
        return currentOccultismLevelExtraPoints.get();
    }

    public int getCurrentOccultismPowersExtraPoints() {
        return currentOccultismPowersExtraPoints.get();
    }

    public int getCurrentWyrdExtraPoints() {
        return currentWyrdExtraPoints.get();
    }

    public int getCurrentCyberneticsExtraPoints() {
        return currentCyberneticsExtraPoints.get();
    }

    public float getFireBirdsExpend() {
        return fireBirdsExpend;
    }
}
