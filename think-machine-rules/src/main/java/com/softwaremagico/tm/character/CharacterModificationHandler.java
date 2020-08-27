package com.softwaremagico.tm.character;

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
	private Set<ICyberneticDeviceUpdated> cyberneticDeviceUpdatedListeners;
	private Set<IEquipmentUpdated> equipmentUpdatedListeners;

	public CharacterModificationHandler() {
		resetListeners();
	}

	public interface ISkillUpdated {
		public void updated(AvailableSkill skill, int rankModifications);
	}

	public interface ICharacteristicUpdated {
		public void updated(Characteristic skill, int rankModifications);
	}

	public interface IBlessingUpdated {
		public void updated(Blessing blessing, boolean removed);
	}

	public interface IBeneficesUpdated {
		public void updated(AvailableBenefice benefice, boolean removed);
	}

	public interface IOccultismLevelUpdated {
		public void updated(OccultismType occultismType, int psyValue);
	}

	public interface IOccultismPowerUpdated {
		public void updated(OccultismPower power, boolean removed);
	}

	public interface ICyberneticDeviceUpdated {
		public void updated(SelectedCyberneticDevice device, boolean removed);
	}

	public interface IEquipmentUpdated {
		public void updated(Equipment<?> equipment, boolean removed);
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

	public void addCyberneticDeviceUpdatedListener(ICyberneticDeviceUpdated listener) {
		cyberneticDeviceUpdatedListeners.add(listener);
	}

	public void addEquipmentUpdatedListener(IEquipmentUpdated listener) {
		equipmentUpdatedListeners.add(listener);
	}

	public void launchSkillUpdatedListener(AvailableSkill skill, int rankModifications) {
		for (final ISkillUpdated listener : skillUpdatedListeners) {
			listener.updated(skill, rankModifications);
		}
	}

	public void launchCharacteristicUpdatedListener(Characteristic characteristic, int rankModifications) {
		for (final ICharacteristicUpdated listener : characteristicUpdatedListeners) {
			listener.updated(characteristic, rankModifications);
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

	public void launchOccultismLevelUpdatedListener(OccultismType occultismType, int psyValue) {
		for (final IOccultismLevelUpdated listener : occultismLevelUpdatedListeners) {
			listener.updated(occultismType, psyValue);
		}
	}

	public void launchOccultismPowerUpdatedListener(OccultismPower power, boolean removed) {
		for (final IOccultismPowerUpdated listener : occultismPowerUpdatedListeners) {
			listener.updated(power, removed);
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
