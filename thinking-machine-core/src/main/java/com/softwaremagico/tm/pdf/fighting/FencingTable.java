package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class FencingTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.2f, 4f, 3f, 3f, 5f };
	private final static int ROWS = 10;

	public FencingTable() {
		super(WIDTHS);
		addCell(createLateralVerticalTitle("Esgrima", ROWS + 1));
		addCell(createTableSubtitleElement("Acción"));
		addCell(createTableSubtitleElement("RA"));
		addCell(createTableSubtitleElement("Daño"));
		addCell(createTableSubtitleElement("Otros"));

		for (int i = 0; i < ROWS; i++) {
			addCell(createElementLine("_____________"));
			addCell(createElementLine("______"));
			addCell(createElementLine("______"));
			addCell(createElementLine("_________________"));
		}

	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
	}

}
