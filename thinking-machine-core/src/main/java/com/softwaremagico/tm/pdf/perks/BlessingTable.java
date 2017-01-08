package com.softwaremagico.tm.pdf.perks;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class BlessingTable extends PdfPTable {
	private final static String GAP = "____";
	private final static float[] WIDTHS = { 8f, 2f, 5f, 10f };

	public BlessingTable() {
		super(WIDTHS);
		addCell(createTitle());

		addCell(createLine("Nombre"));
		addCell(createLine("+/-"));
		addCell(createLine("Rasgo"));
		addCell(createLine("Situación"));

		for (int i = 0; i < MainPerksTable.EMPTY_ROWS; i++) {
			addCell(createLine(GAP+GAP+GAP+GAP+GAP));
			addCell(createLine(GAP));
			addCell(createLine(GAP+GAP+GAP));
			addCell(createLine(GAP+GAP+GAP+GAP+GAP+GAP));
		}
	}

	private PdfPCell createTitle() {
		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.PERKS_TITLE_FONT_SIZE);
		Phrase content = new Phrase("BENDICIONES/MALDICIONES", font);
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
