package com.softwaremagico.tm.pdf.skills;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class SkillsTable extends BaseElement {
	public final static int HEIGHT = 400;
	public final static int PADDING = 2;

	public static PdfPTable getSkillsTable() {
		float[] widths = { 1f, 9f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		
		PdfPCell separator = createSeparator();
		separator.setPadding(PADDING);
		table.addCell(separator);
		table.addCell(separator);
		table.addCell(separator);
		
		PdfPCell vitalityCell = new PdfPCell(new VitalityTable());
		vitalityCell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
		vitalityCell.setPadding(PADDING);
		table.addCell(vitalityCell);
		
		PdfPCell skillsCell = new PdfPCell(new VitalityTable());
		skillsCell.setBorder(0);
		skillsCell.setPadding(PADDING);		
		table.addCell(skillsCell);
		
		PdfPCell wyrdCell = new PdfPCell(new WyrdTable());
		wyrdCell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
		wyrdCell.setPadding(PADDING);
		table.addCell(wyrdCell);
		return table;
	}

}
