package com.softwaremagico.tm.rules.random.definition;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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

public enum RandomProbabilityDefinition {
	MINIMUM(0.01d), LOW(0.1d), FAIR(1d), GOOD(5d);

	private final double probabilityMultiplicator;

	private RandomProbabilityDefinition(double probabilityMultiplicator) {
		this.probabilityMultiplicator = probabilityMultiplicator;
	}

	public static RandomProbabilityDefinition get(String probabilityName) {
		for (final RandomProbabilityDefinition probability : RandomProbabilityDefinition.values()) {
			if (probability.name().equalsIgnoreCase(probabilityName)) {
				return probability;
			}
		}
		return null;
	}

	public double getProbabilityMultiplicator() {
		return probabilityMultiplicator;
	}
}
