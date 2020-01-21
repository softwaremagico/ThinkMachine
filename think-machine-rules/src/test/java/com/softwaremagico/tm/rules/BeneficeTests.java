package com.softwaremagico.tm.rules;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.character.benefices.InvalidBeneficeException;
import com.softwaremagico.tm.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.file.PathManager;

@Test(groups = { "benefices" })
public class BeneficeTests {
	private static final String LANGUAGE = "es";

	@Test(expectedExceptions = InvalidBeneficeException.class)
	public void checkBeneficeRestrictionByFaction()
			throws InvalidXmlElementException, TooManyBlessingsException, TooManyCyberneticDevicesException,
			RequiredCyberneticDevicesException, BlessingAlreadyAddedException, BeneficeAlreadyAddedException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setFaction(
				FactionsFactory.getInstance().getElement("amaltheans", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds3000]", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));
	}
}
