package com.softwaremagico.tm.character.equipment;

import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public abstract class EquipmentSelector<E extends Equipment<?>> extends RandomSelector<E> {
	private Integer currentMoney = null;

	protected EquipmentSelector(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	protected int getCurrentMoney() {
		if (currentMoney == null) {
			currentMoney = getCharacterPlayer().getMoney();
		}
		return currentMoney;
	}

	/**
	 * Not so expensive weapons.
	 * 
	 * @param weapon
	 * @return
	 */
	protected abstract int getWeightCostModificator(E equipment);

	@Override
	protected int getWeight(E equipment) {
		// Weapons only if technology is enough.
		if (equipment.getTechLevel() > getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue()) {
			return NO_PROBABILITY;
		}

		// I can afford it.
		if (equipment.getCost() > getCurrentMoney()) {
			return NO_PROBABILITY;
		}

		return 1;
	}
}
