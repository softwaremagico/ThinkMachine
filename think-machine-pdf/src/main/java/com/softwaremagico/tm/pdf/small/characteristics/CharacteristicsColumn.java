package com.softwaremagico.tm.pdf.small.characteristics;

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

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.CustomPdfTable;

import java.awt.Color;
import java.util.List;

public class CharacteristicsColumn extends CustomPdfTable {
    private static final String GAP = "   ";
    private static final String SKILL_VALUE_GAP = "__";

    private static final int ROW_HEIGHT = 36;
    private static final float[] widths = {1};

    public CharacteristicsColumn(CharacterPlayer characterPlayer, CharacteristicType characteristicType, List<CharacteristicDefinition> content) {
        super(widths);
        final PdfPCell title = createCompactTitle(getTranslator().getTranslatedText(characteristicType.getTranslationTag()),
                FadingSunsTheme.CHARACTER_SMALL_CHARACTERISTICS_TITLE_FONT_SIZE);
        title.setColspan(widths.length);
        addCell(title);
        addCell(createContent(characterPlayer, content));
    }

    private PdfPCell createContent(CharacterPlayer characterPlayer, List<CharacteristicDefinition> content) {
        final float[] widths = {4f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        BaseElement.setTablePropierties(table);
        table.getDefaultCell().setBorder(0);

        if (content != null) {
            for (final CharacteristicDefinition characteristic : content) {
                final Paragraph paragraph = new Paragraph();
                paragraph.add(new Chunk(getTranslator().getTranslatedText(characteristic.getId()),
                        new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTER_SMALL_CHARACTERISTICS_LINE_FONT_SIZE)));
                paragraph.add(new Chunk(" (", new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTER_SMALL_CHARACTERISTICS_LINE_FONT_SIZE)));
                if (characterPlayer == null) {
                    paragraph.add(new Chunk(GAP, new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTER_SMALL_CHARACTERISTICS_LINE_FONT_SIZE)));
                } else {
                    paragraph.add(new Chunk(characterPlayer.getStartingValue(characteristic.getCharacteristicName()) + "",
                            new Font(FadingSunsTheme.getHandwrittingFont(),
                                    FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.CHARACTER_SMALL_CHARACTERISTICS_LINE_FONT_SIZE))));
                }
                paragraph.add(new Chunk(")", new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTER_SMALL_CHARACTERISTICS_LINE_FONT_SIZE)));

                final PdfPCell characteristicTitle = new PdfPCell(paragraph);
                characteristicTitle.setBorder(0);
                characteristicTitle.setMinimumHeight(ROW_HEIGHT / (float) content.size());
                table.addCell(characteristicTitle);

                // Rectangle
                if (characterPlayer == null) {
                    table.addCell(createCharacteristicLine(SKILL_VALUE_GAP));
                } else {
                    table.addCell(getHandwrittingCell(getCharacteristicValueRepresentation(characterPlayer, characteristic.getCharacteristicName()),
                            Element.ALIGN_LEFT));
                }
            }
        }

        final PdfPCell cell = new PdfPCell();
        cell.addElement(table);
        BaseElement.setCellProperties(cell);

        return cell;
    }

    private String getCharacteristicValueRepresentation(CharacterPlayer characterPlayer, CharacteristicName characteristicName) {
        final StringBuilder representation = new StringBuilder();
        representation.append(characterPlayer.getValue(characteristicName));
        if (characterPlayer.hasCharacteristicTemporalModificator(characteristicName)) {
            representation.append("*");
        }
        if (characterPlayer.hasCharacteristicModificator(characteristicName)) {
            representation.append("!");
        }
        return representation.toString();
    }

    private static PdfPCell getHandwrittingCell(String text, int align) {
        return BaseElement.getCell(text, 0, 0, align, Color.WHITE, FadingSunsTheme.getHandwrittingFont(),
                FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.CHARACTER_SMALL_CHARACTERISTICS_LINE_FONT_SIZE));
    }

    private static PdfPCell createCharacteristicLine(String text) {
        return BaseElement.getCell(text, 0, 1, Element.ALIGN_LEFT, Color.WHITE, FadingSunsTheme.getLineFont(),
                FadingSunsTheme.CHARACTER_SMALL_CHARACTERISTICS_LINE_FONT_SIZE);
    }

}
