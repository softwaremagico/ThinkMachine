package com.softwaremagico.tm.character.skills;

/*-
 * #%L
 * The Thinking Machine (Core)
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

public class Skill implements Comparable<Skill> {

	private String name;

	private boolean fromGuild;

	public Skill(String name) {
		fromGuild = name.contains("*");
		this.name = name.replace("*", "").trim();
	}

	@Override
	public int compareTo(Skill skill) {
		return getName().compareTo(skill.getName());
	}

	public boolean isFromGuild() {
		return fromGuild;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

}