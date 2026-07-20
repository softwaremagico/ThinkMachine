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

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;

public class VitalityTable extends CounterTable {

	public VitalityTable(CharacterPlayer characterPlayer) {
		super(characterPlayer);

		getDefaultCell().setBorder(0);

		final Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTER_VITALITY_TITLE_FONT_SIZE);
		final Phrase content = new Phrase(getTranslator().getTranslatedText("vitality"), font);
		final PdfPCell titleCell = new PdfPCell(content);
		titleCell.setBorder(0);
		titleCell.setPaddingRight(0);
		titleCell.setPaddingTop(newPaddingTop);
		titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		addCell(titleCell);

		addedCircle = 0;

		for (int i = 0; i < getNumberOfCircles(); i++) {
			addCell(getCircle());
			addedCircle++;
		}

	}

	@Override
	protected PdfPCell createCircle() {
		if (addedCircle >= 5) {
			return super.createCircle();
		}

		final PdfPCell cell = createValue("-" + (10 - addedCircle * 2), new Font(FadingSunsTheme.getLineFontBold(),
				FadingSunsTheme.CHARACTER_VITALITY_PENALTIES_TITLE_FONT_SIZE), Element.ALIGN_MIDDLE);
		cell.setPaddingTop(0f);
		cell.setPaddingRight(-2f);
		return cell;
	}

	@Override
	protected int getSelectedValue() {
		if (getCharacterPlayer() != null) {
			try {
				return getCharacterPlayer().getVitalityValue().intValue();
			} catch (InvalidXmlElementException e) {
				PdfExporterLog.errorMessage(this.getClass().getName(), e);
			}
		}
		return -1;
	}
}
