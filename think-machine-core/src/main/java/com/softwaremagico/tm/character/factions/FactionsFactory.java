package com.softwaremagico.tm.character.factions;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;

public class FactionsFactory extends XmlFactory<Faction> {
	private final static ITranslator translatorBenefit = LanguagePool.getTranslator("factions.xml");

	private final static String NAME = "name";
	private final static String GROUP = "group";

	private static FactionsFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (FactionsFactory.class) {
				if (instance == null) {
					instance = new FactionsFactory();
				}
			}
		}
	}

	public static FactionsFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorBenefit;
	}

	@Override
	protected Faction createElement(ITranslator translator, String factionId, String language) throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(factionId, NAME, language);
			FactionGroup factionGroup;

			try {
				String groupName = translator.getNodeValue(factionId, GROUP);
				factionGroup = FactionGroup.get(groupName);
			} catch (Exception e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
				factionGroup = FactionGroup.NONE;
			}

			Faction faction = new Faction(factionId, name, factionGroup);
			return faction;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidFactionException("Invalid name in faction '" + factionId + "'.");
		}
	}
}
