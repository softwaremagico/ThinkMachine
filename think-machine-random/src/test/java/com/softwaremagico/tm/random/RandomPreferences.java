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
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.benefices.BeneficeGroup;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.combat.CombatStyleGroup;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.InvalidRanksException;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;

@Test(groups = {"randomPreferences"})
public class RandomPreferences {
    private static final String LANGUAGE = "en";

    @Test(timeOut = 5000)
    public void checkGroups() {
        Assert.assertFalse(RandomPreferenceUtils.getPreferencesByGroup().isEmpty());
    }

    @Test(timeOut = 5000)
    public void checkGroupReader() {
        Assert.assertFalse(RandomPreferenceUtils.getByGroup(IPsiPreference.class).isEmpty());
    }

    @Test(timeOut = 5000)
    public void checkMoneyRequirements() {
        Assert.assertEquals(CashPreferences.get(0f), CashPreferences.LOW);
        Assert.assertEquals(CashPreferences.get(250f), CashPreferences.FAIR);
        Assert.assertEquals(CashPreferences.get(1800f), CashPreferences.GOOD);
        Assert.assertEquals(CashPreferences.get(2800f), CashPreferences.HIGH);
        Assert.assertEquals(CashPreferences.get(5800f), CashPreferences.VERY_HIGH);
    }

    @Test(timeOut = 5000)
    public void checkCombatStylesRequirements() throws InvalidRandomElementSelectedException, InvalidXmlElementException, InvalidRanksException,
            RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                CombatActionsPreferences.HIGH);
        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 8);
        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("fight", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 8);
        randomizeCharacter.createCharacter();
        Assert.assertFalse(characterPlayer.getSelectedBenefices(BeneficeGroup.FIGHTING).isEmpty());
    }

    @Test(timeOut = 5000)
    public void checkCombatStylesPreferences() throws InvalidRandomElementSelectedException, InvalidXmlElementException, InvalidRanksException,
            RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                CombatActionsPreferences.HIGH, CombatActionsGroupPreferences.MELEE);
        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 8);
        randomizeCharacter.createCharacter();
        Assert.assertFalse(characterPlayer.getSelectedBenefices(BeneficeGroup.FIGHTING).isEmpty());
        Assert.assertSame(CombatStyle.getCombatStyle(characterPlayer.getSelectedBenefices(BeneficeGroup.FIGHTING).iterator().next().getBeneficeDefinition(),
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getGroup(), CombatStyleGroup.MELEE);
    }

    @Test(timeOut = 5000)
    public void checkMinimumTech() throws InvalidRandomElementSelectedException, InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        AvailableSkill artifactMelee = AvailableSkillsFactory.getInstance().getElement("artifactMelee", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        HashSet<AvailableSkill> requiredSkills = new HashSet<>();
        requiredSkills.add(artifactMelee);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                new HashSet<>(), new HashSet<>(), requiredSkills, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getCharacteristic(CharacteristicName.TECH).getValue() >=
                artifactMelee.getRandomDefinition().getMinimumTechLevel());
    }

    @Test(timeOut = 5000)
    public void checkNobilityPreferencesHasDefaultCash() {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                FactionPreferences.NOBILITY);
        randomizeCharacter.setDefaultPreferences();
        //At least, FAIR cash must be selected for a noble.
        Assert.assertTrue(CashPreferences.getSelected(randomizeCharacter.getPreferences()).ordinal() > 0);
    }

    @Test(timeOut = 5000)
    public void checkWeaponsCostDefaultCash() throws InvalidXmlElementException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                new HashSet<>(), new HashSet<>(Collections.singletonList(FactionPreferences.NOBILITY)), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(Collections.singletonList(ShieldFactory.getInstance().getElement("battleShield", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER))));
        randomizeCharacter.setDefaultPreferences();
        //At least, FAIR cash must be selected for a noble.
        Assert.assertTrue(CashPreferences.getSelected(randomizeCharacter.getPreferences()).ordinal() > 3);
    }


}
