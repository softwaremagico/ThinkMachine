package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class WeaponsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.6f, 4f, 5f, 2f, 3f, 3f, 3f, 3f, 3f, 7f };
	private final static int ROWS = 12;

	public WeaponsTable() {
		super(WIDTHS);
		addCell(createLateralVerticalTitle("Armas", ROWS + 1));
		addCell(createTableSubtitleElement("Arma"));
		addCell(createTableSubtitleElement("Tirada"));
		addCell(createTableSubtitleElement("RA"));
		addCell(createTableSubtitleElement("Daño"));
		addCell(createTableSubtitleElement("Fue/Alc"));
		addCell(createTableSubtitleElement("Disparos"));
		addCell(createTableSubtitleElement("Ratio"));
		addCell(createTableSubtitleElement("Tam"));
		addCell(createTableSubtitleElement("Otros"));

		for (int i = 0; i < ROWS; i++) {
			addCell(createElementLine("___________"));
			addCell(createElementLine("_____________"));
			addCell(createElementLine("_____"));
			addCell(createElementLine("______"));
			addCell(createElementLine("________"));
			addCell(createElementLine("______"));
			addCell(createElementLine("______"));
			addCell(createElementLine("______"));
			addCell(createElementLine("__________________"));
		}

		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));

	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
	}

}
