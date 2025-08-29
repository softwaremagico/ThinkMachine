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

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.info.CharacterBasicsCompleteTableFactory;

import java.awt.Color;
import java.io.IOException;

public class BaseElement {
	private static final String TRANSLATOR_FILE = "character_sheet.xml";
	private static ITranslator translator;

	public static PdfPCell getCell(String text, int border, int colspan, int align, Color color, BaseFont font,
								   float fontSize) {
		if (text == null) {
			text = "";
		}
		final Phrase content = new Phrase(text, new Font(font, fontSize));
		final PdfPCell cell = new PdfPCell(content);
		cell.setColspan(colspan);
		cell.setBorderWidth(border);
		cell.setHorizontalAlignment(align);
		cell.setBackgroundColor(color);

		return cell;
	}

	public static PdfPCell getCell(Paragraph paragraph, int border, int colspan, int align, Color color) {
		final PdfPCell cell = new PdfPCell(paragraph);
		cell.setColspan(colspan);
		cell.setBorderWidth(border);
		cell.setHorizontalAlignment(align);
		cell.setBackgroundColor(color);

		return cell;
	}

	protected PdfPCell getCell(String text, int border, int colspan, int align, Color color, String font,
			int fontSize, int fontType) {
		if (text == null) {
			text = "";
		}
		final Paragraph p = new Paragraph(text, FontFactory.getFont(font, fontSize, fontType));
		final PdfPCell cell = new PdfPCell(p);
		cell.setColspan(colspan);
		cell.setBorderWidth(border);
		cell.setHorizontalAlignment(align);
		cell.setBackgroundColor(color);

		return cell;
	}

	public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
		final Image img = Image.getInstance(path);
		final PdfPCell cell = new PdfPCell(img, true);
		setCellProperties(cell);
		return cell;
	}

	public static PdfPCell createLogoCell() throws DocumentException, IOException {
		final Image image = Image.getInstance(CharacterBasicsCompleteTableFactory.class.getResource("/"
				+ FadingSunsTheme.LOGO_IMAGE));
		final PdfPCell cell = new PdfPCell(image, true);
		setCellProperties(cell);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingTop(10);
		return cell;
	}

	public static void setCellProperties(PdfPCell cell) {
		cell.setBorder(0);
		cell.setPadding(0);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	}

	public static PdfPCell createSeparator() {
		final float[] widths = { 1f };
		final PdfPTable table = new PdfPTable(widths);
		table.setWidthPercentage(100);
		table.addCell(createWhiteSeparator());
		table.addCell(createBlackSeparator());
		// table.addCell(createWhiteSeparator());

		final PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		setCellProperties(cell);

		return cell;
	}

	public static PdfPCell createBigSeparator() {
		return createBigSeparator(98);
	}

	public static PdfPCell createBigSeparator(int width) {
		final float[] widths = { 1f };
		final PdfPTable table = new PdfPTable(widths);
		table.setWidthPercentage(width);
		table.addCell(createWhiteSeparator());
		table.addCell(createBlackSeparator());
		table.addCell(createWhiteSeparator());

		final PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		setCellProperties(cell);

		return cell;
	}

	public static PdfPCell createBlackSeparator() {
		final PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLACK);
		setCellProperties(cell);
		cell.setMinimumHeight(10f);
		return cell;
	}

	public static PdfPCell createWhiteSeparator() {
		final PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.WHITE);
		setCellProperties(cell);
		cell.setMinimumHeight(6f);
		return cell;
	}

	public static void setTablePropierties(PdfPTable table) {
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.setWidthPercentage(100);
		table.setSpacingAfter(0);
		table.setSpacingBefore(0);
	}

	public static ITranslator getTranslator() {
		if (translator == null) {
			translator = LanguagePool.getTranslator(TRANSLATOR_FILE, (String) null);
		}
		return translator;
	}
}
