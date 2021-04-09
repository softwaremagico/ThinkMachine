package com.softwaremagico.tm.random;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.benefices.BeneficeGroup;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CashPreferences;
import com.softwaremagico.tm.random.selectors.CombatActionsPreferences;
import com.softwaremagico.tm.random.selectors.IPsiPreference;
import com.softwaremagico.tm.random.selectors.RandomPreferenceUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"randomPreferences"})
public class RandomPreferences {
    private static final String LANGUAGE = "en";

    @Test
    public void checkGroups() {
        Assert.assertFalse(RandomPreferenceUtils.getPreferencesByGroup().isEmpty());
    }

    @Test
    public void checkGroupReader() {
        Assert.assertFalse(RandomPreferenceUtils.getByGroup(IPsiPreference.class).isEmpty());
    }

    @Test
    public void checkMoneyRequirements() {
        Assert.assertEquals(CashPreferences.get(0f), CashPreferences.LOW);
        Assert.assertEquals(CashPreferences.get(250f), CashPreferences.FAIR);
        Assert.assertEquals(CashPreferences.get(1800f), CashPreferences.GOOD);
        Assert.assertEquals(CashPreferences.get(2800f), CashPreferences.HIGH);
        Assert.assertEquals(CashPreferences.get(5800f), CashPreferences.VERY_HIGH);
    }

    @Test
    public void checkCombatStylesRequirements() throws InvalidRandomElementSelectedException, InvalidXmlElementException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                CombatActionsPreferences.HIGH);
        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 8);
        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("fight", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 8);
        randomizeCharacter.createCharacter();
        Assert.assertFalse(characterPlayer.getSelectedBenefices(BeneficeGroup.FIGHTING).isEmpty());
    }


}
