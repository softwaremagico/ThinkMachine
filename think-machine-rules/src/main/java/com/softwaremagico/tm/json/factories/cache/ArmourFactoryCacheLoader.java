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
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.armours.ArmourSpecification;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.json.*;
import com.softwaremagico.tm.json.factories.ArmourFactoryElements;
import com.softwaremagico.tm.json.factories.FactoryElements;

import java.util.List;

public class ArmourFactoryCacheLoader extends FactoryCacheLoader<Armour> {

    @Override
    public List<Armour> load(String language, String moduleName) {
        try {
            final FactoryElements<Armour> factoryElements = load(ArmourFactory.class, ArmourFactoryElements.class, language, moduleName);
            if (factoryElements != null && !factoryElements.getElements().isEmpty()) {
                return factoryElements.getElements();
            }
        } catch (InvalidCacheFile invalidCacheFile) {
            // Not cache file on this module.
        }
        return null;
    }

    @Override
    protected FactoryElements<Armour> getFactoryElements(String moduleName, String language) throws InvalidXmlElementException {
        return new ArmourFactoryElements(language, moduleName);
    }

    @Override
    protected GsonBuilder initGsonBuilder(final String language, final String moduleName) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(DamageType.class, new DamageTypeAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Shield.class, new ShieldAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(ArmourSpecification.class, new ArmourSpecificationAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Race.class, new RaceAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Faction.class, new FactionAdapter(language, moduleName));
        return gsonBuilder;
    }


}
