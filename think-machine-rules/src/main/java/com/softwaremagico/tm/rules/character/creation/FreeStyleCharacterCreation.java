package com.softwaremagico.tm.rules.character.creation;

import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.rules.character.races.Race;
import com.softwaremagico.tm.rules.character.races.RaceFactory;
import com.softwaremagico.tm.rules.log.MachineLog;

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

public class FreeStyleCharacterCreation {
	// Human is used as a base in rules.
	private static Race human;
	private static final int MIN_INITIAL_NATURAL_SKILL_VALUE = 3;
	private static final int MIN_INITIAL_CHARACTERISTICS_VALUE = 3;
	private static final int MAX_INITIAL_SKILL_VALUE = 8;
	private static final int MAX_INITIAL_CHARACTERISTIC_VALUE = 8;
	private static final int CHARACTERISTICS_POINTS = 20;
	private static final int SKILLS_POINTS = 30;
	private static final int TRAITS_POINTS = 10;
	private static final int FREE_AVAILABLE_POINTS = 40;
	private static final int MAX_CURSE_POINTS = 7;
	private static final int MAX_BLESSING_MODIFICATIONS = 7;

	static {
		try {
			human = RaceFactory.getInstance().getElement("human", "en");
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(FreeStyleCharacterCreation.class.getName(), e);
		}
	}

	public static int getMinInitialNaturalSkillsValues(Integer age) {
		if (age != null) {
			if (age <= 12) {
				return 1;
			} else if (age <= 16) {
				return 2;
			} else if (age <= 20) {
				return MIN_INITIAL_NATURAL_SKILL_VALUE;
			} else if (age <= 30) {
				return MIN_INITIAL_NATURAL_SKILL_VALUE;
			} else if (age <= 40) {
				return MIN_INITIAL_NATURAL_SKILL_VALUE;
			}
		}
		return MIN_INITIAL_NATURAL_SKILL_VALUE;
	}

	public static int getMinInitialCharacteristicsValues(CharacteristicName characteristicName, Integer age, Race race) {
		if (age != null && characteristicName != null && race != null) {
			if (age <= 12) {
				return Math.min(1, race.get(characteristicName).getInitialValue() - (human.get(characteristicName).getInitialValue() - 1));
			} else if (age <= 16) {
				return Math.min(1, race.get(characteristicName).getInitialValue() - (human.get(characteristicName).getInitialValue() - 2));
			} else if (age <= 20) {
				return race.get(characteristicName).getInitialValue();
			} else if (age <= 30) {
				return race.get(characteristicName).getInitialValue();
			} else if (age <= 40) {
				return race.get(characteristicName).getInitialValue();
			}
		}
		return MIN_INITIAL_CHARACTERISTICS_VALUE;
	}

	public static int getMaxInitialSkillsValues(Integer age) {
		if (age != null) {
			if (age <= 12) {
				return 4;
			} else if (age <= 16) {
				return 5;
			} else if (age <= 20) {
				return 6;
			} else if (age <= 30) {
				return 7;
			} else if (age <= 40) {
				return MAX_INITIAL_SKILL_VALUE;
			}
		}
		return MAX_INITIAL_SKILL_VALUE;
	}

	public static int getMaxInitialCharacteristicsValues(CharacteristicName characteristicName, Integer age, Race race) {
		if (age != null && characteristicName != null && race != null) {
			if (age <= 12) {
				return race.get(characteristicName).getMaximumInitialValue() - (human.get(characteristicName).getMaximumInitialValue() - 4);
			} else if (age <= 16) {
				return race.get(characteristicName).getMaximumInitialValue() - (human.get(characteristicName).getMaximumInitialValue() - 5);
			} else if (age <= 20) {
				return race.get(characteristicName).getMaximumInitialValue() - (human.get(characteristicName).getMaximumInitialValue() - 6);
			} else if (age <= 30) {
				return race.get(characteristicName).getMaximumInitialValue() - (human.get(characteristicName).getMaximumInitialValue() - 7);
			} else if (age <= 40) {
				return race.get(characteristicName).getMaximumInitialValue();
			}
		}
		return MAX_INITIAL_CHARACTERISTIC_VALUE;
	}

	public static int getCharacteristicsPoints(Integer age) {
		if (age != null) {
			if (age <= 12) {
				return 5;
			} else if (age <= 16) {
				return 10;
			} else if (age <= 20) {
				return CHARACTERISTICS_POINTS;
			} else if (age <= 30) {
				return CHARACTERISTICS_POINTS;
			} else if (age <= 40) {
				return CHARACTERISTICS_POINTS;
			} else if (age <= 70) {
				return CHARACTERISTICS_POINTS - ((age - 40) / 5);
			} else {
				return CHARACTERISTICS_POINTS - (6 + ((age - 70) / 2));
			}
		}
		return CHARACTERISTICS_POINTS;
	}

	public static int getSkillsPoints(Integer age) {
		if (age != null) {
			if (age <= 12) {
				return 5;
			} else if (age <= 16) {
				return 15;
			} else if (age <= 20) {
				return SKILLS_POINTS;
			} else if (age <= 30) {
				return SKILLS_POINTS;
			} else if (age <= 40) {
				return SKILLS_POINTS;
			} else if (age <= 70) {
				return SKILLS_POINTS + ((age - 40) / 5) * 3;
			} else {
				return SKILLS_POINTS + (6 + ((age - 70) / 2)) * 3;
			}
		}
		return SKILLS_POINTS;
	}

	public static int getTraitsPoints(Integer age) {
		if (age != null) {
			if (age <= 12) {
				return 0;
			} else if (age <= 16) {
				return 0;
			} else if (age <= 20) {
				return 3;
			} else if (age <= 30) {
				return TRAITS_POINTS;
			} else if (age <= 40) {
				return TRAITS_POINTS;
			}
		}
		return TRAITS_POINTS;
	}

	public static int getFreeAvailablePoints(Integer age) {
		if (age != null) {
			if (age <= 12) {
				return 0;
			} else if (age <= 16) {
				return 0;
			} else if (age <= 20) {
				return 0;
			} else if (age <= 30) {
				return 20;
			} else if (age <= 40) {
				return FREE_AVAILABLE_POINTS;
			}
		}
		return FREE_AVAILABLE_POINTS;
	}

	public static int getMaxCursePoints(Integer age) {
		return MAX_CURSE_POINTS;
	}

	public static int getMaxBlessingModifications(Integer age) {
		return MAX_BLESSING_MODIFICATIONS;
	}

}
