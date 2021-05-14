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
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.InvalidRanksException;
import com.softwaremagico.tm.character.skills.InvalidSkillException;
import com.softwaremagico.tm.file.PathManager;
import org.testng.annotations.Test;

@Test(groups = {"skills"})
public class SkillTests {
    private static final String LANGUAGE = "es";

    @Test(expectedExceptions = InvalidRanksException.class)
    public void checkSkillLimitedToFaction() throws InvalidXmlElementException, InvalidRanksException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.setFaction(FactionsFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("spacecraftOperations", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 1);
    }

    @Test
    public void checkSkillAllowedWithProfessionalContract() throws InvalidXmlElementException, BeneficeAlreadyAddedException, InvalidRanksException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.setFaction(FactionsFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("professionalContract_2", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER));
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("spacecraftOperations", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 1);
    }

    @Test(expectedExceptions = InvalidRanksException.class)
    public void checkSkillNotAllowedWithProfessionalContractLowLevel() throws InvalidXmlElementException, BeneficeAlreadyAddedException, InvalidRanksException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.setFaction(FactionsFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("professionalContract_2", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER));
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("spacecraftOperations", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 3);
    }

    @Test(expectedExceptions = InvalidRanksException.class)
    public void checkSkillHasOtherSkillsRestrictions() throws InvalidXmlElementException, InvalidRanksException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("terraforming", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 3);
    }

    @Test
    public void checkSkillHasOtherSkillsRestrictionsResolved() throws InvalidXmlElementException, InvalidRanksException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.setFaction(FactionsFactory.getInstance().getElement("engineers", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("physicalScience", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 3);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lifeScience", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 3);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("terraforming", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 3);
    }

    @Test(expectedExceptions = InvalidSkillException.class)
    public void checkSkillRestrictedToRace() throws InvalidXmlElementException, InvalidRanksException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.setRace(RaceFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("fly", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 3);
    }

    @Test(expectedExceptions = InvalidSkillException.class)
    public void checkSkillSpecializationRestrictedToRace() throws InvalidXmlElementException, InvalidRanksException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.setRace(RaceFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "kelantiLore", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 3);
    }
}
