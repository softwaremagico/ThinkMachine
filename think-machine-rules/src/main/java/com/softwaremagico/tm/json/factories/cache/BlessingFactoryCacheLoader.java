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
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.character.values.SpecialValue;
import com.softwaremagico.tm.json.*;
import com.softwaremagico.tm.json.factories.BlessingFactoryElements;
import com.softwaremagico.tm.json.factories.FactoryElements;

import java.util.List;

public class BlessingFactoryCacheLoader extends FactoryCacheLoader<Blessing> {

    @Override
    public List<Blessing> load(String language, String moduleName) {
        try {
            final FactoryElements<Blessing> factoryElements = load(BlessingFactory.class, BlessingFactoryElements.class, language, moduleName);
            if (factoryElements != null && !factoryElements.getElements().isEmpty()) {
                return factoryElements.getElements();
            }
        } catch (InvalidCacheFile invalidCacheFile) {
           // Not cache file on this module.
        }
        return null;
    }

    @Override
    protected FactoryElements<Blessing> getFactoryElements(String moduleName, String language) throws InvalidXmlElementException {
        return new BlessingFactoryElements(language, moduleName);
    }

    @Override
    protected GsonBuilder initGsonBuilder(final String language, final String moduleName) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(IValue.class, new InterfaceAdapter<IValue>());
        gsonBuilder.registerTypeAdapter(AvailableSkill.class, new AvailableSkillAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(CharacteristicDefinition.class, new CharacteristicDefinitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(SpecialValue.class, new SpecialValueAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(OccultismType.class, new OccultismTypeAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(SkillDefinition.class, new SkillDefinitionAdapter(language, moduleName));

        return gsonBuilder;
    }


}
