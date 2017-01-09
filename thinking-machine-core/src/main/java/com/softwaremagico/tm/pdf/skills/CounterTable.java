package com.softwaremagico.tm.pdf.skills;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.VerticalHeaderPdfPTable;

public abstract class CounterTable extends VerticalHeaderPdfPTable {
	protected final static float[] WIDTHS = { 1f, 1f };
	protected final static int CIRCLES = 23;
	protected final static int TITLE_SPAN = 5;

	protected CounterTable(float[] widths) {
		super(widths);
	}

	protected PdfPCell space(int rowspan) {
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setRowspan(rowspan);
		emptyCell.setBorder(0);
		return emptyCell;
	}

	protected PdfPCell createCircle() {
		return createValue("O", new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE), Element.ALIGN_TOP);
	}

	protected PdfPCell createValue(String text, Font font, int alignment) {
		Phrase content = new Phrase(text, font);
		PdfPCell circleCell = new PdfPCell(content);
		circleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		circleCell.setVerticalAlignment(alignment);
		circleCell.setBorder(0);
		circleCell.setMinimumHeight(MainSkillsTableFactoryFactory.HEIGHT / CIRCLES);
		return circleCell;
	}

}
