package com.softwaremagico.tm.character.cybernetics;

import com.softwaremagico.tm.Element;

public class Device extends Element<Device> {
	private int points;
	private int incompatibility;
	private String usability;
	private String quality;
	private String activation;
	private String appearence;
	private String others;

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
