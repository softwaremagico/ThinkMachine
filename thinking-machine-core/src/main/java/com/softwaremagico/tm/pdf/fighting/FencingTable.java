package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.VerticalHeaderPdfPTable;

public class FencingTable extends VerticalHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f, 4f, 3f, 3f, 3f };
	private final static int ROWS = 10;

	public FencingTable() {
		super(WIDTHS);
		addCell(createVerticalTitle("Esgrima", ROWS + 1));
		addCell(createTableSubtitleElement("Acción"));
		addCell(createTableSubtitleElement("Inicia."));
		addCell(createTableSubtitleElement("Dif."));
		addCell(createTableSubtitleElement("Daño"));

		for (int i = 0; i < ROWS; i++) {
			addCell(createElementLine("__________"));
			addCell(createElementLine("______"));
			addCell(createElementLine("______"));
			addCell(createElementLine("______"));
		}

	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
	}

}
