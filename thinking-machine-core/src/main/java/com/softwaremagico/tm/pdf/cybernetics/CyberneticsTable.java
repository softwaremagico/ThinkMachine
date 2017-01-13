package com.softwaremagico.tm.pdf.cybernetics;

import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class CyberneticsTable extends VerticalTable {
	private final static float[] WIDTHS = { 3f, 1f, 1f, 2f, 2f, 2f, 2f, 4f };
	private final static int ROWS = 8;
	private final static String GAP = "____";

	public CyberneticsTable() {
		super(WIDTHS);

		addCell(createTitle("Cibernética"));
		addCell(createElementLine("Nombre"));
		addCell(createElementLine("Pts."));
		addCell(createElementLine("Inc."));
		addCell(createElementLine("Usabilidad"));
		addCell(createElementLine("Calidad"));
		addCell(createElementLine("Activación"));
		addCell(createElementLine("Apariencia"));
		addCell(createElementLine("Otros"));

		for (int i = 0; i < ROWS; i++) {
			addCell(createElementLine(GAP + GAP + GAP));
			addCell(createElementLine(GAP));
			addCell(createElementLine(GAP));
			addCell(createElementLine(GAP + GAP));
			addCell(createElementLine(GAP + GAP));
			addCell(createElementLine(GAP + GAP));
			addCell(createElementLine(GAP + GAP));
			addCell(createElementLine(GAP + GAP + GAP + GAP));
		}
	}
}
