package com.softwaremagico.tm.cache;

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
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.log.ConfigurationLog;
import com.softwaremagico.tm.log.MachineModulesLog;
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import com.softwaremagico.tm.random.predefined.characters.NpcFactory;
import com.softwaremagico.tm.random.predefined.profile.RandomProfileFactory;
import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

public class FactoryCacheGenerator {

    public static void main(String[] args) throws InvalidXmlElementException {
        disableLogs();
        final ProfileFactoryCacheLoader profileFactoryCacheLoader = new ProfileFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            profileFactoryCacheLoader.save(RandomProfileFactory.class, moduleName, RandomProfileFactory.getInstance().getTranslatorFile());
        }
        final NpcFactoryCacheLoader npcFactoryCacheLoader = new NpcFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            npcFactoryCacheLoader.save(NpcFactory.class, moduleName, NpcFactory.getInstance().getTranslatorFile());
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
