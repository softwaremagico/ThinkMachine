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
import com.softwaremagico.tm.character.races.Race;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CombatStyle extends Element<CombatStyle> {
    public static final int COMBAT_STYLE_COST = 5;
    private final CombatStyleGroup group;
    private final List<CombatStance> combatStances;
    private List<CombatAction> combatActions;
    private final Set<Race> restrictedRaces;

    public CombatStyle(String id, String name, String description, String language, String moduleName, CombatStyleGroup group) {
        super(id, name, description, language, moduleName);
        combatActions = new ArrayList<CombatAction>();
        this.group = group;
        combatStances = new ArrayList<>();
        restrictedRaces = new HashSet<>();
    }

    public int getCost() {
        return COMBAT_STYLE_COST;
    }

    public CombatStyleGroup getGroup() {
        return group;
    }

    public void addCombatStance(CombatStance stance) {
        combatStances.add(stance);
        Collections.sort(combatStances);
    }

    public List<CombatStance> getCombatStances() {
        return Collections.unmodifiableList(combatStances);
    }

    public CombatStance getCombatStance(String stanceId) {
        for (final CombatStance stance : combatStances) {
            if (stance.getId().equalsIgnoreCase(stanceId)) {
                return stance;
            }
        }
        return null;
    }

    public boolean canUseCombatStyle(CharacterPlayer characterPlayer) {
        if (characterPlayer == null || !isAvailable(characterPlayer)) {
            return false;
        }
        final AtomicBoolean canUse = new AtomicBoolean(false);
        getCombatActions().forEach(combatAction -> {
            if (combatAction.isAvailable(characterPlayer)) {
                canUse.set(true);
            }
        });
        return canUse.get();
    }

    public boolean isAvailable(CharacterPlayer characterPlayer) {
        return getRestrictedRaces().isEmpty() || (characterPlayer.getRace() != null && getRestrictedRaces().contains(characterPlayer.getRace()));
    }

    public void addCombatAction(CombatAction combatAction) {
        combatActions.add(combatAction);
        Collections.sort(combatActions);
    }

    public List<CombatAction> getCombatActions() {
        return Collections.unmodifiableList(combatActions);
    }

    public CombatAction getCombatAction(String actionId) {
        for (final CombatAction action : combatActions) {
            if (action.getId().equalsIgnoreCase(actionId)) {
                return action;
            }
        }
        return null;
    }

    public void addRestrictedRace(Race race) {
        restrictedRaces.add(race);
    }

    public Set<Race> getRestrictedRaces() {
        return restrictedRaces;
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
