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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.random.predefined.RandomPredefinedFactory;
import com.softwaremagico.tm.random.predefined.profile.RandomProfile;
import com.softwaremagico.tm.random.predefined.profile.RandomProfileFactory;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.Set;

public class NpcFactory extends RandomPredefinedFactory<Npc> {
    private static final String TRANSLATOR_FILE = "npc_definitions.xml";
    private static final String MANDATORY_WEAPONS = "weapons";
    private static final String MANDATORY_ARMOURS = "armours";
    private static final String MANDATORY_SHIELDS = "shields";
    private static final String PROFILE = "profile";

    private static class NpcFactoryInit {
        public static final NpcFactory INSTANCE = new NpcFactory();
    }

    public static NpcFactory getInstance() {
        return NpcFactoryInit.INSTANCE;
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    public FactoryCacheLoader<Npc> getFactoryCacheLoader() {
        return null;
    }

    @Override
    protected Npc createNew(String id, String name, String description, String language, String moduleName,
                            Set<IRandomPreference<?>> randomPreferences, Set<Characteristic> characteristicsMinimumValues,
                            Set<AvailableSkill> requiredSkills, Set<AvailableSkill> suggestedSkills,
                            Set<Blessing> mandatoryBlessings, Set<Blessing> suggestedBlessings,
                            Set<BeneficeDefinition> mandatoryBenefices, Set<BeneficeDefinition> suggestedBenefices,
                            Set<AvailableBenefice> mandatoryBeneficeSpecializations, Set<AvailableBenefice> suggestedBeneficeSpecializations,
                            Set<OccultismPath> mandatoryOccultismPaths, Faction faction, Race race) {
        return new Npc(id, name, description, language, moduleName, randomPreferences, characteristicsMinimumValues, requiredSkills,
                suggestedSkills, mandatoryBlessings, suggestedBlessings, mandatoryBenefices, suggestedBenefices,
                mandatoryBeneficeSpecializations, suggestedBeneficeSpecializations, mandatoryOccultismPaths, faction, race);
    }

    @Override
    protected Npc createElement(ITranslator translator, String predefinedId, String name, String description,
                                String language, String moduleName)
            throws InvalidXmlElementException {
        final Npc npc = super.createElement(translator, predefinedId, name, description, language, moduleName);

        final RandomProfile profile = getElement(predefinedId, PROFILE, language, moduleName, RandomProfileFactory.getInstance());
        npc.copy(profile);

        final Set<Weapon> mandatoryWeapons = getCommaSeparatedValues(predefinedId, MANDATORY_WEAPONS, language,
                moduleName, WeaponFactory.getInstance());

        final Set<Armour> mandatoryArmours = getCommaSeparatedValues(predefinedId, MANDATORY_ARMOURS, language,
                moduleName, ArmourFactory.getInstance());

        final Set<Shield> mandatoryShields = getCommaSeparatedValues(predefinedId, MANDATORY_SHIELDS, language,
                moduleName, ShieldFactory.getInstance());

        npc.addMandatoryWeapons(mandatoryWeapons);
        npc.addMandatoryArmours(mandatoryArmours);
        npc.addMandatoryShields(mandatoryShields);

        return npc;
    }
}
