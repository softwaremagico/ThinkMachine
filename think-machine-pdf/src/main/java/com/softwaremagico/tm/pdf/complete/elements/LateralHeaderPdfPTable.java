package com.softwaremagico.tm.pdf.complete.elements;

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
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;

import java.awt.Color;

public abstract class LateralHeaderPdfPTable extends CustomPdfTable {

	protected LateralHeaderPdfPTable(float[] widths) {
		this(widths, true);
	}

	protected LateralHeaderPdfPTable(float[] widths, boolean event) {
		super(widths);
		if (event) {
			setTableEvent(new TableBorderEvent());
		}
	}

	protected abstract int getTitleFontSize();

	protected PdfPCell createLateralVerticalTitle(String title, int rowspan) {
		final Font font = new Font(FadingSunsTheme.getTitleFont(), getTitleFontSize());
		font.setColor(Color.WHITE);
		final Phrase content = new Phrase(title, font);
		final PdfPCell titleCell = new PdfPCell(content);
		titleCell.setPadding(0);
		titleCell.setRowspan(rowspan);
		titleCell.setRotation(90);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		// titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		titleCell.setBackgroundColor(Color.BLACK);
		return titleCell;
	}

	protected static PdfPCell createTableSubtitleElement(String text) {
		return createTableSubtitleElement(text, 10);
	}

	protected static PdfPCell createTableSubtitleElement(String text, int height) {
		final PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER,
				Color.WHITE, FadingSunsTheme.getSubtitleFont(),
				FadingSunsTheme.TABLE_LINE_FONT_SIZE);
		cell.setMinimumHeight(height);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

}
