package com.softwaremagico.tm.random;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.race.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.AgePreferences;
import com.softwaremagico.tm.random.selectors.BlessingNumberPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.CurseNumberPreferences;
import com.softwaremagico.tm.random.selectors.FactionPreferences;
import com.softwaremagico.tm.random.selectors.NamesPreferences;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;
import com.softwaremagico.tm.random.selectors.PsiquePathLevelPreferences;
import com.softwaremagico.tm.random.selectors.RacePreferences;
import com.softwaremagico.tm.random.selectors.SkillGroupPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.StatusPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;
import com.softwaremagico.tm.txt.CharacterSheet;

@Test(groups = { "randomCharacter" })
public class RandomCharacterTests {
	private final static String LANGUAGE = "es";

	@AfterMethod
	public void clearCache() {
		LanguagePool.clearCache();
	}

	@Test(expectedExceptions = { DuplicatedPreferenceException.class })
	public void preferencesCollision() throws InvalidXmlElementException, DuplicatedPreferenceException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		new RandomizeCharacter(characterPlayer, 0, TechnologicalPreferences.MEDIEVAL, TechnologicalPreferences.FUTURIST);
	}

	@Test
	public void chooseRaceAndFactionTest() throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, RacePreferences.HUMAN, FactionPreferences.NOBILITY);
		randomizeCharacter.setCharacterDefinition();

		Assert.assertEquals(characterPlayer.getFaction().getFactionGroup(), FactionGroup.NOBILITY);
		Assert.assertEquals(characterPlayer.getRace(), RaceFactory.getInstance().getElement(RacePreferences.HUMAN.name(), LANGUAGE));
	}

	@Test
	public void chooseRaceAndFactionTestXeno() throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, RacePreferences.OBUN, FactionPreferences.GUILD);
		randomizeCharacter.setCharacterDefinition();

		Assert.assertEquals(characterPlayer.getFaction().getFactionGroup(), FactionGroup.GUILD);
		Assert.assertEquals(characterPlayer.getRace(), RaceFactory.getInstance().getElement(RacePreferences.OBUN.name(), LANGUAGE));
	}

	@Test
	public void readRandomSkillConfigurationarchery() throws InvalidXmlElementException, DuplicatedPreferenceException {
		SkillDefinition skillDefinition = SkillsDefinitionsFactory.getInstance().get("archery", "en");
		Assert.assertEquals(skillDefinition.getRandomDefinition().getMinimumTechLevel().intValue(), 0);
		Assert.assertEquals(skillDefinition.getRandomDefinition().getMaximumTechLevel().intValue(), 2);
	}

	@Test
	public void checkWeightLimitedByDefinition() throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		RandomWeapon randomWeapons = new RandomRangeWeapon(characterPlayer, null);
		Weapon largeRock = WeaponFactory.getInstance().getElement("veryLargeRock", LANGUAGE);
		Assert.assertEquals(randomWeapons.getTotalWeight(largeRock), 0);
	}

	@Test(expectedExceptions = { InvalidRandomElementSelectedException.class })
	public void checkSkillLimitationByTechnology() throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.getCharacteristic(CharacteristicName.TECH).setValue(7);

		RandomSkills randomSkills = new RandomSkills(characterPlayer, null);
		AvailableSkill availableSkill = AvailableSkillsFactory.getInstance().getElement("archery", LANGUAGE);
		randomSkills.validateElement(availableSkill);
	}

	@Test(expectedExceptions = { InvalidRandomElementSelectedException.class })
	public void checkBeneficeLimitationByRace() throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));

		RandomBeneficeDefinition randomBenefice = new RandomBeneficeDefinition(characterPlayer, null);
		AvailableBenefice benefice = AvailableBeneficeFactory.getInstance().getElement("language [urthish]", LANGUAGE);
		randomBenefice.validateElement(benefice.getRandomDefinition());
	}

	@Test
	public void readRandomSkillConfigurationSlugs() throws InvalidXmlElementException, DuplicatedPreferenceException {
		SkillDefinition skillDefinition = SkillsDefinitionsFactory.getInstance().get("slugGuns", "en");
		Assert.assertEquals(skillDefinition.getRandomDefinition().getMinimumTechLevel().intValue(), 2);
		Assert.assertEquals(skillDefinition.getRandomDefinition().getMaximumTechLevel().intValue(), 6);
	}

	@Test
	public void selectSkillGroup() throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, SkillGroupPreferences.COMBAT);
		randomizeCharacter.createCharacter();

		Assert.assertEquals(CostCalculator.getCost(characterPlayer), FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge()));
		Assert.assertTrue(characterPlayer.getRanksAssigned(SkillGroupPreferences.COMBAT.getSkillGroup()) > 10);
	}

	@Test
	public void mustHaveStatus() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		characterPlayer.setFaction(FactionsFactory.getInstance().getElement("hazat", LANGUAGE));
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, StatusPreferences.HIGHT);
		randomizeCharacter.createCharacter();
		Assert.assertNotNull(characterPlayer.getRank());
		Assert.assertEquals(CostCalculator.getCost(characterPlayer), FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge()));
	}

	@Test
	public void checkBlessingPreferences() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, CurseNumberPreferences.FAIR, BlessingNumberPreferences.HIGH);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		randomizeCharacter.createCharacter();
		try {
			Assert.assertTrue(characterPlayer.getCurses().size() >= CurseNumberPreferences.FAIR.minimum());
			Assert.assertTrue(characterPlayer.getCurses().size() <= CurseNumberPreferences.FAIR.maximum());

			Assert.assertTrue(characterPlayer.getAllBlessings().size() >= BlessingNumberPreferences.HIGH.minimum()
					+ characterPlayer.getFaction().getBlessings().size());
			Assert.assertTrue(characterPlayer.getAllBlessings().size() <= BlessingNumberPreferences.HIGH.maximum()
					+ characterPlayer.getFaction().getBlessings().size());
		} catch (Error ae) {
			System.out.println(characterPlayer.getAllBlessings());
			throw ae;
		}
	}

	@Test
	public void createPsiqueCharacter() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, SpecializationPreferences.SPECIALIZED,
				PsiquePathLevelPreferences.HIGH, PsiqueLevelPreferences.HIGH, StatusPreferences.FAIR);
		randomizeCharacter.createCharacter();
		Assert.assertTrue(characterPlayer.getSelectedPowers().values().size() > 0);
	}

	@Test
	public void createChurchCharacter() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		characterPlayer.setFaction(FactionsFactory.getInstance().getElement("orthodox", LANGUAGE));
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, SpecializationPreferences.SPECIALIZED,
				PsiquePathLevelPreferences.HIGH, PsiqueLevelPreferences.HIGH, StatusPreferences.FAIR);
		randomizeCharacter.createCharacter();
		try {
			Assert.assertTrue(characterPlayer.getTotalSelectedPowers() > 0);
		} catch (AssertionError e) {
			CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println(characterSheet.toString());
			throw e;
		}
	}

	@Test
	public void voroxCannotHavePsique() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("vorox", LANGUAGE));
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, SpecializationPreferences.SPECIALIZED,
				PsiquePathLevelPreferences.HIGH, PsiqueLevelPreferences.HIGH);
		randomizeCharacter.createCharacter();
		Assert.assertTrue(characterPlayer.getAfflictions().contains(AvailableBeneficeFactory.getInstance().getElement("noOccult", LANGUAGE)));
		Assert.assertEquals(characterPlayer.getSelectedPowers().values().size(), 0);
	}

	@Test
	public void namesByStatus() throws InvalidXmlElementException, InvalidRandomElementSelectedException, DuplicatedPreferenceException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, NamesPreferences.VERY_HIGHT);
		randomizeCharacter.createCharacter();
		Assert.assertTrue(characterPlayer.getInfo().getNames().size() >= 2);
		Assert.assertTrue(characterPlayer.getInfo().getSurname() != null);
	}

	@Test
	public void weapons() throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("slugGuns", LANGUAGE), 5);
		characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE), 5);
		characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("fight", LANGUAGE), 5);
		characterPlayer.getCharacteristic(CharacteristicName.TECH).setValue(7);
		characterPlayer.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]", LANGUAGE));
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, CombatPreferences.BELLIGERENT);
		randomizeCharacter.createCharacter();
		Assert.assertTrue(characterPlayer.getWeapons().getElements().size() >= 2);
	}

	@Test
	public void age() throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, AgePreferences.PREADOLESCENT);
		randomizeCharacter.createCharacter();
		Assert.assertEquals(
				FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(CharacteristicName.DEXTERITY, characterPlayer.getInfo().getAge(),
						characterPlayer.getRace()), 4);
		Assert.assertEquals(FreeStyleCharacterCreation.getMaxInitialSkillsValues(characterPlayer.getInfo().getAge()), 4);
		for (Characteristic characteristic : characterPlayer.getCharacteristics(CharacteristicType.BODY)) {
			Assert.assertTrue(characteristic.getValue() <= FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
					characteristic.getCharacteristicName(), characterPlayer.getInfo().getAge(), characterPlayer.getRace()));
		}
		for (Characteristic characteristic : characterPlayer.getCharacteristics(CharacteristicType.MIND)) {
			Assert.assertTrue(characteristic.getValue() <= FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
					characteristic.getCharacteristicName(), characterPlayer.getInfo().getAge(), characterPlayer.getRace()));
		}
		for (Characteristic characteristic : characterPlayer.getCharacteristics(CharacteristicType.SPIRIT)) {
			Assert.assertTrue(characteristic.getValue() <= FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
					characteristic.getCharacteristicName(), characterPlayer.getInfo().getAge(), characterPlayer.getRace()));
		}

		for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getNaturalSkills(LANGUAGE)) {
			Assert.assertTrue(characterPlayer.getSkillAssignedRanks(skill) <= FreeStyleCharacterCreation.getMaxInitialSkillsValues(characterPlayer.getInfo()
					.getAge()));
		}
		for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getLearnedSkills(LANGUAGE)) {
			Assert.assertTrue(characterPlayer.getSkillAssignedRanks(skill) <= FreeStyleCharacterCreation.getMaxInitialSkillsValues(characterPlayer.getInfo()
					.getAge()));
		}
	}

	@Test
	public void weaponsSkills() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		characterPlayer.getWeapons().addElement(WeaponFactory.getInstance().getElement("axe", LANGUAGE));
		characterPlayer.getWeapons().addElement(WeaponFactory.getInstance().getElement("martechGold", LANGUAGE));

		Assert.assertTrue(characterPlayer.hasWeaponWithSkill(AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE)));
		Assert.assertTrue(characterPlayer.hasWeaponWithSkill(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE)));

		characterPlayer.getCharacteristic(CharacteristicName.TECH).setValue(6);

		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
		randomizeCharacter.createCharacter();

		RandomSkills randomBenefice = new RandomSkills(characterPlayer, null);
		AvailableSkill energyGuns = AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE);
		randomBenefice.validateElement(energyGuns.getRandomDefinition());
		AvailableSkill fight = AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE);
		randomBenefice.validateElement(fight.getRandomDefinition());

		try {
			Assert.assertTrue(characterPlayer.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE)) > 0);
			Assert.assertTrue(characterPlayer.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE)) > 0);
		} catch (AssertionError e) {
			CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println(characterSheet.toString());
			throw e;
		}
	}
}
