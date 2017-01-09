package com.softwaremagico.tm.pdf.elements;

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
		cb.rectangle(x1, y1, x2 - x1, y2 - y1);
		cb.stroke();
	}
}