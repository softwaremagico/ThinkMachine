package com.softwaremagico.tm.pdf.skills;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class MainSkillsTable extends BaseElement {
	public final static int HEIGHT = 350;
	public final static int PADDING = 2;

	public static PdfPTable getSkillsTable() {
		float[] widths = { 1f, 12f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		
		PdfPCell separator = createSeparator();
		separator.setPadding(PADDING);
		table.addCell(separator);
		table.addCell(separator);
		table.addCell(separator);
		
		PdfPCell vitalityCell = new PdfPCell(new VitalityTable());
		vitalityCell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
		vitalityCell.setPadding(0);
		table.addCell(vitalityCell);
		
		PdfPCell skillsCell = new PdfPCell(SkillsTable.getSkillsTable());
		skillsCell.setBorder(0);
		skillsCell.setPadding(0);
		skillsCell.setPaddingRight(FadingSunsTheme.DEFAULT_MARGIN);
		skillsCell.setPaddingLeft(FadingSunsTheme.DEFAULT_MARGIN);
		table.addCell(skillsCell);
		
		PdfPCell wyrdCell = new PdfPCell(new WyrdTable());
		wyrdCell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
		wyrdCell.setPadding(0);
		table.addCell(wyrdCell);
		return table;
	}

}
