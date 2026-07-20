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

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.small.info.DescriptionTableFactory;

public class SmallCharacterSheetWithDescriptions extends SmallCharacterSheet {
	private CharacterPlayer characterPlayer = null;
	private PdfPTable mainTable;

	public SmallCharacterSheetWithDescriptions(String language, String moduleName) {
		super(language, moduleName);
		Translator.setLanguage(language);
	}

	public SmallCharacterSheetWithDescriptions(CharacterPlayer characterPlayer) {
		this(characterPlayer.getLanguage(), characterPlayer.getModuleName());
		this.characterPlayer = characterPlayer;
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
		mainTable.getDefaultCell().setBorderWidth(0);
		mainTable.getDefaultCell().setPadding(0);
	}

	@Override
	protected void createContent(Document document) throws DocumentException, InvalidXmlElementException {
		initializeTableContent();
		createCharacterPDF(document, characterPlayer);
		createDescriptionPage(characterPlayer);
		document.add(mainTable);
	}

	@Override
	protected void createCharacterPDF(Document document, CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		mainTable.addCell(createCharacterContent(characterPlayer));
	}

	private void createDescriptionPage(CharacterPlayer characterPlayer) {
		mainTable.addCell(DescriptionTableFactory.getDescriptionTable(characterPlayer, getLanguage(), getModuleName()));
	}
}
