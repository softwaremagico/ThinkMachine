package com.softwaremagico.tm.factory;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.file.PathManager;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"characteristicsFactory"})
public class CharacteristicsDefinitionFactoryTests {
    private static final String LANGUAGE = "es";
    private static final int DEFINED_CHARACTERISTICS = 12;
    private static final int VERSION = 1;

    @Test
    public void checkVersion() {
        org.testng.Assert.assertEquals((int) CharacteristicsDefinitionFactory.getInstance().getVersion(PathManager.DEFAULT_MODULE_FOLDER),
                VERSION);
    }

    @Test
    public void checkTotalElements() {
        org.testng.Assert.assertEquals((int) CharacteristicsDefinitionFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER),
                DEFINED_CHARACTERISTICS);
    }

    @Test
    public void readCharacteristics() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_CHARACTERISTICS,
                CharacteristicsDefinitionFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size());
    }
}
