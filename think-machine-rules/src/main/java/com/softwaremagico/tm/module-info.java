/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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
module rules {
    requires gson;
    requires java.sql;
    requires log4j;
	exports com.softwaremagico.tm.rules;
	exports com.softwaremagico.tm.rules.character;
	exports com.softwaremagico.tm.rules.character.benefices;
	exports com.softwaremagico.tm.rules.character.blessings;
	exports com.softwaremagico.tm.rules.character.characteristics;
	exports com.softwaremagico.tm.rules.character.combat;
	exports com.softwaremagico.tm.rules.character.creation;
	exports com.softwaremagico.tm.rules.character.cybernetics;
	exports com.softwaremagico.tm.rules.character.equipment;
	exports com.softwaremagico.tm.rules.character.equipment.weapons;
	exports com.softwaremagico.tm.rules.character.equipment.shields;
	exports com.softwaremagico.tm.rules.character.equipment.armours;
	exports com.softwaremagico.tm.rules.character.factions;
	exports com.softwaremagico.tm.rules.character.occultism;
	exports com.softwaremagico.tm.rules.character.planets;
	exports com.softwaremagico.tm.rules.character.races;
	exports com.softwaremagico.tm.rules.character.skills;
	exports com.softwaremagico.tm.rules.character.values;
	exports com.softwaremagico.tm.rules.log;
	exports com.softwaremagico.tm.rules.random.definition;
	exports com.softwaremagico.tm.rules.json;
	exports com.softwaremagico.tm.rules.language;
	exports com.softwaremagico.tm.rules.party;
	exports com.softwaremagico.tm.rules.txt;
	exports com.softwaremagico.tm.rules.file;
}
