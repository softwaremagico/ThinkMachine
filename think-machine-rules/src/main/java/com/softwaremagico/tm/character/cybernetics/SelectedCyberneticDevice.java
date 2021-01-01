package com.softwaremagico.tm.character.cybernetics;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.IElementWithBonification;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.character.values.StaticValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SelectedCyberneticDevice extends Element<SelectedCyberneticDevice> implements ICyberneticDevice,
        IElementWithBonification {

    private List<CyberneticDeviceTrait> customizations;
    private final CyberneticDevice cyberneticDevice;

    public SelectedCyberneticDevice(CyberneticDevice cyberneticDevice) {
        super(cyberneticDevice.getId(), cyberneticDevice.getName(), cyberneticDevice.getDescription(), cyberneticDevice.getLanguage(),
                cyberneticDevice.getModuleName());
        this.cyberneticDevice = cyberneticDevice;
        customizations = new ArrayList<>();
    }

    @Override
    public int getPoints() {
        int basicPoints = getCyberneticDevice().getPoints();
        for (final CyberneticDeviceTrait customization : customizations) {
            basicPoints += customization.getExtraPoints();
        }
        return basicPoints;
    }

    @Override
    public int getIncompatibility() {
        int basicIncompatibility = getCyberneticDevice().getIncompatibility();
        for (final CyberneticDeviceTrait customization : customizations) {
            basicIncompatibility += customization.getExtraIncompatibility();
        }
        return basicIncompatibility;
    }

    @Override
    public CyberneticDeviceTrait getTrait(CyberneticDeviceTraitCategory category) {
        for (final CyberneticDeviceTrait customization : customizations) {
            if (customization.getCategory().equals(category)) {
                return customization;
            }
        }
        return getCyberneticDevice().getTrait(category);
    }

    public List<CyberneticDeviceTrait> getCustomizations() {
        return customizations;
    }

    @Override
    public int getCost() {
        int basicCost = getCyberneticDevice().getCost();
        for (final CyberneticDeviceTrait customization : customizations) {
            basicCost *= customization.getExtraCostMultiplier();
            basicCost += customization.getExtraCost();
        }
        return basicCost;
    }

    @Override
    public int getTechLevel() {
        int techLevel = getCyberneticDevice().getTechLevel();
        for (final CyberneticDeviceTrait customization : customizations) {
            techLevel = Math.max(techLevel, customization.getMinimumTechLevel());
        }
        return techLevel;
    }

    public CyberneticDevice getCyberneticDevice() {
        return cyberneticDevice;
    }

    @Override
    public Weapon getWeapon() {
        return getCyberneticDevice().getWeapon();
    }

    @Override
    public Set<StaticValue> getStaticValues() {
        return getCyberneticDevice().getStaticValues();
    }

    @Override
    public List<CyberneticDeviceTrait> getTraits() {
        final List<CyberneticDeviceTrait> traits = new ArrayList<>(getCyberneticDevice().getTraits());
        for (final CyberneticDeviceTrait trait : getCyberneticDevice().getTraits()) {
            for (final CyberneticDeviceTrait customization : getCustomizations()) {
                if (customization.getCategory() == trait.getCategory()) {
                    traits.remove(trait);
                }
            }
        }
        traits.addAll(customizations);
        Collections.sort(traits);
        return traits;
    }

    @Override
    public CyberneticDevice getRequirement() {
        return getCyberneticDevice().getRequirement();
    }

    @Override
    public Set<Bonification> getBonifications() {
        return getCyberneticDevice().getBonifications();
    }

    public void addCustomization(CyberneticDeviceTrait trait) {
        customizations.add(trait);
    }

    @Override
    public String toString() {
        return getCyberneticDevice().getId();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void setCustomizations(List<CyberneticDeviceTrait> customizations) {
        this.customizations = customizations;
    }

}
