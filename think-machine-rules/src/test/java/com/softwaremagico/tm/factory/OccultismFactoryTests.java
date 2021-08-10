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

import com.softwaremagico.tm.ElementClassification;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.occultism.*;
import com.softwaremagico.tm.file.PathManager;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"occultismFactory"})
public class OccultismFactoryTests {
    private static final String LANGUAGE = "es";


    private static final int DEFINED_PSI_PATHS = 11;
    private static final int DEFINED_THEURGY_PATHS = 12;
    private static final int DEFINED_RANGES = 5;
    private static final int DEFINED_DURATIONS = 7;
    private static final int DEFINED_THEURGY_COMPONENTS = 3;
    private static final int OCCULTISM_TYPES = 2;
    private static final int VERSION = 1;

    @Test
    public void checkVersion() {
        Assert.assertEquals((int) OccultismPathFactory.getInstance().getVersion(PathManager.DEFAULT_MODULE_FOLDER),
                VERSION);
    }

    @Test
    public void checkTotalElements() {
        Assert.assertEquals((int) OccultismPathFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER),
                DEFINED_PSI_PATHS + DEFINED_THEURGY_PATHS);
    }

    @Test
    public void readPaths() throws InvalidXmlElementException {
        Assert.assertEquals(OccultismPathFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size(),
                DEFINED_PSI_PATHS + DEFINED_THEURGY_PATHS);
    }

    @Test
    public void readPsiPaths() {
        Assert.assertEquals(OccultismPathFactory.getInstance().getPsiPaths(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size(), DEFINED_PSI_PATHS);
    }

    @Test
    public void readTheurgyPaths() {
        Assert.assertEquals(OccultismPathFactory.getInstance().getTheurgyPaths(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .size(), DEFINED_THEURGY_PATHS);
    }

    @Test
    public void readRanges() throws InvalidXmlElementException {
        Assert.assertEquals(OccultismRangeFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size(), DEFINED_RANGES);
    }

    @Test
    public void readDurations() throws InvalidXmlElementException {
        Assert.assertEquals(OccultismDurationFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .size(), DEFINED_DURATIONS);
    }

    @Test
    public void readTheurgyComponents() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_THEURGY_COMPONENTS,
                TheurgyComponentFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size());
    }

    @Test
    public void getOccultismTypes() throws InvalidXmlElementException {
        Assert.assertEquals(OCCULTISM_TYPES, OccultismTypeFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size());
    }

    @Test
    public void getClassifications() throws InvalidXmlElementException {
        Assert.assertEquals(ElementClassification.ENHANCEMENT,
                OccultismPathFactory.getInstance().getElement("sixthSense", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getClassification());
        Assert.assertEquals(ElementClassification.COMBAT,
                OccultismPathFactory.getInstance().getElement("soma", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getClassification());
        Assert.assertEquals(ElementClassification.OTHERS,
                OccultismPathFactory.getInstance().getElement("sympathy", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getClassification());
        Assert.assertEquals(ElementClassification.ALTERATION,
                OccultismPathFactory.getInstance().getElement("templeAvestiRituals", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                        .getClassification());
    }
}
