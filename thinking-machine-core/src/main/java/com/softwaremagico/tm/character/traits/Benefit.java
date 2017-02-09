package com.softwaremagico.tm.character.traits;

public class Benefit implements Comparable<Benefit> {
	private String name;
	private int cost;

	public Benefit(String name, int cost) {
		this.name = name;
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public int getCost() {
		return cost;
	}

	@Override
	public int compareTo(Benefit benefit) {
		return getName().compareTo(benefit.getName());
	}

	@Override
	public String toString() {
		return getName();
	}
}
