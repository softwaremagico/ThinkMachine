package com.softwaremagico.tm.pdf.skills.occultism;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class PowerTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f, 5f, 2f, 5f, 4f, 4f, 4f, 3f };
	private final static int ROWS = 15;
	private final static String GAP = "__";

	public PowerTable() {
		super(WIDTHS);
		addCell(createLateralVerticalTitle("Poderes Ocultos", ROWS + 1));

		addCell(createTableSubtitleElement("Poder/Rito"));
		addCell(createTableSubtitleElement("Nivel"));
		addCell(createTableSubtitleElement("Tirada"));
		addCell(createTableSubtitleElement("Alcance"));
		addCell(createTableSubtitleElement("Duración"));
		addCell(createTableSubtitleElement("Requisitos"));
		addCell(createTableSubtitleElement("Coste"));

		for (int i = 0; i < ROWS; i++) {
			addCell(createElementLine(GAP + GAP + GAP + GAP + GAP));
			addCell(createElementLine(GAP + GAP));
			addCell(createElementLine(GAP + GAP + GAP + GAP + GAP));
			addCell(createElementLine(GAP + GAP + GAP + GAP));
			addCell(createElementLine(GAP + GAP + GAP + GAP + GAP));
			addCell(createElementLine(GAP + GAP + GAP + GAP));
			addCell(createElementLine(GAP + GAP + GAP));
		}

	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.OCCULSTISM_POWERS_TITLE_FONT_SIZE;
	}

}
