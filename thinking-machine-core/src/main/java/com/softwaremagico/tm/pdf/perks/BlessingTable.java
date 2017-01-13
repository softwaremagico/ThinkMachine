package com.softwaremagico.tm.pdf.perks;

import com.softwaremagico.tm.pdf.elements.VerticalTable;


public class BlessingTable extends VerticalTable {
	private final static String GAP = "___";
	private final static float[] WIDTHS = { 8f, 2f, 5f, 10f };

	public BlessingTable() {
		super(WIDTHS);
		addCell(createTitle("BENDICIONES/MALDICIONES"));

		addCell(createSubtitleLine("Nombre"));
		addCell(createSubtitleLine("+/-"));
		addCell(createSubtitleLine("Rasgo"));
		addCell(createSubtitleLine("Situación"));

		for (int i = 0; i < MainPerksTableFactory.EMPTY_ROWS; i++) {
			addCell(createLine(GAP + GAP + GAP + GAP + GAP));
			addCell(createLine(GAP));
			addCell(createLine(GAP + GAP + GAP));
			addCell(createLine(GAP + GAP + GAP + GAP + GAP + GAP));
		}
	}
}
