package com.softwaremagico.tm.pdf.others;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class OthersTable extends PdfPTable {
	private final static int EMPTY_ROWS = 3;
	private final static String GAP = "_______";
	private final static float[] WIDTHS = { 5f, 1f };

	public OthersTable() {
		super(WIDTHS);
		addCell(createTitle());

		addCell(createLine("Rasgo"));
		addCell(createLine("Nivel"));

		for (int i = 0; i < EMPTY_ROWS; i++) {
			addCell(createLine(GAP + GAP + GAP + GAP + GAP));
			addCell(createLine(GAP));
		}
	}

	private PdfPCell createTitle() {
		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.PERKS_TITLE_FONT_SIZE);
		Phrase content = new Phrase("OTROS RASGOS", font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setRowspan(2);
		titleCell.setColspan(WIDTHS.length);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setBorder(0);
		return titleCell;
	}

	private static PdfPCell createLine(String text) {
		PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE,
				FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE);
		cell.setMinimumHeight(10);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}
}
