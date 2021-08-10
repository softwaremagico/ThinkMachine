package com.softwaremagico.tm.character.occultism;

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

import com.softwaremagico.tm.ElementClassification;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.cybernetics.InvalidCyberneticDeviceException;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import com.softwaremagico.tm.log.SuppressFBWarnings;

import java.util.*;

public class OccultismPathFactory extends XmlFactory<OccultismPath> {
    private static final String TRANSLATOR_FILE = "occultismPaths.xml";

    private static final String TYPE = "type";
    private static final String FACTIONS = "factions";

    private static final String OCCULTISM_POWER = "powers";
    private static final String POWER_NAME = "name";
    private static final String POWER_DESCRIPTION = "description";
    private static final String POWER_LEVEL = "level";
    private static final String POWER_CHARACTERISTIC = "characteristic";
    private static final String POWER_SKILL = "skill";
    private static final String POWER_RANGE = "range";
    private static final String POWER_DURATION = "duration";
    private static final String POWER_WYRD = "wyrd";
    private static final String POWER_COMPONENTS = "components";

    private static final String CLASSIFICATION = "classification";

    private static final String VARIABLE_WYRD = "variable";

    private final Set<OccultismPath> psiPaths;
    private final Set<OccultismPath> theurgyPaths;

    private OccultismPathFactory() {
        psiPaths = new HashSet<>();
        theurgyPaths = new HashSet<>();
    }

    private static class OccultismPathFactoryInit {
        public static final OccultismPathFactory INSTANCE = new OccultismPathFactory();
    }

    public static OccultismPathFactory getInstance() {
        return OccultismPathFactoryInit.INSTANCE;
    }

    @Override
    public void refreshCache() {
        psiPaths.clear();
        theurgyPaths.clear();
        super.refreshCache();
    }

    @Override
    @SuppressFBWarnings("REC_CATCH_EXCEPTION")
    protected OccultismPath createElement(ITranslator translator, String occultismId, String name, String description,
                                          String language, String moduleName)
            throws InvalidXmlElementException {

        try {
            final String typeName = translator.getNodeValue(occultismId, TYPE);
            final OccultismType occultismType = OccultismTypeFactory.getInstance().getElement(typeName, language,
                    moduleName);
            if (occultismType == null) {
                throw new InvalidOccultismPowerException("No type defined for '" + occultismId + "'.");
            }

            final Set<Faction> factions = getCommaSeparatedValues(occultismId, FACTIONS, language, moduleName,
                    FactionsFactory.getInstance());

            ElementClassification classification;
            try {
                classification = ElementClassification.get(translator.getNodeValue(occultismId, CLASSIFICATION));
            } catch (Exception e) {
                throw new InvalidCyberneticDeviceException("Invalid classification value in occultism '" + occultismId
                        + "'.");
            }

            final OccultismPath occultismPath = new OccultismPath(occultismId, name, description, language, moduleName,
                    occultismType, factions, classification);

            for (final String powerId : translator.getAllChildrenTags(occultismId, OCCULTISM_POWER)) {
                try {
                    final String powerName = translator.getNodeValue(powerId, POWER_NAME, language);
                    final String powerDescription = translator.getNodeValue(powerId, POWER_DESCRIPTION, language);
                    final String level = translator.getNodeValue(powerId, POWER_LEVEL);
                    final String characteristicName = translator.getNodeValue(powerId, POWER_CHARACTERISTIC);
                    final String skillName = translator.getNodeValue(powerId, POWER_SKILL);
                    final String range = translator.getNodeValue(powerId, POWER_RANGE);
                    final String duration = translator.getNodeValue(powerId, POWER_DURATION);
                    Integer wyrd = null;
                    final String components = translator.getNodeValue(powerId, POWER_COMPONENTS);
                    try {
                        wyrd = Integer.parseInt(translator.getNodeValue(powerId, POWER_WYRD));
                    } catch (NumberFormatException nfe) {
                        // Wyrd is not variable, is an error.
                        if (!translator.getNodeValue(powerId, POWER_WYRD).equalsIgnoreCase(VARIABLE_WYRD)) {
                            throw new InvalidOccultismPowerException("Invalid wyrd value for '"
                                    + translator.getNodeValue(powerId, POWER_WYRD) + "' in power '" + powerId + "'.");
                        }
                    }

                    final List<IValue> values = new ArrayList<>();
                    final StringTokenizer skillTokenizer = new StringTokenizer(skillName, ",");
                    while (skillTokenizer.hasMoreTokens()) {
                        final String value = skillTokenizer.nextToken().trim();
                        try {
                            values.add(SkillsDefinitionsFactory.getInstance().getElement(value, language, moduleName));
                        } catch (InvalidXmlElementException iee) {
                            values.add(OccultismTypeFactory.getInstance().getElement(value, language, moduleName));
                        }
                    }

                    OccultismRange occultismRange = null;
                    if (range != null) {
                        occultismRange = OccultismRangeFactory.getInstance().getElement(range, language, moduleName);
                    }

                    OccultismDuration occultismDuration = null;
                    if (duration != null) {
                        occultismDuration = OccultismDurationFactory.getInstance().getElement(duration, language,
                                moduleName);
                    }

                    final Set<TheurgyComponent> theurgyComponents = new HashSet<>();
                    if (components != null) {
                        for (int i = 0; i < components.length(); i++) {
                            final TheurgyComponent theurgyComponent = TheurgyComponentFactory.getInstance()
                                    .getTheurgyComponent(components.charAt(i), language, moduleName);
                            if (theurgyComponent == null) {
                                throw new InvalidTheurgyComponentException("Invalid theurgy component code '"
                                        + components.charAt(i) + "'.");
                            }
                            theurgyComponents.add(theurgyComponent);
                        }
                    }

                    final OccultismPower occultismPower = new OccultismPower(powerId, powerName, powerDescription, language, moduleName,
                            CharacteristicsDefinitionFactory.getInstance().get(CharacteristicName.get(characteristicName),
                                    language, moduleName), values, Integer.parseInt(level), occultismRange, occultismDuration, wyrd,
                            theurgyComponents);

                    occultismPower.setRestrictedToRaces(occultismPath.getRestrictedToRaces());
                    occultismPower.setRestrictedToFactions(occultismPath.getRestrictedToFactions());
                    occultismPower.setRestricted(occultismPower.isRestricted());
                    occultismPower.setOfficial(occultismPath.isOfficial());
                    occultismPower.setRestrictedToFactionGroup(occultismPath.getRestrictedToFactionGroup());

                    occultismPath.getOccultismPowers().put(powerId, occultismPower);
                } catch (Exception e) {
                    throw new InvalidOccultismPowerException("Invalid structure in occultism path '" + occultismId + "' and power '" + powerId + "'.", e);
                }
            }

            return occultismPath;
        } catch (Exception e) {
            throw new InvalidOccultismPowerException("Invalid structure in occultism path '" + occultismId + "'.", e);
        }
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    public FactoryCacheLoader<OccultismPath> getFactoryCacheLoader() {
        return null;
    }

    public OccultismPath getOccultismPath(OccultismPower power) {
        try {
            for (final OccultismPath occultismPath : getElements(power.getLanguage(), power.getModuleName())) {
                if (occultismPath.getOccultismPowers().containsKey(power.getId())) {
                    return occultismPath;
                }
            }
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
        }
        return null;
    }

    public Set<OccultismPath> getPsiPaths(String language, String moduleName) {
        if (psiPaths.isEmpty()) {
            try {
                for (final OccultismPath path : getElements(language, moduleName)) {
                    if (Objects.equals(path.getOccultismType(), OccultismTypeFactory.getPsi(language, moduleName))) {
                        psiPaths.add(path);
                    }
                }
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return Collections.unmodifiableSet(psiPaths);
    }

    public Set<OccultismPath> getTheurgyPaths(String language, String moduleName) {
        if (theurgyPaths.isEmpty()) {
            try {
                for (final OccultismPath path : getElements(language, moduleName)) {
                    if (Objects.equals(path.getOccultismType(), OccultismTypeFactory.getTheurgy(language, moduleName))) {
                        theurgyPaths.add(path);
                    }
                }
            } catch (InvalidXmlElementException e) {
                MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return Collections.unmodifiableSet(theurgyPaths);
    }
}
