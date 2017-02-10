package com.softwaremagico.tm.character.equipment;

import com.softwaremagico.tm.Element;

public class Armour extends Element<Element<Armour>> {
	private final int protection;
	private final boolean hard;
	private final boolean fire;
	private final boolean laser;
	private final boolean plasma;
	private final boolean impact;
	private final boolean shock;
	private final boolean electrical;
	private final int techLevel;

	private final int dexterityBonus;
	private final int strengthBonus;
	private final int initiativeBonus;
	private final int enduranceBonus;

	public Armour(String name, int protection, boolean hard, boolean fire, boolean laser, boolean plasma, boolean impact, boolean shock, boolean electrical,
			int techLevel, int dexterityBonus, int strengthBonus, int initiativeBonus, int enduranceBonus) {
		super(name);
		this.protection = protection;
		this.hard = hard;
		this.fire = fire;
		this.laser = laser;
		this.plasma = plasma;
		this.impact = impact;
		this.shock = shock;
		this.electrical = electrical;
		this.techLevel = techLevel;
		this.dexterityBonus = dexterityBonus;
		this.strengthBonus = strengthBonus;
		this.initiativeBonus = initiativeBonus;
		this.enduranceBonus = enduranceBonus;
	}

	public int getProtection() {
		return protection;
	}

	public boolean isHard() {
		return hard;
	}

	public boolean isFire() {
		return fire;
	}

	public boolean isPlasma() {
		return plasma;
	}

	public boolean isImpact() {
		return impact;
	}

	public boolean isShock() {
		return shock;
	}

	public int getTechLevel() {
		return techLevel;
	}

	public boolean isElectrical() {
		return electrical;
	}

	public int getDexterityBonus() {
		return dexterityBonus;
	}

	public int getStrengthBonus() {
		return strengthBonus;
	}

	public int getInitiativeBonus() {
		return initiativeBonus;
	}

	public int getEnduranceBonus() {
		return enduranceBonus;
	}

	public boolean isLaser() {
		return laser;
	}

}
