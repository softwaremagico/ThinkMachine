package com.softwaremagico.tm.character.blessings;

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

import java.util.Iterator;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.values.IValue;

public class Blessing extends Element<Blessing> {
	private final Integer cost;
	private final Set<Bonification> bonifications;
	private final BlessingClassification blessingClassification;
	private final BlessingGroup blessingGroup;

	public Blessing(String id, String name, Integer cost, Set<Bonification> bonifications,
			BlessingClassification blessingClassification, BlessingGroup blessingGroup) {
		super(id, name);
		this.cost = cost;
		this.bonifications = bonifications;
		this.blessingClassification = blessingClassification;
		this.blessingGroup = blessingGroup;
	}

	public Integer getCost() {
		if (getBlessingClassification().equals(BlessingClassification.CURSE)) {
			return -cost;
		}
		return cost;
	}

	public BlessingClassification getBlessingClassification() {
		return blessingClassification;
	}

	public BlessingGroup getBlessingGroup() {
		return blessingGroup;
	}

	public Set<Bonification> getBonifications() {
		return bonifications;
	}

	public String getTrait() {
		if (bonifications != null && !bonifications.isEmpty()) {
			Iterator<Bonification> iterator = bonifications.iterator();
			String text = "";
			while (iterator.hasNext()) {
				IValue affects = iterator.next().getAffects();
				if (affects != null && affects.getName() != null) {
					if (text.length() > 0) {
						text += ", ";
					}
					text += affects.getName();
				}
			}
			return text;
		}
		return "";
	}

}
