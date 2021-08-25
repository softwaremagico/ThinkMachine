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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.json.factories.FactoryElements;
import com.softwaremagico.tm.json.factories.cache.InvalidCacheFile;
import com.softwaremagico.tm.random.predefined.profile.RandomProfile;
import com.softwaremagico.tm.random.predefined.profile.RandomProfileFactory;

import java.util.List;

public class ProfileFactoryCacheLoader extends RandomFactoryCacheLoader<RandomProfile> {

    @Override
    public List<RandomProfile> load(String language, String moduleName) {
        try {
            final FactoryElements<RandomProfile> factoryElements = load(RandomProfileFactory.class, RandomProfileFactoryElements.class, language, moduleName);
            if (factoryElements != null && !factoryElements.getElements().isEmpty()) {
                final List<RandomProfile> randomProfiles = factoryElements.getElements();
                RandomProfileFactory.getInstance().updateGroups(randomProfiles);
                return randomProfiles;
            }
        } catch (InvalidCacheFile invalidCacheFile) {
            // Not cache file on this module.
        }
        return null;
    }

    @Override
    protected FactoryElements<RandomProfile> getFactoryElements(String moduleName, String language) throws InvalidXmlElementException {
        return new RandomProfileFactoryElements(language, moduleName);
    }

}
