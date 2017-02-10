package com.softwaremagico.tm.character.occultism;

public class OccultismPower implements Comparable<OccultismPower> {
	private String name;
	private String roll;
	private int level;
	private String range;
	private String duration;
	private String requirements;
	private int cost;

	public OccultismPower(String name, String roll, int level, String range, String duration, String requirements, int cost) {
		this.name = name;
		this.roll = roll;
		this.level = level;
		this.range = range;
		this.duration = duration;
		this.requirements = requirements;
		this.cost = cost;
	}

	public String getName() {
		return name;
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

	@Override
	public int compareTo(OccultismPower power) {
		return getName().compareTo(power.getName());
	}

	@Override
	public String toString() {
		return getName();
	}

}
