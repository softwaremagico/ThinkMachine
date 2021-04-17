package com.softwaremagico.tm.character.races;

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
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;

public class RaceFactory extends XmlFactory<Race> {
    private static final String TRANSLATOR_FILE = "races.xml";

    private static final String MAX_VALUE = "maximumValue";
    private static final String MAX_INITIAL_VALUE = "maximumInitialValue";
    private static final String VALUE = "value";
    private static final String COST = "cost";

    private static final String PSI = "psi";
    private static final String URGE = "urge";
    private static final String THEURGY = "theurgy";
    private static final String HUBRIS = "hubris";

    private static final String BLESSINGS = "blessings";
    private static final String BENEFICES = "benefices";

    private static class RaceFactoryInit {
        public static final RaceFactory INSTANCE = new RaceFactory();
    }

    public static RaceFactory getInstance() {
        return RaceFactoryInit.INSTANCE;
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    public FactoryCacheLoader<Race> getFactoryCacheLoader() {
        return null;
    }

    public void setBlessings(Race race) throws InvalidRaceException {
        try {
            race.setBlessings(getCommaSeparatedValues(race.getId(), BLESSINGS, race.getLanguage(), race.getModuleName(), BlessingFactory.getInstance()));
        } catch (InvalidXmlElementException ixe) {
            throw new InvalidRaceException(
                    "Error in race '" + race + "' structure. Invalid blessing definition. ", ixe);
        }
    }

    public void setBenefices(Race race) throws InvalidRaceException {
        try {
            race.setBenefices(getCommaSeparatedValues(race.getId(), BENEFICES, race.getLanguage(), race.getModuleName(),
                    AvailableBeneficeFactory.getInstance()));
        } catch (InvalidXmlElementException ixe) {
            throw new InvalidRaceException("Error in race '" + race.getId()
                    + "' structure. Invalid benefices definition. ", ixe);
        }
    }

    @Override
    protected Race createElement(ITranslator translator, String raceId, String name, String description,
                                 String language, String moduleName)
            throws InvalidXmlElementException {
        Race race;
        try {
            race = new Race(raceId, name, description, language, moduleName);
        } catch (Exception e) {
            throw new InvalidRaceException("Invalid structure in race '" + raceId + "'.", e);
        }
        try {
            final String cost = translator.getNodeValue(raceId, COST);
            race.setCost(Integer.parseInt(cost));
        } catch (Exception e) {
            throw new InvalidRaceException("Invalid cost in race '" + raceId + "'.");
        }
        for (final CharacteristicName characteristic : CharacteristicName.values()) {
            try {
                final String maxValue = translator.getNodeValue(raceId, characteristic.getId(), MAX_VALUE);
                if (maxValue != null) {
                    race.setMaximumValue(characteristic, Integer.parseInt(maxValue));
                }
                final String maxInitialValue = translator.getNodeValue(raceId, characteristic.getId(),
                        MAX_INITIAL_VALUE);
                if (maxInitialValue != null) {
                    race.setMaximumInitialValue(characteristic, Integer.parseInt(maxInitialValue));
                }
                final String value = translator.getNodeValue(raceId, characteristic.getId(), VALUE);
                if (value != null) {
                    race.setValue(characteristic, Integer.parseInt(value));
                }
            } catch (NumberFormatException nfe) {
                throw new InvalidRaceException("Invalid value for characteristic '" + characteristic.getId()
                        + "' in race '" + raceId + "'.");
            }
        }
        try {
            final String psi = translator.getNodeValue(raceId, PSI);
            race.setPsi(Integer.parseInt(psi));
        } catch (Exception e) {
            throw new InvalidRaceException("Invalid psi value in race '" + raceId + "'.");
        }
        try {
            final String urge = translator.getNodeValue(raceId, URGE);
            race.setUrge(Integer.parseInt(urge));
        } catch (Exception e) {
            throw new InvalidRaceException("Invalid urge value in race '" + raceId + "'.");
        }
        try {
            final String theurgy = translator.getNodeValue(raceId, THEURGY);
            race.setTheurgy(Integer.parseInt(theurgy));
        } catch (Exception e) {
            throw new InvalidRaceException("Invalid theurgy value in race '" + raceId + "'.");
        }
        try {
            final String hubris = translator.getNodeValue(raceId, HUBRIS);
            race.setHubris(Integer.parseInt(hubris));
        } catch (Exception e) {
            throw new InvalidRaceException("Invalid hubris value in race '" + raceId + "'.");
        }

        return race;

    }
}
