package com.softwaremagico.tm.cache;

/*-
 * #%L
 * Think Machine (Random Generator)
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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.party.RandomPartyFactory;
import com.softwaremagico.tm.random.predefined.characters.NpcFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class ModuleLoaderEnforcer {

    public static int loadAllFactories(String language, String moduleName) {
        final long startTime = System.nanoTime();
        final AtomicInteger loadedElements = new AtomicInteger(0);

        loadedElements.addAndGet(com.softwaremagico.tm.file.modules.ModuleLoaderEnforcer.loadAllFactories(language, moduleName));

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(RandomPartyFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(com.softwaremagico.tm.file.modules.ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        futures = new ArrayList<>();

        //Second level
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(NpcFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(com.softwaremagico.tm.file.modules.ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();


        final long duration = (System.nanoTime() - startTime);
        MachineLog.info(com.softwaremagico.tm.file.modules.ModuleLoaderEnforcer.class.getName(),
                "All factories loaded! Total {} elements loaded in {} milliseconds.",
                loadedElements.get(), duration / 1000000);
        return loadedElements.get();
    }
}
