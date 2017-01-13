package com.softwaremagico.tm.pdf.info;

import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class PropertiesTable extends VerticalTable {
	private final static float[] WIDTHS = { 1f };
	private final static int ROWS = 9;

	public PropertiesTable() {
		super(WIDTHS);

		addCell(createTitle("Propiedades"));
		for (int i = 0; i < ROWS; i++) {
			addCell(createElementLine("______________________________________________"));
		}
	}
}
