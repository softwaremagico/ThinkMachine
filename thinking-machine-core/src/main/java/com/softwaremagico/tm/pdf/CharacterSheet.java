package com.softwaremagico.tm.pdf;

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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.pdf.characteristics.CharacteristicsTableFactory;
import com.softwaremagico.tm.pdf.cybernetics.CyberneticsTable;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.fighting.FightingManeuvers;
import com.softwaremagico.tm.pdf.fighting.WeaponsAndArmours;
import com.softwaremagico.tm.pdf.info.CharacterBasicsTableFactory;
import com.softwaremagico.tm.pdf.info.DescriptionTable;
import com.softwaremagico.tm.pdf.info.PropertiesTable;
import com.softwaremagico.tm.pdf.others.AnnotationsTable;
import com.softwaremagico.tm.pdf.others.OthersTable;
import com.softwaremagico.tm.pdf.skills.MainSkillsTableFactory;
import com.softwaremagico.tm.pdf.skills.occultism.OccultismsPowerTable;
import com.softwaremagico.tm.pdf.traits.MainPerksTableFactory;

public class CharacterSheet extends PdfDocument {
	private final static float[] REAR_TABLE_WIDTHS = { 1f, 1f, 1f };
	private CharacterPlayer characterPlayer = null;

	public CharacterSheet() {
		this(Translator.DEFAULT_LANGUAGE);
	}

	public CharacterSheet(String language) {
		super(language);
		Translator.setLanguage(language);
	}

	public CharacterSheet(CharacterPlayer characterPlayer) {
		this(characterPlayer.getLanguage());
		this.characterPlayer = characterPlayer;
	}

	@Override
	protected Rectangle getPageSize() {
		return PageSize.A4;
	}

	@Override
	protected void createPagePDF(Document document) throws Exception {
		// addBackGroundImage(document, Path.returnBackgroundPath(), writer);
		PdfPTable mainTable = CharacterBasicsTableFactory.getCharacterBasicsTable(getCharacterPlayer());
		document.add(mainTable);
		PdfPTable characteristicsTable = CharacteristicsTableFactory.getCharacterBasicsTable(getCharacterPlayer());
		document.add(characteristicsTable);
		PdfPTable skillsTable = MainSkillsTableFactory.getSkillsTable(getCharacterPlayer(), getLanguage());
		document.add(skillsTable);
		PdfPTable perksTable = MainPerksTableFactory.getPerksTable(getCharacterPlayer());
		document.add(perksTable);
		document.newPage();
		document.add(createRearTable());
		document.add(FightingManeuvers.getFightingManoeuvresTable(getCharacterPlayer()));
		document.add(WeaponsAndArmours.getWeaponsAndArmoursTable(getCharacterPlayer()));

		document.newPage();
	}

	private PdfPTable createRearTable() {
		PdfPTable mainTable = new PdfPTable(REAR_TABLE_WIDTHS);
		mainTable.getDefaultCell().setBorder(0);
		mainTable.setWidthPercentage(100);

		mainTable.addCell(new DescriptionTable(getCharacterPlayer()));
		PdfPCell cell = new PdfPCell(new AnnotationsTable());
		cell.setColspan(2);
		mainTable.addCell(cell);

		PdfPCell blackSeparator = BaseElement.createBigSeparator(90);
		mainTable.addCell(blackSeparator);

		PdfPCell separatorCell = new PdfPCell(BaseElement.createWhiteSeparator());
		separatorCell.setColspan(2);
		mainTable.addCell(separatorCell);

		mainTable.addCell(new PropertiesTable());

		PdfPCell psiCell = new PdfPCell(new OccultismsPowerTable(getCharacterPlayer()));
		psiCell.setColspan(2);
		mainTable.addCell(psiCell);

		mainTable.addCell(BaseElement.createBigSeparator(90));

		mainTable.addCell(separatorCell);

		PdfPTable othersTable = new OthersTable();
		mainTable.addCell(othersTable);

		PdfPCell cyberneticsCell = new PdfPCell(new CyberneticsTable(getCharacterPlayer()));
		cyberneticsCell.setColspan(2);
		mainTable.addCell(cyberneticsCell);

		return mainTable;
	}

	public CharacterPlayer getCharacterPlayer() {
		return characterPlayer;
	}

}
