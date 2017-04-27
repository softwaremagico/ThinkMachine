package com.softwaremagico.tm.character.occultism;

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

public class OccultismPower extends Element<OccultismPower> {
	private final String roll;
	private final int level;
	private final String range;
	private final String duration;
	private final String requirements;
	private final int cost;
	private boolean enabled;

	public OccultismPower(String name, String roll, int level, String range, String duration, String requirements, int cost) {
		super(name);
		this.roll = roll;
		this.level = level;
		this.range = range;
		this.duration = duration;
		this.requirements = requirements;
		this.cost = cost;
		enabled = true;
	}

	public OccultismPower(String name, String roll, int level, String range, String duration, String requirements, int cost, boolean enabled) {
		this(name, roll, level, range, duration, requirements, cost);
		setEnabled(enabled);
	}

	public String getRoll() {
		return roll;
	}

	public int getLevel() {
		return level;
	}

	public String getRange() {
		return range;
	}

	public String getDuration() {
		return duration;
	}

	public String getRequirements() {
		return requirements;
	}

	public int getCost() {
		return cost;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
