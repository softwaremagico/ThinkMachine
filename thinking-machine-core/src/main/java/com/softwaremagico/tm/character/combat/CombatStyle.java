package com.softwaremagico.tm.character.combat;

import com.softwaremagico.tm.ElementList;

public class CombatStyle extends ElementList<CombatAction> {
	private final String name;

	public CombatStyle(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getCost() {
		return 5;
	}
}
