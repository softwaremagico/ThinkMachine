package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.VerticalHeaderPdfPTable;

public class FireArmsTable extends VerticalHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f, 4f, 3f, 3f, 3f };
	private final static int ROWS = 10;

	public FireArmsTable() {
		super(WIDTHS);
		addCell(createVerticalTitle("Armas de Fuego", ROWS + 1));
		addCell(createTableSubtitleElement("Acción"));
		addCell(createTableSubtitleElement("Inicia."));
		addCell(createTableSubtitleElement("Dif."));
		addCell(createTableSubtitleElement("Daño"));

		addCell(createElementLine("Apuntar"));
		addCell(createElementLine("-3"));
		addCell(createElementLine("+1/turno"));
		addCell(createElementLine("  "));

		addCell(createElementLine("Ráfaga (3)"));
		addCell(createElementLine("-1"));
		addCell(createElementLine("-1"));
		addCell(createElementLine("+1"));

		addCell(createElementLine("Ráfaga (6)"));
		addCell(createElementLine("-2"));
		addCell(createElementLine("-2"));
		addCell(createElementLine("+3"));

		addCell(createElementLine("Ráf. Total"));
		addCell(createElementLine("-2"));
		addCell(createElementLine("-4"));
		addCell(createElementLine("+5"));

		addCell(createElementLine("Barrido"));
		addCell(createElementLine("-2"));
		addCell(createElementLine("-1/m"));
		addCell(createElementLine("+1"));

		addCell(createElementLine("Tiro a Ciegas"));
		addCell(createElementLine("+2"));
		addCell(createElementLine("-2"));
		addCell(createElementLine("  "));

		for (int i = 0; i < ROWS - 6; i++) {
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