package com.softwaremagico.tm.pdf.skills;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.skills.occultism.OccultismTable;
import com.softwaremagico.tm.skills.SkillFactory;

public class SkillsTable extends BaseElement {
	private final static int ROWS = 26;
	private final static int TITLE_ROWSPAN = 2;
	private static int learnedSkillsAdded = 0;
	private final static String GAP = "____";
	private final static int OCCULTISM_ROWS = 5;

	public static PdfPTable getSkillsTable() {
		float[] widths = { 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.addCell(getFirstColumnTable());
		table.addCell(getSecondColumnTable());
		table.addCell(getThirdColumnTable());
		return table;
	}

	private static PdfPCell getFirstColumnTable() {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		table.addCell(createTitle("Habilidades Naturales"));
		for (String skill : SkillFactory.getNaturalSkills()) {
			table.addCell(createSkillLine(skill));
			table.addCell(createSkillLine(GAP));
		}

		table.addCell(createTitle("Habilidades Adquiridas"));
		for (int i = 0; i < Math.min(SkillFactory.getLearnedSkills().size(), ROWS - (2 * TITLE_ROWSPAN)
				- SkillFactory.getNaturalSkills().size()); i++) {
			table.addCell(createSkillLine(SkillFactory.getLearnedSkills().get(i)));
			table.addCell(createSkillLine(GAP));
			learnedSkillsAdded++;
		}

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

	private static PdfPCell getSecondColumnTable() {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		int maxElements = Math.min(SkillFactory.getLearnedSkills().size(), ROWS + learnedSkillsAdded);

		for (int i = learnedSkillsAdded; i < maxElements; i++) {
			table.addCell(createSkillLine(SkillFactory.getLearnedSkills().get(i)));
			table.addCell(createSkillLine(GAP));
			learnedSkillsAdded++;
		}

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

	private static PdfPCell getThirdColumnTable() {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		int addedElements = 0;
		int maxElements = Math.min(SkillFactory.getLearnedSkills().size(), ROWS + learnedSkillsAdded);

		for (int i = learnedSkillsAdded; i < maxElements; i++) {
			table.addCell(createSkillLine(SkillFactory.getLearnedSkills().get(i)));
			table.addCell(createSkillLine(GAP));
			learnedSkillsAdded++;
			addedElements++;
		}

		// Complete with empty skills.
		for (int i = addedElements; i < ROWS - OCCULTISM_ROWS; i++) {
			table.addCell(createSkillLine("____________"));
			table.addCell(createSkillLine(GAP));
		}

		// Add Occultism table
		PdfPTable occultismTable = new OccultismTable();
		PdfPCell occulstimCell = new PdfPCell();
		//setCellProperties(occulstimCell);
		//occulstimCell.setRowspan(widths.length);
		occulstimCell.setRowspan(OCCULTISM_ROWS);
		occulstimCell.setColspan(2);
		occulstimCell.addElement(occultismTable);
		occulstimCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		table.addCell(occulstimCell);

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

	private static PdfPCell createTitle(String text) {
		PdfPCell cell = getCell(text, 0, 2, Element.ALIGN_CENTER, BaseColor.WHITE,
				FadingSunsTheme.getTitleFont(), FadingSunsTheme.SKILLS_TITLE_FONT_SIZE);
		cell.setMinimumHeight(MainSkillsTable.HEIGHT / (ROWS / TITLE_ROWSPAN) + 3);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static PdfPCell createSkillLine(String text) {
		PdfPCell cell = getCell(text, 0, 1, Element.ALIGN_LEFT, BaseColor.WHITE,
				FadingSunsTheme.getLineFont(), FadingSunsTheme.SKILLS_LINE_FONT_SIZE);
		cell.setMinimumHeight((MainSkillsTable.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

}
