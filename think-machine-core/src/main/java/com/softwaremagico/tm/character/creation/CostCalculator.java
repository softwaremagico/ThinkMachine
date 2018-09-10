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

import java.util.List;
import java.util.Map.Entry;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.log.CostCalculatorLog;

public class CostCalculator {
	public final static int CHARACTERISTIC_EXTRA_POINTS_COST = 3;
	public static final int SKILL_EXTRA_POINTS_COST = 1;

	public final static int PSIQUE_LEVEL_COST = 3;
	public final static int PATH_LEVEL_COST = 1;
	public final static int EXTRA_WYRD_COST = 2;
	public final static int OCCULSTIM_POWER_LEVEL_COST = 1;

	public static int getCost(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int cost = 0;
		if (characterPlayer.getRace() != null) {
			cost += characterPlayer.getRace().getCost();
		}
		cost += getCharacteristicsCost(characterPlayer);
		cost += getSkillCosts(characterPlayer);
		cost += getTraitsCosts(characterPlayer);
		cost += getPsiPowersCosts(characterPlayer);
		cost += getCyberneticsCost(characterPlayer);
		CostCalculatorLog.debug(CostCalculator.class.getName(), "Character '" + characterPlayer.getNameRepresentation()
				+ "' total cost: " + cost + "\n");
		return cost;
	}

	public static int logCost(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int cost = 0;
		CostCalculatorLog.info(CostCalculator.class.getName(), "####################### ");
		CostCalculatorLog.info(CostCalculator.class.getName(), "\t" + characterPlayer.getNameRepresentation());
		CostCalculatorLog.info(CostCalculator.class.getName(), "####################### ");
		if (characterPlayer.getRace() != null) {
			cost += characterPlayer.getRace().getCost();
			CostCalculatorLog.info(CostCalculator.class.getName(), "Race cost: " + characterPlayer.getRace().getCost());
		}
		cost += getCharacteristicsCost(characterPlayer);
		CostCalculatorLog.info(CostCalculator.class.getName(), "Characteristics cost: "
				+ getCharacteristicsCost(characterPlayer));
		cost += getSkillCosts(characterPlayer);
		CostCalculatorLog.info(CostCalculator.class.getName(), "Skills cost: " + getSkillCosts(characterPlayer));
		cost += getTraitsCosts(characterPlayer);
		CostCalculatorLog.info(CostCalculator.class.getName(), "Traits cost: " + getTraitsCosts(characterPlayer));
		cost += getPsiPowersCosts(characterPlayer);
		CostCalculatorLog
				.info(CostCalculator.class.getName(), "Psi powers cost: " + getPsiPowersCosts(characterPlayer));
		cost += getCyberneticsCost(characterPlayer);
		CostCalculatorLog.info(CostCalculator.class.getName(), "Cybernetics cost: "
				+ getCyberneticsCost(characterPlayer));
		CostCalculatorLog.info(CostCalculator.class.getName(), "Total cost: " + cost + "\n");
		return cost;
	}

	private static int getCharacteristicsCost(CharacterPlayer characterPlayer) {
		return (characterPlayer.getCharacteristicsTotalPoints() - FreeStyleCharacterCreation.CHARACTERISTICS_POINTS)
				* CHARACTERISTIC_EXTRA_POINTS_COST;
	}

	private static int getSkillCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		return (characterPlayer.getSkillsTotalPoints() - FreeStyleCharacterCreation.SKILLS_POINTS)
				* SKILL_EXTRA_POINTS_COST;
	}

	private static int getTraitsCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int cost = 0;
		cost += getBlessingCosts(characterPlayer);
		cost += getBeneficesCosts(characterPlayer);
		return cost - FreeStyleCharacterCreation.TRAITS_POINTS;
	}

	private static int getBlessingCosts(CharacterPlayer characterPlayer) {
		int cost = 0;
		for (Blessing blessing : characterPlayer.getAllBlessings()) {
			cost += blessing.getCost();
		}
		return cost;
	}

	public static int getBlessingCosts(List<Blessing> blessings) {
		int cost = 0;
		for (Blessing blessing : blessings) {
			cost += blessing.getCost();
		}
		return cost;
	}

	public static int getBeneficesCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int cost = 0;
		for (AvailableBenefice benefit : characterPlayer.getAllBenefices()) {
			cost += benefit.getCost();
		}
		for (AvailableBenefice affliction : characterPlayer.getAfflictions()) {
			cost += affliction.getCost();
		}
		return cost;
	}

	private static int getPsiPowersCosts(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int cost = 0;
		for (Entry<String, List<String>> occulstismPathEntry : characterPlayer.getSelectedPowers()
				.entrySet()) {
			OccultismPath occultismPath = OccultismPathFactory.getInstance().getElement(occulstismPathEntry.getKey(),
					characterPlayer.getLanguage());
			for (String occultismPowerName : occulstismPathEntry.getValue()) {
				cost += occultismPath.getOccultismPowers().get(occultismPowerName).getLevel()
						* OCCULSTIM_POWER_LEVEL_COST;
			}
		}
		cost += characterPlayer.getExtraWyrd() * EXTRA_WYRD_COST;
		cost += Math.max(
				0,
				(characterPlayer.getPsiqueLevel(
						OccultismTypeFactory.getPsi(characterPlayer.getLanguage())) - characterPlayer.getRace()
						.getPsi()) * PSIQUE_LEVEL_COST);
		cost += Math.max(
				0,
				(characterPlayer.getPsiqueLevel(
						OccultismTypeFactory.getTheurgy(characterPlayer.getLanguage())) - characterPlayer.getRace()
						.getTheurgy()) * PSIQUE_LEVEL_COST);
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
