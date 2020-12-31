package com.softwaremagico.tm.character.cybernetics;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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

import com.softwaremagico.tm.Element;

public class CyberneticDeviceTrait extends Element<CyberneticDeviceTrait> {
    private final CyberneticDeviceTraitCategory category;
    private final int minimumTechLevel;
    private final int extraPoints;
    private final int extraCost;
    private final float extraCostMultiplier;
    private final int extraIncompatibility;

    public CyberneticDeviceTrait(String id, String name, String description, String language, String moduleName,
                                 CyberneticDeviceTraitCategory category, int minimumTechLevel, int extraPoints, int extraCost,
                                 float extraCostMultiplier, int extraIncompatibility) {
        super(id, name, description, language, moduleName);
        this.category = category;
        this.minimumTechLevel = minimumTechLevel;
        this.extraCost = extraCost;
        this.extraCostMultiplier = extraCostMultiplier;
        this.extraPoints = extraPoints;
        this.extraIncompatibility = extraIncompatibility;
    }

    public CyberneticDeviceTraitCategory getCategory() {
        return category;
    }

    @Override
    public int compareTo(CyberneticDeviceTrait element) {
        // Categories with higher preferences first.
        if (getCategory() == null) {
            if (element.getCategory() != null) {
                return -1;
            }
        }
        if (getCategory() != null) {
            if (element.getCategory() == null) {
                return 1;
            }
        }
        if (getCategory() != element.getCategory()) {
            return element.getCategory().getPreference() - getCategory().getPreference();
        }
        return super.compareTo(element);
    }

    public int getMinimumTechLevel() {
        return minimumTechLevel;
    }

    public int getExtraPoints() {
        return extraPoints;
    }

    public float getExtraCostMultiplier() {
        return extraCostMultiplier;
    }

    public int getExtraIncompatibility() {
        return extraIncompatibility;
    }

    public int getExtraCost() {
        return extraCost;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
