package com.softwaremagico.tm.character.equipment.shields;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;

public class ShieldFactory extends XmlFactory<Shield> {
    private static final String TRANSLATOR_FILE = "shields.xml";

    private static final String TECH = "techLevel";
    private static final String IMPACT = "impact";
    private static final String FORCE = "force";
    private static final String HITS = "hits";
    private static final String COST = "cost";

    private static class ShieldFactoryInit {
        public static final ShieldFactory INSTANCE = new ShieldFactory();
    }

    public static ShieldFactory getInstance() {
        return ShieldFactoryInit.INSTANCE;
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    protected Shield createElement(ITranslator translator, String shieldId, String name, String description,
                                   String language, String moduleName)
            throws InvalidXmlElementException {
        Shield shield;

        int techLevel;
        try {
            final String techValue = translator.getNodeValue(shieldId, TECH);
            techLevel = Integer.parseInt(techValue);
        } catch (Exception e) {
            throw new InvalidShieldException("Invalid tech value in shield '" + shieldId + "'.");
        }

        int impact;
        try {
            final String impactValue = translator.getNodeValue(shieldId, IMPACT);
            impact = Integer.parseInt(impactValue);
        } catch (Exception e) {
            throw new InvalidShieldException("Invalid impact value in shield '" + shieldId + "'.");
        }

        int force;
        try {
            final String forceValue = translator.getNodeValue(shieldId, FORCE);
            force = Integer.parseInt(forceValue);
        } catch (Exception e) {
            throw new InvalidShieldException("Invalid force value in shield '" + shieldId + "'.");
        }

        int hits;
        try {
            final String hitsValue = translator.getNodeValue(shieldId, HITS);
            hits = Integer.parseInt(hitsValue);
        } catch (Exception e) {
            throw new InvalidShieldException("Invalid hits value in shield '" + shieldId + "'.");
        }

        float cost;
        try {
            final String costValue = translator.getNodeValue(shieldId, COST);
            cost = Float.parseFloat(costValue);
        } catch (Exception e) {
            throw new InvalidShieldException("Invalid cost value in shield '" + shieldId + "'.");
        }

        shield = new Shield(shieldId, name, description, language, moduleName, techLevel, impact, force, hits, cost);

        return shield;
    }
}
