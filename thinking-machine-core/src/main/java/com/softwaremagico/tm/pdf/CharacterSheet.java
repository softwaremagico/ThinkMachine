package com.softwaremagico.tm.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.characteristics.CharacteristicsTableFactory;
import com.softwaremagico.tm.pdf.cybernetics.CyberneticsTable;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.fighting.FightingManoeuvres;
import com.softwaremagico.tm.pdf.fighting.FightingTable;
import com.softwaremagico.tm.pdf.info.CharacterBasicsTableFactory;
import com.softwaremagico.tm.pdf.info.DescriptionTable;
import com.softwaremagico.tm.pdf.info.PropertiesTable;
import com.softwaremagico.tm.pdf.others.AnnotationsTable;
import com.softwaremagico.tm.pdf.perks.MainPerksTableFactory;
import com.softwaremagico.tm.pdf.skills.MainSkillsTableFactoryFactory;
import com.softwaremagico.tm.pdf.skills.occultism.PowerTable;

public class CharacterSheet extends PdfDocument {
	private final static float[] REAR_TABLE_WIDTHS = { 1f, 1f, 1f };

	public CharacterSheet() {
		super();
	}

	@Override
	protected Rectangle getPageSize() {
		return PageSize.A4;
	}

	@Override
	protected void createPagePDF(Document document) throws Exception {
		// addBackGroundImage(document, Path.returnBackgroundPath(), writer);
		PdfPTable mainTable = CharacterBasicsTableFactory.getCharacterBasicsTable();
		document.add(mainTable);
		PdfPTable characteristicsTable = CharacteristicsTableFactory.getCharacterBasicsTable();
		document.add(characteristicsTable);
		PdfPTable skillsTable = MainSkillsTableFactoryFactory.getSkillsTable();
		document.add(skillsTable);
		PdfPTable perksTable = MainPerksTableFactory.getPerksTable();
		document.add(perksTable);
		document.newPage();
		// PdfPTable othersTable = MainOthersTable.getOthersTable();
		// document.add(othersTable);
		document.add(createRearTable());
		document.add(new CyberneticsTable());
		document.add(FightingManoeuvres.getFightingManoeuvresTable());
		document.add(new FightingTable());

		document.newPage();
	}

	private PdfPTable createRearTable() {
		PdfPTable mainTable = new PdfPTable(REAR_TABLE_WIDTHS);
		mainTable.getDefaultCell().setBorder(0);
		mainTable.setWidthPercentage(100);

		mainTable.addCell(new DescriptionTable());
		PdfPCell cell = new PdfPCell(new AnnotationsTable());
		cell.setColspan(2);
		mainTable.addCell(cell);

		mainTable.addCell(BaseElement.createBigSeparator());
		PdfPCell separatorCell = new PdfPCell(BaseElement.createBigSeparator());
		separatorCell.setColspan(2);
		mainTable.addCell(separatorCell);

		mainTable.addCell(new PropertiesTable());
		PdfPCell psiCell = new PdfPCell(new PowerTable());
		psiCell.setColspan(2);
		mainTable.addCell(psiCell);

		return mainTable;
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

}
