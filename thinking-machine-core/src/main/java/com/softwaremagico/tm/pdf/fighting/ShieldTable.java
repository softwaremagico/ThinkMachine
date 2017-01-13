package com.softwaremagico.tm.pdf.fighting;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class ShieldTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f, 4f };
	private final static int ROWS = 4;

	public ShieldTable() {
		super(WIDTHS);
		getDefaultCell().setBorder(0);
		
		
		addCell(createLateralVerticalTitle("Escudos", ROWS + 1));

		PdfPCell nameCell = createElementLine("___________________");
		nameCell.setColspan(WIDTHS.length);
		nameCell.setMinimumHeight(20);
		addCell(nameCell);

		addCell(getShieldRange());
		addCell(createElementLine("Impactos: _________"));
		addCell(createElementLine("___________________"));
		addCell(createElementLine("___________________"));
	}

	private PdfPTable getShieldRange() {
		float[] widths = { 1f, 1f, 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setPadding(0);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		table.addCell(createElementLine("("));
		table.addCell(createRectangle());
		table.addCell(createElementLine("/"));
		table.addCell(createRectangle());
		table.addCell(createElementLine(")"));

		return table;
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.ARMOUR_TITLE_FONT_SIZE;
	}

}
