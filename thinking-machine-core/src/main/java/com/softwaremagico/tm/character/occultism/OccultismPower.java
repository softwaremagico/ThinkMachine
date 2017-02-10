package com.softwaremagico.tm.character.occultism;

import com.softwaremagico.tm.Element;

public class OccultismPower extends Element<OccultismPower> {
	private final String roll;
	private final int level;
	private final String range;
	private final String duration;
	private final String requirements;
	private final int cost;

	public OccultismPower(String name, String roll, int level, String range, String duration, String requirements, int cost) {
		super(name);
		this.roll = roll;
		this.level = level;
		this.range = range;
		this.duration = duration;
		this.requirements = requirements;
		this.cost = cost;
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

}
