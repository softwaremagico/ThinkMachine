package com.softwaremagico.tm.chracter.xp;

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
		return createdAt;
	}

	public Element<?> getElement() {
		return element;
	}

	public Integer getRank() {
		return rank;
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
	public int compareTo(ExperienceIncrease cost) {
		return getRank().compareTo(cost.getRank());
	}

	public Integer getCost() {
		return cost;
	}

}
