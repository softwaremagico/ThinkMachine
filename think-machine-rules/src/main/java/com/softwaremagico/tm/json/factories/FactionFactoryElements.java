package com.softwaremagico.tm.json.factories;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;

import java.sql.Timestamp;
import java.util.Date;

public class FactionFactoryElements extends FactoryElements<Faction> {

    public FactionFactoryElements() {
        super();
        creationTime = new Timestamp(new Date().getTime());
    }

    public FactionFactoryElements(String language, String moduleName) throws InvalidXmlElementException {
        this();
        creationTime = new Timestamp(new Date().getTime());

        //Skip Json generation in loop.
        final FactionsFactory factionsFactory = new FactionsFactory() {
            @Override
            public FactoryCacheLoader<Faction> getFactoryCacheLoader() {
                return null;
            }
        };

        setElements(factionsFactory.getElements(language, moduleName));
        setVersion(factionsFactory.getVersion(moduleName));
        setTotalElements(factionsFactory.getNumberOfElements(moduleName));
    }
}
