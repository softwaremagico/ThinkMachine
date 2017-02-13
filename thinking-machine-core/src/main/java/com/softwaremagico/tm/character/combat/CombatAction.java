package com.softwaremagico.tm.character.combat;

import com.softwaremagico.tm.Element;

public class CombatAction extends Element<CombatAction> {
	private final Integer goal;
	private final String damage;
	private final String others;

	public CombatAction(String name, Integer goal, String damage, String others) {
		super(name);
		this.goal = goal;
		this.damage = damage;
		this.others = others;
	}

	public Integer getGoal() {
		return goal;
	}

	public String getDamage() {
		return damage;
	}

	public String getOthers() {
		return others;
	}

}
