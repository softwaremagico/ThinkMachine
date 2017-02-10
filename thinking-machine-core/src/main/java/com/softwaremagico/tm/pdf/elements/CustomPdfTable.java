package com.softwaremagico.tm.pdf.elements;

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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.utils.CellUtils;

public abstract class CustomPdfTable extends PdfPTable {
	private float[] columnWidths;
	private static ITranslator translator = LanguagePool.getTranslator("character_sheet.xml");

	public CustomPdfTable(float[] widths) {
		super(widths);
		setColumnWidths(widths);
	}

	protected PdfPCell createTitle(String title) {
		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.VERTICALTABLE_TITLE_FONT_SIZE);
		Phrase content = new Phrase(title, font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setRowspan(2);
		titleCell.setColspan(getColumnWidths().length);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setBorder(0);
		return titleCell;
	}

	protected static PdfPCell createEmptyElementLine(String text) {
		PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getLineFont(),
				FadingSunsTheme.TABLE_LINE_FONT_SIZE);
		cell.setMinimumHeight(10);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static PdfPCell createEmptyElementLine(String text, int maxWidth) {
		String remainingText = CellUtils.getSubStringFitsIn(text, FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE, maxWidth);
		return createEmptyElementLine(remainingText);
	}

	protected static PdfPCell createElementLine(String text) {
		PdfPCell cell = BaseElement.getCell(
				CellUtils.getSubStringFitsIn(text, FadingSunsTheme.getHandwrittingFont(),
						FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.TABLE_LINE_FONT_SIZE), 70), 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE,
				FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.TABLE_LINE_FONT_SIZE));
		cell.setMinimumHeight(12);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static PdfPCell createElementLine(String text, int maxWidth) {
		return createElementLine(text, maxWidth, FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.TABLE_LINE_FONT_SIZE));
	}

	protected static PdfPCell createElementLine(String text, int maxWidth, int fontSize) {
		if (text == null || text.equals("null")) {
			text = "";
		}
		String remainingText = CellUtils.getSubStringFitsIn(text, FadingSunsTheme.getHandwrittingFont(), fontSize, maxWidth);
		return createElementLine(remainingText);
	}

	public float[] getColumnWidths() {
		return columnWidths;
	}

	private void setColumnWidths(float[] columnWidths) {
		this.columnWidths = columnWidths;
	}

	protected PdfPCell createRectangle() {
		PdfPCell box = new PdfPCell();
		box.setMinimumHeight(15);
		box.setBorder(0);
		box.setCellEvent(new CellPaddingEvent());
		return box;
	}

	protected PdfPCell createRectangle(Integer value) {
		return createRectangle(value + "");
	}

	protected PdfPCell createRectangle(String value) {
		if (value == null) {
			return createRectangle();
		}
		PdfPCell box = new PdfPCell(new Paragraph(value, new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.HANDWRITTING_DEFAULT_FONT_SIZE)));
		box.setVerticalAlignment(Element.ALIGN_MIDDLE);
		box.setHorizontalAlignment(Element.ALIGN_CENTER);
		box.setMinimumHeight(15);
		box.setBorder(0);
		box.setCellEvent(new CellPaddingEvent());
		return box;
	}

	public static ITranslator getTranslator() {
		return translator;
	}

}
