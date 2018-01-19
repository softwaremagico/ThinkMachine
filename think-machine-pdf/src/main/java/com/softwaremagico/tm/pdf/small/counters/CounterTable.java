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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.CellCompleteBoxEvent;
import com.softwaremagico.tm.pdf.complete.elements.CustomPdfTable;
import com.softwaremagico.tm.pdf.complete.elements.CellCompleteBoxEvent.Border;

public abstract class CounterTable extends CustomPdfTable {
	protected final static float[] WIDTHS = { 2.8f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f };
	protected int addedCircle = 0;
	// http://stackoverflow.com/questions/5554553/itext-pdftable-cell-vertical-alignment
	protected float paddingTop = -3f;

	private CharacterPlayer characterPlayer;

	public CounterTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		this.characterPlayer = characterPlayer;
	}

	protected int getNumberOfCircles() {
		// Title + circles.
		return WIDTHS.length - 1;
	}

	protected PdfPCell getCircle() {
		if (characterPlayer == null) {
			return createCircle();
		}
		if (addedCircle == getSelectedValue() - 1) {
			PdfPCell cell = createCircle();
			cell.setCellEvent(new CellCompleteBoxEvent(1, new Border[] { Border.TOP, Border.BOTTOM, Border.RIGHT }));
			return cell;
		} else if (addedCircle == 0) {
			PdfPCell cell = createCircle();
			cell.setCellEvent(new CellCompleteBoxEvent(1, new Border[] { Border.TOP, Border.BOTTOM, Border.LEFT }));
			return cell;
		} else if (addedCircle < getSelectedValue() - 1) {
			PdfPCell cell = createCircle();
			cell.setCellEvent(new CellCompleteBoxEvent(1, new Border[] { Border.TOP, Border.BOTTOM }));
			return cell;
		} else {
			return createCircle();
		}
	}

	protected PdfPCell createCircle() {
		PdfPCell cell = createValue("O", new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTER_COUNTER_POINT_SIZE), Element.ALIGN_MIDDLE);
		return cell;
	}

	protected PdfPCell createValue(String text, Font font, int alignment) {
		Phrase content = new Phrase(text, font);
		PdfPCell circleCell = new PdfPCell(content);
		// Not putting correctly the "o" at the center of the cell.
		// http://stackoverflow.com/questions/5554553/itext-pdftable-cell-vertical-alignment
		circleCell.setPaddingTop(paddingTop);
		circleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		circleCell.setVerticalAlignment(alignment);
		circleCell.setBorder(0);
		circleCell.setMinimumHeight(20);
		return circleCell;
	}

	protected abstract int getSelectedValue();

	public CharacterPlayer getCharacterPlayer() {
		return characterPlayer;
	}

}
