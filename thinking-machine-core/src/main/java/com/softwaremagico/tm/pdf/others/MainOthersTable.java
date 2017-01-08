package com.softwaremagico.tm.pdf.others;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class MainOthersTable extends BaseElement {
	public final static int PADDING = 2;

	public static PdfPTable getOthersTable() {
		float[] widths = { 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		PdfPCell separator = createSeparator();
		separator.setPadding(PADDING);
		table.addCell(separator);
		table.addCell(separator);
		table.addCell(separator);

		PdfPCell othersCell = new PdfPCell(new OthersTable());
		othersCell.setPadding(0);
		othersCell.setBorder(0);
		table.addCell(othersCell);

		PdfPCell propertiesCell = new PdfPCell(new OthersTable());
		propertiesCell.setPadding(0);
		propertiesCell.setBorder(0);
		table.addCell(propertiesCell);

		PdfPCell equipmentCell = new PdfPCell(new OthersTable());
		equipmentCell.setPadding(0);
		equipmentCell.setBorder(0);
		table.addCell(equipmentCell);
		return table;
	}

}
