package com.softwaremagico.tm.character.cybernetics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cybernetics {
	private List<Device> devices;

	public Cybernetics() {
		devices = new ArrayList<>();
	}

	public void addDevice(Device device) {
		devices.add(device);
		Collections.sort(devices);
	}

	public List<Device> getDevices() {
		return Collections.unmodifiableList(devices);
	}
}
