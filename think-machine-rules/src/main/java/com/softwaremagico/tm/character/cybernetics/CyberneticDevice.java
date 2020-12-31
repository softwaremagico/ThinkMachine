package com.softwaremagico.tm.character.cybernetics;

import java.util.ArrayList;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.ElementClassification;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.IElementWithBonification;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.character.values.StaticValue;
import com.softwaremagico.tm.log.MachineLog;

public class CyberneticDevice extends Element<CyberneticDevice> implements IElementWithBonification, ICyberneticDevice {
    private final int points;
    private final int incompatibility;
    private final int cost;
    private final int techLevel;
    private final String requirement;
    private final Weapon weapon;
    private final List<CyberneticDeviceTrait> traits;
    private final Set<Bonification> bonifications;
    private final Set<StaticValue> staticValues;
    private final ElementClassification classification;

    public CyberneticDevice(String id, String name, String description, String language, String moduleName, int points,
                            int incompatibility, int cost, int techLevel, String requirement, Weapon weapon,
                            Set<CyberneticDeviceTrait> traits, Set<Bonification> bonifications, Set<StaticValue> staticValues,
                            ElementClassification classification) {
        super(id, name, description, language, moduleName);
        this.points = points;
        this.incompatibility = incompatibility;
        this.cost = cost;
        this.techLevel = techLevel;
        this.traits = new ArrayList<>(traits);
        Collections.sort(this.traits);
        this.requirement = requirement;
        this.weapon = weapon;
        this.bonifications = bonifications;
        this.staticValues = staticValues;
        this.classification = classification;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public int getIncompatibility() {
        return incompatibility;
    }

    @Override
    public CyberneticDeviceTrait getTrait(CyberneticDeviceTraitCategory category) {
        for (final CyberneticDeviceTrait trait : traits) {
            if (trait.getCategory().equals(category)) {
                return trait;
            }
        }
        return null;
    }

    @Override
    public CyberneticDevice getRequirement() {
        if (requirement != null) {
            try {
                return CyberneticDeviceFactory.getInstance().getElement(requirement, getLanguage(), getModuleName());
            } catch (InvalidXmlElementException e) {
                MachineLog.errorMessage(this.getClass().getName(), e);
            }
        }
        return null;
    }

    @Override
    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public int getTechLevel() {
        return techLevel;
    }

    @Override
    public Set<Bonification> getBonifications() {
        return bonifications;
    }

    @Override
    public Set<StaticValue> getStaticValues() {
        return staticValues;
    }

    @Override
    public List<CyberneticDeviceTrait> getTraits() {
        return traits;
    }

    public ElementClassification getClassification() {
        return classification;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
