package com.softwaremagico.tm.pdf.complete.skills;

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
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;
import com.softwaremagico.tm.pdf.complete.elements.CellCompleteBoxEvent.Border;

public abstract class CounterTable extends LateralHeaderPdfPTable {
	protected final static float[] WIDTHS = { 1f, 1f };
	protected final static int CIRCLES = 23;
	protected final static int TITLE_SPAN = 5;
	protected int addedCircle = 0;

	protected CounterTable(float[] widths) {
		super(widths);
	}

	protected PdfPCell space(int rowspan) {
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setRowspan(rowspan);
		emptyCell.setBorder(0);
		return emptyCell;
	}

	protected PdfPCell getCircle(CharacterPlayer characterPlayer) {
		if (characterPlayer == null) {
			return createCircle();
		}
		if (CIRCLES - addedCircle == getSelectedValue(characterPlayer)) {
			PdfPCell cell = createCircle();
			cell.setCellEvent(new CellCompleteBoxEvent(2, new Border[] { Border.TOP, Border.LEFT, Border.RIGHT }));
			return cell;
		} else if (CIRCLES - addedCircle == 1) {
			PdfPCell cell = createCircle();
			cell.setCellEvent(new CellCompleteBoxEvent(2, new Border[] { Border.BOTTOM, Border.LEFT, Border.RIGHT }));
			return cell;
		} else if (CIRCLES - addedCircle < getSelectedValue(characterPlayer)) {
			PdfPCell cell = createCircle();
			cell.setCellEvent(new CellCompleteBoxEvent(2, new Border[] { Border.LEFT, Border.RIGHT }));
			return cell;
		} else {
			return createCircle();
		}
	}

	private PdfPCell createCircle() {
		PdfPCell cell = createValue("O", new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE), Element.ALIGN_MIDDLE);
		return cell;
	}

	protected PdfPCell createValue(String text, Font font, int alignment) {
		Phrase content = new Phrase(text, font);
		PdfPCell circleCell = new PdfPCell(content);
		// Not putting correctly the "o" at the center of the cell.
		// http://stackoverflow.com/questions/5554553/itext-pdftable-cell-vertical-alignment
		circleCell.setPaddingTop(-4f);
		circleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		circleCell.setVerticalAlignment(alignment);
		circleCell.setBorder(0);
		// Some position corrections.
		circleCell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / CIRCLES) + 1.3f);
		return circleCell;
	}

	protected abstract int getSelectedValue(CharacterPlayer characterPlayer);

}
