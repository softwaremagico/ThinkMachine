package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class FightingTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.6f, 4f, 5f, 2f, 3f, 3f, 3f, 3f, 3f, 7f };
	private final static int ROWS = 12;

	public FightingTable() {
		super(WIDTHS);
		addCell(createLateralVerticalTitle("Combate", ROWS + 1));
		addCell(createTableSubtitleElement("Acción"));
		addCell(createTableSubtitleElement("Tirada"));
		addCell(createTableSubtitleElement("RA"));
		addCell(createTableSubtitleElement("Daño"));
		addCell(createTableSubtitleElement("Fue/Alc"));
		addCell(createTableSubtitleElement("Carga."));
		addCell(createTableSubtitleElement("Cad."));
		addCell(createTableSubtitleElement("Tam"));
		addCell(createTableSubtitleElement("Otros"));

		addCell(createElementLine("Golpear"));
		addCell(createElementLine("Ds + Pel/CaC"));
		addCell(createElementLine(""));
		addCell(createElementLine("2d/Arm"));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));

		addCell(createElementLine("Presa"));
		addCell(createElementLine("Ds + Pelea"));
		addCell(createElementLine(""));
		addCell(createElementLine("2d"));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine("Fu+Vig/Fu+Vig"));

		addCell(createElementLine("Derribar"));
		addCell(createElementLine("Ds + Pelea"));
		addCell(createElementLine(""));
		addCell(createElementLine("3d"));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine("Fu+Pel/Ds+Vig"));

		addCell(createElementLine("Dearmar"));
		addCell(createElementLine("Ds + Pel/CaC"));
		addCell(createElementLine("-4"));
		addCell(createElementLine("2d/Arm"));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine("Daño/Fu+Vig"));
		
		addCell(createElementLine("Noquear"));
		addCell(createElementLine("Ds + Pel/CaC"));
		addCell(createElementLine("-4"));
		addCell(createElementLine("2d/Arm"));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine("Especial"));
		
		addCell(createElementLine("Cargar"));
		addCell(createElementLine("Ds + Pel/CaC"));
		addCell(createElementLine(""));
		addCell(createElementLine("1d/m"));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine(""));
		addCell(createElementLine("Máx 4d"));

		for (int i = 0; i < ROWS - 5; i++) {
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
