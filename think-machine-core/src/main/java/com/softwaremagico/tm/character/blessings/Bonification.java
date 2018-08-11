package com.softwaremagico.tm.character.blessings;

import com.softwaremagico.tm.character.values.IValue;

public class Bonification {
	private final Integer bonification;
	private final IValue affects;
	private final String situation;

	public Bonification(Integer bonification, IValue affects, String situation) {
		this.bonification = bonification;
		this.affects = affects;
		this.situation = situation;
	}

	public Integer getBonification() {
		return bonification;
	}

	public String getSituation() {
		return situation;
	}

	public IValue getAffects() {
		return affects;
	}
}
