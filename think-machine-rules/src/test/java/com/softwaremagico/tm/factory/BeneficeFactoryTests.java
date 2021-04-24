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
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.file.PathManager;
import junit.framework.Assert;
import org.testng.annotations.Test;

import java.util.Set;

@Test(groups = {"beneficeFactory"})
public class BeneficeFactoryTests {
    private static final String LANGUAGE = "es";

    private static final int DEFINED_BENEFICES = 84;
    private static final int AVAILABLE_BENEFICES = 253;
    private static final int VERSION = 1;

    @Test
    public void checkVersion() {
        org.testng.Assert.assertEquals((int) BeneficeDefinitionFactory.getInstance().getVersion(PathManager.DEFAULT_MODULE_FOLDER),
                VERSION);
    }

    @Test
    public void checkTotalElements() {
        org.testng.Assert.assertEquals((int) BeneficeDefinitionFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER),
                DEFINED_BENEFICES);
    }

    @Test
    public void readBenefices() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_BENEFICES, BeneficeDefinitionFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .size());
    }

    @Test
    public void getCalculatedBenefices() throws InvalidXmlElementException {
        Assert.assertEquals(AVAILABLE_BENEFICES,
                AvailableBeneficeFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size());
    }

    @Test
    public void getBeneficesClassification() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_BENEFICES, AvailableBeneficeFactory.getInstance()
                .getAvailableBeneficesByDefinition(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).keySet().size());
        int count = 0;
        for (final Set<AvailableBenefice> benefices : AvailableBeneficeFactory.getInstance()
                .getAvailableBeneficesByDefinition(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).values()) {
            count += benefices.size();
        }
        Assert.assertEquals(AVAILABLE_BENEFICES, count);
    }

    @Test
    public void getBeneficeSpecialization() throws InvalidXmlElementException {
        Assert.assertNotNull(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds250]", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test
    public void checkDescription() throws InvalidXmlElementException {
        Assert.assertEquals("El personaje es incapaz de realizar acciones que requieran de una manipulación fina.",
                AvailableBeneficeFactory.getInstance().getElement("noFineManipulation", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getDescription());
    }

    @Test
    public void getRestrictedRaces() throws InvalidXmlElementException {
        Assert.assertTrue(BeneficeDefinitionFactory.getInstance().getElement("prominentFamily", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getRestrictedToRaces().contains(RaceFactory.getInstance().getElement("gannok", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
    }

}
