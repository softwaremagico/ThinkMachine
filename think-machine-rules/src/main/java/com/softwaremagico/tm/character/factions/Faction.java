package com.softwaremagico.tm.character.factions;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.RestrictedBenefice;
import com.softwaremagico.tm.character.benefices.SuggestedBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingClassification;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.log.MachineLog;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Faction extends Element<Faction> {
    private final FactionGroup factionGroup;
    private final Set<FactionRankTranslation> ranksTranslations = new HashSet<>();
    private Set<Blessing> blessings = null;
    private Set<AvailableBenefice> benefices = null;
    private Set<SuggestedBenefice> suggestedBenefices = null;
    private Set<RestrictedBenefice> restrictedBenefices = null;
    private Boolean isOnlyForHuman;

    public Faction(String id, String name, String description, FactionGroup factionGroup, String language,
                   String moduleName) {
        super(id, name, description, language, moduleName);
        this.factionGroup = factionGroup;
    }

    public FactionGroup getFactionGroup() {
        return factionGroup;
    }

    public void addRankTranslation(FactionRankTranslation factionRank) {
        ranksTranslations.add(factionRank);
    }

    public Set<FactionRankTranslation> getRanksTranslations() {
        return ranksTranslations;
    }

    public FactionRankTranslation getRankTranslation(String rankId) {
        for (final FactionRankTranslation factionRankTranslation : getRanksTranslations()) {
            if (Objects.equals(factionRankTranslation.getId(), rankId)) {
                return factionRankTranslation;
            }
        }
        return null;
    }

    public Set<Blessing> getBlessings() {
        if (blessings == null) {
            // Blessings are not read with factions due to a loop
            // factions->blessings->skills->factions
            try {
                FactionsFactory.getInstance().setBlessings(this);
            } catch (InvalidFactionException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return blessings;
    }

    public Set<Blessing> getBlessings(BlessingClassification classification) {
        final Set<Blessing> curses = new HashSet<>();
        for (final Blessing blessing : getBlessings()) {
            if (blessing.getBlessingClassification() == classification) {
                curses.add(blessing);
            }
        }
        return curses;
    }

    public Set<AvailableBenefice> getBenefices() {
        if (benefices == null) {
            // Benefices are not read with factions due to a loop
            // factions->benefices->skills->factions
            try {
                FactionsFactory.getInstance().setBenefices(this);
            } catch (InvalidFactionException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return benefices;
    }

    public Set<SuggestedBenefice> getSuggestedBenefices() {
        if (suggestedBenefices == null) {
            // Benefices are not read with factions due to a loop
            // factions->benefices->skills->factions
            try {
                FactionsFactory.getInstance().setSuggestedBenefices(this, getLanguage());
            } catch (NumberFormatException | InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return suggestedBenefices;
    }

    public Set<RestrictedBenefice> getRestrictedBenefices() {
        if (restrictedBenefices == null) {
            // Benefices are not read with factions due to a loop
            // factions->benefices->skills->factions
            try {
                FactionsFactory.getInstance().setRestrictedBenefices(this, getLanguage());
            } catch (NumberFormatException | InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return restrictedBenefices;
    }

    public void setBlessings(Set<Blessing> blessings) {
        this.blessings = blessings;
    }

    public void setBenefices(Set<AvailableBenefice> benefices) {
        this.benefices = benefices;
    }

    public void setSuggestedBenefices(Set<SuggestedBenefice> suggestedBenefices) {
        this.suggestedBenefices = suggestedBenefices;
    }

    public void setRestrictedBenefices(Set<RestrictedBenefice> restrictedBenefices) {
        this.restrictedBenefices = restrictedBenefices;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public boolean isOnlyForHuman() {
        if (isOnlyForHuman == null) {
            try {
                isOnlyForHuman = getRestrictedToRaces().size() == 1 &&
                        getRestrictedToRaces().contains(RaceFactory.getInstance().getElement("human", getLanguage(), getModuleName()));
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
                isOnlyForHuman = true;
            }
        }
        return isOnlyForHuman;
    }
}
