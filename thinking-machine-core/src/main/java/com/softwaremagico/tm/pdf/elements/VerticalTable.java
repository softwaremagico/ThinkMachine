package com.softwaremagico.tm.pdf.elements;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;

public abstract class VerticalTable extends PdfPTable {
	private float[] columnWidths;

	public VerticalTable(float[] widths) {
		super(widths);
		setColumnWidths(widths);
	}

	protected PdfPCell createTitle(String title) {
		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.VERTICALTABLE_TITLE_FONT_SIZE);
		Phrase content = new Phrase(title, font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setRowspan(2);
		titleCell.setColspan(getColumnWidths().length);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setBorder(0);
		return titleCell;
	}

	protected static PdfPCell createSubtitleLine(String text) {
		PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getSubtitleFont(),
				FadingSunsTheme.TABLE_LINE_FONT_SIZE);
		cell.setMinimumHeight(10);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static PdfPCell createLine(String text) {
		PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getLineFont(),
				FadingSunsTheme.TABLE_LINE_FONT_SIZE);
		cell.setMinimumHeight(10);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	public float[] getColumnWidths() {
		return columnWidths;
	}

	private void setColumnWidths(float[] columnWidths) {
		this.columnWidths = columnWidths;
	}

}
