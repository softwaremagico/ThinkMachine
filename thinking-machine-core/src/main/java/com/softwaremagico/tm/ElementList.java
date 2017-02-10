package com.softwaremagico.tm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElementList<T extends Comparable<T>> {

	private List<T> elements;

	public ElementList() {
		elements = new ArrayList<T>();
	}

	public void addElement(T element) {
		elements.add(element);
		Collections.sort(elements);
	}

	public List<T> getElements() {
		return Collections.unmodifiableList(elements);
	}
}
