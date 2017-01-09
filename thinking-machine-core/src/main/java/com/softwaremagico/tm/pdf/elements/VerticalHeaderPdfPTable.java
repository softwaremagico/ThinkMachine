package com.softwaremagico.tm.pdf.elements;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;

public abstract class VerticalHeaderPdfPTable extends PdfPTable {

	protected VerticalHeaderPdfPTable(float[] widths) {
		super(widths);
		setTableEvent(new TableBorderEvent());
	}

	protected abstract int getTitleFontSize();

	protected PdfPCell createVerticalTitle(String title, int rowspan) {
		Font font = new Font(FadingSunsTheme.getTitleFont(), getTitleFontSize());
		font.setColor(BaseColor.WHITE);
		Phrase content = new Phrase(title, font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setPadding(0);
		titleCell.setRowspan(rowspan);
		titleCell.setRotation(90);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		// titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		titleCell.setBackgroundColor(BaseColor.BLACK);
		return titleCell;
	}

	protected static PdfPCell createElementLine(String text) {
		PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getLineFont(),
				FadingSunsTheme.TABLE_LINE_FONT_SIZE);
		cell.setMinimumHeight(10);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static PdfPCell createTableSubtitleElement(String text) {
		PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getSubtitleFont(),
				FadingSunsTheme.TABLE_LINE_FONT_SIZE);
		cell.setMinimumHeight(10);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

}
