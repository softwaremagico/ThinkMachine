package com.softwaremagico.tm.character;

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

import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.skills.AvailableSkill;

import java.util.HashSet;
import java.util.Set;

public class CharacterModificationHandler {
    private Set<ISkillUpdated> skillUpdatedListeners;
    private Set<ICharacteristicUpdated> characteristicUpdatedListeners;
    private Set<IBlessingUpdated> blessingUpdatedListeners;
    private Set<IBeneficesUpdated> beneficesUpdatedListeners;
    private Set<IOccultismLevelUpdated> occultismLevelUpdatedListeners;
    private Set<IOccultismPowerUpdated> occultismPowerUpdatedListeners;
    private Set<IWyrdUpdated> wyrdUpdatedListener;
    private Set<ICyberneticDeviceUpdated> cyberneticDeviceUpdatedListeners;
    private Set<IEquipmentUpdated> equipmentUpdatedListeners;
    private Set<IInitialFirebirdsUpdated> firebirdsUpdatedListeners;

    public CharacterModificationHandler() {
        resetListeners();
    }

    public interface ISkillUpdated {
        void updated(AvailableSkill skill, int previousRank, int newRank, int minimumRank);
    }

    public interface ICharacteristicUpdated {
        void updated(Characteristic characteristic, int previousRank, int newRank, int minimumRank);
    }

    public interface IBlessingUpdated {
        void updated(Blessing blessing, boolean removed);
    }

    public interface IBeneficesUpdated {
        void updated(AvailableBenefice benefice, boolean removed);
    }

    public interface IOccultismLevelUpdated {
        void updated(OccultismType occultismType, int previousPsyValue, int newPsyValue, int minimumPsyValue);
    }

    public interface IOccultismPowerUpdated {
        void updated(OccultismPower power, boolean removed);
    }

    public interface IWyrdUpdated {
        void updated(int wyrdValue);
    }

    public interface ICyberneticDeviceUpdated {
        void updated(SelectedCyberneticDevice device, boolean removed);
    }

    public interface IEquipmentUpdated {
        void updated(Equipment<?> equipment, boolean removed);
    }

    public interface IInitialFirebirdsUpdated {
        void updated(float initialMoney);
    }

    public void resetListeners() {
        skillUpdatedListeners = new HashSet<>();
        characteristicUpdatedListeners = new HashSet<>();
        blessingUpdatedListeners = new HashSet<>();
        beneficesUpdatedListeners = new HashSet<>();
        occultismLevelUpdatedListeners = new HashSet<>();
        occultismPowerUpdatedListeners = new HashSet<>();
        cyberneticDeviceUpdatedListeners = new HashSet<>();
        equipmentUpdatedListeners = new HashSet<>();
        wyrdUpdatedListener = new HashSet<>();
        firebirdsUpdatedListeners = new HashSet<>();
    }

    public ISkillUpdated addSkillUpdateListener(ISkillUpdated listener) {
        skillUpdatedListeners.add(listener);
        return listener;
    }

    public void removeSkillUpdateListener(ISkillUpdated listener) {
        skillUpdatedListeners.remove(listener);
    }

    public ICharacteristicUpdated addCharacteristicUpdatedListener(ICharacteristicUpdated listener) {
        characteristicUpdatedListeners.add(listener);
        return listener;
    }

    public void removeCharacteristicUpdatedListener(ICharacteristicUpdated listener) {
        characteristicUpdatedListeners.remove(listener);
    }

    public IBlessingUpdated addBlessingUpdatedListener(IBlessingUpdated listener) {
        blessingUpdatedListeners.add(listener);
        return listener;
    }

    public void removeBlessingUpdatedListener(IBlessingUpdated listener) {
        blessingUpdatedListeners.remove(listener);
    }

    public IBeneficesUpdated addBeneficesUpdatedListener(IBeneficesUpdated listener) {
        beneficesUpdatedListeners.add(listener);
        return listener;
    }

    public void removeBeneficesUpdatedListener(IBeneficesUpdated listener) {
        beneficesUpdatedListeners.remove(listener);
    }

    public IOccultismLevelUpdated addOccultismLevelUpdatedListener(IOccultismLevelUpdated listener) {
        occultismLevelUpdatedListeners.add(listener);
        return listener;
    }

    public void removeOccultismLevelUpdatedListener(IOccultismLevelUpdated listener) {
        occultismLevelUpdatedListeners.remove(listener);
    }

    public IOccultismPowerUpdated addOccultismPowerUpdatedListener(IOccultismPowerUpdated listener) {
        occultismPowerUpdatedListeners.add(listener);
        return listener;
    }

    public void removeOccultismPowerUpdatedListener(IOccultismPowerUpdated listener) {
        occultismPowerUpdatedListeners.remove(listener);
    }

    public IWyrdUpdated addWyrdUpdatedListener(IWyrdUpdated listener) {
        wyrdUpdatedListener.add(listener);
        return listener;
    }

    public void removeWyrdUpdatedListener(IWyrdUpdated listener) {
        wyrdUpdatedListener.remove(listener);
    }

    public ICyberneticDeviceUpdated addCyberneticDeviceUpdatedListener(ICyberneticDeviceUpdated listener) {
        cyberneticDeviceUpdatedListeners.add(listener);
        return listener;
    }

    public void removeCyberneticDeviceUpdatedListener(ICyberneticDeviceUpdated listener) {
        cyberneticDeviceUpdatedListeners.remove(listener);
    }

    public IEquipmentUpdated addEquipmentUpdatedListener(IEquipmentUpdated listener) {
        equipmentUpdatedListeners.add(listener);
        return listener;
    }

    public void removeEquipmentUpdatedListener(IEquipmentUpdated listener) {
        equipmentUpdatedListeners.remove(listener);
    }

    public IInitialFirebirdsUpdated addFirebirdsUpdatedListener(IInitialFirebirdsUpdated listener) {
        firebirdsUpdatedListeners.add(listener);
        return listener;
    }

    public void removeFirebirdsUpdatedListener(IInitialFirebirdsUpdated listener) {
        firebirdsUpdatedListeners.remove(listener);
    }

    public void launchSkillUpdatedListener(AvailableSkill skill, int previousRank, int newRank, int minimumRank) {
        for (final ISkillUpdated listener : skillUpdatedListeners) {
            listener.updated(skill, previousRank, newRank, minimumRank);
        }
    }

    public void launchCharacteristicUpdatedListener(Characteristic characteristic, int previousRank, int newRank, int minimumRank) {
        for (final ICharacteristicUpdated listener : characteristicUpdatedListeners) {
            listener.updated(characteristic, previousRank, newRank, minimumRank);
        }
    }

    public void launchBlessingUpdatedListener(Blessing blessing, boolean removed) {
        for (final IBlessingUpdated listener : blessingUpdatedListeners) {
            listener.updated(blessing, removed);
        }
    }

    public void launchBeneficesUpdatedListener(AvailableBenefice benefice, boolean removed) {
        for (final IBeneficesUpdated listener : beneficesUpdatedListeners) {
            listener.updated(benefice, removed);
        }
    }

    public void launchOccultismLevelUpdatedListener(OccultismType occultismType, int previousPsyValue, int newPsyValue, int minimumPsyValue) {
        for (final IOccultismLevelUpdated listener : occultismLevelUpdatedListeners) {
            listener.updated(occultismType, previousPsyValue, newPsyValue, minimumPsyValue);
        }
    }

    public void launchOccultismPowerUpdatedListener(OccultismPower power, boolean removed) {
        for (final IOccultismPowerUpdated listener : occultismPowerUpdatedListeners) {
            listener.updated(power, removed);
        }
    }

    public void launchWyrdUpdatedListener(int wyrdValue) {
        for (final IWyrdUpdated listener : wyrdUpdatedListener) {
            listener.updated(wyrdValue);
        }
    }

    public void launchCyberneticDeviceUpdatedListener(SelectedCyberneticDevice device, boolean removed) {
        for (final ICyberneticDeviceUpdated listener : cyberneticDeviceUpdatedListeners) {
            listener.updated(device, removed);
        }
    }

    public void launchEquipmentUpdatedListener(Equipment<?> equipment, boolean removed) {
        for (final IEquipmentUpdated listener : equipmentUpdatedListeners) {
            listener.updated(equipment, removed);
        }
    }

    public void launchInitialFirebirdsUpdatedListener(float newInitialCash) {
        for (final IInitialFirebirdsUpdated listener : firebirdsUpdatedListeners) {
            listener.updated(newInitialCash);
        }
    }

}
