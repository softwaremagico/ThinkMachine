package com.softwaremagico.tm.file.modules;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2020 Softwaremagico
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
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTraitFactory;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.armours.ArmourSpecificationFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.AccessoryFactory;
import com.softwaremagico.tm.character.equipment.weapons.AmmunitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.*;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.character.values.SpecialValuesFactory;
import com.softwaremagico.tm.log.MachineLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class ModuleLoaderEnforcer {

    public static int loadAllFactories(String language, String moduleName) {
        final long startTime = System.nanoTime();
        final AtomicInteger loadedElements = new AtomicInteger(0);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        MachineLog.info(ModuleLoaderEnforcer.class.getName(), "Loading all factories...");

        //Primitive factories
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(AccessoryFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(AmmunitionFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(ArmourSpecificationFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(BeneficeDefinitionFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(BlessingFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(CharacteristicsDefinitionFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(CyberneticDeviceTraitFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(DamageTypeFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(OccultismDurationFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(OccultismRangeFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(OccultismTypeFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(RaceFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(ShieldFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(TheurgyComponentFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        futures = new ArrayList<>();

        //Second level
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(ArmourFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(CyberneticDeviceFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(FactionsFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        futures = new ArrayList<>();

        //Faction dependency.
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(OccultismPathFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(PlanetFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(SkillsDefinitionsFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(WeaponFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        futures = new ArrayList<>();

        //Depends on skills
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(CombatStyleFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        futures.add(CompletableFuture.runAsync(() -> {
            try {
                loadedElements.addAndGet(SpecialValuesFactory.getInstance().getElements(language, moduleName).size());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(ModuleLoaderEnforcer.class.getName(), e);
            }
        }));
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        final long duration = (System.nanoTime() - startTime);
        MachineLog.info(ModuleLoaderEnforcer.class.getName(), "All factories loaded! Total {} elements loaded in {} milliseconds.",
                loadedElements.get(), duration / 1000000);
        return loadedElements.get();
    }
}
