package com.softwaremagico.tm.pdf.perks;

import com.softwaremagico.tm.pdf.elements.VerticalTable;


public class BenefitsTable extends VerticalTable {
	private final static String GAP = "_____";
	private final static float[] WIDTHS = { 2f, 5f, 2f, 5f };

	public BenefitsTable() {
		super(WIDTHS);
		addCell(createTitle("Beneficios/Afliciones"));

		addCell(createSubtitleLine("Pts."));
		addCell(createSubtitleLine("Beneficios"));
		addCell(createSubtitleLine("Pts."));
		addCell(createSubtitleLine("Afliciones"));

		for (int i = 0; i < MainPerksTableFactory.EMPTY_ROWS * 2; i++) {
			addCell(createElementLine(GAP));
			addCell(createElementLine(GAP + GAP + GAP + GAP));
		}
	}

	
}
