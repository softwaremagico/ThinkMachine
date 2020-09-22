package com.softwaremagico.tm.file;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.log.MachineLog;

@Test(groups = "moduleManager")
public class ModuleManagerTests {
	private static final String MODULE1 = "think-machine-last-week.jar";
	private static final String MODULE2 = "another-module.jar";
	private static final String SOURCE_MODULE_FOLDER = "src/test/resources/";

	private File modulesDirectory;

	@Test
	public void checkModuleReader() throws IOException {
		Assert.assertEquals(ModuleManager.getAvailableModules().size(), 1);
	}

	@Test(dependsOnMethods = "checkModuleReader")
	public void copyModule() throws IOException {
		modulesDirectory = new File(Files.createTempDirectory("Modules_").toString());
		modulesDirectory.deleteOnExit();
		MachineLog.info(ModuleManagerTests.class.getName(), "Temporal modules folder '{}' created.", modulesDirectory.getAbsolutePath());
		Files.copy(Paths.get(SOURCE_MODULE_FOLDER + File.separator + MODULE1), Paths.get(modulesDirectory.getPath() + File.separator + MODULE1),
				StandardCopyOption.REPLACE_EXISTING);
		Assert.assertEquals(new File(SOURCE_MODULE_FOLDER + File.separator + MODULE1).length(),
				new File(modulesDirectory.getPath() + File.separator + MODULE1).length());
	}

	@Test(dependsOnMethods = "copyModule")
	public void checkNewModules() {
		ModuleManager.setModulesFolder(modulesDirectory.getPath());
		Assert.assertEquals(ModuleManager.getAvailableModules().size(), 2);
	}

	/*
	 * File watcher has been disabled
	 */
	@Test(dependsOnMethods = "checkNewModules", enabled = false)
	public void checkFileWatcher() throws IOException, InterruptedException {
		Files.copy(Paths.get(SOURCE_MODULE_FOLDER + File.separator + MODULE2), Paths.get(modulesDirectory.getPath() + File.separator + MODULE2),
				StandardCopyOption.REPLACE_EXISTING);
		// Wait until the watcher does its work and refresh the modules.
		Thread.sleep(1000);
		// Watcher must be launched and modules must be reloaded.
		Assert.assertEquals(ModuleManager.getAvailableModules().size(), 3);
	}

	@Test(dependsOnMethods = "checkNewModules")
	public void checkModulesContent() throws InvalidXmlElementException {
		Assert.assertNotNull(AvailableSkillsFactory.getInstance().getElement("thinkMachine", "en", "Fading Suns Revised Edition"));
		Assert.assertNotNull(AvailableSkillsFactory.getInstance().getElement("mechanics", "en", "The Last Week"));
	}

	@AfterClass
	public void deleteModules() {
		File[] files = modulesDirectory.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				f.delete();
			}
		}
		modulesDirectory.delete();
	}
}
