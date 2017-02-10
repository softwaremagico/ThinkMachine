package com.softwaremagico.tm.character.traits;

import com.softwaremagico.tm.Element;

public class Benefit extends Element<Benefit> {
	private int cost;

	public Benefit(String name, int cost) {
		super(name);
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}

}
