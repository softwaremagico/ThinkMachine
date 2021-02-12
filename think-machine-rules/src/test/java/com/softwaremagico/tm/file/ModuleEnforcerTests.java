package com.softwaremagico.tm.file;

import com.softwaremagico.tm.file.modules.ModuleLoaderEnforcer;
import org.testng.annotations.Test;

@Test(groups = "moduleEnforcer")
public class ModuleEnforcerTests {

    @Test
    public void launchModuleEnforcer() {
        ModuleLoaderEnforcer.loadAllFactories("es", PathManager.DEFAULT_MODULE_FOLDER);
    }
}
