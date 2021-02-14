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
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.json.factories.cache.BlessingFactoryCacheLoader;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.json.factories.cache.SkillDefinitionsFactoryCacheLoader;
import com.softwaremagico.tm.json.factories.cache.WeaponsFactoryCacheLoader;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;

@Test(groups = {"jsonCache"})
public class JsonCacheLoaderTests {
    private static final String LANGUAGE = "es";
    private static final int ITERATIONS = 5;

    @Test(enabled = false)
    public void loadWeaponsCache() {
        WeaponsFactoryCacheLoader weaponsFactoryCacheLoader = new WeaponsFactoryCacheLoader();
        Assert.assertTrue(weaponsFactoryCacheLoader.load(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size() > 0);
    }

    @Test()
    public void checkWeaponsImprovement() throws InvalidXmlElementException {
        //Skip Json generation.
        WeaponFactory weaponFactory = new WeaponFactory() {
            @Override
            public FactoryCacheLoader<Weapon> getFactoryCacheLoader() {
                return null;
            }
        };


        Instant start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            weaponFactory.getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
            weaponFactory.removeData();
        }
        Instant end = Instant.now();
        Duration xmlMethod = Duration.between(start, end);

        start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            WeaponsFactoryCacheLoader weaponsFactoryCacheLoader = new WeaponsFactoryCacheLoader();
            Assert.assertTrue(weaponsFactoryCacheLoader.load(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size() > 0);
            WeaponFactory.getInstance().removeData();
        }
        end = Instant.now();
        Duration jsonMethod = Duration.between(start, end);
        Assert.assertTrue(jsonMethod.getNano() * 10 < xmlMethod.getNano());
        //System.out.println("Weapons [Xml: " + xmlMethod + ", Json: " + jsonMethod + " ]");

    }

    @Test()
    public void checkSkillsImprovement() throws InvalidXmlElementException {
        //Force Json generation.

        SkillsDefinitionsFactory skillsDefinitionsFactory = new SkillsDefinitionsFactory() {
            @Override
            public FactoryCacheLoader<SkillDefinition> getFactoryCacheLoader() {
                return null;
            }
        };

        Instant start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            skillsDefinitionsFactory.getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
            skillsDefinitionsFactory.removeData();
        }
        Instant end = Instant.now();
        Duration xmlMethod = Duration.between(start, end);

        start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            SkillDefinitionsFactoryCacheLoader skillDefinitionsFactoryCacheLoader = new SkillDefinitionsFactoryCacheLoader();
            Assert.assertTrue(skillDefinitionsFactoryCacheLoader.load(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size() > 0);
            SkillsDefinitionsFactory.getInstance().removeData();
        }
        end = Instant.now();
        Duration jsonMethod = Duration.between(start, end);

        //Check speed is at least 10x
        Assert.assertTrue(jsonMethod.getNano() * 10 < xmlMethod.getNano());
        //System.out.println("Skills [Xml: " + xmlMethod + ", Json: " + jsonMethod + " ]");

    }

    @Test()
    public void checkBlessingsImprovement() throws InvalidXmlElementException {
        //Force Json generation.

        BlessingFactory blessingFactory = new BlessingFactory() {
            @Override
            public FactoryCacheLoader<Blessing> getFactoryCacheLoader() {
                return null;
            }
        };

        Instant start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            blessingFactory.getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
            blessingFactory.removeData();
        }
        Instant end = Instant.now();
        Duration xmlMethod = Duration.between(start, end);

        start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            BlessingFactoryCacheLoader blessingFactoryCacheLoader = new BlessingFactoryCacheLoader();
            Assert.assertTrue(blessingFactoryCacheLoader.load(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size() > 0);
            BlessingFactory.getInstance().removeData();
        }
        end = Instant.now();
        Duration jsonMethod = Duration.between(start, end);

        //Check speed is at least 10x
        Assert.assertTrue(jsonMethod.getNano() * 10 < xmlMethod.getNano());
        //System.out.println("Skills [Xml: " + xmlMethod + ", Json: " + jsonMethod + " ]");

    }


}
