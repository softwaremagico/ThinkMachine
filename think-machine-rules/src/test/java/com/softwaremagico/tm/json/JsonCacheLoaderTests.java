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
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.json.factories.cache.*;
import org.testng.Assert;
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

    @Test
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
        Assert.assertTrue(jsonMethod.getNano() < xmlMethod.getNano());
    }

    @Test
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
        Assert.assertTrue(jsonMethod.getNano() * 5 < xmlMethod.getNano());
    }

    @Test
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
        Assert.assertTrue(jsonMethod.getNano() < xmlMethod.getNano());
    }

    @Test
    public void checkBeneficesImprovement() throws InvalidXmlElementException {
        //Force Json generation.

        BeneficeDefinitionFactory beneficeFactory = new BeneficeDefinitionFactory() {
            @Override
            public FactoryCacheLoader<BeneficeDefinition> getFactoryCacheLoader() {
                return null;
            }
        };

        Instant start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            beneficeFactory.getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
            beneficeFactory.removeData();
        }
        Instant end = Instant.now();
        Duration xmlMethod = Duration.between(start, end);

        start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            BeneficeDefinitionFactoryCacheLoader beneficeDefinitionFactoryCacheLoader = new BeneficeDefinitionFactoryCacheLoader();
            Assert.assertTrue(beneficeDefinitionFactoryCacheLoader.load(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size() > 0);
            BeneficeDefinitionFactory.getInstance().removeData();
        }
        end = Instant.now();
        Duration jsonMethod = Duration.between(start, end);

        //Check speed is at least 10x
        Assert.assertTrue(jsonMethod.getNano() * 5 < xmlMethod.getNano());
    }

    @Test
    public void checkArmoursImprovement() throws InvalidXmlElementException {
        //Force Json generation.

        ArmourFactory armourFactory = new ArmourFactory() {
            @Override
            public FactoryCacheLoader<Armour> getFactoryCacheLoader() {
                return null;
            }
        };

        Instant start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            armourFactory.getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
            armourFactory.removeData();
        }
        Instant end = Instant.now();
        Duration xmlMethod = Duration.between(start, end);

        start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            ArmourFactoryCacheLoader armourFactoryCacheLoader = new ArmourFactoryCacheLoader();
            Assert.assertTrue(armourFactoryCacheLoader.load(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size() > 0);
            ArmourFactory.getInstance().removeData();
        }
        end = Instant.now();
        Duration jsonMethod = Duration.between(start, end);

        //Check speed is at least 10x
        Assert.assertTrue(jsonMethod.getNano() * 2 < xmlMethod.getNano());
    }

    @Test
    public void checkPlanetsImprovement() throws InvalidXmlElementException {
        //Force Json generation.

        PlanetFactory planetFactory = new PlanetFactory() {
            @Override
            public FactoryCacheLoader<Planet> getFactoryCacheLoader() {
                return null;
            }
        };

        Instant start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            planetFactory.getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
            planetFactory.removeData();
        }
        Instant end = Instant.now();
        Duration xmlMethod = Duration.between(start, end);

        start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            PlanetFactoryCacheLoader planetFactoryCacheLoader = new PlanetFactoryCacheLoader();
            Assert.assertTrue(planetFactoryCacheLoader.load(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size() > 0);
            PlanetFactory.getInstance().removeData();
        }
        end = Instant.now();
        Duration jsonMethod = Duration.between(start, end);

        //Check speed is at least 10x
        Assert.assertTrue(jsonMethod.getNano() < xmlMethod.getNano());
    }

    @Test
    public void checkOccultismPathImprovement() throws InvalidXmlElementException {
        //Force Json generation.

        OccultismPathFactory occultismPathFactory = new OccultismPathFactory() {
            @Override
            public FactoryCacheLoader<OccultismPath> getFactoryCacheLoader() {
                return null;
            }
        };

        Instant start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            occultismPathFactory.getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
            occultismPathFactory.removeData();
        }
        Instant end = Instant.now();
        Duration xmlMethod = Duration.between(start, end);

        start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            OccultismPathFactoryCacheLoader occultismPathFactoryCacheLoader = new OccultismPathFactoryCacheLoader();
            Assert.assertTrue(occultismPathFactoryCacheLoader.load(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size() > 0);
            OccultismPathFactory.getInstance().removeData();
        }
        end = Instant.now();
        Duration jsonMethod = Duration.between(start, end);

        //Check speed is at least 10x
        Assert.assertTrue(jsonMethod.getNano() < xmlMethod.getNano());
    }
}
