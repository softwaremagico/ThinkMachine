package com.softwaremagico.tm.json;

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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.character.skills.InvalidRanksException;
import com.softwaremagico.tm.characters.CustomCharacter;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.party.Party;
import com.softwaremagico.tm.txt.CharacterSheet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
	public void init()
			throws InvalidXmlElementException, TooManyBlessingsException, TooManyCyberneticDevicesException,
			RequiredCyberneticDevicesException, BlessingAlreadyAddedException, BeneficeAlreadyAddedException, InvalidRanksException {
		LanguagePool.clearCache();
		player = CustomCharacter.create(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		Assert.assertEquals(CostCalculator.getCost(player), CustomCharacter.COST);
		party = new Party(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		party.setPartyName("JSON Test");
		party.addMember(player);
	}

	@Test
	public void exportCharacterPlayerToJson() throws InvalidXmlElementException, IOException {
		originalPlayerJson = CharacterJsonManager.toJson(player);
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_CHARACTER_PATH)), true)) {
			out.println(originalPlayerJson);
		}
	}

	@Test(dependsOnMethods = { "exportCharacterPlayerToJson" })
	public void importCharacterPlayerFromJson() throws IOException, InvalidXmlElementException, InvalidJsonException, URISyntaxException {
		final CharacterPlayer player = CharacterJsonManager.fromFile(OUTPUT_CHARACTER_PATH);
		Assert.assertEquals(player.getInfo().getNames().size(), 1);
		
		Assert.assertEquals(CostCalculator.getCost(player), CustomCharacter.COST);
		Assert.assertEquals(CharacterJsonManager.toJson(player), originalPlayerJson);
		
		final String text = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
				.getResource("CustomCharacter.txt").toURI())));
		final CharacterSheet characterSheet = new CharacterSheet(player);
		Assert.assertEquals(characterSheet.toString(), text);
	}

	@Test
	public void exportPartyToJson() throws InvalidXmlElementException, IOException {
		originalPartyJson = PartyJsonManager.toJson(party);
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_PARTY_PATH)), true)) {
			out.println(originalPartyJson);
		}
	}

	@Test(dependsOnMethods = { "exportPartyToJson" })
	public void importPartyFromJson() throws IOException, InvalidXmlElementException, InvalidJsonException {
		final Party party = PartyJsonManager.fromFile(OUTPUT_PARTY_PATH);
		Assert.assertEquals(CostCalculator.getCost(party.getMembers().iterator().next()), CustomCharacter.COST);
		Assert.assertEquals(PartyJsonManager.toJson(party), originalPartyJson);
	}
}
