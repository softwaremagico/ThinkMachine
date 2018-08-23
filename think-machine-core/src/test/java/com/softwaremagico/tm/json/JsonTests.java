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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.characters.CustomCharacter;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;

@Test(groups = { "jsonExporter" })
public class JsonTests {
	private final static String OUTPUT_PATH = System.getProperty("java.io.tmpdir") + File.separator + "Character.json";
	private final static String LANGUAGE = "es";

	private CharacterPlayer player;

	@BeforeClass
	public void clearCache() throws InvalidXmlElementException, TooManyBlessingsException {
		LanguagePool.clearCache();
		player = CustomCharacter.create(LANGUAGE);
		Assert.assertEquals(CostCalculator.getCost(player), 50);
	}

	@Test
	public void exportToJson() throws InvalidXmlElementException, DuplicatedPreferenceException, IOException {
		Assert.assertNotNull(player);
		String jsonText = CharacterJsonManager.toJson(player);
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_PATH)), true)) {
			out.println(jsonText);
		}
	}

	@Test(dependsOnMethods = { "exportToJson" })
	public void importFromJson() throws IOException, InvalidXmlElementException {
		CharacterPlayer player = CharacterJsonManager.fromFile(OUTPUT_PATH);
		Assert.assertEquals(CostCalculator.getCost(player), 50);
	}
}
