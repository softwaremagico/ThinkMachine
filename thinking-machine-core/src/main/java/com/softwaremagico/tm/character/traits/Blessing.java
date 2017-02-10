package com.softwaremagico.tm.character.traits;

import com.softwaremagico.tm.Element;

public class Blessing extends Element<Blessing> {
	private Integer cost;
	private Integer bonification;
	private String trait;
	private String situation;

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
