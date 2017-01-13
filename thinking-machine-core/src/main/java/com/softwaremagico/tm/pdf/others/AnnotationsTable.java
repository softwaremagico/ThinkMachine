package com.softwaremagico.tm.pdf.others;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.elements.VerticalHeaderPdfPTable;

public class AnnotationsTable extends VerticalHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f, 24f };
	private final static int CELL_HEIGHT = 70;

	public AnnotationsTable() {
		super(WIDTHS);
		addCell(createVerticalTitle("Anotaciones", 2));

		addCell(createSubtitleLine("Del personaje"));
		addCell(createSubtitleLine("La historia"));
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.ANNOTATIONS_TITLE_FONT_SIZE;
	}

	protected static PdfPCell createSubtitleLine(String text) {
		PdfPCell cell = BaseElement.getCell(text, 1, 1, Element.ALIGN_LEFT, BaseColor.WHITE, FadingSunsTheme.getTitleFont(),
				FadingSunsTheme.ANNOTATIONS_SUBTITLE_FONT_SIZE);
		cell.setMinimumHeight(CELL_HEIGHT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

}
