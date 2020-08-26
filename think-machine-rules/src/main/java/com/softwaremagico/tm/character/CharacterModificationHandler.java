package com.softwaremagico.tm.character;

import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.skills.AvailableSkill;

public class CharacterModificationHandler {
	private Set<ISkillUpdated> skillUpdateListeners;
	private Set<ICharacteristicUpdated> characteristicUpdateListeners;
	private Set<IBlessingUpdated> blessingUpdateListeners;
	private Set<IBeneficesUpdated> beneficesUpdateListeners;

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
		public void updated(Blessing blessing);
	}

	public interface IBeneficesUpdated {
		public void updated(AvailableBenefice benefice);
	}

	public void resetListeners() {
		skillUpdateListeners = new HashSet<>();
		characteristicUpdateListeners = new HashSet<>();
		blessingUpdateListeners = new HashSet<>();
		beneficesUpdateListeners = new HashSet<>();
	}

	public void addSkillUpdateListener(ISkillUpdated listener) {
		skillUpdateListeners.add(listener);
	}

	public void addCharacteristicUpdateListener(ICharacteristicUpdated listener) {
		characteristicUpdateListeners.add(listener);
	}

	public void launchSkillUpdateListener(AvailableSkill skill, int rankModifications) {
		for (ISkillUpdated listener : skillUpdateListeners) {
			listener.updated(skill, rankModifications);
		}
	}

	public void launchCharacteristicUpdateListener(Characteristic characteristic, int rankModifications) {
		for (ICharacteristicUpdated listener : characteristicUpdateListeners) {
			listener.updated(characteristic, rankModifications);
		}
	}
}
