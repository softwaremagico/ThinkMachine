package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.pdf.elements.VerticalHeaderPdfPTable;

public class FightingTable extends VerticalHeaderPdfPTable {
	private final static int ROW_WIDTH = 60;
	private final static float[] widths = { 1f, 5f };

	public FightingTable() {
		super(widths);
	}

	@Override
	protected int getTitleFontSize() {
		// TODO Auto-generated method stub
		return 0;
	}
}
