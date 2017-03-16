package com.softwaremagico.tm.smallpdf.counters;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.FadingSunsTheme;

public class VitalityTable extends CounterTable {

	public VitalityTable(CharacterPlayer characterPlayer) {
		super(characterPlayer);

		getDefaultCell().setBorder(0);

		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTER_VITALITY_TITLE_FONT_SIZE);
		Phrase content = new Phrase(getTranslator().getTranslatedText("vitality"), font);
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
	protected PdfPCell createCircle() {
		if (addedCircle >= 5) {
			return super.createCircle();
		}

		PdfPCell cell = createValue("-" + (10 - addedCircle * 2), new Font(FadingSunsTheme.getLineFontBold(),
				FadingSunsTheme.CHARACTER_VITALITY_PENALTIES_TITLE_FONT_SIZE), Element.ALIGN_MIDDLE);
		cell.setPaddingTop(1f);
		cell.setPaddingRight(-2f);
		return cell;
	}

	@Override
	protected int getSelectedValue() {
		if (getCharacterPlayer() != null) {
			return getCharacterPlayer().getVitalityValue().intValue();
		}
		return -1;
	}
}
