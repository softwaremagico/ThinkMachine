package com.softwaremagico.tm.pdf.complete.others;

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
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class AnnotationsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f, 24f };
	private final static int CELL_HEIGHT = 70;

	public AnnotationsTable() {
		super(WIDTHS, false);

		PdfPCell cell = createLateralVerticalTitle(getTranslator()
				.getTranslatedText("annotationsTable"), 2);
		cell.setBorderWidth(2);
		addCell(cell);

		cell = createSubtitleLine(getTranslator().getTranslatedText(
				"characterAnnotations"));
		cell.setBorderWidth(0);
		addCell(cell);
		cell = createSubtitleLine(getTranslator().getTranslatedText(
				"historyAnnotations"));
		cell.setBorderWidth(2);
		addCell(cell);
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.ANNOTATIONS_TITLE_FONT_SIZE;
	}

	protected static PdfPCell createSubtitleLine(String text) {
		PdfPCell cell = BaseElement.getCell(text, 1, 1, Element.ALIGN_LEFT,
				BaseColor.WHITE, FadingSunsTheme.getTitleFont(),
				FadingSunsTheme.ANNOTATIONS_SUBTITLE_FONT_SIZE);
		cell.setMinimumHeight(CELL_HEIGHT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

}
