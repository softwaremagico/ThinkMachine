package com.softwaremagico.tm.character.equipment.weapons;

public class WeaponRandomDefinition {
	private double probabilityMultiplier;

	public WeaponRandomDefinition() {
		probabilityMultiplier = 1d;
	}

	public double getProbabilityMultiplier() {
		return probabilityMultiplier;
	}

	public void setProbabilityMultiplier(double probabilityMultiplier) {
		this.probabilityMultiplier = probabilityMultiplier;
	}
}
