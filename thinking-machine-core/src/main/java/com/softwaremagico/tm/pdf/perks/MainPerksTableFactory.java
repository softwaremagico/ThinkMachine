package com.softwaremagico.tm.pdf.perks;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class MainPerksTableFactory extends BaseElement {
	protected final static int EMPTY_ROWS = 8;
	public final static int PADDING = 2;

	public static PdfPTable getPerksTable() {
		float[] widths = { 4f, 0.1f, 4f, 0.1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setPadding(PADDING);

		PdfPCell whiteSeparator = createWhiteSeparator();
		whiteSeparator.setColspan(widths.length);
		table.addCell(whiteSeparator);

		PdfPCell blackSeparator = createBlackSeparator();
		whiteSeparator.setColspan(1);
		table.addCell(blackSeparator);
		table.addCell(whiteSeparator);
		table.addCell(blackSeparator);
		table.addCell(whiteSeparator);

		PdfPCell victoryPointsCell = new PdfPCell(new VictoryPointsTable());
		victoryPointsCell.setPadding(0);
		victoryPointsCell.setRowspan(2);
		table.addCell(victoryPointsCell);

		PdfPCell blessingCell = new PdfPCell(new BlessingTable());
		blessingCell.setPadding(0);
		blessingCell.setBorder(0);
		table.addCell(blessingCell);
		
		table.addCell(whiteSeparator);

		PdfPCell perksCell = new PdfPCell(new BenefitsTable());
		perksCell.setPadding(0);
		perksCell.setBorder(0);
		table.addCell(perksCell);
		
		table.addCell(whiteSeparator);

		return table;
	}

}
