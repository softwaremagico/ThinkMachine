package com.softwaremagico.tm.character.occultism;

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
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.InvalidBeneficeException;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class RandomPsique extends RandomSelector<OccultismType> {

    public RandomPsique(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences)
            throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidRandomElementSelectedException, InvalidXmlElementException {
        // Select which type of psique.
        final OccultismType selectedOccultismType = selectElementByWeight();
        RandomGenerationLog.info(this.getClass().getName(), "Assinged psique '{}'.", selectedOccultismType);
        // Select a level of psique.
        final int level = assignLevelOfPsique(selectedOccultismType);
        RandomGenerationLog.info(this.getClass().getName(), "Assinged psique level of '{}'.", level);
        // Assign to the character.
        getCharacterPlayer().setOccultismLevel(selectedOccultismType, level);
    }

    @Override
    protected Collection<OccultismType> getAllElements() throws InvalidXmlElementException {
        return OccultismTypeFactory.getInstance().getElements(getCharacterPlayer().getLanguage(),
                getCharacterPlayer().getModuleName());
    }

    @Override
    protected int getWeight(OccultismType element) throws InvalidRandomElementSelectedException {
        //Check if you have a path for your faction.
        try {
            for (final OccultismPath occultismPath : OccultismPathFactory.getInstance().getElements(getCharacterPlayer().getLanguage(),
                    getCharacterPlayer().getModuleName())) {
                if (!occultismPath.getFactionsAllowed().isEmpty() && occultismPath.getFactionsAllowed().contains(getCharacterPlayer().getFaction())) {
                    if (Objects.equals(element, occultismPath.getOccultismType())) {
                        return 1;
                    } else {
                        throw new InvalidRandomElementSelectedException("Faction '" + getCharacterPlayer().getFaction() + "' cannot use '" + element + "'.");
                    }
                }
            }
        } catch (InvalidXmlElementException e) {
            RandomGenerationLog.errorMessage(this.getClass().getName(), e);
        }

        // Church factions must have always theurgy.
        if (Objects.equals(element,
                OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName()))) {
            if (getCharacterPlayer().getFaction() != null && (getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.CHURCH ||
                    getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.MINOR_CHURCH)) {
                throw new InvalidRandomElementSelectedException("Psi not allowed to church factions.");
            }
        }

        //remove not selected types
        final OccultismTypePreferences psiqueLevelSelector = OccultismTypePreferences.getSelected(getPreferences());
        if (psiqueLevelSelector != OccultismTypePreferences.ANY &&
                !Objects.equals(psiqueLevelSelector.name().toLowerCase(), element.getName().toLowerCase())) {
            return 0;
        }

        // No church factions have psi or cannot have any path.
        if (Objects.equals(element, OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage(),
                getCharacterPlayer().getModuleName()))) {
            if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() != FactionGroup.CHURCH &&
                    getCharacterPlayer().getFaction().getFactionGroup() != FactionGroup.MINOR_CHURCH) {
                return 0;
            }
        }

        //No theurgy, then psi has better probability
        return VERY_GOOD_PROBABILITY;
    }

    private int assignLevelOfPsique(OccultismType psique) throws InvalidXmlElementException {
        // A curse does not allow occultism.
        try {
            if (getCharacterPlayer().getAfflictions().contains(
                    AvailableBeneficeFactory.getInstance().getElement("noOccult", getCharacterPlayer().getLanguage(),
                            getCharacterPlayer().getModuleName())) ||
                    getCharacterPlayer().getAfflictions().contains(
                            AvailableBeneficeFactory.getInstance().getElement("noPsi", getCharacterPlayer().getLanguage(),
                                    getCharacterPlayer().getModuleName()))) {
                return 0;
            }
        } catch (InvalidBeneficeException e) {
            // Module without noocc benefice.
        }
        final IGaussianDistribution psiqueLevelSelector = OccultismLevelPreferences.getSelected(getPreferences());
        int maxLevelSelected = psiqueLevelSelector.randomGaussian();
        if (maxLevelSelected > psiqueLevelSelector.maximum()) {
            maxLevelSelected = psiqueLevelSelector.maximum();
        }

        //Check if there are points enough
        final DifficultLevelPreferences difficultyLevel = DifficultLevelPreferences.getSelected(getPreferences());
        final int remainingPoints = FreeStyleCharacterCreation
                .getFreeAvailablePoints(getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace())
                - CostCalculator.getCost(getCharacterPlayer(), difficultyLevel.getSkillsBonus(),
                difficultyLevel.getCharacteristicsBonus());

        //Ensure enough points also for acquiring paths.
        if (maxLevelSelected * CostCalculator.PSIQUE_LEVEL_COST > remainingPoints - maxLevelSelected) {
            maxLevelSelected = (remainingPoints - maxLevelSelected) / CostCalculator.PSIQUE_LEVEL_COST;
        }

        if (maxLevelSelected < 0) {
            maxLevelSelected = 0;
        }

        return maxLevelSelected;
    }

    @Override
    protected void assignIfMandatory(OccultismType element) throws InvalidXmlElementException,
            ImpossibleToAssignMandatoryElementException {
        return;
    }

    @Override
    protected void assignMandatoryValues(Set<OccultismType> mandatoryValues) throws InvalidXmlElementException {
        return;
    }
}
