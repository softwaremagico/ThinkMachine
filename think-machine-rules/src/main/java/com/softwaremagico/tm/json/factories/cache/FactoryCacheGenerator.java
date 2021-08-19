package com.softwaremagico.tm.json.factories.cache;

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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.log.ConfigurationLog;
import com.softwaremagico.tm.log.MachineModulesLog;
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

public class FactoryCacheGenerator {

    public static void main(String[] args) throws InvalidXmlElementException {
        disableLogs();
        final WeaponsFactoryCacheLoader weaponsFactoryCacheLoader = new WeaponsFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            weaponsFactoryCacheLoader.save(WeaponFactory.class, moduleName, WeaponFactory.getInstance().getTranslatorFile());
        }
        final ArmourFactoryCacheLoader armourFactoryCacheLoader = new ArmourFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            armourFactoryCacheLoader.save(ArmourFactory.class, moduleName, ArmourFactory.getInstance().getTranslatorFile());
        }
        final SkillDefinitionsFactoryCacheLoader skillDefinitionsFactoryCacheLoader = new SkillDefinitionsFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            skillDefinitionsFactoryCacheLoader.save(SkillsDefinitionsFactory.class, moduleName, SkillsDefinitionsFactory.getInstance().getTranslatorFile());
        }
        final BlessingFactoryCacheLoader blessingFactoryCacheLoader = new BlessingFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            blessingFactoryCacheLoader.save(BlessingFactory.class, moduleName, BlessingFactory.getInstance().getTranslatorFile());
        }
        final BeneficeDefinitionFactoryCacheLoader beneficeDefinitionFactoryCacheLoader = new BeneficeDefinitionFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            beneficeDefinitionFactoryCacheLoader.save(BeneficeDefinitionFactory.class, moduleName,
                    BeneficeDefinitionFactory.getInstance().getTranslatorFile());
        }
        final PlanetFactoryCacheLoader planetFactoryCacheLoader = new PlanetFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            planetFactoryCacheLoader.save(PlanetFactory.class, moduleName,
                    PlanetFactory.getInstance().getTranslatorFile());
        }
        final OccultismPathFactoryCacheLoader occultismPathFactoryCacheLoader = new OccultismPathFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            occultismPathFactoryCacheLoader.save(OccultismPathFactory.class, moduleName,
                    OccultismPathFactory.getInstance().getTranslatorFile());
        }
        final RaceFactoryCacheLoader raceFactoryCacheLoader = new RaceFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            raceFactoryCacheLoader.save(RaceFactory.class, moduleName,
                    RaceFactory.getInstance().getTranslatorFile());
        }
        final FactionFactoryCacheLoader factionFactoryCacheLoader = new FactionFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            factionFactoryCacheLoader.save(FactionsFactory.class, moduleName,
                    FactionsFactory.getInstance().getTranslatorFile());
        }
    }

    private static void disableLogs() {
        Logger logger = (Logger) LoggerFactory.getLogger(MachineXmlReaderLog.class);
        logger.setLevel(Level.OFF);
        logger = (Logger) LoggerFactory.getLogger(ConfigurationLog.class);
        logger.setLevel(Level.OFF);
        logger = (Logger) LoggerFactory.getLogger(MachineModulesLog.class);
        logger.setLevel(Level.OFF);
        logger = (Logger) LoggerFactory.getLogger(Reflections.class);
        logger.setLevel(Level.OFF);
    }
}
