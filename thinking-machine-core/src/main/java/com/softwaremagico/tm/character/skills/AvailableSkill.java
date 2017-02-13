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

public class AvailableSkill extends Skill {
	private boolean fromGuild;
	private boolean natural;
	private boolean generalizable;
	private String generalization = null;
	private int indexOfGeneralization = 0;

	public AvailableSkill(String name, boolean natural) {
		generalizable = name.contains("[]");
		name = name.replace("[]", "").trim();
		fromGuild = name.contains("*");
		setName(name.replace("*", "").trim());
		this.natural = natural;
	}

	public boolean isFromGuild() {
		return fromGuild;
	}

	public boolean isNatural() {
		return natural;
	}

	public boolean isGeneralizable() {
		return generalizable;
	}

	public String getGeneralization() {
		return generalization;
	}

	public void setGeneralization(String generalization) {
		this.generalization = generalization;
	}

	public int getIndexOfGeneralization() {
		return indexOfGeneralization;
	}

	public void setIndexOfGeneralization(int indexOfGeneralization) {
		this.indexOfGeneralization = indexOfGeneralization;
	}

}
