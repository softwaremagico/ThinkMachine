package com.softwaremagico.tm.smallpdf;

/*-
 * #%L
 * The Thinking Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *  
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *  
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.pdf.PdfDocument;
import com.softwaremagico.tm.pdf.SheetBackgroundEvent;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.smallpdf.characteristics.CharacteristicsTableFactory;
import com.softwaremagico.tm.smallpdf.fighting.ArmourTable;
import com.softwaremagico.tm.smallpdf.info.CharacterBasicsReducedTableFactory;
import com.softwaremagico.tm.smallpdf.skills.LearnedSkillsTable;
import com.softwaremagico.tm.smallpdf.skills.NaturalSkillsTable;
import com.softwaremagico.tm.smallpdf.traits.BeneficesTable;
import com.softwaremagico.tm.smallpdf.traits.BlessingTable;

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
		mainTable.getDefaultCell().setPadding(0);

		PdfPTable infoTable = CharacterBasicsReducedTableFactory.getCharacterBasicsTable(getCharacterPlayer());
		PdfPCell infoCell = new PdfPCell(infoTable);
		mainTable.addCell(infoCell);

		PdfPTable learnedSkillsTable = LearnedSkillsTable.getSkillsTable(getCharacterPlayer(), getLanguage());
		PdfPCell learnedSkillsCell = new PdfPCell(learnedSkillsTable);
		learnedSkillsCell.setColspan(2);
		learnedSkillsCell.setRowspan(3);
		mainTable.addCell(learnedSkillsCell);

		PdfPTable basicTable = new PdfPTable(new float[] { 5f, 4f });
		BaseElement.setTablePropierties(basicTable);
		basicTable.getDefaultCell().setBorder(0);

		PdfPTable characteristicsTable = CharacteristicsTableFactory.getCharacteristicsBasicsTable(getCharacterPlayer());
		PdfPCell characteristicCell = new PdfPCell(characteristicsTable);
		basicTable.addCell(characteristicCell);

		PdfPTable naturalSkillsTable = NaturalSkillsTable.getSkillsTable(getCharacterPlayer(), getLanguage());
		PdfPCell naturalSkillsCell = new PdfPCell(naturalSkillsTable);
		basicTable.addCell(naturalSkillsCell);

		mainTable.addCell(basicTable);

		PdfPTable composedTable = new PdfPTable(new float[] { 5f, 2f });

		PdfPTable blessingsTable = new BlessingTable(getCharacterPlayer());
		PdfPCell blessingsCell = new PdfPCell(blessingsTable);
		composedTable.addCell(blessingsCell);
		
		PdfPTable beneficesTable = new BeneficesTable(getCharacterPlayer());
		PdfPCell beneficesCell = new PdfPCell(beneficesTable);
		composedTable.addCell(beneficesCell);

		PdfPCell composedCell = new PdfPCell(composedTable);
		composedCell.setRowspan(2);
		mainTable.addCell(composedCell);

		PdfPTable armourTable = new ArmourTable(getCharacterPlayer());
		PdfPCell armourCell = new PdfPCell(armourTable);
		mainTable.addCell(armourCell);

		// PdfPCell victoryPointsCell = new PdfPCell(new VictoryPointsTable());
		// victoryPointsCell.setPadding(0);
		// victoryPointsCell.setRowspan(2);
		// mainTable.addCell(victoryPointsCell);

		document.add(mainTable);
	}

	@Override
	protected void addDocumentWriterEvents(PdfWriter writer) {
		writer.setPageEvent(new SheetBackgroundEvent());
	}
}
