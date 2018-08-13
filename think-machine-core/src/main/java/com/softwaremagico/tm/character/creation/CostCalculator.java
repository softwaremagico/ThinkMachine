package com.softwaremagico.tm.character.creation;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.log.MachineLog;

public class CostCalculator {

	public static int getCost(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int cost = 0;
		MachineLog.info(CostCalculator.class.getName(), "####################### ");
		MachineLog.info(CostCalculator.class.getName(), "\t" + characterPlayer.getInfo().getName());
		MachineLog.info(CostCalculator.class.getName(), "####################### ");
		if (characterPlayer.getRace() != null) {
			cost += characterPlayer.getRace().getCost();
			MachineLog.info(CostCalculator.class.getName(), "Race cost: " + characterPlayer.getRace().getCost());
		}
		cost += getCharacteristicsCost(characterPlayer);
		MachineLog.info(CostCalculator.class.getName(), "Characteristics cost: " + getCharacteristicsCost(characterPlayer));
		cost += getSkillCosts(characterPlayer);
		MachineLog.info(CostCalculator.class.getName(), "Skills cost: " + getSkillCosts(characterPlayer));
		cost += getTraitsCosts(characterPlayer);
		MachineLog.info(CostCalculator.class.getName(), "Traits cost: " + getTraitsCosts(characterPlayer));
		cost += getPsiPowersCosts(characterPlayer);
		MachineLog.info(CostCalculator.class.getName(), "Psi powers cost: " + getPsiPowersCosts(characterPlayer));
		cost += getCyberneticsCost(characterPlayer);
		MachineLog.info(CostCalculator.class.getName(), "Cybernetics cost: " + getCyberneticsCost(characterPlayer));
		MachineLog.info(CostCalculator.class.getName(), "Total cost: " + cost + "\n");
		return cost;
	}

	private static int getCharacteristicsCost(CharacterPlayer characterPlayer) {
		return (characterPlayer.getCharacteristicsTotalPoints() - FreeStyleCharacterCreation.CHARACTERISTICS_POINTS) * 3;
	}

	private static int getSkillCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		return (characterPlayer.getSkillsTotalPoints() - FreeStyleCharacterCreation.SKILLS_POINTS);
	}

	private static int getTraitsCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int cost = 0;
		cost += getBlessingCosts(characterPlayer);
		cost += getBeneficesCosts(characterPlayer);
		return cost - FreeStyleCharacterCreation.TRAITS_POINTS;
	}

	private static int getBlessingCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (Blessing blessing : characterPlayer.getBlessings()) {
			cost += blessing.getCost();
		}
		return cost;
	}

	public static int getBeneficesCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int cost = 0;
		for (AvailableBenefice benefit : characterPlayer.getBenefices()) {
			cost += benefit.getCost();
		}
		for (AvailableBenefice affliction : characterPlayer.getAfflictions()) {
			cost += affliction.getCost();
		}
		return cost;
	}

	private static int getPsiPowersCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (OccultismPower occultismPower : characterPlayer.getOccultism().getElements()) {
			cost += occultismPower.getLevel();
		}
		cost += characterPlayer.getOccultism().getExtraWyrd() * 2;
		cost += Math.max(0, (characterPlayer.getOccultism().getPsiValue() - characterPlayer.getRace().getPsi()) * 3);
		cost += Math.max(0, (characterPlayer.getOccultism().getTeurgyValue() - characterPlayer.getRace().getTheurgy()) * 3);
		return cost;
	}

	private static int getCyberneticsCost(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (Device device : characterPlayer.getCybernetics().getElements()) {
			cost += device.getPoints();
		}
		return cost;
	}
}
