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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.softwaremagico.tm.character.factions.Faction;

public class Occultism {
	private int psiValue = 0;
	private int theurgyValue = 0;
	private int extraWyrd = 0;
	private int urge = 0;
	private int hubris = 0;

	// Path --> Set<Power>
	private final Map<String, Set<String>> selectedPowers;

	public Occultism() {
		selectedPowers = new HashMap<>();
	}

	public int getExtraWyrd() {
		return extraWyrd;
	}

	public void setExtraWyrd(int extraWyrd) {
		this.extraWyrd = extraWyrd;
	}

	public int getPsiValue() {
		return psiValue;
	}

	public void setPsiValue(int psyValue) {
		this.psiValue = psyValue;
	}

	public int getTheurgyValue() {
		return theurgyValue;
	}

	public void setTeurgyValue(int teurgyValue) {
		this.theurgyValue = teurgyValue;
	}

	public int getUrge() {
		return urge;
	}

	public void setUrge(int urge) {
		this.urge = urge;
	}

	public int getHubris() {
		return hubris;
	}

	public void setHubris(int hubris) {
		this.hubris = hubris;
	}

	public Map<String, Set<String>> getSelectedPowers() {
		return selectedPowers;
	}

	public void addPower(OccultismPower power, String language, Faction faction) throws InvalidOccultismPowerException {
		if (power == null) {
			throw new InvalidOccultismPowerException("Power cannot be null.");
		}
		OccultismPath path = OccultismPathFactory.getInstance().getOccultismPath(power, language);
		// Correct level of psi or teurgy
		if (path.getOccultismType().equals(OccultismType.PSI) && power.getLevel() > getPsiValue()) {
			throw new InvalidPsiqueLevelException("Insuficient psi level to acquire '" + power + "'.");
		}
		if (path.getOccultismType().equals(OccultismType.THEURGY) && power.getLevel() > getTheurgyValue()) {
			throw new InvalidPsiqueLevelException("Insuficient theurgy level to acquire '" + power + "'.");
		}
		// Limited to some factions
		if (!path.getFactionsAllowed().isEmpty() && !path.getFactionsAllowed().contains(faction)) {
			throw new InvalidFactionOfPowerException("Power can only be acquired by  '" + path.getFactionsAllowed() + "'.");
		}

		// Psi must have previous level.
		if (path.getOccultismType() == OccultismType.PSI) {
			boolean acquiredLevel = false;
			for (OccultismPower previousLevelPower : path.getPreviousLevelPowers(power)) {
				if (selectedPowers.get(path.getId()) != null
						&& selectedPowers.get(path.getId()).contains(previousLevelPower.getId())) {
					acquiredLevel = true;
					break;
				}
			}
			if (!acquiredLevel && !path.getPreviousLevelPowers(power).isEmpty()) {
				throw new InvalidPowerLevelException("At least one power of '" + path.getPreviousLevelPowers(power)
						+ "' must be selected.");
			}
		}
		if (selectedPowers.get(path.getId()) == null) {
			selectedPowers.put(path.getId(), new HashSet<String>());
		}
		selectedPowers.get(path.getId()).add(power.getId());
	}
}
