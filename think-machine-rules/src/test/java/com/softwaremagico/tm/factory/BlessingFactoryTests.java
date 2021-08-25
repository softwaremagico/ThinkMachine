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
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.file.PathManager;
import junit.framework.Assert;
import org.testng.annotations.Test;

@Test(groups = {"blessingFactory"})
public class BlessingFactoryTests {
    private static final String LANGUAGE = "es";

    private static final int DEFINED_BLESSINGS = 159;
    private static final int DEFINED_BONUS_MISSING_EYE = 2;
    private static final int VERSION = 1;

    @Test
    public void checkVersion() {
        org.testng.Assert.assertEquals((int) BlessingFactory.getInstance().getVersion(PathManager.DEFAULT_MODULE_FOLDER),
                VERSION);
    }

    @Test
    public void checkTotalElements() {
        org.testng.Assert.assertEquals((int) BlessingFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER),
                DEFINED_BLESSINGS);
    }

    @Test
    public void readBlessings() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_BLESSINGS, BlessingFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size());
    }

    @Test
    public void multiplesBonifications() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_BONUS_MISSING_EYE,
                BlessingFactory.getInstance().getElement("missingEye", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getBonifications().size());
    }
}
