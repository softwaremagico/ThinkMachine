package com.softwaremagico.tm.character.occultism;

import java.util.HashMap;
import java.util.Map;

import com.softwaremagico.tm.Element;

public class OccultismPath extends Element<OccultismPath> {

	private final OccultismType occultismType;
	private final Map<Integer, OccultismPower> occultismPowers;

	public OccultismPath(String id, String name, OccultismType occultismType) {
		super(id, name);
		this.occultismType = occultismType;
		occultismPowers = new HashMap<>();
	}

	public OccultismType getOccultismType() {
		return occultismType;
	}

	public Map<Integer, OccultismPower> getOccultismPowers() {
		return occultismPowers;
	}

}
