package com.softwaremagico.tm.factory;

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

import com.softwaremagico.tm.CacheHandler;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.file.PathManager;
import org.junit.BeforeClass;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"factionsFactory"})
public class FactionFactoryTests {
    private static final int DEFINED_FACTIONS = 36;
    private static final int DEFINED_MALE_NAMES = 103;
    private static final int DEFINED_FEMALE_NAMES = 100;
    private static final int DEFINED_SURNAMES = 125;
    private static final String LANGUAGE = "es";
    private static final int VERSION = 1;


    @BeforeClass
    public void clearCache() {
        CacheHandler.clearCache();
    }

    @Test
    public void checkVersion() {
        Assert.assertEquals((int) FactionsFactory.getInstance().getVersion(PathManager.DEFAULT_MODULE_FOLDER),
                VERSION);
    }

    @Test
    public void checkTotalElements() {
        Assert.assertEquals((int) FactionsFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER),
                DEFINED_FACTIONS);
    }

    @Test
    public void readFactions() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_FACTIONS,
                FactionsFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size());
    }

    @Test
    public void readNames() throws InvalidXmlElementException {
        final Faction hazat = FactionsFactory.getInstance().getElement("hazat", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertNotNull(hazat);
        Assert.assertTrue(FactionsFactory.getInstance().getAllNames(hazat, Gender.MALE).size() >= DEFINED_MALE_NAMES);
        Assert.assertTrue(
                FactionsFactory.getInstance().getAllNames(hazat, Gender.FEMALE).size() >= DEFINED_FEMALE_NAMES);
        Assert.assertTrue(FactionsFactory.getInstance().getAllSurnames(hazat).size() >= DEFINED_SURNAMES);
    }

    @Test
    public void checkSuggestedBenefices() throws InvalidXmlElementException {
        final Faction obun = FactionsFactory.getInstance().getElement("obun", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(obun.getSuggestedBenefices().size(), 1);
        Assert.assertEquals(obun.getSuggestedBenefices().iterator().next().getId(), "refuge");
        Assert.assertEquals((int) obun.getSuggestedBenefices().iterator().next().getValue(), 4);
    }

    @Test
    public void checkRestrictedBenefices() throws InvalidXmlElementException {
        final Faction amaltheans = FactionsFactory.getInstance().getElement("amaltheans", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(amaltheans.getRestrictedBenefices().size(), 1);
        Assert.assertEquals(amaltheans.getRestrictedBenefices().iterator().next().getId(), "cash");
        Assert.assertEquals((int) amaltheans.getRestrictedBenefices().iterator().next().getMaxValue(), 8);
    }
}
