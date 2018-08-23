package com.softwaremagico.tm.pdf.complete.info;

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

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;

public class CharacterBasicsCompleteTableFactory extends CharacterBasicsTableFactory {

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

		table.addCell(createField(characterPlayer, "name", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE));
		table.addCell(createField(characterPlayer, "player", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE));
		table.addCell(createField(characterPlayer, "gender", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE));
		table.addCell(createField(characterPlayer, "age", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE));

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);

		return cell;
	}

	private static PdfPCell getSecondColumnTable(CharacterPlayer characterPlayer) {
		float[] widths = { 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		table.addCell(createField(characterPlayer, "race", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE));
		table.addCell(createField(characterPlayer, "planet", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE));
		table.addCell(createField(characterPlayer, "faction", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE));
		table.addCell(createField(characterPlayer, "rank", FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE));

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);

		return cell;
	}

	private static PdfPCell getThirdColumnTable() {
		try {
			return createLogoCell();
		} catch (DocumentException | IOException e) {
			PdfExporterLog.errorMessage(CharacterBasicsCompleteTableFactory.class.getName(), e);
		}
		return null;
	}

}
