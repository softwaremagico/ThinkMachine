package com.softwaremagico.tm.character.occultism;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Occultism {
	private int psiValue = 0;
	private int teurgyValue = 0;
	private int extraWyrd = 0;
	private int urge = 0;
	private int hubris = 0;
	private List<OccultismPower> occultismPowers;

	public Occultism() {
		occultismPowers = new ArrayList<>();
	}

	public int getExtraWyrd() {
		return extraWyrd;
	}

	public void setExtraWyrd(int extraWyrd) {
		this.extraWyrd = extraWyrd;
	}

	public int getPsiValue() {
		return psiValue;
	}

	public void setPsiValue(int psyValue) {
		this.psiValue = psyValue;
	}

	public int getTeurgyValue() {
		return teurgyValue;
	}

	public void setTeurgyValue(int teurgyValue) {
		this.teurgyValue = teurgyValue;
	}

	public int getUrge() {
		return urge;
	}

	public void setUrge(int urge) {
		this.urge = urge;
	}

	public int getHubris() {
		return hubris;
	}

	public void setHubris(int hubris) {
		this.hubris = hubris;
	}

	public void addOccultismPower(OccultismPower occultismPower) {
		occultismPowers.add(occultismPower);
		Collections.sort(occultismPowers);
	}

	public List<OccultismPower> getOccultismPowers() {
		return Collections.unmodifiableList(occultismPowers);
	}

}
