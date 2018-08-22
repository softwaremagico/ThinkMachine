package com.softwaremagico.tm.pdf.small.counters;

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
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;

public class WyrdTable extends CounterTable {

	public WyrdTable(CharacterPlayer characterPlayer) {
		super(characterPlayer);

		getDefaultCell().setBorder(0);

		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTER_VITALITY_TITLE_FONT_SIZE);
		Phrase content = new Phrase(getTranslator().getTranslatedText("wyrd"), font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setBorder(0);
		titleCell.setPaddingTop(paddingTop);
		titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		addCell(titleCell);

		addedCircle = 0;

		for (int i = 0; i < getNumberOfCircles(); i++) {
			addCell(getCircle());
			addedCircle++;
		}

	}

	@Override
	protected int getSelectedValue() {
		if (getCharacterPlayer() != null) {
			try {
				return getCharacterPlayer().getWyrdValue().intValue();
			} catch (InvalidXmlElementException e) {
				PdfExporterLog.errorMessage(this.getClass().getName(), e);
			}
		}
		return -1;
	}
}
