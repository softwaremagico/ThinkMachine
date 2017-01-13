package com.softwaremagico.tm.pdf.characteristics;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class CharacteristicsTableFactory extends BaseElement {
	private final static String GAP = "   ";

	public static PdfPTable getCharacterBasicsTable() {
		float[] widths = { 1f, 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		PdfPCell separator = createSeparator();
		separator.setColspan(widths.length);
		table.addCell(separator);

		Phrase content = new Phrase("CARACTERÍSTICAS", new Font(FadingSunsTheme.getTitleFont(),
				FadingSunsTheme.TITLE_FONT_SIZE));
		PdfPCell titleCell = new PdfPCell(content);
		setCellProperties(titleCell);
		titleCell.setColspan(widths.length);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setFixedHeight(30);
		table.addCell(titleCell);
		table.getDefaultCell().setPadding(0);

		table.addCell(new CharacteristicColumn("Cuerpo", new String[] { "Fuerza (" + GAP + ")",
				"Destreza (" + GAP + ")", "Fortaleza (" + GAP + ")" }));
		table.addCell(new CharacteristicColumn("Mente", new String[] { "Astucia (" + GAP + ")",
				"Percepción (" + GAP + ")", "Tecnología (" + GAP + ")" }));
		table.addCell(new CharacteristicColumn("Espíritu", new String[] { "Presencia (" + GAP + ")",
				"Voluntad (" + GAP + ")", "Fe (" + GAP + ")" }));
		table.addCell(new CharacteristicColumn("Otras", new String[] { "Iniciativa",
				"Movimiento (" + GAP + ")", "Defensa (1)" }));

		return table;
	}

}
