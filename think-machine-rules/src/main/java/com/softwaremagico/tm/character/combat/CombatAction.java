package com.softwaremagico.tm.character.combat;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.values.IValue;

import java.util.Objects;
import java.util.Set;

public class CombatAction extends Element<CombatAction> {
    private final String goal;
    private final String damage;
    private final String others;
    private final Set<CombatActionRequirement> requirements;

    public CombatAction(String id, String name, String description, String language, String moduleName, String goal, String damage, String others,
                        Set<CombatActionRequirement> requirements) {
        super(id, name, description, language, moduleName);
        this.goal = goal;
        this.damage = damage;
        this.others = others;
        this.requirements = requirements;
    }

    public String getGoal() {
        return goal;
    }

    public String getDamage() {
        return damage;
    }

    public String getOthers() {
        return others;
    }

    public Set<CombatActionRequirement> getRequirements() {
        return requirements;
    }

    public boolean isAvailable(CharacterPlayer characterPlayer) {
        for (final CombatActionRequirement requirement : getRequirements()) {
            boolean allowed = false;
            for (final IValue restriction : requirement.getRequirements()) {
                if (restriction instanceof AvailableSkill) {
                    if (characterPlayer.getSkillTotalRanks((AvailableSkill) restriction) >= requirement.getValue()) {
                        allowed = true;
                    }
                } else if (restriction instanceof CharacteristicDefinition) {
                    if (characterPlayer.getCharacteristic(restriction.getId()).getValue() >= requirement.getValue()) {
                        allowed = true;
                    }
                }
            }
            if (!allowed) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRestriction(String skillId) {
        for (final CombatActionRequirement requirement : getRequirements()) {
            for (final IValue restriction : requirement.getRequirements()) {
                if (restriction instanceof AvailableSkill) {
                    if (Objects.equals(restriction.getId(), skillId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isMeleeAction() {
        return checkRestriction("melee");
    }

    public boolean isFightAction() {
        return checkRestriction("fight");
    }

    public boolean isShootAction() {
        return checkRestriction("slugGuns") || checkRestriction("energyGuns");
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
