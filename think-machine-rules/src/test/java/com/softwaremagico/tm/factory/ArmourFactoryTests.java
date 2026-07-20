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
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.armours.ArmourSpecificationFactory;
import com.softwaremagico.tm.file.PathManager;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"armourFactory"})
public class ArmourFactoryTests {
    private static final int DEFINED_ARMOURS = 30;
    private static final String LANGUAGE = "es";
    private static final int VERSION = 1;

    @Test
    public void checkVersion() {
        org.testng.Assert.assertEquals((int) ArmourFactory.getInstance().getVersion(PathManager.DEFAULT_MODULE_FOLDER),
                VERSION);
    }

    @Test
    public void checkTotalElements() {
        Assert.assertEquals((int) ArmourFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER),
                DEFINED_ARMOURS);
    }

    @Test
    public void readArmours() throws InvalidXmlElementException {
        Assert.assertEquals(ArmourFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size(),
                DEFINED_ARMOURS);
    }

    @Test
    public void readArmoursSpecifications() throws InvalidXmlElementException {
        Assert.assertEquals(ArmourFactory.getInstance().getElement("adeptRobes", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getSpecifications().size(), 7);
    }

    @Test
    public void readShieldsFromArmour() throws InvalidXmlElementException {
        Assert.assertEquals(ArmourFactory.getInstance().getElement("synthsilk", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getAllowedShields().size(), 4);
        Assert.assertEquals(ArmourFactory.getInstance().getElement("adeptRobes", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getAllowedShields().size(), 1);
    }

    @Test
    public void readDamagesFromArmour() throws InvalidXmlElementException {
        Assert.assertEquals(
                ArmourFactory.getInstance().getElement("ceramsteelExoframe", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                        .getDamageTypes().size(), 4);
        Assert.assertEquals(ArmourFactory.getInstance().getElement("spacesuit", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getDamageTypes().size(), 4);
    }

    @Test
    public void readOthersFromArmour() throws InvalidXmlElementException {
        Assert.assertEquals(
                ArmourFactory.getInstance().getElement("ceramsteelExoframe", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                        .getSpecifications().size(), 2);
        Assert.assertEquals(ArmourFactory.getInstance().getElement("halfPlateMetal", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getSpecifications().size(), 1);
        Assert.assertTrue(ArmourFactory.getInstance().getElement("chainMailMetal", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getSpecifications().contains(ArmourSpecificationFactory.getInstance().getElement("metal", LANGUAGE,
                        PathManager.DEFAULT_MODULE_FOLDER)));
    }
}
