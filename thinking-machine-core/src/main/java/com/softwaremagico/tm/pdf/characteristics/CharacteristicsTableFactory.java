package com.softwaremagico.tm.pdf.characteristics;

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

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class CharacteristicsTableFactory extends BaseElement {
	private final static String GAP = "   ";

	public static PdfPTable getCharacterBasicsTable() {
		float[] widths = { 1f, 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		PdfPCell separator = createSeparator();
		separator.setColspan(widths.length);
		table.addCell(separator);

		Phrase content = new Phrase(getTranslator().getTranslatedText("characteristics").toUpperCase(), new Font(FadingSunsTheme.getTitleFont(),
				FadingSunsTheme.TITLE_FONT_SIZE));
		PdfPCell titleCell = new PdfPCell(content);
		setCellProperties(titleCell);
		titleCell.setColspan(widths.length);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setFixedHeight(30);
		table.addCell(titleCell);
		table.getDefaultCell().setPadding(0);

		table.addCell(new CharacteristicColumn(getTranslator().getTranslatedText("bodyCharacteristics"), new String[] {
				getTranslator().getTranslatedText("strengthCharacteristic") + " (" + GAP + ")",
				getTranslator().getTranslatedText("dexterityCharacteristic") + " (" + GAP + ")",
				getTranslator().getTranslatedText("enduranceCharacteristic") + " (" + GAP + ")" }));
		table.addCell(new CharacteristicColumn(getTranslator().getTranslatedText("mindCharacteristics"), new String[] {
				getTranslator().getTranslatedText("witsCharacteristic") + " (" + GAP + ")",
				getTranslator().getTranslatedText("perceptionCharacteristic") + " (" + GAP + ")",
				getTranslator().getTranslatedText("techCharacteristic") + " (" + GAP + ")" }));
		table.addCell(new CharacteristicColumn(getTranslator().getTranslatedText("spiritCharacteristics"), new String[] {
				getTranslator().getTranslatedText("presenceCharacteristic") + " (" + GAP + ")",
				getTranslator().getTranslatedText("willCharacteristic") + " (" + GAP + ")", getTranslator().getTranslatedText("faithCharacteristic") + " (" + GAP + ")" }));
		table.addCell(new CharacteristicColumn(getTranslator().getTranslatedText("othersCharacteristics"), new String[] {
				getTranslator().getTranslatedText("initiativeValue"), getTranslator().getTranslatedText("movementValue") + " (" + GAP + ")",
				getTranslator().getTranslatedText("defenseValue") + " (1)" }));

		return table;
	}

}
