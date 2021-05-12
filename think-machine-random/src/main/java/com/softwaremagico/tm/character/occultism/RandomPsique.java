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
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.OccultismLevelPreferences;
import com.softwaremagico.tm.random.selectors.OccultismTypePreference;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class RandomPsique extends RandomSelector<OccultismType> {

    public RandomPsique(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences)
            throws InvalidXmlElementException {
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
        // Church factions must have always theurgy.
        if (Objects.equals(element,
                OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName()))) {
            if (getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.CHURCH) {
                throw new InvalidRandomElementSelectedException("Psi not allowed to church factions");
            }
        }

        //remove not selected types
        final OccultismTypePreference psiqueLevelSelector = OccultismTypePreference.getSelected(getPreferences());
        if (psiqueLevelSelector != OccultismTypePreference.ANY &&
                !Objects.equals(psiqueLevelSelector.name().toLowerCase(), element.getName().toLowerCase())) {
            return 0;
        }

        // No church factions usually have psi.
        if (Objects.equals(element, OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage(),
                getCharacterPlayer().getModuleName()))) {
            if (getCharacterPlayer().getFaction().getFactionGroup() != FactionGroup.CHURCH) {
                return BASIC_PROBABILITY;
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
