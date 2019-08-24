package com.softwaremagico.tm.rules;

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

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.InvalidFactionOfPowerException;
import com.softwaremagico.tm.character.occultism.InvalidPowerLevelException;
import com.softwaremagico.tm.character.occultism.InvalidPsiqueLevelException;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.races.InvalidRaceException;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.characters.CustomCharacter;
import com.softwaremagico.tm.file.PathManager;

@Test(groups = { "occultism" })
public class OccultismTests {
	private static final String LANGUAGE = "es";


	@Test(expectedExceptions = { InvalidPowerLevelException.class })
	public void cannotAddPowersIncorrectPsiqueLevel() throws InvalidXmlElementException, TooManyBlessingsException,
			TooManyCyberneticDevicesException, RequiredCyberneticDevicesException, BlessingAlreadyAddedException,
			BeneficeAlreadyAddedException {
		final CharacterPlayer player = CustomCharacter.create(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("psyche", player.getLanguage(), player.getModuleName()).getOccultismPowers().get("emote"));
	}

	@Test(expectedExceptions = { InvalidPsiqueLevelException.class })
	public void cannotAddPowersIncorrectPathLevel() throws InvalidXmlElementException, TooManyBlessingsException,
			TooManyCyberneticDevicesException, RequiredCyberneticDevicesException, BlessingAlreadyAddedException,
			BeneficeAlreadyAddedException {
		final CharacterPlayer player = CustomCharacter.create(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("farHand", player.getLanguage(), player.getModuleName()).getOccultismPowers()
				.get("farWall"));
	}

	@Test
	public void canAddPowersWithMissingLevels() throws InvalidXmlElementException, TooManyBlessingsException,
			TooManyCyberneticDevicesException, RequiredCyberneticDevicesException, BlessingAlreadyAddedException,
			BeneficeAlreadyAddedException {
		final CharacterPlayer player = CustomCharacter.create(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setPsiqueLevel(OccultismTypeFactory.getPsi(player.getLanguage(), player.getModuleName()), 6);
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("farHand", player.getLanguage(), player.getModuleName()).getOccultismPowers()
				.get("farWall"));
	}

	@Test(expectedExceptions = { InvalidFactionOfPowerException.class })
	public void cannotAddPowerOfDifferentFaction() throws TooManyBlessingsException, InvalidXmlElementException,
			TooManyCyberneticDevicesException, RequiredCyberneticDevicesException, BlessingAlreadyAddedException,
			BeneficeAlreadyAddedException {
		final CharacterPlayer player = CustomCharacter.create(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setPsiqueLevel(OccultismTypeFactory.getTheurgy(player.getLanguage(), player.getModuleName()), 5);
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("orthodoxRituals", player.getLanguage(), player.getModuleName()).getOccultismPowers()
				.get("consecration"));
	}

	@Test(expectedExceptions = { InvalidPsiqueLevelException.class })
	public void voroxCannotHavePsiqueLevel() throws InvalidRaceException, InvalidXmlElementException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setRace(RaceFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		player.setFaction(FactionsFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		player.setPsiqueLevel(OccultismTypeFactory.getPsi(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 1);
	}

}
