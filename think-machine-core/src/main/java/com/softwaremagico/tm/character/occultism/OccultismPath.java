package com.softwaremagico.tm.character.occultism;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.factions.Faction;

public class OccultismPath extends Element<OccultismPath> {

	private final OccultismType occultismType;
	private final Map<String, OccultismPower> occultismPowers;
	private final Set<Faction> factionsAllowed;

	public OccultismPath(String id, String name, OccultismType occultismType, Set<Faction> allowedFactions) {
		super(id, name);
		this.occultismType = occultismType;
		occultismPowers = new HashMap<>();
		this.factionsAllowed = allowedFactions;
	}

	public OccultismType getOccultismType() {
		return occultismType;
	}

	public Map<String, OccultismPower> getOccultismPowers() {
		return occultismPowers;
	}

	/**
	 * Gets the previous level powers form a power. At least one of them must be aquired to purchase this power if is a
	 * psi path.
	 * 
	 * @param path
	 *            the path of the power.
	 * @param power
	 *            the power that has one level more than the previous one
	 * @return A set with one or more powers.
	 */
	public Set<OccultismPower> getPreviousLevelPowers(OccultismPower power) {
		Integer previousLevel = getPreviousLevelWithPowers(power);
		if (previousLevel != null) {
			return getPowersOfLevel(previousLevel);
		}
		return new HashSet<OccultismPower>();
	}

	private Integer getPreviousLevelWithPowers(OccultismPower power) {
		List<OccultismPower> powersOfPath = new ArrayList<>(occultismPowers.values());

		// Sort by level inverse.
		Collections.sort(powersOfPath, new Comparator<OccultismPower>() {

			@Override
			public int compare(OccultismPower power0, OccultismPower power1) {
				if (power1.getLevel() != power1.getLevel()) {
					return power1.getLevel() - power0.getLevel();
				}
				return power1.compareTo(power0);
			}

		});

		// From up to down.
		Iterator<OccultismPower> powerIterator = powersOfPath.iterator();
		while (powerIterator.hasNext()) {
			OccultismPower next = powerIterator.next();
			if (next.getLevel() < power.getLevel()) {
				return next.getLevel();
			}
		}
		return null;
	}

	public Set<OccultismPower> getPowersOfLevel(int level) {
		Set<OccultismPower> powersOfLevel = new HashSet<>();
		for (OccultismPower power : getOccultismPowers().values()) {
			if (power.getLevel() == level) {
				powersOfLevel.add(power);
			}
		}
		return powersOfLevel;
	}

	public Set<Faction> getFactionsAllowed() {
		return factionsAllowed;
	}
}
