package com.softwaremagico.tm.random.profile;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.predefined.PredefinedMerger;
import com.softwaremagico.tm.random.predefined.profile.RandomProfileFactory;
import com.softwaremagico.tm.random.selectors.*;
import com.softwaremagico.tm.txt.CharacterSheet;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

@Test(groups = "profile")
public class ProfileTests {
    private static final String LANGUAGE = "en";

    private void checkCharacterisic(CharacterPlayer characterPlayer, CharacteristicName characteristicName, int value) {
        try {
            Assert.assertTrue(characterPlayer.getValue(characteristicName) >= value);
        } catch (AssertionError e) {
            System.out.println("################ ERROR ################");
            final CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
            System.out.println(characterSheet);
            throw e;
        }
    }

    @Test(timeOut = 5000)
    public void getGroups() {
        Assert.assertFalse(RandomProfileFactory.getInstance().getGroups(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).isEmpty());
    }

    @Test(timeOut = 5000)
    public void checkPreferencesReader() throws InvalidXmlElementException {
        Assert.assertEquals(RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences().size(),
                7);
        Assert.assertTrue(RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences()
                .contains(CombatPreferences.BELLIGERENT));
        Assert.assertEquals(RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getCharacteristicMinimumValues(CharacteristicName.DEXTERITY).getValue(), 6);
    }

    @Test(timeOut = 5000)
    public void checkParent() throws InvalidXmlElementException {
        Assert.assertEquals(
                RandomProfileFactory.getInstance().getElement("heavySoldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences().size(), 7);
        Assert.assertTrue(RandomProfileFactory.getInstance().getElement("heavySoldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences()
                .contains(CombatPreferences.BELLIGERENT));
        Assert.assertEquals(RandomProfileFactory.getInstance().getElement("heavySoldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getCharacteristicMinimumValues(CharacteristicName.DEXTERITY).getValue(), 6);
        Assert.assertEquals(RandomProfileFactory.getInstance().getElement("heavySoldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getCharacteristicMinimumValues(CharacteristicName.STRENGTH).getValue(), 6);
    }

    @Test(timeOut = 5000)
    public void soldier() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
    }

    @Test(timeOut = 5000)
    public void heavySoldier() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                RandomProfileFactory.getInstance().getElement("heavySoldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(RandomProfileFactory.getInstance().getElement("heavySoldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getCharacteristicMinimumValues(CharacteristicName.STRENGTH).getValue(), 6);
        randomizeCharacter.createCharacter();
        checkCharacterisic(characterPlayer, CharacteristicName.STRENGTH, 6);
    }

    @Test(timeOut = 5000)
    public void nobility() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, new HashSet<>(Collections.singletonList(RacePreferences.HUMAN)),
                RandomProfileFactory.getInstance().getElement("nobility", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction().getFactionGroup(), FactionGroup.NOBILITY);
    }

    @Test(timeOut = 5000)
    public void clergy() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, new HashSet<>(Collections.singletonList(RacePreferences.HUMAN)),
                RandomProfileFactory.getInstance().getElement("clergy", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction().getFactionGroup(), FactionGroup.CHURCH);
    }

    @Test(timeOut = 5000)
    public void teenager() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                RandomProfileFactory.getInstance().getElement("young", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getInfo().getAge() >= 13 && characterPlayer.getInfo().getAge() <= 20);
    }

    @Test(timeOut = 5000)
    public void notOfficial() throws InvalidXmlElementException {
        Assert.assertFalse(RandomProfileFactory.getInstance().getElement("shantor", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).isOfficial());
    }

    @Test(timeOut = 5000)
    public void official() throws InvalidXmlElementException {
        Assert.assertTrue(RandomProfileFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).isOfficial());
    }

    @Test(timeOut = 5000)
    public void old() throws DuplicatedPreferenceException, InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                RandomProfileFactory.getInstance().getElement("old", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getInfo().getAge() >= 51 && characterPlayer.getInfo().getAge() <= 110);
    }

    @Test(timeOut = 5000)
    public void mergingPreferences() {
        Set<IRandomPreference<?>> originalPreferences = Stream.of(CombatPreferences.PEACEFUL, AgePreferences.ADULT)
                .collect(Collectors.toCollection(HashSet::new));
        Set<IRandomPreference<?>> newPreferences = Stream.of(CombatPreferences.BELLIGERENT, BlessingPreferences.APPEARANCE)
                .collect(Collectors.toCollection(HashSet::new));
        PredefinedMerger.mergePreferences(originalPreferences, newPreferences);
        Assert.assertEquals(originalPreferences.size(), 3);
        boolean found = false;
        for (IRandomPreference<?> preference : originalPreferences) {
            if (Objects.equals(preference.getClass().getName(), CombatPreferences.class.getName())) {
                Assert.assertEquals(preference, CombatPreferences.FAIR);
                found = true;
            }
        }
        Assert.assertTrue(found);
    }

    @Test(timeOut = 5000)
    public void mergingPreferencesRounded() {
        Set<IRandomPreference<?>> originalPreferences = Stream.of(AgePreferences.CHILD)
                .collect(Collectors.toCollection(HashSet::new));
        Set<IRandomPreference<?>> newPreferences = Stream.of(AgePreferences.VERY_OLD)
                .collect(Collectors.toCollection(HashSet::new));
        PredefinedMerger.mergePreferences(originalPreferences, newPreferences);
        Assert.assertEquals(originalPreferences.size(), 1);
        boolean found = false;
        for (IRandomPreference<?> preference : originalPreferences) {
            if (Objects.equals(preference.getClass().getName(), AgePreferences.class.getName())) {
                Assert.assertEquals(preference, AgePreferences.ADULT);
                found = true;
            }
        }
        Assert.assertTrue(found);
    }
}
