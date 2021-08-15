package com.softwaremagico.tm.random.npc;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.predefined.PredefinedMerger;
import com.softwaremagico.tm.random.predefined.characters.Npc;
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
            System.out.println(characterSheet);
            throw e;
        }
    }

    private void checkContainsBlessing(CharacterPlayer characterPlayer, Blessing blessing) {
        for (Blessing existingBlessing : characterPlayer.getAllBlessings()) {
            if (Objects.equals(existingBlessing, blessing)) {
                return;
            }
        }
        throw new AssertionError();
    }

    private void checkContainsBenefice(CharacterPlayer characterPlayer, AvailableBenefice availableBenefice) {
        for (AvailableBenefice benefice : characterPlayer.getAllBenefices()) {
            if (Objects.equals(benefice, availableBenefice)) {
                return;
            }
        }
        throw new AssertionError();
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

    private void checkContainsShield(CharacterPlayer characterPlayer, Shield shield) {
        if (!Objects.equals(characterPlayer.getShield(), shield)) {
            throw new AssertionError();
        }
    }

    @Test(timeOut = 5000)
    public void getGroups() {
        Assert.assertFalse(NpcFactory.getInstance().getGroups(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).isEmpty());
    }

    @Test(timeOut = 5000)
    public void checkPreferencesReader() throws InvalidXmlElementException {
        Assert.assertEquals(NpcFactory.getInstance().getElement("infantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences().size(),
                7);
        Assert.assertTrue(NpcFactory.getInstance().getElement("infantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences()
                .contains(CombatPreferences.BELLIGERENT));
        Assert.assertEquals(NpcFactory.getInstance().getElement("infantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getCharacteristicMinimumValues(CharacteristicName.DEXTERITY).getValue(), 6);
    }

    @Test(timeOut = 5000)
    public void checkFaction() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("peasant", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction(), FactionsFactory.getInstance().getElement("noFaction", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test(timeOut = 5000)
    public void checkRace() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("voroxCommand", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getRace(), RaceFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test(timeOut = 5000)
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

    @Test(timeOut = 5000)
    public void checkNoError() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        for (Npc npc : NpcFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)) {
            final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
            final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, npc);
            randomizeCharacter.createCharacter();
        }
    }

    @Test(timeOut = 5000)
    public void serf() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
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

    @Test(timeOut = 5000)
    public void thug() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
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

    @Test(timeOut = 5000)
    public void burglar() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("burglar", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(
                NpcFactory.getInstance().getElement("burglar", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getMandatoryBenefices().size(), 0);
        Assert.assertEquals(
                NpcFactory.getInstance().getElement("burglar", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getSuggestedBenefices().size(), 7);
        randomizeCharacter.createCharacter();
    }

    @Test(timeOut = 5000)
    public void slayer() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("slayer", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(
                NpcFactory.getInstance().getElement("slayer", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getSuggestedBenefices().size(), 4);
        randomizeCharacter.createCharacter();
    }


    @Test(timeOut = 5000)
    public void heavySoldier() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
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

    @Test(timeOut = 5000)
    public void tracker() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
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

    @Test(timeOut = 5000)
    public void manifestLight() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("manifestLight", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        checkContainsBenefice(characterPlayer, AvailableBeneficeFactory.getInstance().getElement("language [latinLanguage]",
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        checkContainsShield(characterPlayer,
                ShieldFactory.getInstance().getElement("standardShield", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        checkContainsBlessing(characterPlayer,
                BlessingFactory.getInstance().getElement("disciplined", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test(timeOut = 5000)
    public void dervishes() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("dervishes", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)) > 0);
        Assert.assertTrue(characterPlayer.hasOccultismPath(OccultismPathFactory.getInstance().getElement("farHand", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
        Assert.assertTrue(characterPlayer.hasOccultismPath(OccultismPathFactory.getInstance().getElement("soma", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
    }

    @Test(timeOut = 5000)
    public void checkRaceInherit() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("obun", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getRace(), RaceFactory.getInstance().getElement("obun", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test(timeOut = 5000)
    public void checkSuggestedAvailableBenefices() throws InvalidXmlElementException, InvalidRandomElementSelectedException,
            RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("obun", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        characterPlayer.getAllBenefices().parallelStream().forEach(availableBenefice -> {
            try {
                if (Objects.equals(availableBenefice.getBeneficeDefinition(), BeneficeDefinitionFactory.getInstance().getElement("refuge",
                        LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER))) {
                    //If has refuge, must be refuge (4).
                    Assert.assertEquals(availableBenefice.getId(), "refuge_4");
                }
            } catch (InvalidXmlElementException e) {
                Assert.fail();
            }
        });
    }

    @Test(timeOut = 5000)
    public void hironemPriest() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("hironemPriest", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
    }

    @Test(timeOut = 5000)
    public void hironemWarrior() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("hironemWarrior", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
    }

    @Test(timeOut = 5000)
    public void etyriHuarrayghq() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("etyriHuarrayghq", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
    }

    @Test(timeOut = 5000)
    public void ascorbite() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("ascorbite", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction(), FactionsFactory.getInstance().getElement("nofaction", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test(timeOut = 5000)
    public void oroym() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("oroym", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertNotNull(characterPlayer.getFaction());
    }

    @Test(timeOut = 5000)
    public void kurgan() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("kurgan", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction(), FactionsFactory.getInstance().getElement("kurgan", characterPlayer.getLanguage(),
                characterPlayer.getModuleName()));
        Assert.assertTrue(characterPlayer.getMoney() >= 0);
    }

    @Test(timeOut = 5000)
    public void theMasque() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("theMasque", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction(), FactionsFactory.getInstance().getElement("theMasque", characterPlayer.getLanguage(),
                characterPlayer.getModuleName()));
    }

    @Test(timeOut = 5000)
    public void vagabonds() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("vagabonds", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction(), FactionsFactory.getInstance().getElement("vagabonds", characterPlayer.getLanguage(),
                characterPlayer.getModuleName()));
    }

    @Test(timeOut = 5000)
    public void prospectors() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("prospectors", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction(), FactionsFactory.getInstance().getElement("prospectors", characterPlayer.getLanguage(),
                characterPlayer.getModuleName()));
    }

    @Test(timeOut = 5000)
    public void apothecaries() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("apothecaries", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction(), FactionsFactory.getInstance().getElement("apothecaries", characterPlayer.getLanguage(),
                characterPlayer.getModuleName()));
    }

    @Test(timeOut = 5000)
    public void weaponsmiths() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("weaponsmiths", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction(), FactionsFactory.getInstance().getElement("weaponsmiths", characterPlayer.getLanguage(),
                characterPlayer.getModuleName()));
    }

    @Test(timeOut = 5000)
    public void courtesan() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("courtesans", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getFaction(), FactionsFactory.getInstance().getElement("courtesans", characterPlayer.getLanguage(),
                characterPlayer.getModuleName()));
    }


    @Test(timeOut = 5000)
    public void restricted() throws InvalidXmlElementException {
        Assert.assertFalse(NpcFactory.getInstance().getElement("shantor", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).isOfficial());
    }

    @Test(timeOut = 5000)
    public void pilot() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.getSettings().setOnlyOfficialAllowed(false);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
                NpcFactory.getInstance().getElement("pilot", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getMoney() >= 0);
    }
}
