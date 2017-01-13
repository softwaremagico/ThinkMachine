package com.softwaremagico.tm.pdf.fighting;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class FightingManoeuvres extends BaseElement {
	public final static int PADDING = 2;

	public static PdfPTable getFightingManoeuvresTable() {
		float[] widths = { 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setPadding(PADDING);
		table.getDefaultCell().setBorder(0);

		table.addCell(BaseElement.createWhiteSeparator());
		table.addCell(BaseElement.createWhiteSeparator());

		table.addCell(BaseElement.createWhiteSeparator());
		table.addCell(BaseElement.createWhiteSeparator());

		PdfPCell fireArmsCell = new PdfPCell(new FireArmsTable());
		table.addCell(fireArmsCell);

		PdfPCell fencingCell = new PdfPCell(new FightingTable());
		table.addCell(fencingCell);

		return table;
	}

}
