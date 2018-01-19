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

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEventAfterSplit;

public class TableBorderEvent implements PdfPTableEventAfterSplit {
	protected int rowCount;
	protected boolean bottom = true;
	protected boolean top = true;

	@Override
	public void splitTable(PdfPTable table) {
		if (table.getRows().size() != rowCount) {
			bottom = false;
		}
	}

	@Override
	public void afterSplitTable(PdfPTable table, PdfPRow startRow, int startIdx) {
		if (table.getRows().size() != rowCount) {
			// if the table gains a row, a row was split
			rowCount = table.getRows().size();
			top = false;
		}
	}

	@Override
	public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvas) {
		float width[] = widths[0];
		float x1 = width[0];
		float x2 = width[width.length - 1];
		float y1 = heights[0];
		float y2 = heights[heights.length - 1];

		PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
		Rectangle rect1 = new Rectangle(x1, y1, x2, y2);
		rect1.setBorder(Rectangle.BOX);
		rect1.setBorderWidth(2);
		cb.rectangle(rect1);
		cb.stroke();
	}
}
