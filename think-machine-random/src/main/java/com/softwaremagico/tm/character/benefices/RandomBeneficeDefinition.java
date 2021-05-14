package com.softwaremagico.tm.character.benefices;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.combat.CombatStyleGroup;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.exceptions.NotExistingPreferenceException;
import com.softwaremagico.tm.random.selectors.*;

import java.util.*;

public class RandomBeneficeDefinition extends RandomSelector<BeneficeDefinition> {
    private static final int MAX_AFFLICTIONS = 2;
    private static final String CASH_BENEFICE_ID = "cash";
    private static final String ESCAPED_PREFIX = "escaped";
    private Integer totalCombatActions;
    private final Set<AvailableBenefice> suggestedAvailableBenefices;

    public RandomBeneficeDefinition(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences)
            throws InvalidXmlElementException, RestrictedElementException {
        this(characterPlayer, preferences, new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public RandomBeneficeDefinition(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences,
                                    Set<BeneficeDefinition> mandatoryBenefices, Set<BeneficeDefinition> suggestedBenefices,
                                    Set<AvailableBenefice> suggestedAvailableBenefices)
            throws InvalidXmlElementException, RestrictedElementException {
        super(characterPlayer, null, preferences, mandatoryBenefices, suggestedBenefices);
        this.suggestedAvailableBenefices = suggestedAvailableBenefices;
    }

    @Override
    public void assign() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        // Later, the others.
        while (CostCalculator.getBeneficesCosts(getCharacterPlayer()) < FreeStyleCharacterCreation
                .getTraitsPoints(getCharacterPlayer().getInfo().getAge()) && !getWeightedElements().isEmpty()) {
            // Select a benefice
            final BeneficeDefinition selectedBenefice = selectElementByWeight();
            try {
                assignBenefice(selectedBenefice,
                        FreeStyleCharacterCreation.getTraitsPoints(getCharacterPlayer().getInfo().getAge())
                                - CostCalculator.getBeneficesCosts(getCharacterPlayer()));
            } catch (RestrictedElementException e) {
                //Ignore restricted benefice.
            }
        }
    }

    protected void assignBenefice(BeneficeDefinition benefice) throws InvalidXmlElementException, RestrictedElementException {
        final Set<AvailableBenefice> beneficeLevels = AvailableBeneficeFactory.getInstance()
                .getAvailableBeneficesByDefinition(getCharacterPlayer().getLanguage(),
                        getCharacterPlayer().getModuleName(), benefice);
        if (beneficeLevels.size() != 1) {
            throw new InvalidBeneficeException("Only benefices without multiples specializations can be use here.");
        }
        final AvailableBenefice availableBenefice = beneficeLevels.stream().findAny().get();
        addBenefice(availableBenefice);
        removeElementWeight(availableBenefice.getBeneficeDefinition());
    }

    protected void assignBenefice(BeneficeDefinition selectedBenefice, int maxPoints)
            throws InvalidXmlElementException, RestrictedElementException {
        // Select the range of the benefice.
        final AvailableBenefice selectedBeneficeWithLevel = assignLevelOfBenefice(selectedBenefice, maxPoints);
        if (selectedBeneficeWithLevel != null) {
            // Only a few afflictions.
            if (selectedBeneficeWithLevel.getBeneficeClassification() == BeneficeClassification.AFFLICTION) {
                if (getCharacterPlayer().getAfflictions().size() >= MAX_AFFLICTIONS) {
                    //Not add more afflictions
                    removeElementWeight(selectedBenefice);
                    return;
                }
            }

            //Only elements with enough Tech level.
            if (selectedBeneficeWithLevel.getBeneficeDefinition().getGroup() == BeneficeGroup.TECHNOLOGY) {
                try {
                    final Weapon weapon = WeaponFactory.getInstance().getElement(selectedBenefice.getId(), getCharacterPlayer().getLanguage(),
                            getCharacterPlayer().getModuleName());
                    if (weapon.getTechLevel() > getCharacterPlayer().getCharacteristicValue(CharacteristicName.TECH)) {
                        //Not enough tech level.
                        removeElementWeight(selectedBenefice);
                        return;
                    }
                } catch (InvalidXmlElementException e) {
                    // Benefice is not a weapon.
                }
                try {
                    final Armour armour = ArmourFactory.getInstance().getElement(selectedBenefice.getId(), getCharacterPlayer().getLanguage(),
                            getCharacterPlayer().getModuleName());
                    if (armour.getTechLevel() > getCharacterPlayer().getCharacteristicValue(CharacteristicName.TECH)) {
                        //Not enough tech level.
                        removeElementWeight(selectedBenefice);
                        return;
                    }
                } catch (InvalidXmlElementException e) {
                    // Benefice is not an armour.
                }
            }
            addBenefice(selectedBeneficeWithLevel);
        }
        removeElementWeight(selectedBenefice);

        // Only few fighting style by character.
        if (getCharacterPlayer().getSelectedBenefices(BeneficeGroup.FIGHTING).size() >= getTotalCombatActions()) {
            if (selectedBenefice.getGroup().equals(BeneficeGroup.FIGHTING)) {
                for (final BeneficeDefinition beneficeDefinition : BeneficeDefinitionFactory.getInstance().getBenefices(
                        BeneficeGroup.FIGHTING, getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())) {
                    removeElementWeight(beneficeDefinition);
                }
            }
        }

        // Only one 'escaped' benefice
        if (selectedBenefice.getId().startsWith(ESCAPED_PREFIX)) {
            for (final BeneficeDefinition checkBenefice : getAllElements()) {
                if (checkBenefice.getId().startsWith(ESCAPED_PREFIX)) {
                    removeElementWeight(checkBenefice);
                }
            }
        }
    }

    private void addBenefice(AvailableBenefice availableBenefice) throws InvalidBeneficeException, RestrictedElementException {
        try {
            getCharacterPlayer().addBenefice(availableBenefice);
            RandomGenerationLog.info(this.getClass().getName(),
                    "Added benefice '{}'.", availableBenefice);
        } catch (BeneficeAlreadyAddedException e) {
            // If level is bigger... replace it.
            final AvailableBenefice originalBenefice = getCharacterPlayer()
                    .getBenefice(availableBenefice.getBeneficeDefinition().getId());
            if (originalBenefice.getCost() < availableBenefice.getCost()) {
                getCharacterPlayer().removeBenefice(originalBenefice);
                try {
                    getCharacterPlayer().addBenefice(availableBenefice);
                    RandomGenerationLog.info(this.getClass().getName(), "Replacing benefice '{}' with '{}'.",
                            originalBenefice, availableBenefice);
                } catch (BeneficeAlreadyAddedException e1) {
                    RandomGenerationLog.errorMessage(this.getClass().getName(), e1);
                }
            }
        } catch (IncompatibleBeneficeException e) {
            //Incompatible. Cannot be added.
        }
    }

    private int getTotalCombatActions() {
        if (totalCombatActions == null) {
            final CombatActionsPreferences combatActionsPreferences = CombatActionsPreferences.getSelected(getPreferences());
            totalCombatActions = combatActionsPreferences.randomGaussian();
        }
        return totalCombatActions;
    }

    private boolean getCombatStyleGroupSelected(BeneficeDefinition benefice) throws NotExistingPreferenceException {
        final CombatActionsGroupPreferences combatActionsGroupPreferences = CombatActionsGroupPreferences.getSelected(getPreferences());
        if (combatActionsGroupPreferences == null) {
            throw new NotExistingPreferenceException("No combat action group selected.");
        }
        final CombatStyle combatStyle = CombatStyle.getCombatStyle(benefice, getCharacterPlayer().getLanguage(),
                getCharacterPlayer().getModuleName());
        if (combatStyle != null) {
            switch (combatActionsGroupPreferences) {
                case FIGHT:
                    if (combatStyle.getGroup() == CombatStyleGroup.FIGHT) {
                        return true;
                    }
                    break;
                case MELEE:
                    if (combatStyle.getGroup() == CombatStyleGroup.MELEE) {
                        return true;
                    }
                    break;
                case RANGED:
                    if (combatStyle.getGroup() == CombatStyleGroup.RANGED) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    @Override
    protected Collection<BeneficeDefinition> getAllElements() throws InvalidXmlElementException {
        return BeneficeDefinitionFactory.getInstance().getElements(getCharacterPlayer().getLanguage(),
                getCharacterPlayer().getModuleName());
    }

    @Override
    protected int getWeight(BeneficeDefinition benefice) throws InvalidRandomElementSelectedException {
        // No restricted benefices.
        if (benefice.getRestrictedToFactionGroup() != null && getCharacterPlayer().getFaction() != null
                && benefice.getRestrictedToFactionGroup() != getCharacterPlayer().getFaction().getFactionGroup()) {
            throw new InvalidRandomElementSelectedException(
                    "Benefice '" + benefice + "' is restricted to '" + benefice.getRestrictedToFactionGroup() + "'.");
        }

        // No special benefices
        if (benefice.isRestricted()) {
            throw new InvalidRandomElementSelectedException("Benefice '" + benefice + "' is restricted.");
        }

        // No benefices limited to race
        if (!benefice.getRestrictedToRaces().isEmpty() && !benefice.getRestrictedToRaces().contains(getCharacterPlayer().getRace())) {
            throw new InvalidRandomElementSelectedException("Benefice '" + benefice + "' is restricted to races '" + benefice.getRestrictedToRaces() + "'.");
        }

        // Set faction limitations.
        if (getCharacterPlayer().getFaction() != null) {
            for (final RestrictedBenefice restrictedBenefice : getCharacterPlayer().getFaction()
                    .getRestrictedBenefices()) {
                if (Objects.equals(restrictedBenefice.getBeneficeDefinition(), benefice)) {
                    if (restrictedBenefice.getMaxValue() == null) {
                        throw new InvalidRandomElementSelectedException("Benefice '" + benefice
                                + "' is restricted by faction '" + getCharacterPlayer().getFaction() + "'.");
                    }
                    break;
                }
            }
        }

        // Suggested benefices by faction.
        if (getCharacterPlayer().getFaction() != null) {
            for (final SuggestedBenefice suggestedBenefice : getCharacterPlayer().getFaction()
                    .getSuggestedBenefices()) {
                if (Objects.equals(suggestedBenefice.getBeneficeDefinition(), benefice)) {
                    return VERY_GOOD_PROBABILITY;
                }

            }
        }

        // PNJs likes money changes.
        if (benefice.getId().equalsIgnoreCase(CASH_BENEFICE_ID)) {
            return GOOD_PROBABILITY;
        }

        // Add extra probability to fight styles
        final CombatActionsPreferences combatActionsPreferences = CombatActionsPreferences.getSelected(getPreferences());
        if (benefice.getGroup() == BeneficeGroup.FIGHTING && combatActionsPreferences.minimum() > 0) {
            try {
                if (getCombatStyleGroupSelected(benefice)) {
                    return VERY_GOOD_PROBABILITY;
                }
            } catch (NotExistingPreferenceException e) {
                return GOOD_PROBABILITY;
            }
        }

        // No faction preference selected. All benefices has the same
        // probability.
        return 1;
    }

    /**
     * Returns a cost for a benefice depending on the preferences of the character.
     */
    private AvailableBenefice assignLevelOfBenefice(BeneficeDefinition benefice, int maxPoints)
            throws InvalidXmlElementException {

        if (suggestedAvailableBenefices != null) {
            for (final AvailableBenefice availableBenefice : suggestedAvailableBenefices) {
                if (Objects.equals(availableBenefice.getBeneficeDefinition(), benefice)) {
                    return availableBenefice;
                }
            }
        }

        IGaussianDistribution selectedTraitCost = TraitCostPreferences.getSelected(getPreferences());

        if (benefice.getId().equalsIgnoreCase(CASH_BENEFICE_ID)) {
            final DifficultLevelPreferences difficultPreferences = DifficultLevelPreferences
                    .getSelected(getPreferences());
            switch (difficultPreferences) {
                case EASY:
                case VERY_EASY:
                    selectedTraitCost = TraitCostPreferences.LOW;
                    break;
                case MEDIUM:
                case HARD:
                    // Be careful. maxPoints can limit this value.
                    selectedTraitCost = TraitCostPreferences.GOOD;
                    break;
                case VERY_HARD:
                    // Be careful. maxPoints can limit this value.
                    selectedTraitCost = TraitCostPreferences.VERY_HIGH;
                    break;
            }
        }

        if (benefice.getGroup() != null && benefice.getGroup().equals(BeneficeGroup.STATUS)) {
            // Status has also an special preference.
            final IGaussianDistribution selectedStatus = StatusPreferences.getSelected(getPreferences());
            if (selectedStatus != null) {
                selectedTraitCost = selectedStatus;
            }
        }

        int maxRangeSelected = selectedTraitCost.randomGaussian();

        // Suggested benefices values by faction.
        if (getCharacterPlayer().getFaction() != null) {
            for (final SuggestedBenefice suggestedBenefice : getCharacterPlayer().getFaction()
                    .getSuggestedBenefices()) {
                if (Objects.equals(suggestedBenefice.getBeneficeDefinition(), benefice)) {
                    if (suggestedBenefice.getValue() != null) {
                        RandomGenerationLog.debug(this.getClass().getName(), "Suggested benefice '{}' has a value of '{}'.",
                                benefice, suggestedBenefice.getValue());
                        maxRangeSelected = suggestedBenefice.getValue();
                    }
                }
            }
        }

        // Maximum points to be spent.
        if (maxRangeSelected > maxPoints) {
            maxRangeSelected = maxPoints;
        }

        // Set faction limitations.
        if (getCharacterPlayer().getFaction() != null) {
            for (final RestrictedBenefice restrictedBenefice : getCharacterPlayer().getFaction()
                    .getRestrictedBenefices()) {
                if (Objects.equals(restrictedBenefice.getBeneficeDefinition(), benefice)) {
                    if (maxRangeSelected > restrictedBenefice.getMaxValue()) {
                        RandomGenerationLog.debug(this.getClass().getName(), "Suggested benefice '{}' has a restriction of value of '{}}'.",
                                benefice, restrictedBenefice.getMaxValue());
                        maxRangeSelected = restrictedBenefice.getMaxValue();
                    }
                    break;
                }
            }
        }

        RandomGenerationLog.info(this.getClass().getName(),
                "MaxPoints of '{}' are '{}'.", benefice, maxRangeSelected);
        Set<AvailableBenefice> beneficeLevels = AvailableBeneficeFactory.getInstance()
                .getAvailableBeneficesByDefinition(getCharacterPlayer().getLanguage(),
                        getCharacterPlayer().getModuleName(), benefice);
        // Cannot be null, but...
        if (beneficeLevels == null) {
            beneficeLevels = new HashSet<>();
        }
        final List<AvailableBenefice> sortedBenefices = new ArrayList<>(beneficeLevels);
        // Sort by cost (descending). Adding if a benefice has preferences
        // (ascending).
        sortedBenefices.sort((o1, o2) -> {
            final double o1Preferred = getRandomDefinitionBonus(o1.getRandomDefinition());
            final double o2Preferred = getRandomDefinitionBonus(o2.getRandomDefinition());

            if ((int) (o1Preferred - o2Preferred) != 0) {
                return (int) (o1Preferred - o2Preferred);
            }
            return Integer.compare(o2.getCost(), o1.getCost());
        });
        RandomGenerationLog.info(this.getClass().getName(),
                "Available benefice levels of '{}' are '{}'.", benefice, sortedBenefices);
        for (final AvailableBenefice availableBenefice : sortedBenefices) {
            try {
                validateElement(availableBenefice.getRandomDefinition());
            } catch (InvalidRandomElementSelectedException e) {
                continue;
            }
            if (Math.abs(availableBenefice.getCost()) <= maxRangeSelected
                    // Or is mandatory and is the last of the list.
                    || (isMandatory(availableBenefice.getBeneficeDefinition())
                    && Objects.equals(availableBenefice, sortedBenefices.get(sortedBenefices.size() - 1)))) {
                return availableBenefice;
            }
        }
        return null;
    }

    @Override
    protected void assignIfMandatory(BeneficeDefinition benefice)
            throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException, RestrictedElementException {
        // Set status of the character.
        try {
            if ((benefice.getGroup() != null && benefice.getGroup().equals(BeneficeGroup.STATUS))
                    && getWeight(benefice) > 0 && getCharacterPlayer().getFaction() != null
                    && Objects.equals(benefice.getRestrictedToFactionGroup(),
                    getCharacterPlayer().getFaction().getFactionGroup())) {
                final IGaussianDistribution selectedStatus = StatusPreferences.getSelected(getPreferences());
                if (selectedStatus != null) {
                    RandomGenerationLog.debug(this.getClass().getName(),
                            "Searching grade '{}' of benefice '{}'.", selectedStatus.maximum(), benefice);
                    assignBenefice(benefice, selectedStatus.maximum());
                }
            }
        } catch (InvalidRandomElementSelectedException e) {
            // Weight is zero. Do nothing.
        }

        // Money requirements for equipment.
        if (Objects.equals(benefice.getId(), "cash")) {
            final IGaussianDistribution selectedCash = CashPreferences.getSelected(getPreferences());
            if (selectedCash != null) {
                RandomGenerationLog.debug(this.getClass().getName(),
                        "Searching grade '{}' of benefice '{}'.", selectedCash.maximum(), benefice);
                assignBenefice(benefice, selectedCash.maximum());
            }
        }

        //Some combat styles are mandatory.
        if (benefice.getGroup() == BeneficeGroup.FIGHTING &&
                getCharacterPlayer().getSelectedBenefices(BeneficeGroup.FIGHTING).size() < getTotalCombatActions()) {
            try {
                if (getCombatStyleGroupSelected(benefice)) {
                    assignBenefice(benefice);
                }
            } catch (NotExistingPreferenceException e) {
                //No special group selected, choose any.
                assignBenefice(benefice);
            }
        }
    }

    @Override
    protected void assignMandatoryValues(Set<BeneficeDefinition> mandatoryValues) throws InvalidXmlElementException, RestrictedElementException {
        for (final BeneficeDefinition selectedBenefice : mandatoryValues) {
            // Mandatory benefices can exceed the initial traits points.
            assignBenefice(selectedBenefice,
                    FreeStyleCharacterCreation.getFreeAvailablePoints(getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace()));
        }
    }
}
