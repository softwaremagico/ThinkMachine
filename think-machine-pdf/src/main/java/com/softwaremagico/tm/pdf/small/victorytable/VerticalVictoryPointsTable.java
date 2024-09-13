package com.softwaremagico.tm.pdf.small.victorytable;

/*-
 * #%L
 * Think Machine (Core)
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

import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

import java.awt.Color;

public class VerticalVictoryPointsTable extends VerticalTable {
	private static final float[] WIDTHS = { 4f, 3f };

	public VerticalVictoryPointsTable() {
		super(WIDTHS);
		addCell(createTitle(getTranslator().getTranslatedText("victoryChartReduced"), 11));

		addCell(createSubTitle(getTranslator().getTranslatedText("dice"), Color.WHITE));
		addCell(createSubTitle(getTranslator().getTranslatedText("victoryPoints"), Color.WHITE));

		addCell(createLine("1", Color.LIGHT_GRAY));
		addCell(createLine("0", Color.LIGHT_GRAY));

		addCell(createLine("2-3", Color.WHITE));
		addCell(createLine("1", Color.WHITE));

		addCell(createLine("4-5", Color.LIGHT_GRAY));
		addCell(createLine("2", Color.LIGHT_GRAY));

		addCell(createLine("6-7", Color.WHITE));
		addCell(createLine("3", Color.WHITE));

		addCell(createLine("8-9", Color.LIGHT_GRAY));
		addCell(createLine("4", Color.LIGHT_GRAY));

		addCell(createLine("10-11", Color.WHITE));
		addCell(createLine("5", Color.WHITE));

		addCell(createLine("12-13", Color.LIGHT_GRAY));
		addCell(createLine("6", Color.LIGHT_GRAY));

		addCell(createLine("14-15", Color.WHITE));
		addCell(createLine("7", Color.WHITE));

		addCell(createLine("16-17", Color.LIGHT_GRAY));
		addCell(createLine("8", Color.LIGHT_GRAY));

		addCell(createLine("18-19", Color.WHITE));
		addCell(createLine("9", Color.WHITE));

		addCell(createLine("20", Color.LIGHT_GRAY));
		addCell(createLine("*", Color.LIGHT_GRAY));

	}

	private static PdfPCell createLine(String text, Color color) {
		final PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, color,
				FadingSunsTheme.getLineFont(), FadingSunsTheme.VICTORY_SMALL_POINTS_FONT_SIZE);
		cell.setMinimumHeight(12);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static PdfPCell createSubTitle(String text, Color color) {
		final PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, color,
				FadingSunsTheme.getLineFontBold(), FadingSunsTheme.VICTORY_SMALL_POINTS_FONT_SIZE + 1f);
		cell.setMinimumHeight(18);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

}
