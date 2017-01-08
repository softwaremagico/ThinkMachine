package com.softwaremagico.tm.pdf.perks;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class PerksTable extends PdfPTable {
	private final static String GAP = "_____";
	private final static float[] WIDTHS = { 2f, 5f, 2f, 5f };

	public PerksTable() {
		super(WIDTHS);
		addCell(createTitle());

		addCell(createLine("Pts."));
		addCell(createLine("Beneficios"));
		addCell(createLine("Pts."));
		addCell(createLine("Afliciones"));

		for (int i = 0; i < MainPerksTable.EMPTY_ROWS * 2; i++) {
			addCell(createLine(GAP));
			addCell(createLine(GAP + GAP + GAP + GAP));
		}
	}

	private PdfPCell createTitle() {
		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.PERKS_TITLE_FONT_SIZE);
		Phrase content = new Phrase("BENEFICIOS/AFLICIONES", font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setRowspan(2);
		titleCell.setColspan(WIDTHS.length);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setBorder(0);
		return titleCell;
	}

	private static PdfPCell createLine(String text) {
		PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE,
				FadingSunsTheme.getLineFont(), FadingSunsTheme.PERKS_SUBTITLE_FONT_SIZE);
		cell.setMinimumHeight(10);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}
}
