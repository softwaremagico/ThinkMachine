package com.softwaremagico.tm.character.equipment;

import com.softwaremagico.tm.Element;

public class Weapon extends Element<Weapon> {
	private final String roll;
	private final int goal;
	private final int damage;
	private final String strengthOrRange;
	private final Integer shots;
	private final String rate;
	private final Size size;
	private final int tech;
	private String others;

	public Weapon(String name, String roll, int goal, int damage, String strengthOrRange, Integer shots, String rate, int tech, Size size) {
		super(name);
		this.roll = roll;
		this.goal = goal;
		this.damage = damage;
		this.strengthOrRange = strengthOrRange;
		this.shots = shots;
		this.rate = rate;
		this.size = size;
		this.tech = tech;
	}

	public String getRoll() {
		return roll;
	}

	public int getGoal() {
		return goal;
	}

	public int getDamage() {
		return damage;
	}

	public String getStrengthOrRange() {
		return strengthOrRange;
	}

	public Integer getShots() {
		return shots;
	}

	public String getRate() {
		return rate;
	}

	public Size getSize() {
		return size;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public int getTech() {
		return tech;
	}
}
