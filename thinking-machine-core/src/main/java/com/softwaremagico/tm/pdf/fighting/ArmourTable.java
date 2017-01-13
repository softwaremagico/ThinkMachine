package com.softwaremagico.tm.pdf.fighting;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class ArmourTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 0.75f, 1f, 1f, 1f };
	private final static int ROWS = 5;

	public ArmourTable() {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createLateralVerticalTitle("Armadura", ROWS + 1));

		PdfPCell nameCell = createElementLine("___________________");
		nameCell.setColspan(WIDTHS.length);
		nameCell.setMinimumHeight(20);
		addCell(nameCell);
		PdfPCell protectionCell = createElementLine("Protección: ____ d");
		protectionCell.setColspan(WIDTHS.length);
		addCell(protectionCell);
		
		PdfPCell malusCell = createElementLine("Fr: __  Ds: __  In: __");
		malusCell.setColspan(WIDTHS.length);
		addCell(malusCell);

		addCell(getArmourProperty("D"));
		addCell(getArmourProperty("F"));
		addCell(getArmourProperty("L"));
		addCell(getArmourProperty("P"));
		addCell(getArmourProperty("I"));
		addCell(getArmourProperty("E"));

	}

	private PdfPTable getArmourProperty(String text) {
		float[] widths = { 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setPadding(0);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		table.addCell(createRectangle());
		table.addCell(createElementLine(text));

		return table;
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.ARMOUR_TITLE_FONT_SIZE;
	}

}
