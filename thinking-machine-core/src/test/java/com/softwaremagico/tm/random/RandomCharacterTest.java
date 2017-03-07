package com.softwaremagico.tm.random;

/*-
 * #%L
 * The Thinking Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

import java.io.File;

import org.testng.annotations.Test;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.CharacterSheet;

@Test(groups = { "randomCharacter" })
public class RandomCharacterTest {

	@Test
	public void basicCharacterCreation() {
		CharacterPlayer characterPlayer = new CharacterPlayer("es");
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 3, 1, 1, 0);
		randomizeCharacter.createCharacter();
		
		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(characterPlayer);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Random.pdf");

	}
}
