package com.softwaremagico.tm.pdf.fighting;

/*-
 * #%L
 * The Thinking Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *  
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *  
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
