package com.softwaremagico.tm.random.predefined.characters;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2021 Softwaremagico
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

import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.random.predefined.IRandomPredefined;
import com.softwaremagico.tm.random.predefined.RandomPredefined;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.HashSet;
import java.util.Set;

public class Npc extends RandomPredefined<Npc> implements IRandomPredefined {
    private final Set<Weapon> mandatoryWeapons;
    private final Set<Armour> mandatoryArmours;
    private final Set<Shield> mandatoryShields;

    public Npc(String id, String name, String description, String language, String moduleName,
               Set<IRandomPreference<?>> randomPreferences, Set<Characteristic> characteristicsMinimumValues,
               Set<AvailableSkill> requiredSkills, Set<AvailableSkill> suggestedSkills,
               Set<Blessing> mandatoryBlessings, Set<Blessing> suggestedBlessings,
               Set<BeneficeDefinition> mandatoryBenefices, Set<BeneficeDefinition> suggestedBenefices,
               Set<AvailableBenefice> mandatoryBeneficeSpecializations, Set<AvailableBenefice> suggestedBeneficeSpecializations,
               Set<OccultismPath> mandatoryOccultismPaths, Faction faction, Race race) {
        super(id, name, description, language, moduleName, randomPreferences, characteristicsMinimumValues, requiredSkills,
                suggestedSkills, mandatoryBlessings, suggestedBlessings, mandatoryBenefices, suggestedBenefices,
                mandatoryBeneficeSpecializations, suggestedBeneficeSpecializations, mandatoryOccultismPaths, faction, race);
        this.mandatoryWeapons = new HashSet<>();
        this.mandatoryArmours = new HashSet<>();
        this.mandatoryShields = new HashSet<>();
    }

    public Npc(String id, String name, String description, String language, String moduleName) {
        this(id, name, description, language, moduleName, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                null, null);
    }

    public void addMandatoryWeapons(Set<Weapon> weapons) {
        mandatoryWeapons.addAll(weapons);
    }

    public void addMandatoryArmours(Set<Armour> armours) {
        mandatoryArmours.addAll(armours);
    }

    public void addMandatoryShields(Set<Shield> shields) {
        mandatoryShields.addAll(shields);
    }

    @Override
    public Set<Weapon> getMandatoryWeapons() {
        return mandatoryWeapons;
    }

    @Override
    public Set<Armour> getMandatoryArmours() {
        return mandatoryArmours;
    }

    @Override
    public Set<Shield> getMandatoryShields() {
        return mandatoryShields;
    }
}
