package com.softwaremagico.tm.character.occultism;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.log.MachineLog;

public class Occultism {
	private Map<String, Integer> psiqueValue;
	private Map<String, Integer> darkSideValue;
	private int extraWyrd = 0;

	// Path --> Set<Power>
	private final Map<String, List<String>> selectedPowers;

	public Occultism() {
		selectedPowers = new HashMap<>();
		psiqueValue = new HashMap<>();
		darkSideValue = new HashMap<>();
	}

	public int getExtraWyrd() {
		return extraWyrd;
	}

	public void setExtraWyrd(int extraWyrd) {
		if (extraWyrd > 0) {
			this.extraWyrd = extraWyrd;
		} else {
			this.extraWyrd = 0;
		}
	}

	public int getPsiqueLevel(OccultismType occultismType) {
		if (psiqueValue.get(occultismType.getId()) != null) {
			return psiqueValue.get(occultismType.getId());
		}
		return 0;
	}

	public void setPsiqueLevel(OccultismType occultismType, int psyValue,
			String language, Faction faction) throws InvalidPsiqueLevelException {
		if (psyValue < 0) {
			throw new InvalidPsiqueLevelException(
					"Psique level cannot be less than zero.");
		}
		AvailableBenefice noOccult = null;
		try {
			noOccult = AvailableBeneficeFactory.getInstance().getElement(
					"noOccult", language);
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		}
		if (psyValue != 0
				&& (faction.getBenefices().contains(noOccult) || faction == null)) {
			throw new InvalidPsiqueLevelException("Faction '" + faction
					+ "' cannot have psique levels.");
		}
		psiqueValue.put(occultismType.getId(), new Integer(psyValue));
	}

	public int getDarkSideLevel(OccultismType occultismType) {
		if (darkSideValue.get(occultismType.getId()) != null) {
			return darkSideValue.get(occultismType.getId());
		}
		return 0;
	}

	public void setDarkSideLevel(OccultismType occultismType, int darkSideValue) {
		this.darkSideValue.put(occultismType.getId(),
				new Integer(darkSideValue));
	}

	public Map<String, List<String>> getSelectedPowers() {
		return selectedPowers;
	}

	public void addPower(OccultismPower power, String language, Faction faction)
			throws InvalidOccultismPowerException {
		if (power == null) {
			throw new InvalidOccultismPowerException("Power cannot be null.");
		}
		OccultismPath path = OccultismPathFactory.getInstance()
				.getOccultismPath(power, language);
		// Correct level of psi or teurgy
		if (Objects.equals(path.getOccultismType(),
				OccultismTypeFactory.getPsi(language))
				&& power.getLevel() > getPsiqueLevel(OccultismTypeFactory
						.getPsi(language))) {
			throw new InvalidPsiqueLevelException(
					"Insuficient psi level to acquire '" + power + "'.");
		}
		if (Objects.equals(path.getOccultismType(),
				OccultismTypeFactory.getTheurgy(language))
				&& power.getLevel() > getPsiqueLevel(OccultismTypeFactory
						.getTheurgy(language))) {
			throw new InvalidPsiqueLevelException(
					"Insuficient theurgy level to acquire '" + power + "'.");
		}
		// Limited to some factions
		if (!path.getFactionsAllowed().isEmpty()
				&& !path.getFactionsAllowed().contains(faction)) {
			throw new InvalidFactionOfPowerException("Power '" + power
					+ "' can only be acquired by  '"
					+ path.getFactionsAllowed() + "' character faction is '"
					+ faction + "'.");
		}

		// Psi must have previous level.
		if (Objects.equals(path.getOccultismType(),
				OccultismTypeFactory.getPsi(language))) {
			boolean acquiredLevel = false;
			for (OccultismPower previousLevelPower : path
					.getPreviousLevelPowers(power)) {
				if (selectedPowers.get(path.getId()) != null
						&& selectedPowers.get(path.getId()).contains(
								previousLevelPower.getId())) {
					acquiredLevel = true;
					break;
				}
			}
			if (!acquiredLevel && !path.getPreviousLevelPowers(power).isEmpty()) {
				throw new InvalidPowerLevelException("At least one power of '"
						+ path.getPreviousLevelPowers(power)
						+ "' must be selected.");
			}
		}
		if (selectedPowers.get(path.getId()) == null) {
			selectedPowers.put(path.getId(), new ArrayList<String>());
		}
		selectedPowers.get(path.getId()).add(power.getId());
	}
}
