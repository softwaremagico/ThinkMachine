package com.softwaremagico.tm.txt;

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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.characters.CustomCharacter;

@Test(groups = { "exportTxt" })
public class ExportTxtTests {
	private final static String LANGUAGE = "es";

	@Test
	private void checkCustomCharacter() throws TooManyBlessingsException, InvalidXmlElementException, IOException, URISyntaxException,
			TooManyCyberneticDevicesException, RequiredCyberneticDevicesException {
		CharacterPlayer player = CustomCharacter.create(LANGUAGE);
		CharacterSheet characterSheet = new CharacterSheet(player);

		String text = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("CustomCharacter.txt").toURI())));
		Assert.assertEquals(characterSheet.toString(), text);
	}
}