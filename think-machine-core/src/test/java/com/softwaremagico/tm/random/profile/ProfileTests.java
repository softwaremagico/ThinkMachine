package com.softwaremagico.tm.random.profile;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.random.RandomizeCharacter;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.txt.CharacterSheet;

@Test(groups = "profile")
public class ProfileTests {
	private final static String LANGUAGE = "en";

	@Test
	public void Soldier() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException, TooManyBlessingsException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, new Soldier());
		randomizeCharacter.createCharacter();
		CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
		System.out.println(characterSheet.toString());
	}
}
