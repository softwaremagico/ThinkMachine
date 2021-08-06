package com.softwaremagico.tm.cache;

/*-
 * #%L
 * Think Machine (Rules)
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

import com.google.gson.GsonBuilder;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Accessory;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.json.*;
import com.softwaremagico.tm.json.factories.FactoryElements;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.json.factories.cache.InvalidCacheFile;
import com.softwaremagico.tm.random.predefined.characters.Npc;
import com.softwaremagico.tm.random.predefined.characters.NpcFactory;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.List;

public class NpcFactoryCacheLoader extends FactoryCacheLoader<Npc> {

    @Override
    public List<Npc> load(String language, String moduleName) {
        try {
            final FactoryElements<Npc> factoryElements = load(NpcFactory.class, NpcFactoryElements.class, language, moduleName);
            if (factoryElements != null && !factoryElements.getElements().isEmpty()) {
                return factoryElements.getElements();
            }
        } catch (InvalidCacheFile invalidCacheFile) {
           // Not cache file on this module.
        }
        return null;
    }

    @Override
    protected FactoryElements<Npc> getFactoryElements(String moduleName, String language) throws InvalidXmlElementException {
        return new NpcFactoryElements(language, moduleName);
    }

    @Override
    protected GsonBuilder initGsonBuilder(final String language, final String moduleName) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(AvailableSkill.class, new AvailableSkillAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(CharacteristicDefinition.class, new CharacteristicDefinitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Characteristic.class, new CharacteristicAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Accessory.class, new AccessoryAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(DamageType.class, new DamageTypeAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Ammunition.class, new AmmunitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Race.class, new RaceAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Faction.class, new FactionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Weapon.class, new WeaponAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Armour.class, new ArmourAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Shield.class, new ShieldAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(BeneficeDefinition.class, new BeneficeDefinitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(AvailableBenefice.class, new AvailableBeneficeAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Blessing.class, new BlessingAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(OccultismPath.class, new OccultismPathAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(IRandomPreference.class, new RandomPreferenceAdapter());
        return gsonBuilder;
    }


}
