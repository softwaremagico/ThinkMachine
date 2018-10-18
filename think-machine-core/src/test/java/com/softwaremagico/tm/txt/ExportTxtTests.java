package com.softwaremagico.tm.txt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.characters.CustomCharacter;

@Test(groups = { "exportTxt" })
public class ExportTxtTests {
	private final static String LANGUAGE = "es";

	@Test
	private void checkCustomCharacter() throws TooManyBlessingsException, InvalidXmlElementException, IOException, URISyntaxException {
		CharacterPlayer player = CustomCharacter.create(LANGUAGE);
		CharacterSheet characterSheet = new CharacterSheet(player);

		String text = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("CustomCharacter.txt").toURI())));
		Assert.assertEquals(characterSheet.toString(), text);
	}
}
