package com.softwaremagico.tm.json.factories.cache;

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
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.weapons.Accessory;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.json.*;
import com.softwaremagico.tm.json.factories.FactoryElements;
import com.softwaremagico.tm.json.factories.WeaponsFactoryElements;

import java.util.List;

public class WeaponsFactoryCacheLoader extends FactoryCacheLoader<Weapon> {

    @Override
    public List<Weapon> load(String language, String moduleName) {
        try {
            final FactoryElements<Weapon> factoryElements = load(WeaponFactory.class, WeaponsFactoryElements.class, language, moduleName);
            if (factoryElements != null && !factoryElements.getElements().isEmpty()) {
                return factoryElements.getElements();
            }
        } catch (InvalidCacheFile invalidCacheFile) {
           // Not cache file on this module.
        }
        return null;
    }

    @Override
    protected FactoryElements<Weapon> getFactoryElements(String moduleName, String language) throws InvalidXmlElementException {
        return new WeaponsFactoryElements(language, moduleName);
    }

    @Override
    protected GsonBuilder initGsonBuilder(final String language, final String moduleName) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(AvailableSkill.class, new AvailableSkillAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(CharacteristicDefinition.class, new CharacteristicDefinitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Accessory.class, new AccessoryAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(DamageType.class, new DamageTypeAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Ammunition.class, new AmmunitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Race.class, new RaceAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Faction.class, new FactionAdapter(language, moduleName));
        return gsonBuilder;
    }


}
