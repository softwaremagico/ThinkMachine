package com.softwaremagico.tm.character.creation;

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

import java.util.HashSet;
import java.util.Set;

public class CostCalculatorModificationHandler {
    private Set<ICurrentCharacteristicPointsUpdatedListener> characteristicPointsUpdatedListeners;
    private Set<ICurrentCharacteristicExtraPointsUpdatedListener> characteristicExtraPointsUpdatedListeners;
    private Set<ICurrentSkillsPointsUpdatedListener> skillsPointsUpdatedListeners;
    private Set<ICurrentSkillsExtraPointsUpdatedListener> skillsExtraPointsUpdatedListeners;
    private Set<ICurrentTraitsPointsUpdatedListener> traitsPointsUpdatedListeners;
    private Set<ICurrentTraitsExtraPointsUpdatedListener> traitsExtraPointsUpdatedListeners;
    private Set<ICurrentOccultismLevelExtraPointUpdatedListener> occultismLevelExtraPointUpdatedListeners;
    private Set<ICurrentOccultismPowerExtraPointUpdatedListener> occultismPowerExtraPointUpdatedListeners;
    private Set<ICurrentWyrdExtraPointUpdatedListener> wyrdExtraPointUpdatedListeners;
    private Set<ICurrentCyberneticExtraPointsListener> cyberneticExtraPointsListeners;
    private Set<ICurrentFirebirdSpendListener> firebirdSpendListeners;
    private Set<IInitialFirebirdsUpdated> initialFirebirdsUpdatedListeners;
    private Set<IExtraPointUpdatedListener> extraPointUpdatedListeners;


    public interface ICurrentCharacteristicPointsUpdatedListener {
        void updated(int value);
    }

    public interface ICurrentCharacteristicExtraPointsUpdatedListener {
        void updated(int value);
    }

    public interface ICurrentSkillsPointsUpdatedListener {
        void updated(int value);
    }

    public interface ICurrentSkillsExtraPointsUpdatedListener {
        void updated(int value);
    }

    public interface ICurrentTraitsPointsUpdatedListener {
        void updated(int value);
    }

    public interface ICurrentTraitsExtraPointsUpdatedListener {
        void updated(int value);
    }

    public interface ICurrentOccultismLevelExtraPointUpdatedListener {
        void updated(int value);
    }

    public interface ICurrentOccultismPowerExtraPointUpdatedListener {
        void updated(int value);
    }

    public interface ICurrentWyrdExtraPointUpdatedListener {
        void updated(int value);
    }

    public interface ICurrentCyberneticExtraPointsListener {
        void updated(int value);
    }

    public interface ICurrentFirebirdSpendListener {
        void updated(float value);
    }

    public interface IExtraPointUpdatedListener {
        void updated();
    }

    public interface IInitialFirebirdsUpdated {
        void updated(float initialMoney);
    }

    public CostCalculatorModificationHandler() {
        resetListeners();
    }

    public void resetListeners() {
        characteristicPointsUpdatedListeners = new HashSet<>();
        characteristicExtraPointsUpdatedListeners = new HashSet<>();
        skillsPointsUpdatedListeners = new HashSet<>();
        skillsExtraPointsUpdatedListeners = new HashSet<>();
        traitsPointsUpdatedListeners = new HashSet<>();
        traitsExtraPointsUpdatedListeners = new HashSet<>();
        occultismLevelExtraPointUpdatedListeners = new HashSet<>();
        occultismPowerExtraPointUpdatedListeners = new HashSet<>();
        wyrdExtraPointUpdatedListeners = new HashSet<>();
        cyberneticExtraPointsListeners = new HashSet<>();
        firebirdSpendListeners = new HashSet<>();
        initialFirebirdsUpdatedListeners = new HashSet<>();
        extraPointUpdatedListeners = new HashSet<>();
    }

    public IExtraPointUpdatedListener addExtraPointsUpdatedListeners(IExtraPointUpdatedListener listener) {
        extraPointUpdatedListeners.add(listener);
        return listener;
    }

    public void removeExtraPointsUpdatedListeners(IExtraPointUpdatedListener listener) {
        extraPointUpdatedListeners.remove(listener);
    }

    public void launchExtraPointsUpdatedListeners() {
        for (final IExtraPointUpdatedListener listener : extraPointUpdatedListeners) {
            listener.updated();
        }
    }

    public ICurrentCharacteristicPointsUpdatedListener addCharacteristicPointsUpdatedListeners(ICurrentCharacteristicPointsUpdatedListener listener) {
        characteristicPointsUpdatedListeners.add(listener);
        return listener;
    }

    public void removeCharacteristicPointsUpdatedListeners(ICurrentCharacteristicPointsUpdatedListener listener) {
        characteristicPointsUpdatedListeners.remove(listener);
    }

    public void launchCharacteristicPointsUpdatedListeners(int value) {
        if (value != 0) {
            for (final ICurrentCharacteristicPointsUpdatedListener listener : characteristicPointsUpdatedListeners) {
                listener.updated(value);
            }
        }
    }

    public ICurrentCharacteristicExtraPointsUpdatedListener addCharacteristicExtraPointsUpdatedListeners(
            ICurrentCharacteristicExtraPointsUpdatedListener listener) {
        characteristicExtraPointsUpdatedListeners.add(listener);
        return listener;
    }

    public void removeCharacteristicExtraPointsUpdatedListeners(ICurrentCharacteristicExtraPointsUpdatedListener listener) {
        characteristicExtraPointsUpdatedListeners.remove(listener);
    }

    public void launchCharacteristicExtraPointsUpdatedListeners(int value) {
        if (value != 0) {
            for (final ICurrentCharacteristicExtraPointsUpdatedListener listener : characteristicExtraPointsUpdatedListeners) {
                listener.updated(value);
            }
        }
    }

    public ICurrentSkillsPointsUpdatedListener addSkillsPointsUpdatedListeners(ICurrentSkillsPointsUpdatedListener listener) {
        skillsPointsUpdatedListeners.add(listener);
        return listener;
    }

    public void removeSkillsPointsUpdatedListeners(ICurrentSkillsPointsUpdatedListener listener) {
        skillsPointsUpdatedListeners.remove(listener);
    }

    public void launchSkillsPointsUpdatedListeners(int value) {
        if (value != 0) {
            for (final ICurrentSkillsPointsUpdatedListener listener : skillsPointsUpdatedListeners) {
                listener.updated(value);
            }
        }
    }

    public ICurrentSkillsExtraPointsUpdatedListener addSkillsExtraPointsUpdatedListeners(ICurrentSkillsExtraPointsUpdatedListener listener) {
        skillsExtraPointsUpdatedListeners.add(listener);
        return listener;
    }

    public void removeSkillsExtraPointsUpdatedListeners(ICurrentSkillsExtraPointsUpdatedListener listener) {
        skillsExtraPointsUpdatedListeners.remove(listener);
    }

    public void launchSkillsExtraPointsUpdatedListeners(int value) {
        if (value != 0) {
            for (final ICurrentSkillsExtraPointsUpdatedListener listener : skillsExtraPointsUpdatedListeners) {
                listener.updated(value);
            }
        }
    }

    public ICurrentTraitsPointsUpdatedListener addTraitsPointsUpdatedListeners(ICurrentTraitsPointsUpdatedListener listener) {
        traitsPointsUpdatedListeners.add(listener);
        return listener;
    }

    public void removeTraitsPointsUpdatedListeners(ICurrentTraitsPointsUpdatedListener listener) {
        traitsPointsUpdatedListeners.remove(listener);
    }

    public void launchTraitsPointsUpdatedListeners(int value) {
        if (value != 0) {
            for (final ICurrentTraitsPointsUpdatedListener listener : traitsPointsUpdatedListeners) {
                listener.updated(value);
            }
        }
    }

    public ICurrentTraitsExtraPointsUpdatedListener addTraitsExtraPointsUpdatedListeners(ICurrentTraitsExtraPointsUpdatedListener listener) {
        traitsExtraPointsUpdatedListeners.add(listener);
        return listener;
    }

    public void removeTraitsExtraPointsUpdatedListeners(ICurrentTraitsExtraPointsUpdatedListener listener) {
        traitsExtraPointsUpdatedListeners.remove(listener);
    }

    public void launchTraitsExtraPointsUpdatedListeners(int value) {
        if (value != 0) {
            for (final ICurrentTraitsExtraPointsUpdatedListener listener : traitsExtraPointsUpdatedListeners) {
                listener.updated(value);
            }
        }
    }

    public ICurrentOccultismLevelExtraPointUpdatedListener addOccultismLevelExtraPointUpdatedListeners(
            ICurrentOccultismLevelExtraPointUpdatedListener listener) {
        occultismLevelExtraPointUpdatedListeners.add(listener);
        return listener;
    }

    public void removeOccultismLevelExtraPointUpdatedListeners(ICurrentOccultismLevelExtraPointUpdatedListener listener) {
        occultismLevelExtraPointUpdatedListeners.remove(listener);
    }

    public void launchOccultismLevelExtraPointUpdatedListeners(int value) {
        if (value != 0) {
            for (final ICurrentOccultismLevelExtraPointUpdatedListener listener : occultismLevelExtraPointUpdatedListeners) {
                listener.updated(value);
            }
        }
    }

    public ICurrentOccultismPowerExtraPointUpdatedListener addOccultismPowerExtraPointUpdatedListeners(
            ICurrentOccultismPowerExtraPointUpdatedListener listener) {
        occultismPowerExtraPointUpdatedListeners.add(listener);
        return listener;
    }

    public void removeOccultismPowerExtraPointUpdatedListeners(ICurrentOccultismPowerExtraPointUpdatedListener listener) {
        occultismPowerExtraPointUpdatedListeners.remove(listener);
    }

    public void launchOccultismPowerExtraPointUpdatedListeners(int value) {
        if (value != 0) {
            for (final ICurrentOccultismPowerExtraPointUpdatedListener listener : occultismPowerExtraPointUpdatedListeners) {
                listener.updated(value);
            }
        }
    }

    public ICurrentWyrdExtraPointUpdatedListener addWyrdExtraPointUpdatedListeners(ICurrentWyrdExtraPointUpdatedListener listener) {
        wyrdExtraPointUpdatedListeners.add(listener);
        return listener;
    }

    public void removeWyrdExtraPointUpdatedListeners(ICurrentWyrdExtraPointUpdatedListener listener) {
        wyrdExtraPointUpdatedListeners.remove(listener);
    }

    public void launchWyrdExtraPointUpdatedListeners(int value) {
        for (final ICurrentWyrdExtraPointUpdatedListener listener : wyrdExtraPointUpdatedListeners) {
            listener.updated(value);
        }
    }

    public void removeCyberneticExtraPointsListeners(ICurrentCyberneticExtraPointsListener listener) {
        cyberneticExtraPointsListeners.remove(listener);
    }

    public void launchCyberneticExtraPointsListeners(int value) {
        if (value != 0) {
            for (final ICurrentCyberneticExtraPointsListener listener : cyberneticExtraPointsListeners) {
                listener.updated(value);
            }
        }
    }

    public ICurrentFirebirdSpendListener addFirebirdSpendListeners(ICurrentFirebirdSpendListener listener) {
        firebirdSpendListeners.add(listener);
        return listener;
    }

    public void removeFirebirdSpendListeners(ICurrentFirebirdSpendListener listener) {
        firebirdSpendListeners.remove(listener);
    }

    public void launchFirebirdSpendListeners(float value) {
        if (value != 0) {
            for (final ICurrentFirebirdSpendListener listener : firebirdSpendListeners) {
                listener.updated(value);
            }
        }
    }

    public IInitialFirebirdsUpdated addInitialFirebirdListeners(IInitialFirebirdsUpdated listener) {
        initialFirebirdsUpdatedListeners.add(listener);
        return listener;
    }

    public void removeInitialFirebirdListeners(IInitialFirebirdsUpdated listener) {
        initialFirebirdsUpdatedListeners.remove(listener);
    }

    public void launchInitialFirebirdListeners(float value) {
        if (value != 0) {
            for (final IInitialFirebirdsUpdated listener : initialFirebirdsUpdatedListeners) {
                listener.updated(value);
            }
        }
    }
}
