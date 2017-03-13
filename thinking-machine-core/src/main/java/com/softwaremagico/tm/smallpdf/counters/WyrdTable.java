package com.softwaremagico.tm.smallpdf.counters;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.FadingSunsTheme;

public class WyrdTable extends CounterTable {

	public WyrdTable(CharacterPlayer characterPlayer) {
		super(characterPlayer);

		getDefaultCell().setBorder(0);

		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTER_VITALITY_TITLE_FONT_SIZE);
		Phrase content = new Phrase(getTranslator().getTranslatedText("wyrd"), font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setBorder(0);
		titleCell.setPaddingTop(paddingTop);
		titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		addCell(titleCell);

		addedCircle = 0;

		for (int i = 0; i < getNumberOfCircles(); i++) {
			addCell(getCircle());
			addedCircle++;
		}

	}

	@Override
	protected int getSelectedValue() {
		if (getCharacterPlayer() != null) {
			return getCharacterPlayer().getWyrdValue().intValue();
		}
		return -1;
	}
}
