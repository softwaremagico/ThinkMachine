package com.softwaremagico.tm.pdf.info;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public abstract class CharacterBasicsTableFactory extends BaseElement {
	private static ITranslator translator = LanguagePool.getTranslator("character_values.xml");

	protected final static String LINE = "_______________";
	protected final static String LANGUAGE_PREFIX = "info";
	protected final static int MAX_VALUE_LENGTH = 13;

	protected static PdfPCell createField(CharacterPlayer characterPlayer, String tag, int fontSize) {
		float[] widths = { 0.7f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		table.addCell(getCell(getTranslatedTag(tag), Element.ALIGN_RIGHT, fontSize));
		if (characterPlayer == null) {
			table.addCell(getCell(LINE, Element.ALIGN_LEFT, fontSize));
		} else {
			if (tag.equals("race")) {
				String raceValue = translator.getTranslatedText(translator.convertToXmlTag(characterPlayer.getRace().getName()));
				table.addCell(getHandwrittingCell(raceValue != null ? raceValue : characterPlayer.getRace().getName(), Element.ALIGN_LEFT, fontSize - 1));
			} else {
				table.addCell(getHandwrittingCell(characterPlayer.getInfo().getTranslatedParameter(tag), Element.ALIGN_LEFT, fontSize - 1));
			}
		}

		PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		setCellProperties(cell);

		return cell;
	}

	protected static PdfPCell getCell(String text, int align, int fontSize) {
		PdfPCell cell = getCell(text, 0, 1, align, BaseColor.WHITE, FadingSunsTheme.getLineFont(), fontSize);
		return cell;
	}

	protected static PdfPCell getHandwrittingCell(String text, int align, int fontSize) {
		PdfPCell cell = getCell(text, 0, 1, align, BaseColor.WHITE, FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.getHandWrittingFontSize(fontSize));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static String getTranslatedTag(String tag) {
		String value = getTranslator().getTranslatedText(LANGUAGE_PREFIX + tag.substring(0, 1).toUpperCase() + tag.substring(1));
		if (value != null) {
			if (value.length() > MAX_VALUE_LENGTH) {
				return value.substring(0, MAX_VALUE_LENGTH + 1);
			}
		}
		return value;
	}

}
