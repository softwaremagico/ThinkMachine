package com.softwaremagico.tm.pdf.perks;


public class BenefitsTable extends PerksTable {
	private final static String GAP = "_____";
	private final static float[] WIDTHS = { 2f, 5f, 2f, 5f };

	public BenefitsTable() {
		super(WIDTHS);
		addCell(createTitle("BENEFICIOS/AFLICIONES"));

		addCell(createSubtitleLine("Pts."));
		addCell(createSubtitleLine("Beneficios"));
		addCell(createSubtitleLine("Pts."));
		addCell(createSubtitleLine("Afliciones"));

		for (int i = 0; i < MainPerksTableFactory.EMPTY_ROWS * 2; i++) {
			addCell(createLine(GAP));
			addCell(createLine(GAP + GAP + GAP + GAP));
		}
	}

	
}
