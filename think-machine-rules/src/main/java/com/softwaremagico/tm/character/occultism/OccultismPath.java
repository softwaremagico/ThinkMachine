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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.ElementClassification;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.races.Race;

import java.util.*;

public class OccultismPath extends Element<OccultismPath> {

    private final OccultismType occultismType;
    private final Map<String, OccultismPower> occultismPowers;
    private final Set<Faction> factionsAllowed;
    private final ElementClassification classification;

    public OccultismPath(String id, String name, String description, String language, String moduleName, OccultismType occultismType,
                         Set<Faction> allowedFactions, ElementClassification classification) {
        super(id, name, description, language, moduleName);
        this.occultismType = occultismType;
        occultismPowers = new HashMap<>();
        this.factionsAllowed = allowedFactions;
        this.classification = classification;
    }

    public OccultismType getOccultismType() {
        return occultismType;
    }

    public Map<String, OccultismPower> getOccultismPowers() {
        return occultismPowers;
    }

    /**
     * Gets the previous level powers form a power. At least one of them must be
     * aquired to purchase this power if is a psi path.
     *
     * @param power the power that has one level more than the previous one
     * @return A set with one or more powers for one level.
     */
    public Set<OccultismPower> getPreviousLevelPowers(OccultismPower power) {
        final Integer previousLevel = getPreviousLevelWithPowers(power);
        if (previousLevel != null) {
            return getPowersOfLevel(previousLevel);
        }
        return new HashSet<>();
    }

    private Integer getPreviousLevelWithPowers(OccultismPower power) {
        final List<OccultismPower> powersOfPath = new ArrayList<>(occultismPowers.values());

        // Sort by level inverse.
        powersOfPath.sort((power0, power1) -> {
            if (power1.getLevel() != power1.getLevel()) {
                return power1.getLevel() - power0.getLevel();
            }
            return power1.compareTo(power0);
        });

        // From up to down.
        for (final OccultismPower next : powersOfPath) {
            if (next.getLevel() < power.getLevel()) {
                return next.getLevel();
            }
        }
        return null;
    }

    public Set<OccultismPower> getPowersOfLevel(int level) {
        final Set<OccultismPower> powersOfLevel = new HashSet<>();
        for (final OccultismPower power : getOccultismPowers().values()) {
            if (power.getLevel() == level) {
                powersOfLevel.add(power);
            }
        }
        return powersOfLevel;
    }

    public Set<Faction> getFactionsAllowed() {
        return factionsAllowed;
    }

    public ElementClassification getClassification() {
        return classification;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public void setRestrictedToRaces(Set<Race> restrictedToRaces) {
        super.setRestrictedToRaces(restrictedToRaces);
        occultismPowers.forEach((s, occultismPower) -> occultismPower.setRestrictedToRaces(restrictedToRaces));
    }

    @Override
    public void setRestrictedToFactions(Set<Faction> restrictedToFactions) {
        super.setRestrictedToFactions(restrictedToFactions);
        occultismPowers.forEach((s, occultismPower) -> occultismPower.setRestrictedToFactions(restrictedToFactions));
    }

    @Override
    public void setRestricted(boolean restricted) {
        super.setRestricted(restricted);
        occultismPowers.forEach((s, occultismPower) -> occultismPower.setRestricted(restricted));
    }

    @Override
    public void setRestrictedToFactionGroup(FactionGroup factionGroup) {
        super.setRestrictedToFactionGroup(factionGroup);
        occultismPowers.forEach((s, occultismPower) -> occultismPower.setRestrictedToFactionGroup(factionGroup));
    }

    @Override
    public void setOfficial(boolean official) {
        super.setOfficial(official);
        occultismPowers.forEach((s, occultismPower) -> occultismPower.setOfficial(official));
    }
}
