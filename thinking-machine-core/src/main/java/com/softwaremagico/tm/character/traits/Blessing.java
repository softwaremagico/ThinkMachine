package com.softwaremagico.tm.character.traits;

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

import com.softwaremagico.tm.Element;

public class Blessing extends Element<Blessing> {
	private Integer cost;
	private Integer bonification;
	private String trait;
	private String situation;

	public Blessing(String id, String name) {
		super(id, name);
	}

	public Blessing(String name, Integer cost, Integer bonification, String trait, String situation) {
		this(null, name);
		this.cost = cost;
		this.bonification = bonification;
		this.trait = trait;
		this.situation = situation;
	}

	public Integer getCost() {
		return cost;
	}

	public Integer getBonification() {
		return bonification;
	}

	public String getTrait() {
		return trait;
	}

	public String getSituation() {
		return situation;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public void setBonification(Integer bonification) {
		this.bonification = bonification;
	}

	public void setTrait(String trait) {
		this.trait = trait;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

}
