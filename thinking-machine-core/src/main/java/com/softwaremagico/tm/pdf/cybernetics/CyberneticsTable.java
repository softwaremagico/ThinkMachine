package com.softwaremagico.tm.pdf.cybernetics;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class CyberneticsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 0.8f, 3f, 1f, 1f, 2f, 2f, 2f, 2f, 4f };
	private final static int ROWS = 8;
	private final static String GAP = "____";

	public CyberneticsTable() {
		super(WIDTHS);

		addCell(createLateralVerticalTitle("Cibernética", ROWS + 1));
		addCell(createTableSubtitleElement("Nombre"));
		addCell(createTableSubtitleElement("Pts."));
		addCell(createTableSubtitleElement("Inc."));
		addCell(createTableSubtitleElement("Usabilidad"));
		addCell(createTableSubtitleElement("Calidad"));
		addCell(createTableSubtitleElement("Activación"));
		addCell(createTableSubtitleElement("Apariencia"));
		addCell(createTableSubtitleElement("Otros"));

		for (int i = 0; i < ROWS - 1; i++) {
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

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.CYBERNETICS_TITLE_FONT_SIZE;
	}
}
