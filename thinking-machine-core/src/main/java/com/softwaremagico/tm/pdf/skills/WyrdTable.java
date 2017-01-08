package com.softwaremagico.tm.pdf.skills;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;

public class WyrdTable extends PdfPTable {
	private final static float[] WIDTHS = { 1f, 1f };
	private final static int CIRCLES = 20;
	private final static int TITLE_SPAN = 5;

	public WyrdTable() {
		super(WIDTHS);
		addCell(createCircle());
		addCell(createTitle());
		for (int i = 0; i < TITLE_SPAN; i++) {
			addCell(createCircle());
		}
		addCell(space());
		for (int i = 0; i < CIRCLES - TITLE_SPAN; i++) {
			addCell(createCircle());
		}
	}

	private PdfPCell createTitle() {
		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE);
		font.setColor(BaseColor.WHITE);
		Phrase content = new Phrase("Wyrd", font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setRotation(270);
		titleCell.setRowspan(TITLE_SPAN);
		titleCell.setBackgroundColor(BaseColor.BLACK);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return titleCell;
	}

	private PdfPCell space() {
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setRowspan(CIRCLES - TITLE_SPAN);
		emptyCell.setBorder(0);
		return emptyCell;
	}

	private PdfPCell createCircle() {
		return createValue("O", new Font(FadingSunsTheme.getTitleFont(),
				FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE));
	}

	private PdfPCell createValue(String text, Font font) {
		Phrase content = new Phrase(text, font);
		PdfPCell circleCell = new PdfPCell(content);
		circleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		circleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		circleCell.setBorder(0);
		circleCell.setMinimumHeight(MainSkillsTable.HEIGHT / CIRCLES);
		return circleCell;
	}

}
