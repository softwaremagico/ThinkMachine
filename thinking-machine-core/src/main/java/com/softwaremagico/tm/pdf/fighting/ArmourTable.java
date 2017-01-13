package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.VerticalHeaderPdfPTable;

public class ArmourTable extends VerticalHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f };
	private final static int ROWS = 8;

	public ArmourTable() {
		super(WIDTHS);
		addCell(createVerticalTitle("Armadura", ROWS + 1));
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.ARMOUR_TITLE_FONT_SIZE;
	}

}
