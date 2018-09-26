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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;

@Test(groups = { "beneficeFactory" })
public class BeneficeFactoryTests {
	private final static String LANGUAGE = "es";
	private final static int DEFINED_BENEFICES = 74;
	private final static int AVAILABLE_BENEFICES = 206;

	@Test
	public void readBenefices() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_BENEFICES, BeneficeDefinitionFactory.getInstance().getElements(LANGUAGE).size());
	}

	@Test
	public void getCalculatedBenefices() throws InvalidXmlElementException {
		Assert.assertEquals(AVAILABLE_BENEFICES, AvailableBeneficeFactory.getInstance().getElements(LANGUAGE).size());
	}

	@Test
	public void getBeneficesClassification() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_BENEFICES, AvailableBeneficeFactory.getInstance().getAvailableBeneficesByDefinition(LANGUAGE).keySet().size());
		int count = 0;
		for (Set<AvailableBenefice> benefices : AvailableBeneficeFactory.getInstance().getAvailableBeneficesByDefinition(LANGUAGE).values()) {
			count += benefices.size();
		}
		Assert.assertEquals(AVAILABLE_BENEFICES, count);
	}

	@Test
	public void getBeneficeSpecialization() throws InvalidXmlElementException {
		Assert.assertNotNull(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds250]", LANGUAGE));
	}

	@Test
	public void getMoneyStandard() throws InvalidXmlElementException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		Assert.assertEquals(250, player.getInitialMoney());
	}

	@Test
	public void getMoneyBenefice() throws InvalidXmlElementException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]", LANGUAGE));
		Assert.assertEquals(1000, player.getInitialMoney());
	}

	@Test
	public void getMoneyRemaining() throws InvalidXmlElementException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		// Weapon without cost.
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("fluxSword", LANGUAGE));
		// Weapon with cost.
		player.getWeapons().addElement(WeaponFactory.getInstance().getElement("typicalShotgun", LANGUAGE));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]", LANGUAGE));
		Assert.assertEquals(2, player.getAllWeapons().size());
		Assert.assertEquals(700, player.getMoney());
	}

}
