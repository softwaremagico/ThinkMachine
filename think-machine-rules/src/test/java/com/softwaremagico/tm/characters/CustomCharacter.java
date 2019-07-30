package com.softwaremagico.tm.characters;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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

import com.softwaremagico.tm.rules.CacheHandler;
import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.Gender;
import com.softwaremagico.tm.rules.character.Name;
import com.softwaremagico.tm.rules.character.Surname;
import com.softwaremagico.tm.rules.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.rules.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.rules.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.rules.character.blessings.BlessingFactory;
import com.softwaremagico.tm.rules.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.rules.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.rules.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.rules.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.rules.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.rules.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.rules.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.rules.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.rules.character.factions.FactionsFactory;
import com.softwaremagico.tm.rules.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.rules.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.rules.character.planets.PlanetFactory;
import com.softwaremagico.tm.rules.character.races.RaceFactory;
import com.softwaremagico.tm.rules.character.skills.AvailableSkillsFactory;

public class CustomCharacter {

	public static CharacterPlayer create(String language) throws InvalidXmlElementException, TooManyBlessingsException, TooManyCyberneticDevicesException,
			RequiredCyberneticDevicesException, BlessingAlreadyAddedException, BeneficeAlreadyAddedException {
		CacheHandler.clearCache();
		final CharacterPlayer player = new CharacterPlayer(language);
		player.getInfo().addName(new Name("Oliver", language, Gender.MALE, null));
		player.getInfo().setSurname(new Surname("Queen", language, null));
		player.getInfo().setPlayer("Player 1");
		player.getInfo().setGender(Gender.MALE);
		player.getInfo().setAge(31);
		player.setRace(RaceFactory.getInstance().getElement("human", language));
		player.getInfo().setPlanet(PlanetFactory.getInstance().getElement("sutek", language));
		player.setFaction(FactionsFactory.getInstance().getElement("hazat", language));

		player.getInfo().setBirthdate("4996-09-16");
		player.getInfo().setHair("Moreno");
		player.getInfo().setEyes("Marrones");
		player.getInfo().setComplexion("Delgado");
		player.getInfo().setHeight("1,76m");
		player.getInfo().setWeight("78kg");

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(2);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(2);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(3);
		player.getCharacteristic(CharacteristicName.WITS).setValue(4);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(5);
		player.getCharacteristic(CharacteristicName.TECH).setValue(6);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(7);
		player.getCharacteristic(CharacteristicName.WILL).setValue(8);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(9);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", language), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("sneak", language), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("gaming", language), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lockpicking", language), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", language), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("warfare", language), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "jumpwebLore", language), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "beastsLore", language), 2);

		player.setPsiqueLevel(OccultismTypeFactory.getPsi(language), 4);
		player.setDarkSideLevel(OccultismTypeFactory.getPsi(language), 1);

		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage()).getOccultismPowers().get("liftingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage()).getOccultismPowers().get("throwingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("sixthSense", player.getLanguage()).getOccultismPowers().get("sensitivity"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("soma", player.getLanguage()).getOccultismPowers().get("toughening"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("soma", player.getLanguage()).getOccultismPowers().get("strengthening"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("soma", player.getLanguage()).getOccultismPowers().get("quickening"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("soma", player.getLanguage()).getOccultismPowers().get("hardening"));

		player.addBlessing(BlessingFactory.getInstance().getElement("handsome", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("curious", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("missingEye", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("luckyAtCards", player.getLanguage()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("stigma_1", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("heir", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("wireblade", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("pistola", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("shaidan", player.getLanguage()));

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("engineersEye", language));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("jonah", language));

		player.addWeapon(WeaponFactory.getInstance().getElement("mace", language));
		player.addWeapon(WeaponFactory.getInstance().getElement("martechGold", language));

		player.setArmour(ArmourFactory.getInstance().getElement("synthsilk", language));

		player.setShield(ShieldFactory.getInstance().getElement("assaultShield", language));

		return player;
	}
}
