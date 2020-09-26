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

import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.character.equipment.Equipment;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.skills.AvailableSkill;

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
    }

    public void addSkillUpdateListener(ISkillUpdated listener) {
        skillUpdatedListeners.add(listener);
    }

    public void addCharacteristicUpdatedListener(ICharacteristicUpdated listener) {
        characteristicUpdatedListeners.add(listener);
    }

    public void addBlessingUpdatedListener(IBlessingUpdated listener) {
        blessingUpdatedListeners.add(listener);
    }

    public void addBeneficesUpdatedListener(IBeneficesUpdated listener) {
        beneficesUpdatedListeners.add(listener);
    }

    public void addOccultismLevelUpdatedListener(IOccultismLevelUpdated listener) {
        occultismLevelUpdatedListeners.add(listener);
    }

    public void addOccultismPowerUpdatedListener(IOccultismPowerUpdated listener) {
        occultismPowerUpdatedListeners.add(listener);
    }

    public void addWyrdUpdatedListener(IWyrdUpdated listener) {
        wyrdUpdatedListener.add(listener);
    }

    public void addCyberneticDeviceUpdatedListener(ICyberneticDeviceUpdated listener) {
        cyberneticDeviceUpdatedListeners.add(listener);
    }

    public void addEquipmentUpdatedListener(IEquipmentUpdated listener) {
        equipmentUpdatedListeners.add(listener);
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

}
