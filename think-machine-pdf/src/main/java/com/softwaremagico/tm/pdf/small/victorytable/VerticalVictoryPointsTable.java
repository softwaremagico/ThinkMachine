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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class VerticalVictoryPointsTable extends VerticalTable {
	private final static float[] WIDTHS = { 4f, 3f };

	public VerticalVictoryPointsTable() {
		super(WIDTHS);
		addCell(createTitle(getTranslator().getTranslatedText("victoryChartReduced"), 11));

		addCell(createSubTitle(getTranslator().getTranslatedText("dice"), BaseColor.WHITE));
		addCell(createSubTitle(getTranslator().getTranslatedText("victoryPoints"), BaseColor.WHITE));

		addCell(createLine("1", BaseColor.LIGHT_GRAY));
		addCell(createLine("0", BaseColor.LIGHT_GRAY));

		addCell(createLine("2-3", BaseColor.WHITE));
		addCell(createLine("1", BaseColor.WHITE));

		addCell(createLine("4-5", BaseColor.LIGHT_GRAY));
		addCell(createLine("2", BaseColor.LIGHT_GRAY));

		addCell(createLine("6-7", BaseColor.WHITE));
		addCell(createLine("3", BaseColor.WHITE));

		addCell(createLine("8-9", BaseColor.LIGHT_GRAY));
		addCell(createLine("4", BaseColor.LIGHT_GRAY));

		addCell(createLine("10-11", BaseColor.WHITE));
		addCell(createLine("5", BaseColor.WHITE));

		addCell(createLine("12-13", BaseColor.LIGHT_GRAY));
		addCell(createLine("6", BaseColor.LIGHT_GRAY));

		addCell(createLine("14-15", BaseColor.WHITE));
		addCell(createLine("7", BaseColor.WHITE));

		addCell(createLine("16-17", BaseColor.LIGHT_GRAY));
		addCell(createLine("8", BaseColor.LIGHT_GRAY));

		addCell(createLine("18-19", BaseColor.WHITE));
		addCell(createLine("9", BaseColor.WHITE));

		addCell(createLine("20", BaseColor.LIGHT_GRAY));
		addCell(createLine("*", BaseColor.LIGHT_GRAY));

	}

	private static PdfPCell createLine(String text, BaseColor color) {
		PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, color, FadingSunsTheme.getLineFont(),
				FadingSunsTheme.VICTORY_SMALL_POINTS_FONT_SIZE);
		cell.setMinimumHeight(11);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static PdfPCell createSubTitle(String text, BaseColor color) {
		PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, color, FadingSunsTheme.getLineFontBold(),
				FadingSunsTheme.VICTORY_SMALL_POINTS_FONT_SIZE + 1);
		cell.setMinimumHeight(18);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

}
