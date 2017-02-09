package com.softwaremagico.tm.pdf.elements;

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

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

public class CellPaddingEvent implements PdfPCellEvent {
	private int border = 1;

	public CellPaddingEvent() {

	}

	public CellPaddingEvent(int border) {
		this.border = border;
	}

	public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
		float x1 = position.getLeft() + 2;
		float x2 = position.getRight() - 2;
		float y1 = position.getTop() - 2;
		float y2 = position.getBottom() + 2;
		PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];

		Rectangle rect = new Rectangle(x1, y1, x2, y2);
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(border);
		canvas.rectangle(rect);

		canvas.stroke();
	}
}
