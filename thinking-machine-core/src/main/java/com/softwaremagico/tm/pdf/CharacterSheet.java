package com.softwaremagico.tm.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.characteristics.CharacteristicsTableFactory;
import com.softwaremagico.tm.pdf.fighting.FightingManoeuvres;
import com.softwaremagico.tm.pdf.fighting.FightingTable;
import com.softwaremagico.tm.pdf.info.CharacterBasicsTableFactory;
import com.softwaremagico.tm.pdf.others.AnnotationsTable;
import com.softwaremagico.tm.pdf.perks.MainPerksTableFactory;
import com.softwaremagico.tm.pdf.skills.MainSkillsTableFactoryFactory;
import com.softwaremagico.tm.pdf.skills.occultism.PowerTable;

public class CharacterSheet extends PdfDocument {

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
		document.add(new AnnotationsTable());
		document.add(new PowerTable());
		document.add(FightingManoeuvres.getFightingManoeuvresTable());
		document.add(new FightingTable());

		document.newPage();
	}

	public float[] getTableWidths() {
		float[] widths = { 0.60f, 0.30f };
		return widths;
	}

	public void setTablePropierties(PdfPTable mainTable) {
		mainTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		mainTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

}
