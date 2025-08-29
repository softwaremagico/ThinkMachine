package com.softwaremagico.tm.pdf.complete.skills;

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

import com.lowagie.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;

public class WyrdTable extends CounterTable {

    public WyrdTable(CharacterPlayer characterPlayer) {
        super(WIDTHS);
        addedCircle = 0;
        addCell(getCircle(characterPlayer));
        addedCircle++;
        addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("wyrd"), TITLE_SPAN));
        for (int i = 0; i < TITLE_SPAN; i++) {
            addCell(getCircle(characterPlayer));
            addedCircle++;
        }
        addCell(space(CIRCLES - TITLE_SPAN));
        for (int i = 0; i < CIRCLES - TITLE_SPAN; i++) {
            addCell(getCircle(characterPlayer));
            addedCircle++;
        }
    }

    @Override
    protected int getSelectedValue(CharacterPlayer characterPlayer) {
        if (characterPlayer != null) {
            return characterPlayer.getWyrdValue().intValue();
        }
        return -1;
    }

    @Override
    protected PdfPCell createLateralVerticalTitle(String title, int rowspan) {
        final PdfPCell titleCell = super.createLateralVerticalTitle(title, rowspan);
        titleCell.setRotation(270);
        return titleCell;
    }

    @Override
    protected int getTitleFontSize() {
        return FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE;
    }

}
