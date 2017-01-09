package com.softwaremagico.tm.pdf.others;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.VerticalHeaderPdfPTable;

public class AnnotationsTable extends VerticalHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f, 12f };

	public AnnotationsTable() {
		super(WIDTHS);
		addCell(createVerticalTitle("Anotaciones", 2));

		addCell(createTableSubtitleElement("Poder/Rito"));
		addCell(createTableSubtitleElement("Nivel"));
		addCell(createTableSubtitleElement("Tirada"));
		addCell(createTableSubtitleElement("Alcance"));
		addCell(createTableSubtitleElement("Duración"));
		addCell(createTableSubtitleElement("Requisitos"));
		addCell(createTableSubtitleElement("Coste"));
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.ANNOTATIONS_TITLE_FONT_SIZE;
	}

}
