package com.softwaremagico.tm.tests.factory;

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

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.rules.ElementClassification;
import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.occultism.OccultismDurationFactory;
import com.softwaremagico.tm.rules.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.rules.character.occultism.OccultismRangeFactory;
import com.softwaremagico.tm.rules.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.rules.character.occultism.TheurgyComponentFactory;

@Test(groups = { "occultismFactory" })
public class OccultismFactoryTests {
	private static final String LANGUAGE = "es";

	private static final int DEFINED_PSI_PATHS = 7;
	private static final int DEFINED_THEURGY_PATHS = 6;
	private static final int DEFINED_RANGES = 5;
	private static final int DEFINED_DURATIONS = 7;
	private static final int DEFINED_THEURGY_COMPONENTS = 3;
	private static final int OCCULTISM_TYPES = 2;

	@Test
	public void readPaths() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_PSI_PATHS + DEFINED_THEURGY_PATHS,
				OccultismPathFactory.getInstance().getElements(LANGUAGE).size());
	}

	@Test
	public void readPsiPaths() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_PSI_PATHS, OccultismPathFactory.getInstance().getPsiPaths(LANGUAGE).size());
	}

	@Test
	public void readTheurgyPaths() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_THEURGY_PATHS, OccultismPathFactory.getInstance().getTheurgyPaths(LANGUAGE).size());
	}

	@Test
	public void readRanges() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_RANGES, OccultismRangeFactory.getInstance().getElements(LANGUAGE).size());
	}

	@Test
	public void readDurations() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_DURATIONS, OccultismDurationFactory.getInstance().getElements(LANGUAGE).size());
	}

	@Test
	public void readTheurgyComponents() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_THEURGY_COMPONENTS, TheurgyComponentFactory.getInstance().getElements(LANGUAGE)
				.size());
	}

	@Test
	public void getOcculstimTypes() throws InvalidXmlElementException {
		Assert.assertEquals(OCCULTISM_TYPES, OccultismTypeFactory.getInstance().getElements(LANGUAGE).size());
	}

	@Test
	public void getClassifications() throws InvalidXmlElementException {
		Assert.assertEquals(ElementClassification.ENHANCEMENT,
				OccultismPathFactory.getInstance().getElement("sixthSense", LANGUAGE).getClassification());
		Assert.assertEquals(ElementClassification.COMBAT,
				OccultismPathFactory.getInstance().getElement("soma", LANGUAGE).getClassification());
		Assert.assertEquals(ElementClassification.OTHERS,
				OccultismPathFactory.getInstance().getElement("sympathy", LANGUAGE).getClassification());
		Assert.assertEquals(ElementClassification.ALTERATION,
				OccultismPathFactory.getInstance().getElement("templeAvestiRituals", LANGUAGE).getClassification());
	}
}
