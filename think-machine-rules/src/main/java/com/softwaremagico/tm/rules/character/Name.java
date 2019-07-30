package com.softwaremagico.tm.rules.character;

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

import com.softwaremagico.tm.rules.Element;
import com.softwaremagico.tm.rules.character.factions.Faction;
import com.softwaremagico.tm.rules.json.ExcludeFromJson;

public class Name extends Element<Name> {

	@ExcludeFromJson
	private final Gender gender;

	private final Faction faction;

	public Faction getFaction() {
		return faction;
	}

	public Name(String name, String language, Gender gender, Faction faction) {
		super(name.toLowerCase(), name, language);
		this.gender = gender;
		this.faction = faction;
	}

	public Gender getGender() {
		return gender;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

}
