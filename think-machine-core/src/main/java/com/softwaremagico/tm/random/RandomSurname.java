package com.softwaremagico.tm.random;

import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.race.InvalidRaceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;

public class RandomSurname extends RandomSelector<Surname> {
	private final static int GOOD_PROBABILITY = 1;

	protected RandomSurname(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignSurname() throws InvalidRaceException, InvalidRandomElementSelectedException {
		getCharacterPlayer().getInfo().setSurname(selectElementByWeight());
	}

	@Override
	protected TreeMap<Integer, Surname> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, Surname> weightedSurnames = new TreeMap<>();
		int count = 1;
		for (Surname surname : FactionsFactory.getInstance().getAllSurnames()) {
			int weight = getWeight(surname);
			if (weight > 0) {
				weightedSurnames.put(count, surname);
				count += weight;
			}
		}
		return weightedSurnames;
	}

	@Override
	protected int getWeight(Surname surname) {
		// Nobility has faction as surname
		if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY) {
			if (getCharacterPlayer().getFaction().getName().contains(surname.getName())) {
				return GOOD_PROBABILITY;
			} else {
				return 0;
			}
		}
		// Not nobility, use surnames of the planet.
		if (getCharacterPlayer().getInfo().getPlanet() != null && !getCharacterPlayer().getInfo().getPlanet().getSurnames().isEmpty()) {
			if (getCharacterPlayer().getInfo().getPlanet().getFactions().contains(surname.getFaction())) {
				return GOOD_PROBABILITY;
			} else {
				return 0;
			}
		}
		return 1;
	}
}
