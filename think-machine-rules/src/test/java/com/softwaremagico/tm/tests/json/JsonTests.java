package com.softwaremagico.tm.tests.json;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.rules.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.rules.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.rules.character.creation.CostCalculator;
import com.softwaremagico.tm.rules.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.rules.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.rules.json.CharacterJsonManager;
import com.softwaremagico.tm.rules.json.PartyJsonManager;
import com.softwaremagico.tm.rules.language.LanguagePool;
import com.softwaremagico.tm.rules.party.Party;
import com.softwaremagico.tm.tests.characters.CustomCharacter;

@Test(groups = { "jsonExporter" })
public class JsonTests {
	private static final String OUTPUT_CHARACTER_PATH = System.getProperty("java.io.tmpdir") + File.separator
			+ "Character.json";
	private static final String OUTPUT_PARTY_PATH = System.getProperty("java.io.tmpdir") + File.separator
			+ "Party.json";
	private static final String LANGUAGE = "es";

	private CharacterPlayer player;
	private String originalPlayerJson;

	private Party party;
	private String originalPartyJson;

	@BeforeClass
	public void clearCache() throws InvalidXmlElementException, TooManyBlessingsException,
			TooManyCyberneticDevicesException, RequiredCyberneticDevicesException, BlessingAlreadyAddedException,
			BeneficeAlreadyAddedException {
		LanguagePool.clearCache();
		player = CustomCharacter.create(LANGUAGE);
		Assert.assertEquals(CostCalculator.getCost(player), 50);
		party = new Party(LANGUAGE);
		party.setPartyName("JSON Test");
		party.addMember(player);
	}

	@Test
	public void exportCharacterPlayerToJson() throws InvalidXmlElementException, IOException {
		Assert.assertNotNull(player);
		originalPlayerJson = CharacterJsonManager.toJson(player);
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_CHARACTER_PATH)), true)) {
			out.println(originalPlayerJson);
		}
	}

	@Test(dependsOnMethods = { "exportCharacterPlayerToJson" })
	public void importCharacterPlayerFromJson() throws IOException, InvalidXmlElementException {
		final CharacterPlayer player = CharacterJsonManager.fromFile(OUTPUT_CHARACTER_PATH);
		Assert.assertEquals(CostCalculator.getCost(player), 50);
		Assert.assertEquals(CharacterJsonManager.toJson(player), originalPlayerJson);
	}

	@Test
	public void exportPartyToJson() throws InvalidXmlElementException, IOException {
		Assert.assertNotNull(party);
		originalPartyJson = PartyJsonManager.toJson(party);
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_PARTY_PATH)), true)) {
			out.println(originalPartyJson);
		}
	}

	@Test(dependsOnMethods = { "exportPartyToJson" })
	public void importPartyFromJson() throws IOException, InvalidXmlElementException {
		final Party party = PartyJsonManager.fromFile(OUTPUT_PARTY_PATH);
		Assert.assertEquals(CostCalculator.getCost(party.getMembers().iterator().next()), 50);
		Assert.assertEquals(PartyJsonManager.toJson(party), originalPartyJson);
	}
}
