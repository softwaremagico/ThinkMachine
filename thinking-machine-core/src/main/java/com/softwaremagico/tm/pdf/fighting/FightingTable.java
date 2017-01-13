package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.VerticalHeaderPdfPTable;

public class FightingTable extends VerticalHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f, 4f, 5f, 3f, 3f, 3f, 3f, 3f, 3f, 3f };
	private final static int ROWS = 8;

	public FightingTable() {
		super(WIDTHS);
		addCell(createVerticalTitle("Combate", ROWS + 1));
		addCell(createTableSubtitleElement("Acción"));
		addCell(createTableSubtitleElement("Tirada"));
		addCell(createTableSubtitleElement("Inicia."));
		addCell(createTableSubtitleElement("RA"));
		addCell(createTableSubtitleElement("Daño"));
		addCell(createTableSubtitleElement("Fue/Alc"));
		addCell(createTableSubtitleElement("Carga."));
		addCell(createTableSubtitleElement("Cad."));
		addCell(createTableSubtitleElement("Tam"));

		addCell(createElementLine("Puñetazo"));
		addCell(createElementLine("Des + Pelea"));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine("2d"));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));

		addCell(createElementLine("Patada"));
		addCell(createElementLine("Des + Pelea"));
		addCell(createElementLine("-1"));
		addCell(createElementLine(""));
		addCell(createElementLine("3d"));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));

		addCell(createElementLine("Presa"));
		addCell(createElementLine("Des + Pelea"));
		addCell(createElementLine("-2"));
		addCell(createElementLine(""));
		addCell(createElementLine("2d"));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));

		addCell(createElementLine("Carga"));
		addCell(createElementLine("Des + Pelea"));
		addCell(createElementLine("-3"));
		addCell(createElementLine(""));
		addCell(createElementLine("3d"));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));

		for (int i = 0; i < ROWS - 4; i++) {
			addCell(createElementLine("___________"));
			addCell(createElementLine("_____________"));
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
