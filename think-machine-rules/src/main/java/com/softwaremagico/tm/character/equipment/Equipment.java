package com.softwaremagico.tm.character.equipment;

import com.softwaremagico.tm.Element;

public abstract class Equipment<E extends com.softwaremagico.tm.Element<?>> extends Element<E> {
	private final float cost;
	private final int techLevel;

	public Equipment(String id, String name, float cost, int techLevel, String language) {
		super(id, name, language);
		this.cost = cost;
		this.techLevel = techLevel;
	}

	public float getCost() {
		return cost;
	}

	public int getTechLevel() {
		return techLevel;
	}

}
