package com.softwaremagico.tm.character.cybernetics;

public class Device implements Comparable<Device> {
	private String name;
	private int points;
	private int incompatibility;
	private String usability;
	private String quality;
	private String activation;
	private String appearence;
	private String others;

	public Device(String name, int points, int incompatibility, String usability, String quality, String activation, String appearence, String others) {
		super();
		this.name = name;
		this.points = points;
		this.incompatibility = incompatibility;
		this.usability = usability;
		this.quality = quality;
		this.activation = activation;
		this.appearence = appearence;
		this.others = others;
	}

	public String getName() {
		return name;
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

	@Override
	public int compareTo(Device device) {
		return getName().compareTo(device.getName());
	}

	@Override
	public String toString() {
		return getName();
	}
}
