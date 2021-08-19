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
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.json.*;
import com.softwaremagico.tm.json.factories.FactionFactoryElements;
import com.softwaremagico.tm.json.factories.FactoryElements;

import java.util.List;

public class FactionFactoryCacheLoader extends FactoryCacheLoader<Faction> {

    @Override
    public List<Faction> load(String language, String moduleName) {
        try {
            final FactoryElements<Faction> factoryElements = load(FactionsFactory.class, FactionFactoryElements.class, language, moduleName);
            if (factoryElements != null && !factoryElements.getElements().isEmpty()) {
                final List<Faction> factions = factoryElements.getElements();
                factions.forEach(faction -> {
                    faction.getNames().forEach(name -> {
                        name.setFaction(faction);
                        FactionsFactory.getInstance().addName(name);
                    });
                    faction.getSurnames().forEach(surname -> {
                        surname.setFaction(faction);
                        FactionsFactory.getInstance().addSurname(surname);
                    });
                });

                return factions;
            }
        } catch (InvalidCacheFile invalidCacheFile) {
            // Not cache file on this module.
        }
        return null;
    }

    @Override
    protected FactoryElements<Faction> getFactoryElements(String moduleName, String language) throws InvalidXmlElementException {
        return new FactionFactoryElements(language, moduleName);
    }

    @Override
    protected GsonBuilder initGsonBuilder(final String language, final String moduleName) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.setExclusionStrategies(new AnnotationExclusionStrategy()).create();
        gsonBuilder.registerTypeAdapter(Race.class, new RaceAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Blessing.class, new BlessingAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(AvailableBenefice.class, new AvailableSkillAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(BeneficeDefinition.class, new BeneficeDefinitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Name.class, new NameAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Surname.class, new SurnameAdapter(language, moduleName));
        return gsonBuilder;
    }


}
