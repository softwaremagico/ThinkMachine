package com.softwaremagico.tm.character.cybernetics;

import com.softwaremagico.tm.Element;

public class Device extends Element<Device> {
	private final int points;
	private final int incompatibility;
	private final String usability;
	private final String quality;
	private final String activation;
	private final String appearence;
	private final String others;

	public Device(String name, int points, int incompatibility, String usability, String quality, String activation, String appearence, String others) {
		super(name);
		this.points = points;
		this.incompatibility = incompatibility;
		this.usability = usability;
		this.quality = quality;
		this.activation = activation;
		this.appearence = appearence;
		this.others = others;
	}

	public int getPoints() {
		return points;
	}

	public int getIncompatibility() {
		return incompatibility;
	}

	public String getUsability() {
		return usability;
	}

	public String getQuality() {
		return quality;
	}

	public String getActivation() {
		return activation;
	}

	public String getAppearence() {
		return appearence;
	}

	public String getOthers() {
		return others;
	}
}
