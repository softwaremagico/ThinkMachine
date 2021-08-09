package com.softwaremagico.tm.rules;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2021 Softwaremagico
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
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.file.PathManager;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"race"})
public class RaceTests {
    private static final String LANGUAGE = "es";

    @Test(expectedExceptions = RestrictedElementException.class)
    public void checkRaceRestriction() throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.setFaction(FactionsFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test
    public void checkRaceBenefices() throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.setFaction(FactionsFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setRace(RaceFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertTrue(player.getAfflictions().contains(AvailableBeneficeFactory.getInstance().getElement("noOccult",
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
    }

    @Test
    public void checkRaceBeneficesMaxNumber() throws InvalidXmlElementException, TooManyBlessingsException, BlessingAlreadyAddedException,
            RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.setFaction(FactionsFactory.getInstance().getElement("scravers", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setRace(RaceFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        //I can add 7 benefices despite faction and race benefices
        player.addBlessing(BlessingFactory.getInstance().getElement("handsome", player.getLanguage(),
                player.getModuleName()));
        player.addBlessing(BlessingFactory.getInstance().getElement("nosy", player.getLanguage(),
                player.getModuleName()));
        player.addBlessing(BlessingFactory.getInstance().getElement("phobic", player.getLanguage(),
                player.getModuleName()));
        player.addBlessing(BlessingFactory.getInstance().getElement("badHeart", player.getLanguage(),
                player.getModuleName()));
        player.addBlessing(BlessingFactory.getInstance().getElement("secretive", player.getLanguage(),
                player.getModuleName()));
        player.addBlessing(BlessingFactory.getInstance().getElement("surly", player.getLanguage(),
                player.getModuleName()));
        player.addBlessing(BlessingFactory.getInstance().getElement("vain", player.getLanguage(),
                player.getModuleName()));
    }

    @Test
    public void checkShantor() throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.getSettings().setOnlyOfficialAllowed(false);
        player.setRace(RaceFactory.getInstance().getElement("shantor", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertTrue(player.getAfflictions().contains(AvailableBeneficeFactory.getInstance().getElement("noOccult",
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
    }

    @Test
    public void checkNullRace() throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setRace(null);
        Assert.assertNull(player.getRace());
    }
}
