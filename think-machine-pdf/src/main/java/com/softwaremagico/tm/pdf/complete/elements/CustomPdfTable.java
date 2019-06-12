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
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.CellCompleteBoxEvent.Border;
import com.softwaremagico.tm.pdf.complete.utils.CellUtils;

public abstract class CustomPdfTable extends PdfPTable {
	private float[] columnWidths;
	private static ITranslator translator = LanguagePool.getTranslator("character_sheet.xml");

	public CustomPdfTable(float[] widths) {
		super(widths);
		setColumnWidths(widths);
	}

	protected PdfPCell createTitle(String title, int fontSize) {
		final PdfPCell titleCell = createCompactTitle(title, fontSize);
		titleCell.setRowspan(2);
		return titleCell;
	}

	protected PdfPCell createCompactTitle(String title, int fontSize) {
		final Font font = new Font(FadingSunsTheme.getTitleFont(), fontSize);
		final Phrase content = new Phrase(title, font);
		final PdfPCell titleCell = new PdfPCell(content);
		titleCell.setColspan(getColumnWidths().length);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setBorder(0);
		return titleCell;
	}

	protected static PdfPCell createFirstEmptyElementLine(String text) {
		return createEmptyElementLine(Element.ALIGN_LEFT, text);
	}

	protected static PdfPCell createEmptyElementLine(String text) {
		return createEmptyElementLine(Element.ALIGN_CENTER, text);
	}

	protected static PdfPCell createEmptyElementLine(int alignment, String text) {
		final PdfPCell cell = BaseElement.getCell(text, 0, 1, alignment, BaseColor.WHITE,
				FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE);
		cell.setMinimumHeight(10);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	public static PdfPCell createEmptyElementLine(String text, int maxWidth) {
		final String remainingText = CellUtils.getSubStringFitsIn(text, FadingSunsTheme.getLineFont(),
				FadingSunsTheme.TABLE_LINE_FONT_SIZE, maxWidth);
		return createEmptyElementLine(remainingText);
	}

	private static PdfPCell createBasicElementLine(String text, int fontSize) {
		return createBasicElementLine(text, fontSize, Element.ALIGN_CENTER);
	}

	private static PdfPCell createBasicElementLine(String text, int fontSize, int horizontalAlignment) {
		final PdfPCell cell = BaseElement.getCell(text, 0, 1, horizontalAlignment, BaseColor.WHITE,
				FadingSunsTheme.getHandwrittingFont(), fontSize);
		cell.setMinimumHeight(12);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static PdfPCell createElementLine(String text, int maxWidth) {
		return createElementLine(text, maxWidth,
				FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.TABLE_LINE_FONT_SIZE));
	}

	protected static PdfPCell createElementLine(Integer value, int maxWidth) {
		return createElementLine(value, maxWidth,
				FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.TABLE_LINE_FONT_SIZE));
	}

	protected static PdfPCell createElementLine(Integer value, int maxWidth, int fontSize) {
		if (value == null) {
			return createElementLine("", maxWidth, fontSize);
		}
		return createElementLine((value > 0 ? "+" + value : value + ""), maxWidth, fontSize);
	}

	protected static PdfPCell createEmptyElementLine(int fontSize) {
		return createBasicElementLine("", fontSize, Element.ALIGN_CENTER);
	}

	protected static PdfPCell createFirstElementLine(String text, int maxWidth, int fontSize) {
		final PdfPCell cell = createElementLine(text, maxWidth, fontSize);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		return cell;
	}

	protected static PdfPCell createElementLine(String text, int maxWidth, int fontSize) {
		if (text == null || text.equals("null")) {
			text = "";
		}
		final String remainingText = CellUtils.getSubStringFitsIn(text, FadingSunsTheme.getHandwrittingFont(),
				fontSize, maxWidth);
		return createBasicElementLine(remainingText, fontSize);
	}

	protected static PdfPCell createElementLine(String text, int maxWidth, int fontSize, int alignment) {
		if (text == null || text.equals("null")) {
			text = "";
		}
		final String remainingText = CellUtils.getSubStringFitsIn(text, FadingSunsTheme.getHandwrittingFont(),
				fontSize, maxWidth);
		return createBasicElementLine(remainingText, fontSize, alignment);
	}

	public float[] getColumnWidths() {
		return columnWidths;
	}

	private void setColumnWidths(float[] columnWidths) {
		this.columnWidths = columnWidths;
	}

	protected PdfPCell createRectangle() {
		final PdfPCell box = new PdfPCell();
		box.setMinimumHeight(15);
		box.setBorder(0);
		box.setCellEvent(new CellCompleteBoxEvent(new Border[] { Border.TOP, Border.BOTTOM, Border.LEFT, Border.RIGHT }));
		return box;
	}

	protected PdfPCell createRectangle(Integer value) {
		return createRectangle(value + "");
	}

	protected PdfPCell createRectangle(String value) {
		if (value == null) {
			return createRectangle();
		}
		final PdfPCell box = new PdfPCell(new Paragraph(value, new Font(FadingSunsTheme.getHandwrittingFont(),
				FadingSunsTheme.HANDWRITTING_DEFAULT_FONT_SIZE)));
		box.setVerticalAlignment(Element.ALIGN_MIDDLE);
		box.setHorizontalAlignment(Element.ALIGN_CENTER);
		box.setMinimumHeight(15);
		box.setBorder(0);
		box.setCellEvent(new CellCompleteBoxEvent(new Border[] { Border.TOP, Border.BOTTOM, Border.LEFT, Border.RIGHT }));
		return box;
	}

	protected PdfPCell getCell(Paragraph paragraph, int border, int colspan, int align) {
		final PdfPCell cell = new PdfPCell(paragraph);
		cell.setColspan(colspan);
		cell.setBorderWidth(border);
		cell.setHorizontalAlignment(align);

		return cell;
	}

	public static ITranslator getTranslator() {
		return translator;
	}

}
