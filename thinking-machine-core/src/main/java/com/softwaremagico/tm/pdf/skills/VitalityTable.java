package com.softwaremagico.tm.pdf.skills;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;

public class VitalityTable extends PdfPTable {
	private final static float[] WIDTHS = { 1f, 1f };
	private final static int CIRCLES = 20;
	private final static int TITLE_SPAN = 5;
	private final static int MODIFICATORS_SPAN = 5;

	public VitalityTable() {
		super(WIDTHS);
		addCell(createTitle());
		for (int i = 0; i < TITLE_SPAN; i++) {
			addCell(createCircle());
		}
		addCell(space());
		for (int i = 0; i < CIRCLES - TITLE_SPAN - MODIFICATORS_SPAN; i++) {
			addCell(createCircle());
		}

		for (int i = 1; i <= MODIFICATORS_SPAN; i++) {
			addCell(createValue("-" + (i * 2), new Font(FadingSunsTheme.getLineFont(),
					FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE - 4), Element.ALIGN_MIDDLE));
			addCell(createCircle());
		}
		// addCell(space());
	}

	private PdfPCell createTitle() {
		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE);
		font.setColor(BaseColor.WHITE);
		Phrase content = new Phrase("Vitalidad", font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setRotation(90);
		titleCell.setRowspan(TITLE_SPAN);
		titleCell.setBackgroundColor(BaseColor.BLACK);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return titleCell;
	}

	private PdfPCell space() {
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setRowspan(CIRCLES - TITLE_SPAN - MODIFICATORS_SPAN);
		emptyCell.setBorder(0);
		return emptyCell;
	}

	private PdfPCell createCircle() {
		return createValue("O", new Font(FadingSunsTheme.getTitleFont(),
				FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE), Element.ALIGN_TOP);
	}

	private PdfPCell createValue(String text, Font font, int alignment) {
		Phrase content = new Phrase(text, font);
		PdfPCell circleCell = new PdfPCell(content);
		circleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		circleCell.setVerticalAlignment(alignment);
		circleCell.setBorder(0);
		circleCell.setMinimumHeight(MainSkillsTable.HEIGHT / CIRCLES);
		return circleCell;
	}

}
