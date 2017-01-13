package com.softwaremagico.tm.pdf.cybernetics;

import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class CyberneticsTable extends VerticalTable {
	private final static float[] WIDTHS = { 3f, 1f, 1f, 2f, 2f, 2f, 2f, 4f };
	private final static int ROWS = 8;
	private final static String GAP = "____";

	public CyberneticsTable() {
		super(WIDTHS);

		addCell(createTitle("Cibernética"));
		addCell(createLine("Nombre"));
		addCell(createLine("Pts."));
		addCell(createLine("Inc."));
		addCell(createLine("Usabilidad"));
		addCell(createLine("Calidad"));
		addCell(createLine("Activación"));
		addCell(createLine("Apariencia"));
		addCell(createLine("Otros"));

		for (int i = 0; i < ROWS; i++) {
			addCell(createLine(GAP + GAP + GAP));
			addCell(createLine(GAP));
			addCell(createLine(GAP));
			addCell(createLine(GAP + GAP));
			addCell(createLine(GAP + GAP));
			addCell(createLine(GAP + GAP));
			addCell(createLine(GAP + GAP));
			addCell(createLine(GAP + GAP + GAP + GAP));
		}
	}
}
