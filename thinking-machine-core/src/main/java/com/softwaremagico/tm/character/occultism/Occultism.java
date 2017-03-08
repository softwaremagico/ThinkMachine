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

import com.softwaremagico.tm.ElementList;

public class Occultism extends ElementList<OccultismPower> {
	private int psiValue = 0;
	private int teurgyValue = 0;
	private int extraWyrd = 0;
	private int urge = 0;
	private int hubris = 0;

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

	public int getTeurgyValue() {
		return teurgyValue;
	}

	public void setTeurgyValue(int teurgyValue) {
		this.teurgyValue = teurgyValue;
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

}
