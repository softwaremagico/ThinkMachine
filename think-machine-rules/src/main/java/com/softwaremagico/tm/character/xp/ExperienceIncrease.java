package com.softwaremagico.tm.character.xp;

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

import java.util.Date;

import com.softwaremagico.tm.Element;

public class ExperienceIncrease implements Comparable<ExperienceIncrease> {
	private final Element<?> element;
	private final Integer rank;
	private final Integer cost;
	private final Date createdAt;

	public ExperienceIncrease(Element<?> element, int rank, int cost) {
		this.createdAt = new Date();
		this.element = element;
		this.rank = Integer.valueOf(rank);
		this.cost = Integer.valueOf(cost);
	}

	public Date getCreatedAt() {
		return new Date(createdAt.getTime());
	}

	public Element<?> getElement() {
		return element;
	}

	public Integer getRank() {
		return rank;
	}

	@Override
	public int compareTo(ExperienceIncrease cost) {
		return getRank().compareTo(cost.getRank());
	}

	public Integer getCost() {
		return cost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ExperienceIncrease other = (ExperienceIncrease) obj;
		if (element == null) {
			if (other.element != null) {
				return false;
			}
		} else if (!element.equals(other.element)) {
			return false;
		}
		if (rank == null) {
			if (other.rank != null) {
				return false;
			}
		} else if (!rank.equals(other.rank)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "{" + element + " (" + getRank() + ")" + "}";
	}

}
