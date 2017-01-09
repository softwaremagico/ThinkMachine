package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.VerticalHeaderPdfPTable;

public class FightingTable extends VerticalHeaderPdfPTable {
	private final static float[] widths = { 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f };
	private final static int ROWS = 5;

	public FightingTable() {
		super(widths);
		addCell(createVerticalTitle("COMBATE", ROWS + 1));
		addCell(createTableSubtitleElement("Acción"));
		addCell(createTableSubtitleElement("Tirada"));
		addCell(createTableSubtitleElement("Inicia."));
		addCell(createTableSubtitleElement("RA"));
		addCell(createTableSubtitleElement("Daño"));
		addCell(createTableSubtitleElement("Fue/Alc"));
		addCell(createTableSubtitleElement("Carga."));
		addCell(createTableSubtitleElement("Cad."));
		addCell(createTableSubtitleElement("Tam"));

		for (int i = 0; i < ROWS; i++) {
			addCell(createElementLine("________"));
			addCell(createElementLine("________"));
			addCell(createElementLine("______"));
			addCell(createElementLine("______"));
			addCell(createElementLine("______"));
			addCell(createElementLine("________"));
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
