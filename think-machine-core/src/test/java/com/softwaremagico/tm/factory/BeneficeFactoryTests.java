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

import java.util.Set;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.traits.AvailableBenefice;
import com.softwaremagico.tm.character.traits.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.traits.BeneficeDefinitionFactory;

@Test(groups = { "beneficeFactory" })
public class BeneficeFactoryTests {
	private final static int DEFINED_BENEFICES = 69;
	private final static int AVAILABLE_BENEFICES = 186;

	@Test
	public void readBenefices() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_BENEFICES, BeneficeDefinitionFactory.getInstance().getElements("es").size());
	}

	@Test
	public void getCalculatedBenefices() throws InvalidXmlElementException {
		Assert.assertEquals(AVAILABLE_BENEFICES, AvailableBeneficeFactory.getInstance().getElements("es").size());
	}

	@Test
	public void getBeneficesClassification() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_BENEFICES, AvailableBeneficeFactory.getInstance().getAvailableBeneficesByDefinition("es").keySet().size());
		int count = 0;
		for (Set<AvailableBenefice> benefices : AvailableBeneficeFactory.getInstance().getAvailableBeneficesByDefinition("es").values()) {
			count += benefices.size();
		}
		Assert.assertEquals(AVAILABLE_BENEFICES, count);
	}
}
