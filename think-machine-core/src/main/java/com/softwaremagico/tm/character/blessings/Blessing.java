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
	private final Integer bonification;
	private final Set<IValue> affects;
	private final String situation;
	private final BlessingClassification blessingClassification;
	private final BlessingGroup blessingGroup;

	public Blessing(String id, String name, Integer cost, Integer bonification, Set<IValue> affects, String situation,
			BlessingClassification blessingClassification, BlessingGroup blessingGroup) {
		super(id, name);
		this.cost = cost;
		this.bonification = bonification;
		this.situation = situation;
		this.affects = affects;
		this.blessingClassification = blessingClassification;
		this.blessingGroup = blessingGroup;
	}

	public Integer getCost() {
		if (getBlessingClassification().equals(BlessingClassification.CURSE)) {
			return -cost;
		}
		return cost;
	}

	public Integer getBonification() {
		return bonification;
	}

	public String getTrait() {
		if (affects != null && !affects.isEmpty()) {
			Iterator<IValue> iterator = affects.iterator();
			String text = "";
			while (iterator.hasNext()) {
				if (text.length() > 0) {
					text += ", ";
				}
				text += iterator.next().getName();
			}
			return text;
		}
		return "";
	}

	public String getSituation() {
		return situation;
	}

	public BlessingClassification getBlessingClassification() {
		return blessingClassification;
	}

	public BlessingGroup getBlessingGroup() {
		return blessingGroup;
	}

}
