package com.softwaremagico.tm.character.traits;

import com.softwaremagico.tm.Element;

public class Blessing extends Element<Blessing> {
	private final Integer cost;
	private final Integer bonification;
	private final String trait;
	private final String situation;

	public Blessing(String name, Integer cost, Integer bonification, String trait, String situation) {
		super(name);
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

}
