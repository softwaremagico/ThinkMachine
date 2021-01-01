package com.softwaremagico.tm.character.cybernetics;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2018 Softwaremagico
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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.log.SuppressFBWarnings;

public class CyberneticDeviceTraitFactory extends XmlFactory<CyberneticDeviceTrait> {
    private static final String TRANSLATOR_FILE = "cybernetics_traits.xml";

    private static final String CATEGORY = "category";

    private static final String MIN_TECH_LEVEL = "minTechLevel";
    private static final String EXTRA_POINTS = "extraPoints";
    private static final String EXTRA_COST = "extraCostMultiplier";
    private static final String EXTRA_INCOMPATIBILITY = "extraIncompatibility";

    private static class CyberneticDeviceTraitFactoryInit {
        public static final CyberneticDeviceTraitFactory INSTANCE = new CyberneticDeviceTraitFactory();
    }

    public static CyberneticDeviceTraitFactory getInstance() {
        return CyberneticDeviceTraitFactoryInit.INSTANCE;
    }

    @Override
    public void clearCache() {
        super.clearCache();
    }

    @Override
    protected String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    @SuppressFBWarnings("REC_CATCH_EXCEPTION")
    protected CyberneticDeviceTrait createElement(ITranslator translator, String cyberneticDeviceTraitId, String name, String description,
                                                  String language, String moduleName) throws InvalidXmlElementException {
        try {
            final String categoryName = translator.getNodeValue(cyberneticDeviceTraitId, CATEGORY);
            final CyberneticDeviceTraitCategory cyberneticDeviceTraitCategory = CyberneticDeviceTraitCategory
                    .get(categoryName);
            if (cyberneticDeviceTraitCategory == null) {
                throw new InvalidCyberneticDeviceTraitException("Invalid category definition for '"
                        + cyberneticDeviceTraitId + "'.");
            }

            int extraPoints;
            try {
                final String pointsName = translator.getNodeValue(cyberneticDeviceTraitId, EXTRA_POINTS);
                extraPoints = Integer.parseInt(pointsName);
            } catch (Exception e) {
                throw new InvalidCyberneticDeviceTraitException("Invalid cost in cybernetic trait '"
                        + cyberneticDeviceTraitId + "'.");
            }

            int minimumTechLevel;
            try {
                final String techLevelName = translator.getNodeValue(cyberneticDeviceTraitId, MIN_TECH_LEVEL);
                minimumTechLevel = Integer.parseInt(techLevelName);
            } catch (Exception e) {
                throw new InvalidCyberneticDeviceTraitException("Invalid minimum tech level in cybernetic trait '"
                        + cyberneticDeviceTraitId + "'.");
            }

            int extraIncompatibility;
            try {
                final String incompatibilityName = translator.getNodeValue(cyberneticDeviceTraitId,
                        EXTRA_INCOMPATIBILITY);
                extraIncompatibility = Integer.parseInt(incompatibilityName);
            } catch (Exception e) {
                throw new InvalidCyberneticDeviceTraitException("Invalid extra incomptability in cybernetic trait '"
                        + cyberneticDeviceTraitId + "'.");
            }

            int extraCost = 0;
            try {
                final String extraCostName = translator.getNodeValue(cyberneticDeviceTraitId, EXTRA_INCOMPATIBILITY);
                extraCost = Integer.parseInt(extraCostName);
            } catch (NullPointerException npe) {
                // Not mandatory
            } catch (NumberFormatException e) {
                throw new InvalidCyberneticDeviceTraitException("Invalid extra cost in cybernetic trait '"
                        + cyberneticDeviceTraitId + "'.");
            }

            float extraCostMultiplier = 1;
            try {
                final String costName = translator.getNodeValue(cyberneticDeviceTraitId, EXTRA_COST);
                extraCostMultiplier = Float.parseFloat(costName);
            } catch (NullPointerException npe) {
                // Not mandatory
            } catch (NumberFormatException e) {
                throw new InvalidCyberneticDeviceTraitException("Invalid cost in cybernetic trait '"
                        + cyberneticDeviceTraitId + "'.");
            }

            return new CyberneticDeviceTrait(cyberneticDeviceTraitId, name, description, language, moduleName,
                    cyberneticDeviceTraitCategory, minimumTechLevel, extraPoints, extraCost, extraCostMultiplier,
                    extraIncompatibility);
        } catch (Exception e) {
            throw new InvalidCyberneticDeviceTraitException("Invalid cybernetic trait definition for '"
                    + cyberneticDeviceTraitId + "'.", e);
        }
    }
}
