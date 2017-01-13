package com.softwaremagico.tm.pdf.info;

import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class DescriptionTable extends VerticalTable {
	private final static float[] WIDTHS = { 1f };

	public DescriptionTable() {
		super(WIDTHS);

		addCell(createTitle("Descripción"));
		addCell(createLine("Fecha de Nacimiento ________"));
		addCell(createLine("Cabello ______________"));
		addCell(createLine("Ojos _________________"));
		addCell(createLine("Complexión ______________"));
		addCell(createLine("Estatura ____________"));
		addCell(createLine("Peso ____________"));
		addCell(createLine("Imagen ____________"));
		addCell(createLine("_______________________"));
		addCell(createLine("_______________________"));
		addCell(createLine("_______________________"));
		addCell(createLine("_______________________"));
		addCell(createLine("_______________________"));
	}
}
