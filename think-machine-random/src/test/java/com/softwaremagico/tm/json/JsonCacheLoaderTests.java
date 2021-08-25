package com.softwaremagico.tm.json;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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
import com.softwaremagico.tm.cache.NpcFactoryCacheLoader;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.random.predefined.characters.Npc;
import com.softwaremagico.tm.random.predefined.characters.NpcFactory;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Test(groups = {"jsonCache"})
public class JsonCacheLoaderTests {
    private static final String LANGUAGE = "es";
    private static final int ITERATIONS = 5;

    @Test(timeOut = 10000)
    public void loadNpcCache() {
        NpcFactoryCacheLoader npcFactoryCacheLoader = new NpcFactoryCacheLoader();
        List<Npc> npcs = npcFactoryCacheLoader.load(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertTrue(npcs.size() > 0);
        //Check random configuration enums.
        Npc alMalik = npcs.stream().filter(npc -> npc.getId().equals("alMalik")).findFirst().orElseThrow(AssertionError::new);
        Assert.assertTrue(alMalik.getPreferences().contains(TechnologicalPreferences.FUTURIST));
        Assert.assertTrue(alMalik.getPreferences().contains(SpecializationPreferences.SPECIALIZED));
    }

    @Test(enabled = false)
    public void checkNpcsImprovement() throws InvalidXmlElementException {
        //Skip Json generation.
        NpcFactory npcFactory = new NpcFactory() {
            @Override
            public FactoryCacheLoader<Npc> getFactoryCacheLoader() {
                return null;
            }
        };


        Instant start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            npcFactory.getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
            npcFactory.removeData();
        }
        Instant end = Instant.now();
        Duration xmlMethod = Duration.between(start, end);

        start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            NpcFactoryCacheLoader npcFactoryCacheLoader = new NpcFactoryCacheLoader();
            Assert.assertTrue(npcFactoryCacheLoader.load(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size() > 0);
            NpcFactory.getInstance().removeData();
        }
        end = Instant.now();
        Duration jsonMethod = Duration.between(start, end);
        Assert.assertTrue(jsonMethod.getNano() < xmlMethod.getNano());
    }
}
