package com.softwaremagico.tm.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.pdf.characteristics.CharacteristicsSmallTableFactory;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.info.CharacterBasicsSmallTableFactory;
import com.softwaremagico.tm.pdf.skills.LearnedSkillsSmallTable;
import com.softwaremagico.tm.pdf.skills.NaturalSkillsSmallTable;

public class SmallCharacterSheet extends PdfDocument {
	private CharacterPlayer characterPlayer = null;

	public SmallCharacterSheet() {
		this(Translator.DEFAULT_LANGUAGE);
	}

	public SmallCharacterSheet(String language) {
		super(language);
		Translator.setLanguage(language);
	}

	public SmallCharacterSheet(CharacterPlayer characterPlayer) {
		this(characterPlayer.getLanguage());
		this.characterPlayer = characterPlayer;
	}

	@Override
	protected Rectangle getPageSize() {
		return PageSize.A5;
	}

	public CharacterPlayer getCharacterPlayer() {
		return characterPlayer;
	}

	@Override
	protected void createPagePDF(Document document) throws Exception {
		float[] widths = { 2f, 1f };
		PdfPTable mainTable = new PdfPTable(widths);
		BaseElement.setTablePropierties(mainTable);
		mainTable.getDefaultCell().setBorder(0);

		PdfPTable infoTable = CharacterBasicsSmallTableFactory.getCharacterBasicsTable(getCharacterPlayer());
		PdfPCell infoCell = new PdfPCell(infoTable);
		mainTable.addCell(infoCell);

		PdfPTable learnedSkillsTable = LearnedSkillsSmallTable.getSkillsTable(getCharacterPlayer(), getLanguage());
		PdfPCell learnedSkillsCell = new PdfPCell(learnedSkillsTable);
		learnedSkillsCell.setColspan(2);
		learnedSkillsCell.setRowspan(2);
		mainTable.addCell(learnedSkillsCell);

		PdfPTable basicTable = new PdfPTable(new float[] { 5f, 4f });
		BaseElement.setTablePropierties(basicTable);
		basicTable.getDefaultCell().setBorder(0);

		PdfPTable characteristicsTable = CharacteristicsSmallTableFactory.getCharacteristicsBasicsTable(getCharacterPlayer());
		PdfPCell characteristicCell = new PdfPCell(characteristicsTable);
		basicTable.addCell(characteristicCell);

		PdfPTable naturalSkillsTable = NaturalSkillsSmallTable.getSkillsTable(getCharacterPlayer(), getLanguage());
		PdfPCell naturalSkillsCell = new PdfPCell(naturalSkillsTable);
		basicTable.addCell(naturalSkillsCell);
		
		mainTable.addCell(basicTable);

		document.add(mainTable);
	}
}
