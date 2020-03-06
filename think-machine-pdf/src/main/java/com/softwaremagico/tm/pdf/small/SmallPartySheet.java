package com.softwaremagico.tm.pdf.small;

/*-
 * #%L
 * Think Machine (PDF Sheets)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.party.Party;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.events.PartyFooterEvent;
import com.softwaremagico.tm.pdf.small.info.DescriptionTableFactory;

public class SmallPartySheet extends SmallCharacterSheetWithDescriptions {
	private Party party;
	private PdfPTable mainTable;

	public SmallPartySheet(String language, String moduleName) {
		super(language, moduleName);
		Translator.setLanguage(language);
	}

	public SmallPartySheet(Party party) {
		this(party.getLanguage(), party.getModuleName());
		this.party = party;
	}

	@Override
	protected Rectangle getPageSize() {
		return PageSize.A4.rotate();
	}

	private void initializeTableContent() {
		final float[] widths = { 1f, 1f };
		mainTable = new PdfPTable(widths);
		BaseElement.setTablePropierties(mainTable);
		mainTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
		mainTable.getDefaultCell().setBorderWidth(2);
		mainTable.getDefaultCell().setPadding(0);
	}

	@Override
	protected void createContent(Document document) throws DocumentException, InvalidXmlElementException  {
		initializeTableContent();
		boolean hasDescription = false;
		for (final CharacterPlayer characterPlayer : party.getMembers()) {
			if ((characterPlayer.getInfo().getBackgroundDecription() != null && !characterPlayer.getInfo()
					.getBackgroundDecription().isEmpty())
					|| (characterPlayer.getInfo().getCharacterDescription() != null && !characterPlayer.getInfo()
							.getCharacterDescription().isEmpty())) {
				hasDescription = true;
				break;
			}
		}
		for (final CharacterPlayer characterPlayer : party.getMembers()) {
			createCharacterPDF(document, characterPlayer);
			if (hasDescription) {
				createDescriptionPage(characterPlayer);
			}
		}
		if (party.getMembers().size() % 2 > 0 && !hasDescription) {
			mainTable.addCell(new PdfPCell());
		}
		document.add(mainTable);
	}

	@Override
	protected void createCharacterPDF(Document document, CharacterPlayer characterPlayer) throws InvalidXmlElementException  {
		mainTable.addCell(createCharacterContent(characterPlayer));
	}

	private void createDescriptionPage(CharacterPlayer characterPlayer) {
		mainTable.addCell(DescriptionTableFactory.getDescriptionTable(characterPlayer, getLanguage(), getModuleName()));
	}

	@Override
	protected void addEvent(PdfWriter writer) {
		writer.setPageEvent(new PartyFooterEvent(party, -15));
	}

}
