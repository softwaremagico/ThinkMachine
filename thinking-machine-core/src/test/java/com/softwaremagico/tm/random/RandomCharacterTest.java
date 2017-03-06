package com.softwaremagico.tm.random;

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
