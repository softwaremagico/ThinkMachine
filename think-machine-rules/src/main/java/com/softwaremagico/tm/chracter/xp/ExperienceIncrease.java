package com.softwaremagico.tm.chracter.xp;

import java.util.Date;

public class ExperienceIncrease implements Comparable<ExperienceIncrease> {
	private final String elementId;
	private final Integer rank;
	private final Integer cost;
	private final Date createdAt;

	public ExperienceIncrease(String elementId, int rank, int cost) {
		this.createdAt = new Date();
		this.elementId = elementId;
		this.rank = Integer.valueOf(rank);
		this.cost = Integer.valueOf(cost);
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public String getElementId() {
		return elementId;
	}

	public Integer getRank() {
		return rank;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elementId == null) ? 0 : elementId.hashCode());
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
		if (elementId == null) {
			if (other.elementId != null) {
				return false;
			}
		} else if (!elementId.equals(other.elementId)) {
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
