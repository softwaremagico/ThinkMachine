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
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.combat.CombatAction;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.combat.LearnedStance;
import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.character.equipment.Armour;
import com.softwaremagico.tm.character.equipment.Shield;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.planet.PlanetFactory;
import com.softwaremagico.tm.character.race.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;

public class CustomCharacter {

	public static CharacterPlayer create(String language) throws InvalidXmlElementException, TooManyBlessingsException {
		CacheHandler.clearCache();
		CharacterPlayer player = new CharacterPlayer(language);
		player.getInfo().addName(new Name("Oliver", Gender.MALE, null));
		player.getInfo().setSurname(new Surname("Queen", null));
		player.getInfo().setPlayer("Player 1");
		player.getInfo().setGender(Gender.MALE);
		player.getInfo().setAge(30);
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

		player.getCybernetics().addElement(new Device("Ojo de Ingeniero", 6, 5, "Normal", "Normal", "Automático", "Visible", ""));
		player.getCybernetics().addElement(new Device("Jonás", 7, 4, "Normal", "Normal", "Ds+Arquería", "Incógnito", ""));

		CombatStyle gun = new CombatStyle("pistola");
		gun.addElement(new CombatAction("Disparo Instantáneo", null, null, "-2 por 3 disparos"));
		gun.addElement(new CombatAction("Rueda y Dispara", null, null, "Mover 3m"));
		gun.addElement(new CombatAction("Corre y Dispara", null, null, "Especial"));
		player.getRangedCombatStyles().add(gun);

		CombatStyle shaidan = new CombatStyle("shaidan");
		shaidan.addElement(new CombatAction("Palma Real", null, "-1", ""));
		shaidan.addElement(new CombatAction("Con un Pie en el Trono", 4, null, "+4 a resistir derribos"));
		shaidan.addElement(new CombatAction("Decreto Imperial", null, "+1 / 1W", null));
		player.getMeleeCombatStyles().add(shaidan);

		player.getLearnedStances().add(new LearnedStance("Posición Acrobática", "+1 a defensa por volteretas"));

		player.getWeapons().addElement(WeaponFactory.getInstance().getElement("mace", language));
		player.getWeapons().addElement(WeaponFactory.getInstance().getElement("martechGold", language));

		player.setArmour(new Armour("Cuero Sintético", 7, true, false, false, false, false, false, true, 6, -1, 0, 0, 0, 500));

		player.setShield(new Shield("Escudo de Asalto", 5, 15, 20, 3000));

		return player;
	}
}
