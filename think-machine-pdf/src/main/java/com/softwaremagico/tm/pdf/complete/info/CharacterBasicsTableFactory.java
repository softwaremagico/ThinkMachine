package com.softwaremagico.tm.pdf.complete.info;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;

import java.awt.Color;

public abstract class CharacterBasicsTableFactory extends BaseElement {

    protected static final String LINE = "_______________";
    protected static final String LANGUAGE_PREFIX = "info";

    protected static PdfPCell createField(CharacterPlayer characterPlayer, String tag, int fontSize, int maxWidth) {
        final float[] widths = {0.7f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTablePropierties(table);

        table.addCell(getCell(getTranslatedTag(tag, maxWidth), Element.ALIGN_RIGHT, fontSize));
        if (characterPlayer == null) {
            table.addCell(getCell(LINE, Element.ALIGN_LEFT, fontSize));
        } else {
            if (tag.equals("name")) {
                table.addCell(getHandwrittingCell(characterPlayer.getCompleteNameRepresentation(), Element.ALIGN_LEFT,
                        fontSize - 1, maxWidth));
            } else if (tag.equals("race")) {
                if (characterPlayer.getRace() != null) {
                    table.addCell(getHandwrittingCell(characterPlayer.getRace().getName(), Element.ALIGN_LEFT,
                            fontSize - 1, maxWidth));
                } else {
                    table.addCell(getHandwrittingCell("", Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                }
            } else if (tag.equals("faction")) {
                if (characterPlayer.getFaction() != null) {
                    table.addCell(getHandwrittingCell(characterPlayer.getFaction().getName(), Element.ALIGN_LEFT,
                            fontSize - 1, maxWidth));
                } else {
                    table.addCell(getHandwrittingCell("", Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                }
            } else if (tag.equals("rank")) {
                if (characterPlayer.getRank() != null) {
                    table.addCell(getHandwrittingCell(characterPlayer.getRank(), Element.ALIGN_LEFT, fontSize - 1,
                            maxWidth));
                } else {
                    table.addCell(getHandwrittingCell("", Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                }
            } else if (tag.equals("planet")) {
                if (characterPlayer.getInfo().getPlanet() != null) {
                    table.addCell(getHandwrittingCell(characterPlayer.getInfo().getPlanet().getName(),
                            Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                } else {
                    table.addCell(getHandwrittingCell("", Element.ALIGN_LEFT, fontSize - 1, maxWidth));
                }
            } else {
                table.addCell(getHandwrittingCell(
                        characterPlayer.getInfo().getTranslatedParameter(tag, characterPlayer.getModuleName()),
                        Element.ALIGN_LEFT, fontSize - 1, maxWidth));
            }
        }

        final PdfPCell cell = new PdfPCell();
        cell.addElement(table);
        setCellProperties(cell);
        cell.setMinimumHeight(20);

        return cell;
    }

    protected static PdfPCell getCell(String text, int align, int fontSize) {
        final PdfPCell cell = getCell(text, 0, 1, align, Color.WHITE, FadingSunsTheme.getLineFont(), fontSize);
        return cell;
    }

    protected static PdfPCell getHandwrittingCell(String text, int align, int fontSize, int maxWidth) {
        if (text != null && text.length() > maxWidth) {
            text = text.substring(0, maxWidth + 1);
        }
        final PdfPCell cell = getCell(text, 0, 1, align, Color.WHITE, FadingSunsTheme.getHandwrittingFont(),
                FadingSunsTheme.getHandWrittingFontSize(fontSize));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    protected static String getTranslatedTag(String tag, int maxWidth) {
        final String value = getTranslator().getTranslatedText(
                LANGUAGE_PREFIX + tag.substring(0, 1).toUpperCase() + tag.substring(1));
        if (value != null) {
            if (value.length() > maxWidth) {
                return value.substring(0, maxWidth + 1);
            }
        }
        return value;
    }

}
