package com.softwaremagico.tm.character.blessings;

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

import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.character.values.SpecialValue;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class BlessingFactory extends XmlFactory<Blessing> {
	private final static ITranslator translatorBlessing = LanguagePool.getTranslator("blessings.xml");

	private final static String NAME = "name";
	private final static String COST = "cost";
	private final static String BONIFICATION = "bonification";
	private final static String VALUE = "value";
	private final static String AFFECTS = "affects";
	private final static String SITUATION = "situation";
	private final static String CURSE = "curse";
	private final static String GROUP = "group";

	private static BlessingFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (BlessingFactory.class) {
				if (instance == null) {
					instance = new BlessingFactory();
				}
			}
		}
	}

	public static BlessingFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected Blessing createElement(ITranslator translator, String blessingId, String language)
			throws InvalidXmlElementException {

		try {
			String name = translator.getNodeValue(blessingId, NAME, language);

			String cost = translator.getNodeValue(blessingId, COST);

			BlessingGroup blessingGroup = null;
			String groupName = translator.getNodeValue(blessingId, GROUP);
			if (groupName != null) {
				blessingGroup = BlessingGroup.get(groupName);
			}

			Set<Bonification> bonifications = new HashSet<>();
			int node = 0;
			while (true) {
				try {
					String bonificationValue = translator.getNodeValue(blessingId, BONIFICATION, VALUE, node);
					String valueName = translator.getNodeValue(blessingId,  BONIFICATION, AFFECTS, node);
					IValue affects = null;
					if (valueName != null) {
						affects = SpecialValue.getValue(valueName, language);
					}
					String situation = translator.getNodeValue(blessingId, SITUATION, language, node);

					Bonification bonification = new Bonification(Integer.parseInt(bonificationValue), affects,
							situation);
					bonifications.add(bonification);
					node++;
				} catch (Exception e) {
					break;
				}
			}

			String curseTag = translator.getNodeValue(blessingId, CURSE);
			BlessingClassification blessingClassification = BlessingClassification.BLESSING;

			if (curseTag != null) {
				if (Boolean.parseBoolean(curseTag)) {
					blessingClassification = BlessingClassification.CURSE;
				}
			}

			Blessing blessing = new Blessing(blessingId, name, Integer.parseInt(cost), bonifications,
					blessingClassification, blessingGroup);
			return blessing;
		} catch (Exception e) {
			throw new InvalidBlessingException("Invalid structure in blessing '" + blessingId + "'.", e);
		}
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorBlessing;
	}
}
