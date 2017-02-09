package com.softwaremagico.tm.character.traits;

public class Blessing implements Comparable<Blessing> {
	private String name;
	private Integer cost;
	private Integer bonification;
	private String trait;
	private String situation;

	public Blessing(String name, Integer cost, Integer bonification, String trait, String situation) {
		this.name = name;
		this.cost = cost;
		this.bonification = bonification;
		this.trait = trait;
		this.situation = situation;
	}

	public String getName() {
		return name;
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

	@Override
	public int compareTo(Blessing blessing) {
		return getName().compareTo(blessing.getName());
	}

	@Override
	public String toString() {
		return getName();
	}
}
