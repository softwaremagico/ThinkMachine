package com.softwaremagico.tm.character.equipment;

import com.softwaremagico.tm.Element;

public class Shield extends Element<Shield> {
	private final int impact;
	private final int force;
	private final int hits;

	public Shield(String name, int impact, int force, int hits) {
		super(name);
		this.impact = impact;
		this.force = force;
		this.hits = hits;
	}

	public int getImpact() {
		return impact;
	}

	public int getForce() {
		return force;
	}

	public int getHits() {
		return hits;
	}

}
