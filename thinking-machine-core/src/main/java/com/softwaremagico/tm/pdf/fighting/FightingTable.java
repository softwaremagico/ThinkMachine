package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class FightingTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.2f, 4f, 3f, 3f, 5f };
	private final static int ROWS = 12;

	public FightingTable() {
		super(WIDTHS);
		addCell(createLateralVerticalTitle("Cuerpo a Cuerpo", ROWS + 1));
		addCell(createTableSubtitleElement("Acción"));
		addCell(createTableSubtitleElement("RA"));
		addCell(createTableSubtitleElement("Daño"));
		addCell(createTableSubtitleElement("Otros"));
		
		addCell(createElementLine("Golpear"));
		addCell(createElementLine(""));
		addCell(createElementLine("2d/Arm"));
		addCell(createElementLine(""));

		addCell(createElementLine("Presa"));
		addCell(createElementLine(""));
		addCell(createElementLine("2d"));
		addCell(createElementLine("Fu+Vig/Fu+Vig"));

		addCell(createElementLine("Derribar"));
		addCell(createElementLine(""));
		addCell(createElementLine("3d"));
		addCell(createElementLine("Fu+Pel/Ds+Vig"));

		addCell(createElementLine("Dearmar"));
		addCell(createElementLine("-4"));
		addCell(createElementLine("2d/Arm"));
		addCell(createElementLine("Daño/Fu+Vig"));
		
		addCell(createElementLine("Noquear"));
		addCell(createElementLine("-4"));
		addCell(createElementLine("2d/Arm"));
		addCell(createElementLine("Especial"));
		
		addCell(createElementLine("Cargar"));
		addCell(createElementLine(""));
		addCell(createElementLine("1d/m"));
		addCell(createElementLine("Máx 4d"));

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
