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
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.*;

import java.util.*;

public class RandomPsiquePath extends RandomSelector<OccultismPath> {
    private static final int TOTAL_PDF_PSI_ROWS = 7;

    private int totalPowers;

    public RandomPsiquePath(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences, Set<OccultismPath> mandatoryOccultismPaths)
            throws InvalidXmlElementException {
        super(characterPlayer, null, preferences, mandatoryOccultismPaths, new HashSet<>());
    }

    @Override
    public void assign() throws InvalidRandomElementSelectedException, InvalidXmlElementException {
        // Random number of paths.
        final IGaussianDistribution pathNumber = OccultismPathLevelPreferences.getSelected(getPreferences());
        final int totalPaths = pathNumber.randomGaussian();
        totalPowers = getCharacterPlayer().getTotalSelectedPowers();
        for (int i = totalPowers; i < totalPaths; i++) {
            try {
                final OccultismPath selectedOccultismPath = selectElementByWeight();
                // Select a level of psique.
                final int pathLevel = assignLevelOfPath(selectedOccultismPath);
                // Assign path to the character.
                assignPowersOfPath(selectedOccultismPath, pathLevel);
                removeElementWeight(selectedOccultismPath);
            } catch (InvalidRandomElementSelectedException irese) {
                // No elements to select, probably no power is available.
            }
        }

    }

    @Override
    protected Collection<OccultismPath> getAllElements() throws InvalidXmlElementException {
        return OccultismPathFactory.getInstance().getElements(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName());
    }

    @Override
    protected int getWeight(OccultismPath element) throws InvalidRandomElementSelectedException {
        // Other factions path are forbidden
        if (!element.getFactionsAllowed().isEmpty()
                && !element.getFactionsAllowed().contains(getCharacterPlayer().getFaction())) {
            throw new InvalidRandomElementSelectedException("Path '" + element + "' restricted to faction '"
                    + element.getFactionsAllowed() + "'.");
        }

        // Only paths with psique level.
        try {
            for (final OccultismType occultismType : OccultismTypeFactory.getInstance().getElements(
                    getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())) {
                if (getCharacterPlayer().getOccultismLevel(occultismType) == 0) {
                    if (Objects.equals(element.getOccultismType(), occultismType)) {
                        throw new InvalidRandomElementSelectedException(
                                "Character must have a minimum psi level for path '" + element + "'.");
                    }
                }
            }
        } catch (InvalidXmlElementException e) {
            RandomGenerationLog.errorMessage(this.getClass().getName(), e);
        }

        // Combat psi characters prefer specific paths.
        final CombatPreferences combatPreferences = CombatPreferences.getSelected(getPreferences());
        if (combatPreferences == CombatPreferences.BELLIGERENT
                && (element.getId().equals("farHand") || element.getId().equals("soma"))) {
            return FAIR_PROBABILITY;
        }
        return 1;
    }

    private int assignLevelOfPath(OccultismPath path) {
        // Use psique level preferences for the path level.
        final IGaussianDistribution psiqueLevelSelector = OccultismLevelPreferences.getSelected(getPreferences());
        int maxLevelSelected = psiqueLevelSelector.randomGaussian();
        if (maxLevelSelected > psiqueLevelSelector.maximum()) {
            maxLevelSelected = psiqueLevelSelector.maximum();
        }
        if (maxLevelSelected > getCharacterPlayer().getOccultismLevel(path.getOccultismType())) {
            maxLevelSelected = getCharacterPlayer().getOccultismLevel(path.getOccultismType());
        }
        return maxLevelSelected;
    }

    private void assignPowersOfPath(OccultismPath path, int maxLevelSelected) throws InvalidXmlElementException {
        final DifficultLevelPreferences difficultyLevel = DifficultLevelPreferences.getSelected(getPreferences());

        int remainingPoints = FreeStyleCharacterCreation
                .getFreeAvailablePoints(getCharacterPlayer().getInfo().getAge())
                - CostCalculator.getCost(getCharacterPlayer(), difficultyLevel.getSkillsBonus(),
                difficultyLevel.getCharacteristicsBonus());
        // Select powers to set.
        final List<OccultismPower> powersToAdd = new ArrayList<>();

        final SpecializationPreferences specializationPreferences = SpecializationPreferences
                .getSelected(getPreferences());
        // Psi must have at least one power by level.
        if (Objects.equals(path.getOccultismType(),
                OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName()))) {
            for (int i = 1; i <= maxLevelSelected; i++) {
                final List<OccultismPower> powers = new ArrayList<>(path.getPowersOfLevel(i));
                // If has more than one power at one level, choose one of them
                // at least.
                if (!powers.isEmpty()) {
                    Collections.shuffle(powers);
                    powersToAdd.add(powers.get(0));
                }
            }
        }
        // Theurgy does not need to have all levels.
        if (Objects.equals(path.getOccultismType(), OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage(),
                getCharacterPlayer().getModuleName()))) {
            // Levels to add.
            final int numberOfPowers = specializationPreferences.randomGaussian();
            final List<OccultismPower> powers = new ArrayList<>(path.getOccultismPowers().values());
            Collections.shuffle(powers);
            while (numberOfPowers > 0 && !powers.isEmpty()) {
                // It is possible to add this power.
                if (powers.get(0).getLevel() <= maxLevelSelected) {
                    powersToAdd.add(powers.get(0));
                }
                powers.remove(0);
            }
        }

        // Add selected powers if enough point
        for (final OccultismPower power : powersToAdd) {
            // Enough points
            if (totalPowers >= TOTAL_PDF_PSI_ROWS) {
                RandomGenerationLog.info(this.getClass().getName(), "No more psi power room is left.");
                break;
            }
            if (remainingPoints - power.getLevel() * CostCalculator.OCCULTISM_POWER_LEVEL_COST >= 0) {
                getCharacterPlayer().addOccultismPower(power);
                RandomGenerationLog.info(this.getClass().getName(), "Assinged power '{}' to path '{}'.", power, path);
                remainingPoints -= power.getLevel() * CostCalculator.OCCULTISM_POWER_LEVEL_COST;
                totalPowers++;
            }

        }
    }

    @Override
    protected void assignIfMandatory(OccultismPath path) throws InvalidXmlElementException,
            ImpossibleToAssignMandatoryElementException {
        // Own factions paths are a must.
        if (path.getFactionsAllowed().contains(getCharacterPlayer().getFaction())) {
            // Select a level of psique.
            final int pathLevel = assignLevelOfPath(path);
            // Assign path to the character.
            assignPowersOfPath(path, pathLevel);
            removeElementWeight(path);
        }
    }

    @Override
    protected void assignMandatoryValues(Set<OccultismPath> mandatoryValues) {
        mandatoryValues.forEach(mandatoryValue -> {
            final int pathLevel = assignLevelOfPath(mandatoryValue);
            try {
                assignPowersOfPath(mandatoryValue, pathLevel);
            } catch (InvalidXmlElementException e) {
                RandomGenerationLog.errorMessage(this.getClass().getName(), e);
            }
        });
    }
}
