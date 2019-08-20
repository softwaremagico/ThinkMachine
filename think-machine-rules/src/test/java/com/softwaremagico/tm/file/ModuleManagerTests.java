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
import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.softwaremagico.tm.file.configurator.MachineConfigurationReader;
import com.softwaremagico.tm.file.modules.ModuleManager;

@Test(groups = "moduleManager")
public class ModuleManagerTests {

	@Test(enabled=false)
	public void checkModuleReader() throws IOException {
		Assert.assertEquals(ModuleManager.getAvailableModules().size(), 1);
		// Emulate some jars.
		Set<File> modules = new HashSet<>();
		MachineConfigurationReader.getInstance().setProperty("modulesPath", "/tmp");
		for (int i = 0; i < 10; i++) {
			File module = new File(MachineConfigurationReader.getInstance().getModulesPath() + File.separator
					+ "module" + i + ".jar");
			module.createNewFile();
			modules.add(module);
		}
		Assert.assertEquals(ModuleManager.getAvailableModules().size(), 11);
		for (File module : modules) {
			module.delete();
		}
		Assert.assertEquals(ModuleManager.getAvailableModules().size(), 0);
	}
}
