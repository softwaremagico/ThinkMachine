package com.softwaremagico.tm.character;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2021 Softwaremagico
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

public class Settings {
    private boolean onlyOfficialAllowed = false;
    private boolean restrictionsChecked = true;


    public boolean isOnlyOfficialAllowed() {
        return onlyOfficialAllowed;
    }

    public void setOnlyOfficialAllowed(boolean onlyOfficialAllowed) {
        this.onlyOfficialAllowed = onlyOfficialAllowed;
    }

    public boolean isRestrictionsChecked() {
        return restrictionsChecked;
    }

    public void setRestrictionsChecked(boolean restrictionsChecked) {
        this.restrictionsChecked = restrictionsChecked;
    }

    public void copy(Settings settings) {
        if (settings != null) {
            this.onlyOfficialAllowed = settings.onlyOfficialAllowed;
            this.restrictionsChecked = settings.restrictionsChecked;
        }
    }
}
