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
		float[] widths = { 1f, 1f };
		PdfPTable mainTable = new PdfPTable(widths);
		BaseElement.setTablePropierties(mainTable);

		PdfPTable infoTable = CharacterBasicsSmallTableFactory.getCharacterBasicsTable(getCharacterPlayer());
		PdfPCell infoCell = new PdfPCell(infoTable);
		infoCell.setColspan(2);
		infoCell.setBorder(1);
		mainTable.addCell(infoCell);

		PdfPTable characteristicsTable = CharacteristicsSmallTableFactory.getCharacteristicsBasicsTable(getCharacterPlayer());
		PdfPCell characteristicCell = new PdfPCell(characteristicsTable);
		characteristicCell.setColspan(2);
		characteristicCell.setBorder(1);
		mainTable.addCell(characteristicCell);

		PdfPTable naturalSkillsTable = NaturalSkillsSmallTable.getSkillsTable(getCharacterPlayer(), getLanguage());
		PdfPCell naturalSkillsCell = new PdfPCell(naturalSkillsTable);
		naturalSkillsCell.setColspan(2);
		naturalSkillsCell.setBorder(1);
		mainTable.addCell(naturalSkillsCell);

		document.add(mainTable);
	}
}
