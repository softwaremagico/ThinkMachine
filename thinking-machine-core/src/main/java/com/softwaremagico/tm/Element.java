package com.softwaremagico.tm;

public class Element<T extends Element<?>> implements Comparable<T> {
	private String name;

	public Element(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public int compareTo(T device) {
		return getName().compareTo(device.getName());
	}

	@Override
	public String toString() {
		return getName();
	}
}
