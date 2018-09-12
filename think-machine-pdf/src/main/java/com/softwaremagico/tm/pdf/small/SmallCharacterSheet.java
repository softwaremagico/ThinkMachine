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

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.softwaremagico.tm.character.CharacterPlayer;
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
	protected void addEvent(PdfWriter writer) {
		super.addEvent(writer);
		writer.setPageEvent(new SheetBackgroundEvent());
	}

	@Override
	protected void createPagePDF(Document document) throws Exception {
		float[] widths = { 2f, 1f };
		PdfPTable mainTable = new PdfPTable(widths);
		BaseElement.setTablePropierties(mainTable);
		mainTable.getDefaultCell().setPadding(0);

		PdfPTable infoTable = CharacterBasicsReducedTableFactory.getCharacterBasicsTable(getCharacterPlayer());
		PdfPCell infoCell = new PdfPCell(infoTable);
		infoCell.setBorderWidthTop(0);
		infoCell.setBorderWidthLeft(0);
		infoCell.setBorderWidthBottom(1);
		mainTable.addCell(infoCell);

		PdfPTable learnedSkillsTable = LearnedSkillsTable.getSkillsTable(getCharacterPlayer(), getLanguage());
		PdfPCell learnedSkillsCell = new PdfPCell(learnedSkillsTable);
		learnedSkillsCell.setColspan(2);
		learnedSkillsCell.setRowspan(3);
		learnedSkillsCell.setBorderWidthTop(0);
		learnedSkillsCell.setBorderWidthRight(0);
		mainTable.addCell(learnedSkillsCell);

		PdfPTable basicTable = new PdfPTable(new float[] { 5f, 4f });
		BaseElement.setTablePropierties(basicTable);
		basicTable.getDefaultCell().setBorder(0);

		PdfPTable characteristicsTable = CharacteristicsTableFactory
				.getCharacteristicsBasicsTable(getCharacterPlayer());
		PdfPCell characteristicCell = new PdfPCell(characteristicsTable);
		characteristicCell.setBorderWidthLeft(0);
		basicTable.addCell(characteristicCell);

		PdfPTable naturalSkillsTable = NaturalSkillsTable.getSkillsTable(getCharacterPlayer(), getLanguage());
		PdfPCell naturalSkillsCell = new PdfPCell(naturalSkillsTable);
		naturalSkillsCell.setBorderWidthRight(0);
		basicTable.addCell(naturalSkillsCell);

		PdfPCell basicComposedCell = new PdfPCell(basicTable);
		basicComposedCell.setBorder(0);
		mainTable.addCell(basicComposedCell);

		PdfPTable composedTable = new PdfPTable(new float[] { 5f, 2f });

		PdfPTable blessingsTable = new BlessingTable(getCharacterPlayer());
		PdfPCell blessingsCell = new PdfPCell(blessingsTable);
		blessingsCell.setBorderWidthLeft(0);
		blessingsCell.setBorderWidthBottom(1);
		composedTable.addCell(blessingsCell);

		PdfPTable beneficesTable = new BeneficesTable(getCharacterPlayer());
		PdfPCell beneficesCell = new PdfPCell(beneficesTable);
		beneficesCell.setBorderWidthBottom(1);
		composedTable.addCell(beneficesCell);

		PdfPCell composedCell = new PdfPCell(composedTable);
		composedCell.setRowspan(2);
		composedCell.setBorder(0);
		mainTable.addCell(composedCell);

		PdfPTable armourTable = new ArmourTable(getCharacterPlayer());
		PdfPCell armourCell = new PdfPCell(armourTable);
		armourCell.setBorderWidthRight(0);
		armourCell.setBorderWidthBottom(1);
		mainTable.addCell(armourCell);

		PdfPTable fightTable = new PdfPTable(new float[] { 3f, 5f, 1f });

		if (characterPlayer==null  || (characterPlayer.getSelectedPowers().isEmpty()
				&& !characterPlayer.getCybernetics().getElements().isEmpty())) {
			PdfPTable cyberneticsTable = new CyberneticsTable(getCharacterPlayer());
			PdfPCell cyberneticsCell = new PdfPCell(cyberneticsTable);
			cyberneticsCell.setBorderWidthLeft(0);
			fightTable.addCell(cyberneticsCell);
		} else {
			PdfPTable occultismTable = new OccultismTable(getCharacterPlayer(), getLanguage());
			PdfPCell occultismCell = new PdfPCell(occultismTable);
			occultismCell.setBorderWidthLeft(0);
			fightTable.addCell(occultismCell);
		}

		PdfPTable weaponsTable = new WeaponsTable(getCharacterPlayer());
		fightTable.addCell(weaponsTable);

		PdfPCell victoryPointsCell = new PdfPCell(new VerticalVictoryPointsTable());
		victoryPointsCell.setPadding(0);
		victoryPointsCell.setRowspan(3);
		fightTable.addCell(victoryPointsCell);

		PdfPTable vitalityTable = new VitalityTable(getCharacterPlayer());
		PdfPCell vitalityCell = new PdfPCell(vitalityTable);
		vitalityCell.setColspan(2);
		vitalityCell.setBorderWidth(1);
		fightTable.addCell(vitalityCell);

		PdfPTable wyrdTable = new WyrdTable(getCharacterPlayer());
		PdfPCell wyrdCell = new PdfPCell(wyrdTable);
		wyrdCell.setBorderWidth(1);
		wyrdCell.setColspan(2);
		fightTable.addCell(wyrdCell);

		PdfPCell fightCell = new PdfPCell(fightTable);
		fightCell.setBorder(0);
		fightCell.setColspan(2);

		mainTable.addCell(fightCell);
		document.add(mainTable);
	}

	@Override
	protected void addDocumentWriterEvents(PdfWriter writer) {
		writer.setPageEvent(new SheetBackgroundEvent());
	}
}
