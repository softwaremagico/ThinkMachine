package com.softwaremagico.tm.random.npc;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.predefined.PredefinedMerger;
import com.softwaremagico.tm.random.predefined.characters.NpcFactory;
import com.softwaremagico.tm.random.selectors.AgePreferences;
import com.softwaremagico.tm.random.selectors.BlessingPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.txt.CharacterSheet;
import org.testng.Assert;
import org.testng.annotations.Test;

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
public class NpcTests {
    private static final String LANGUAGE = "en";

    private void checkCharacteristic(CharacterPlayer characterPlayer, CharacteristicName characteristicName, int value) {
        try {
            Assert.assertTrue(characterPlayer.getValue(characteristicName) >= value);
        } catch (AssertionError e) {
            System.out.println("################ ERROR ################");
            final CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
            System.out.println(characterSheet.toString());
            throw e;
        }
    }

    private void checkContainsWeapon(CharacterPlayer characterPlayer, Weapon weapon) {
        for (Weapon characterWeapon : characterPlayer.getAllWeapons()) {
            if (Objects.equals(characterWeapon, weapon)) {
                return;
            }
        }
        throw new AssertionError();
    }

    private void checkContainsArmour(CharacterPlayer characterPlayer, Armour armour) {
        if (!Objects.equals(characterPlayer.getArmour(), armour)) {
            throw new AssertionError();
        }
    }

    @Test
    public void getGroups() {
        Assert.assertFalse(NpcFactory.getInstance().getGroups(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).isEmpty());
    }

    @Test
    public void checkPreferencesReader() throws InvalidXmlElementException {
        Assert.assertEquals(NpcFactory.getInstance().getElement("infantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences().size(),
                7);
        Assert.assertTrue(NpcFactory.getInstance().getElement("infantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences()
                .contains(CombatPreferences.BELLIGERENT));
        Assert.assertEquals(NpcFactory.getInstance().getElement("infantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getCharacteristicMinimumValues(CharacteristicName.DEXTERITY).getValue(), 6);
    }

    @Test
    public void checkFaction() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("peasant", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction(), FactionsFactory.getInstance().getElement("noFaction", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test
    public void checkRace() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("voroxCommand", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getRace(), RaceFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test
    public void checkParent() throws InvalidXmlElementException {
        Assert.assertEquals(
                NpcFactory.getInstance().getElement("heavyInfantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences().size(), 7);
        Assert.assertTrue(NpcFactory.getInstance().getElement("heavyInfantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences()
                .contains(CombatPreferences.BELLIGERENT));
        Assert.assertEquals(NpcFactory.getInstance().getElement("heavyInfantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getCharacteristicMinimumValues(CharacteristicName.DEXTERITY).getValue(), 6);
        Assert.assertEquals(NpcFactory.getInstance().getElement("heavyInfantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getCharacteristicMinimumValues(CharacteristicName.STRENGTH).getValue(), 6);
    }

    @Test
    public void serf() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("serf", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(NpcFactory.getInstance().getElement("serf", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getRequiredSkills().size(),
                1);
        Assert.assertEquals(NpcFactory.getInstance().getElement("serf", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getSuggestedSkills().size(),
                2);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getSkillTotalRanks(
                AvailableSkillsFactory.getInstance().getElement("craft", "household", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)) > 0);
    }

    @Test
    public void thug() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("thug", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(
                NpcFactory.getInstance().getElement("thug", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getMandatoryBenefices().size(), 1);
        Assert.assertEquals(
                NpcFactory.getInstance().getElement("thug", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getSuggestedBenefices().size(), 5);
        randomizeCharacter.createCharacter();
        Assert.assertNotNull(characterPlayer.getBenefice("outlaw"));
    }

    @Test
    public void slayer() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("slayer", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(
                NpcFactory.getInstance().getElement("slayer", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getSuggestedBenefices().size(), 4);
        randomizeCharacter.createCharacter();
    }


    @Test
    public void heavySoldier() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("heavyInfantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(NpcFactory.getInstance().getElement("heavyInfantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getCharacteristicMinimumValues(CharacteristicName.STRENGTH).getValue(), 6);
        Assert.assertEquals(NpcFactory.getInstance().getElement("heavyInfantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getMandatoryWeapons().size(), 1);
        randomizeCharacter.createCharacter();
        checkCharacteristic(characterPlayer, CharacteristicName.STRENGTH, 6);
        checkContainsWeapon(characterPlayer, WeaponFactory.getInstance().getElement("jahnisak040MG", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        checkContainsArmour(characterPlayer, ArmourFactory.getInstance().getElement("leatherJerkin", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test
    public void tracker() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("tracker", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(NpcFactory.getInstance().getElement("tracker", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getCharacteristicMinimumValues(CharacteristicName.PERCEPTION).getValue(), 6);
        Assert.assertEquals(NpcFactory.getInstance().getElement("tracker", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getMandatoryWeapons().size(), 1);
        randomizeCharacter.createCharacter();
        checkCharacteristic(characterPlayer, CharacteristicName.PERCEPTION, 6);
        checkContainsWeapon(characterPlayer, WeaponFactory.getInstance().getElement("typicalSniperRifle", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        checkContainsArmour(characterPlayer,
                ArmourFactory.getInstance().getElement("leatherJerkin", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test
    public void mergingPreferences() {
        Set<IRandomPreference> originalPreferences = Stream.of(CombatPreferences.PEACEFUL, AgePreferences.ADULT)
                .collect(Collectors.toCollection(HashSet::new));
        Set<IRandomPreference> newPreferences = Stream.of(CombatPreferences.BELLIGERENT, BlessingPreferences.APPEARANCE)
                .collect(Collectors.toCollection(HashSet::new));
        PredefinedMerger.mergePreferences(originalPreferences, newPreferences);
        Assert.assertEquals(originalPreferences.size(), 3);
        boolean found = false;
        for (IRandomPreference preference : originalPreferences) {
            if (Objects.equals(preference.getClass().getName(), CombatPreferences.class.getName())) {
                Assert.assertEquals(preference, CombatPreferences.FAIR);
                found = true;
            }
        }
        Assert.assertTrue(found);
    }

    @Test
    public void mergingPreferencesRounded() {
        Set<IRandomPreference> originalPreferences = Stream.of(AgePreferences.CHILD)
                .collect(Collectors.toCollection(HashSet::new));
        Set<IRandomPreference> newPreferences = Stream.of(AgePreferences.VERY_OLD)
                .collect(Collectors.toCollection(HashSet::new));
        PredefinedMerger.mergePreferences(originalPreferences, newPreferences);
        Assert.assertEquals(originalPreferences.size(), 1);
        boolean found = false;
        for (IRandomPreference preference : originalPreferences) {
            if (Objects.equals(preference.getClass().getName(), AgePreferences.class.getName())) {
                Assert.assertEquals(preference, AgePreferences.ADULT);
                found = true;
            }
        }
        Assert.assertTrue(found);
    }
}
