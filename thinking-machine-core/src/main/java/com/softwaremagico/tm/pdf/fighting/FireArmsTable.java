package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class FireArmsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.2f, 4f, 3f, 3f, 5f };
	private final static int ROWS = 10;

	public FireArmsTable() {
		super(WIDTHS);
		addCell(createLateralVerticalTitle("Armas de Fuego", ROWS + 1));
		addCell(createTableSubtitleElement("Acción"));
		addCell(createTableSubtitleElement("RA"));
		addCell(createTableSubtitleElement("Daño"));
		addCell(createTableSubtitleElement("Otros"));

		addCell(createElementLine("Apuntar"));
		addCell(createElementLine("+1/turno"));
		addCell(createElementLine(""));
		addCell(createElementLine("Máx 3 turnos"));

		addCell(createElementLine("Barrido"));
		addCell(createElementLine("-1/m"));
		addCell(createElementLine("+1"));
		addCell(createElementLine(""));

		addCell(createElementLine("Ráfaga (3)"));
		addCell(createElementLine("+2"));
		addCell(createElementLine("+3"));
		addCell(createElementLine(""));

		addCell(createElementLine("Ráfaga (6)"));
		addCell(createElementLine("-2"));
		addCell(createElementLine("+5"));
		addCell(createElementLine("Ignora 2 Defensa"));

		addCell(createElementLine("Cargador Vacío"));
		addCell(createElementLine("-4"));
		addCell(createElementLine("+7"));
		addCell(createElementLine("Ignora 3 Defensa"));

		addCell(createElementLine("Fuego Supresión"));
		addCell(createElementLine("-2"));
		addCell(createElementLine("  "));
		addCell(createElementLine("Especial"));

		for (int i = 0; i < ROWS - 6; i++) {
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
