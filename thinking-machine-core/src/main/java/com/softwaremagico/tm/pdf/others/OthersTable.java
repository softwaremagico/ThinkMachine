package com.softwaremagico.tm.pdf.others;

import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class OthersTable extends VerticalTable {
	private final static int EMPTY_ROWS = 7;
	private final static float[] WIDTHS = { 1f };

	public OthersTable() {
		super(WIDTHS);
		getDefaultCell().setPaddingRight(20);
		addCell(createTitle("Otros"));

		for (int i = 0; i < EMPTY_ROWS; i++) {
			addCell(createElementLine("_______________________________________________"));
		}
	}
}
