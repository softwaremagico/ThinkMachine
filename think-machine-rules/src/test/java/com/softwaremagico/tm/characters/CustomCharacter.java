package com.softwaremagico.tm.characters;

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

import com.softwaremagico.tm.CacheHandler;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.*;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;

public class CustomCharacter {
	public final static int COST = 58;

	public static CharacterPlayer create(String language, String moduleName)
			throws InvalidXmlElementException, TooManyBlessingsException, TooManyCyberneticDevicesException,
			RequiredCyberneticDevicesException, BlessingAlreadyAddedException, BeneficeAlreadyAddedException {
		CacheHandler.clearCache();
		final CharacterPlayer player = new CharacterPlayer(language, moduleName);
		player.getInfo().addName(new Name("Oliver", language, moduleName, Gender.MALE, null));
		player.getInfo().setSurname(new Surname("Queen", language, moduleName, null));
		player.getInfo().setPlayer("Player 1");
		player.getInfo().setGender(Gender.MALE);
		player.getInfo().setAge(31);
		player.setRace(RaceFactory.getInstance().getElement("human", language, moduleName));
		player.getInfo().setPlanet(PlanetFactory.getInstance().getElement("sutek", language, moduleName));
		player.setFaction(FactionsFactory.getInstance().getElement("hazat", language, moduleName));

		player.getInfo().setBirthdate("4996-09-16");
		player.getInfo().setHair("Moreno");
		player.getInfo().setEyes("Marrones");
		player.getInfo().setComplexion("Delgado");
		player.getInfo().setHeight("1,76m");
		player.getInfo().setWeight("78kg");

		//Min value for STG and DEX is 3!
		player.setCharacteristic(CharacteristicName.STRENGTH, 2);
		player.setCharacteristic(CharacteristicName.DEXTERITY, 2);
		player.setCharacteristic(CharacteristicName.ENDURANCE, 3);
		player.setCharacteristic(CharacteristicName.WITS, 4);
		player.setCharacteristic(CharacteristicName.PERCEPTION, 5);
		player.setCharacteristic(CharacteristicName.TECH, 6);
		player.setCharacteristic(CharacteristicName.PRESENCE, 7);
		player.setCharacteristic(CharacteristicName.WILL, 8);
		player.setCharacteristic(CharacteristicName.FAITH, 9);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", language, moduleName), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("sneak", language, moduleName), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("gaming", language, moduleName), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lockpicking", language, moduleName), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", language, moduleName), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("warfare", language, moduleName), 5);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("lore", "jumpwebLore", language, moduleName), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "beastsLore", language, moduleName),
				2);

		player.setPsiqueLevel(OccultismTypeFactory.getPsi(language, moduleName), 4);
		player.setDarkSideLevel(OccultismTypeFactory.getPsi(language, moduleName), 1);

		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("liftingHand"));
		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("throwingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("sixthSense", player.getLanguage(), player.getModuleName()).getOccultismPowers()
				.get("sensitivity"));
		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("soma", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("toughening"));
		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("soma", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("strengthening"));
		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("soma", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("quickening"));
		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("soma", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("hardening"));

		player.addBlessing(
				BlessingFactory.getInstance().getElement("handsome", player.getLanguage(), player.getModuleName()));
		player.addBlessing(
				BlessingFactory.getInstance().getElement("curious", player.getLanguage(), player.getModuleName()));
		player.addBlessing(
				BlessingFactory.getInstance().getElement("missingEye", player.getLanguage(), player.getModuleName()));
		player.addBlessing(
				BlessingFactory.getInstance().getElement("luckyAtCards", player.getLanguage(), player.getModuleName()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("stigma_1", player.getLanguage(),
				player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("heir", player.getLanguage(),
				player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("wireblade", player.getLanguage(),
				player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("pistola", player.getLanguage(),
				player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("shaidan", player.getLanguage(),
				player.getModuleName()));

		SelectedCyberneticDevice engineersEye = player.addCybernetics(CyberneticDeviceFactory.getInstance()
				.getElement("engineersEye", player.getLanguage(), player.getModuleName()));
		engineersEye.addCustomization(CyberneticDeviceTraitFactory.getInstance().getElement("hidden",
				player.getLanguage(), player.getModuleName()));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("jonah", player.getLanguage(),
				player.getModuleName()));

		player.addWeapon(WeaponFactory.getInstance().getElement("mace", player.getLanguage(), player.getModuleName()));
		player.addWeapon(
				WeaponFactory.getInstance().getElement("nitobiBlasterAxe", player.getLanguage(), player.getModuleName()));

		player.setArmour(
				ArmourFactory.getInstance().getElement("synthsilk", player.getLanguage(), player.getModuleName()));

		player.setShield(
				ShieldFactory.getInstance().getElement("assaultShield", player.getLanguage(), player.getModuleName()));

		return player;
	}
}
