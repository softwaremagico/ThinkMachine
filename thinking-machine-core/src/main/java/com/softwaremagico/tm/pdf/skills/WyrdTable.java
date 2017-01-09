package com.softwaremagico.tm.pdf.skills;

import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.pdf.FadingSunsTheme;

public class WyrdTable extends CounterTable {

	public WyrdTable() {
		super(WIDTHS);
		addCell(createCircle());
		addCell(createVerticalTitle("Wyrd", TITLE_SPAN));
		for (int i = 0; i < TITLE_SPAN; i++) {
			addCell(createCircle());
		}
		addCell(space(CIRCLES - TITLE_SPAN));
		for (int i = 0; i < CIRCLES - TITLE_SPAN; i++) {
			addCell(createCircle());
		}
	}

	@Override
	protected PdfPCell createVerticalTitle(String title, int rowspan) {
		PdfPCell titleCell = super.createVerticalTitle(title, rowspan);
		titleCell.setRotation(270);
		return titleCell;
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE;
	}

}
