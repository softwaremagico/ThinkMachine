package com.softwaremagico.tm.pdf.small.characteristics;

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

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;

public class CharacteristicsTableFactory extends BaseElement {

	public static PdfPTable getCharacteristicsBasicsTable(CharacterPlayer characterPlayer) {
		float[] widths = { 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setBorder(0);

		Phrase content = new Phrase(getTranslator().getTranslatedText("characteristics"), new Font(FadingSunsTheme.getTitleFont(),
				FadingSunsTheme.CHARACTER_SMALL_TITLE_FONT_SIZE));
		PdfPCell titleCell = new PdfPCell(content);
		setCellProperties(titleCell);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setColspan(widths.length);
		titleCell.setFixedHeight(30);
		table.addCell(titleCell);
		table.getDefaultCell().setPadding(0);

		for (CharacteristicType type : CharacteristicType.values()) {
			table.addCell(new CharacteristicsColumn(characterPlayer, type, CharacteristicsDefinitionFactory.getInstance()
					.getAll(type, Translator.getLanguage())));
		}

		return table;
	}

}
