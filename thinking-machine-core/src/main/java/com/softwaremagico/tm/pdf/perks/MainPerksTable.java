package com.softwaremagico.tm.pdf.perks;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class MainPerksTable extends BaseElement {
	protected final static int EMPTY_ROWS = 8;
	public final static int PADDING = 2;

	public static PdfPTable getPerksTable() {
		float[] widths = { 4f, 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		PdfPCell separator = createSeparator();
		separator.setPadding(PADDING);
		table.addCell(separator);
		table.addCell(separator);
		table.addCell(separator);

		PdfPCell blessingCell = new PdfPCell(new BlessingTable());
		blessingCell.setPadding(0);
		blessingCell.setBorder(0);
		table.addCell(blessingCell);

		PdfPCell perksCell = new PdfPCell(new PerksTable());
		perksCell.setPadding(0);
		perksCell.setBorder(0);
		table.addCell(perksCell);

		PdfPCell victoryPointsCell = new PdfPCell(new VictoryPointsTable());
		victoryPointsCell.setPadding(0);
		//victoryPointsCell.setBorder();
		table.addCell(victoryPointsCell);

		return table;
	}

}
