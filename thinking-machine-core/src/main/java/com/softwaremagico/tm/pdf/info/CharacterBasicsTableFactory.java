package com.softwaremagico.tm.pdf.info;

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

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class CharacterBasicsTableFactory extends BaseElement {
	private final static String LINE = "_______________";
	private final static String LANGUAGE_PREFIX = "info";
	private final static int MAX_VALUE_LENGTH = 13;

	public static PdfPTable getCharacterBasicsTable(CharacterPlayer characterPlayer) {
		float[] widths = { 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.addCell(getFirstColumnTable(characterPlayer));
		table.addCell(getSecondColumnTable(characterPlayer));
		table.addCell(getThirdColumnTable());
		return table;
	}

	private static PdfPCell getFirstColumnTable(CharacterPlayer characterPlayer) {
		float[] widths = { 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		table.addCell(createField(characterPlayer, "name"));
		table.addCell(createField(characterPlayer, "player"));
		table.addCell(createField(characterPlayer, "gender"));
		table.addCell(createField(characterPlayer, "age"));

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);

		return cell;
	}

	private static PdfPCell getSecondColumnTable(CharacterPlayer characterPlayer) {
		float[] widths = { 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		table.addCell(createField(characterPlayer, "race"));
		table.addCell(createField(characterPlayer, "planet"));
		table.addCell(createField(characterPlayer, "alliance"));
		table.addCell(createField(characterPlayer, "rank"));

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);

		return cell;
	}

	private static PdfPCell getThirdColumnTable() {
		try {
			return createLogoCell();
		} catch (DocumentException | IOException e) {
			MachineLog.errorMessage(CharacterBasicsTableFactory.class.getName(), e);
		}
		return null;
	}

	private static PdfPCell createField(CharacterPlayer characterPlayer, String tag) {
		float[] widths = { 0.7f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		table.addCell(getCell(getTranslatedTag(tag), Element.ALIGN_RIGHT));
		if (characterPlayer == null) {
			table.addCell(getCell(LINE, Element.ALIGN_LEFT));
		} else {
			table.addCell(getHandwrittingCell(characterPlayer.getTranslatedParameter(tag), Element.ALIGN_LEFT));
		}

		PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		setCellProperties(cell);

		return cell;
	}

	private static PdfPCell getCell(String text, int align) {
		PdfPCell cell = getCell(text, 0, 1, align, BaseColor.WHITE, FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE);
		return cell;
	}

	private static PdfPCell getHandwrittingCell(String text, int align) {
		PdfPCell cell = getCell(text, 0, 1, align, BaseColor.WHITE, FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE - 1);
		return cell;
	}

	private static String getTranslatedTag(String tag) {
		String value = getTranslator().getTranslatedText(LANGUAGE_PREFIX + tag.substring(0, 1).toUpperCase() + tag.substring(1));
		if (value != null) {
			if (value.length() > MAX_VALUE_LENGTH) {
				return value.substring(0, MAX_VALUE_LENGTH + 1);
			}
		}
		return value;
	}

}
