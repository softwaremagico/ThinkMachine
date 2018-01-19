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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;

public abstract class VerticalTable extends CustomPdfTable {

	public VerticalTable(float[] widths) {
		super(widths);
	}

	protected static PdfPCell createSubtitleLine(String text, int fontSize) {
		return createSubtitleLine(text, fontSize, Element.ALIGN_CENTER);
	}

	protected static PdfPCell createSubtitleLine(String text, int fontSize, int alignment) {
		return createSubtitleLine(text, fontSize, 1, alignment);
	}

	protected static PdfPCell createSubtitleLine(String text, int fontSize, int colspan, int alignment) {
		PdfPCell cell = BaseElement.getCell(text, 0, colspan, alignment, BaseColor.WHITE, FadingSunsTheme.getSubtitleFont(), fontSize);
		cell.setMinimumHeight(10);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static PdfPCell createValueLine(String text, int fontSize) {
		PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getHandwrittingFont(), fontSize);
		cell.setMinimumHeight(10);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}
}
