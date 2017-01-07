package com.softwaremagico.tm.pdf.skills;

import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class SkillsTable extends BaseElement {

	public static PdfPTable getSkillsTable() {
		float[] widths = { 1f, 9f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
//		table.addCell(getFirstColumnTable());
//		table.addCell(getSecondColumnTable());
//		table.addCell(getThirdColumnTable());
		return table;
	}

}
