package com.softwaremagico.tm.character.equipment.armours;

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

import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.equipment.shields.Shield;

public class Armour extends Equipment<Armour> {
    private final int protection;
    private final Set<DamageType> damageTypes;
    private final ArmourPenalization standardPenalization;
    private final ArmourPenalization specialPenalization;
    private final Set<Shield> allowedShields;
    private final Set<ArmourSpecification> specifications;

    /**
     * For creating empty elements.
     */
    public Armour() {
        super();
        this.protection = 0;
        this.damageTypes = new HashSet<>();
        this.standardPenalization = new ArmourPenalization(0, 0, 0, 0);
        this.specialPenalization = new ArmourPenalization(0, 0, 0, 0);
        this.allowedShields = new HashSet<>();
        this.specifications = new HashSet<>();
    }

    public Armour(String id, String name, String description, String language, String moduleName, int techLevel, int protection, Set<DamageType> damageTypes, float cost) {
        super(id, name, description, cost, techLevel, language, moduleName);
        this.protection = protection;
        this.damageTypes = damageTypes;
        this.standardPenalization = new ArmourPenalization(0, 0, 0, 0);
        this.specialPenalization = new ArmourPenalization(0, 0, 0, 0);
        this.allowedShields = new HashSet<>();
        this.specifications = new HashSet<>();
    }

    public Armour(String id, String name, String description, String language, String moduleName, int techLevel, int protection, Set<DamageType> damageTypes,
                  ArmourPenalization specialPenalization, ArmourPenalization otherPenalization, Set<Shield> allowedShields, Set<ArmourSpecification> specifications,
                  float cost) {
        super(id, name, description, cost, techLevel, language, moduleName);
        this.protection = protection;
        this.damageTypes = damageTypes;
        this.standardPenalization = specialPenalization;
        this.specialPenalization = otherPenalization;
        this.allowedShields = allowedShields;
        this.specifications = specifications;
    }

    public int getProtection() {
        return protection;
    }

    public Set<DamageType> getDamageTypes() {
        return damageTypes;
    }

    public ArmourPenalization getStandardPenalization() {
        return standardPenalization;
    }

    public ArmourPenalization getSpecialPenalization() {
        return specialPenalization;
    }

    public Set<Shield> getAllowedShields() {
        return allowedShields;
    }

    public boolean isHeavy() {
        return standardPenalization.getDexterityModification() > 0 || standardPenalization.getStrengthModification() > 0
                || standardPenalization.getEnduranceModification() > 0;
    }

    public Set<ArmourSpecification> getSpecifications() {
        return specifications;
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
