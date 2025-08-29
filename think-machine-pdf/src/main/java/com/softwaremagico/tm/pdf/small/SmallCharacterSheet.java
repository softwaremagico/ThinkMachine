package com.softwaremagico.tm.pdf.small;

/*-
 * #%L
 * Think Machine (Core)
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

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.pdf.complete.PdfDocument;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.events.SheetBackgroundEvent;
import com.softwaremagico.tm.pdf.small.characteristics.CharacteristicsTableFactory;
import com.softwaremagico.tm.pdf.small.counters.VitalityTable;
import com.softwaremagico.tm.pdf.small.counters.WyrdTable;
import com.softwaremagico.tm.pdf.small.cybernetics.CyberneticsTable;
import com.softwaremagico.tm.pdf.small.fighting.ArmourTable;
import com.softwaremagico.tm.pdf.small.fighting.WeaponsTable;
import com.softwaremagico.tm.pdf.small.fighting.WeaponsTableLong;
import com.softwaremagico.tm.pdf.small.info.CharacterBasicsReducedTableFactory;
import com.softwaremagico.tm.pdf.small.occultism.OccultismTable;
import com.softwaremagico.tm.pdf.small.skills.LearnedSkillsTable;
import com.softwaremagico.tm.pdf.small.skills.NaturalSkillsTable;
import com.softwaremagico.tm.pdf.small.traits.BeneficesTable;
import com.softwaremagico.tm.pdf.small.traits.BlessingTable;
import com.softwaremagico.tm.pdf.small.victorytable.VerticalVictoryPointsTable;

public class SmallCharacterSheet extends PdfDocument {
	private CharacterPlayer characterPlayer = null;

	public SmallCharacterSheet() {
		this(Translator.DEFAULT_LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
	}

	public SmallCharacterSheet(String language, String moduleName) {
		super(language, moduleName);
		Translator.setLanguage(language);
	}

	public SmallCharacterSheet(CharacterPlayer characterPlayer) {
		this(characterPlayer.getLanguage(), characterPlayer.getModuleName());
		this.characterPlayer = characterPlayer;
	}

	@Override
	protected void createContent(Document document) throws DocumentException, InvalidXmlElementException {
		createCharacterPDF(document, getCharacterPlayer());
	}

	@Override
	protected Rectangle getPageSize() {
		return PageSize.A5;
	}

	private CharacterPlayer getCharacterPlayer() {
		return characterPlayer;
	}

	@Override
	protected void addEvent(PdfWriter writer) {
		super.addEvent(writer);
		writer.setPageEvent(new SheetBackgroundEvent());
	}

	protected PdfPTable createCharacterContent(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		final float[] widths = { 2.2f, 1f };
		final PdfPTable mainTable = new PdfPTable(widths);
		BaseElement.setTablePropierties(mainTable);
		mainTable.getDefaultCell().setPadding(0);

		final PdfPTable infoTable = CharacterBasicsReducedTableFactory.getCharacterBasicsTable(characterPlayer);
		final PdfPCell infoCell = new PdfPCell(infoTable);
		infoCell.setBorderWidthTop(0);
		infoCell.setBorderWidthLeft(0);
		infoCell.setBorderWidthBottom(1);
		mainTable.addCell(infoCell);

		final PdfPTable learnedSkillsTable = LearnedSkillsTable.getSkillsTable(characterPlayer);
		final PdfPCell learnedSkillsCell = new PdfPCell(learnedSkillsTable);
		learnedSkillsCell.setColspan(2);
		learnedSkillsCell.setRowspan(3);
		learnedSkillsCell.setBorderWidthTop(0);
		learnedSkillsCell.setBorderWidthRight(0);
		mainTable.addCell(learnedSkillsCell);

		final PdfPTable basicTable = new PdfPTable(new float[] { 5f, 4f });
		BaseElement.setTablePropierties(basicTable);
		basicTable.getDefaultCell().setBorder(0);

		final PdfPTable characteristicsTable = CharacteristicsTableFactory.getCharacteristicsBasicsTable(characterPlayer, getModuleName());
		final PdfPCell characteristicCell = new PdfPCell(characteristicsTable);
		characteristicCell.setBorderWidthLeft(0);
		basicTable.addCell(characteristicCell);

		final PdfPTable naturalSkillsTable = NaturalSkillsTable.getSkillsTable(characterPlayer, getLanguage(), getModuleName());
		final PdfPCell naturalSkillsCell = new PdfPCell(naturalSkillsTable);
		naturalSkillsCell.setBorderWidthRight(0);
		basicTable.addCell(naturalSkillsCell);

		final PdfPCell basicComposedCell = new PdfPCell(basicTable);
		basicComposedCell.setBorder(0);
		mainTable.addCell(basicComposedCell);

		final PdfPTable composedTable = new PdfPTable(new float[] { 5f, 2f });

		final PdfPTable blessingsTable = new BlessingTable(characterPlayer);
		final PdfPCell blessingsCell = new PdfPCell(blessingsTable);
		blessingsCell.setBorderWidthLeft(0);
		blessingsCell.setBorderWidthBottom(1);
		composedTable.addCell(blessingsCell);

		final PdfPTable beneficesTable = new BeneficesTable(characterPlayer);
		final PdfPCell beneficesCell = new PdfPCell(beneficesTable);
		beneficesCell.setBorderWidthBottom(1);
		composedTable.addCell(beneficesCell);

		final PdfPCell composedCell = new PdfPCell(composedTable);
		composedCell.setRowspan(2);
		composedCell.setBorder(0);
		mainTable.addCell(composedCell);

		final PdfPTable armourTable = new ArmourTable(characterPlayer);
		final PdfPCell armourCell = new PdfPCell(armourTable);
		armourCell.setBorderWidthRight(0);
		armourCell.setBorderWidthBottom(1);
		mainTable.addCell(armourCell);

		final PdfPTable fightTable = new PdfPTable(new float[] { 3f, 5f, 1f });

		// Only weapons table.
		if (characterPlayer != null && (characterPlayer.getSelectedPowers().isEmpty() && characterPlayer.getCybernetics().isEmpty())) {
			final PdfPTable weaponsTable = new WeaponsTableLong(characterPlayer);
			final PdfPCell weaponsCell = new PdfPCell(weaponsTable);
			weaponsCell.setBorderWidthLeft(0);
			weaponsCell.setColspan(2);
			fightTable.addCell(weaponsCell);
		} else {
			// Include cybernetics
			if (characterPlayer != null && (characterPlayer.getSelectedPowers().isEmpty() && !characterPlayer.getCybernetics().isEmpty())) {
				final PdfPTable cyberneticsTable = new CyberneticsTable(characterPlayer);
				final PdfPCell cyberneticsCell = new PdfPCell(cyberneticsTable);
				cyberneticsCell.setBorderWidthLeft(0);
				fightTable.addCell(cyberneticsCell);
				// Include occultism
			} else {
				final PdfPTable occultismTable = new OccultismTable(characterPlayer, getLanguage(), getModuleName());
				final PdfPCell occultismCell = new PdfPCell(occultismTable);
				occultismCell.setBorderWidthLeft(0);
				fightTable.addCell(occultismCell);
			}

			final PdfPTable weaponsTable = new WeaponsTable(characterPlayer);
			fightTable.addCell(weaponsTable);
		}

		final PdfPCell victoryPointsCell = new PdfPCell(new VerticalVictoryPointsTable());
		victoryPointsCell.setPadding(0);
		victoryPointsCell.setRowspan(3);
		fightTable.addCell(victoryPointsCell);

		final PdfPTable vitalityTable = new VitalityTable(characterPlayer);
		final PdfPCell vitalityCell = new PdfPCell(vitalityTable);
		vitalityCell.setColspan(2);
		vitalityCell.setBorderWidth(1);
		fightTable.addCell(vitalityCell);

		final PdfPTable wyrdTable = new WyrdTable(characterPlayer);
		final PdfPCell wyrdCell = new PdfPCell(wyrdTable);
		wyrdCell.setBorderWidth(1);
		wyrdCell.setColspan(2);
		fightTable.addCell(wyrdCell);

		final PdfPCell fightCell = new PdfPCell(fightTable);
		fightCell.setBorder(0);
		fightCell.setColspan(2);

		mainTable.addCell(fightCell);
		return mainTable;
	}

	@Override
	protected void createCharacterPDF(Document document, CharacterPlayer characterPlayer) throws DocumentException, InvalidXmlElementException {
		document.add(createCharacterContent(characterPlayer));
	}

	@Override
	protected void addDocumentWriterEvents(PdfWriter writer) {
		writer.setPageEvent(new SheetBackgroundEvent());
	}
}
