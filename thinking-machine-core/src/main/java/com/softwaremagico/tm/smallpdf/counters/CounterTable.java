package com.softwaremagico.tm.smallpdf.counters;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.CellCompleteBoxEvent;
import com.softwaremagico.tm.pdf.elements.CellCompleteBoxEvent.Border;
import com.softwaremagico.tm.pdf.elements.CustomPdfTable;

public abstract class CounterTable extends CustomPdfTable {
	protected final static float[] WIDTHS = { 3f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f };
	protected int addedCircle = 0;
	// http://stackoverflow.com/questions/5554553/itext-pdftable-cell-vertical-alignment
	protected float paddingTop = -3f;

	private CharacterPlayer characterPlayer;

	public CounterTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		this.characterPlayer = characterPlayer;
	}

	protected int getNumberOfCircles() {
		// Title + circles.
		return WIDTHS.length - 1;
	}

	protected PdfPCell getCircle() {
		if (characterPlayer == null) {
			return createCircle();
		}
		if (addedCircle == getSelectedValue()) {
			PdfPCell cell = createCircle();
			cell.setCellEvent(new CellCompleteBoxEvent(1, new Border[] { Border.TOP, Border.BOTTOM, Border.RIGHT }));
			return cell;
		} else if (addedCircle == 0) {
			PdfPCell cell = createCircle();
			cell.setCellEvent(new CellCompleteBoxEvent(1, new Border[] { Border.TOP, Border.BOTTOM, Border.LEFT }));
			return cell;
		} else if (addedCircle < getSelectedValue()) {
			PdfPCell cell = createCircle();
			cell.setCellEvent(new CellCompleteBoxEvent(1, new Border[] { Border.TOP, Border.BOTTOM }));
			return cell;
		} else {
			return createCircle();
		}
	}

	protected PdfPCell createCircle() {
		PdfPCell cell = createValue("O", new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE), Element.ALIGN_MIDDLE);
		return cell;
	}

	protected PdfPCell createValue(String text, Font font, int alignment) {
		Phrase content = new Phrase(text, font);
		PdfPCell circleCell = new PdfPCell(content);
		// Not putting correctly the "o" at the center of the cell.
		// http://stackoverflow.com/questions/5554553/itext-pdftable-cell-vertical-alignment
		circleCell.setPaddingTop(paddingTop);
		circleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		circleCell.setVerticalAlignment(alignment);
		circleCell.setBorder(0);
		circleCell.setMinimumHeight(20);
		return circleCell;
	}

	protected abstract int getSelectedValue();

	public CharacterPlayer getCharacterPlayer() {
		return characterPlayer;
	}

}
