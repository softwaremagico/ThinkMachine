package com.softwaremagico.tm.pdf.skills;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.softwaremagico.tm.pdf.FadingSunsTheme;

public class VitalityTable extends CounterTable {
	private final static float[] WIDTHS = { 1f, 1f };
	private final static int CIRCLES = 23;
	private final static int TITLE_SPAN = 5;
	private final static int MODIFICATORS_SPAN = 5;

	public VitalityTable() {
		super(WIDTHS);
		addCell(createVerticalTitle("Vitalidad", TITLE_SPAN));
		for (int i = 0; i < TITLE_SPAN; i++) {
			addCell(createCircle());
		}
		addCell(space(CIRCLES - TITLE_SPAN - MODIFICATORS_SPAN));
		for (int i = 0; i < CIRCLES - TITLE_SPAN - MODIFICATORS_SPAN; i++) {
			addCell(createCircle());
		}

		for (int i = 1; i <= MODIFICATORS_SPAN; i++) {
			addCell(createValue("-" + (i * 2), new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE - 6),
					Element.ALIGN_MIDDLE));
			addCell(createCircle());
		}
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE;
	}

}
