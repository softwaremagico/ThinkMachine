package com.softwaremagico.tm.pdf.fighting;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class WeaponsAndArmours extends BaseElement {
	public final static int PADDING = 2;

	public static PdfPTable getWeaponsAndArmoursTable() {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setPadding(PADDING);
		table.getDefaultCell().setBorder(0);

		PdfPCell fireArmsCell = new PdfPCell(new WeaponsTable());
		fireArmsCell.setRowspan(2);
		table.addCell(fireArmsCell);

		PdfPCell armourCell = new PdfPCell(new ArmourTable());
		table.addCell(armourCell);

		PdfPCell shieldCell = new PdfPCell(new ShieldTable());
		table.addCell(shieldCell);

		return table;
	}
}
