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
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class BlessingFactory extends XmlFactory<Blessing> {
	private final static ITranslator translatorBlessing = LanguagePool.getTranslator("blessings.xml");

	private final static String NAME = "name";
	private final static String COST = "cost";
	private final static String BONIFICATION = "bonification";
	private final static String SKILL = "skill";
	private final static String CHARACTERISTIC = "characteristic";
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
	protected Blessing createElement(ITranslator translator, String blessingId, String language) throws InvalidXmlElementException {

		try {
			String name = translator.getNodeValue(blessingId, NAME, language);

			String cost = translator.getNodeValue(blessingId, COST);

			String bonification = translator.getNodeValue(blessingId, BONIFICATION);

			BlessingGroup blessingGroup = null;
			String groupName = translator.getNodeValue(blessingId, GROUP);
			if (groupName != null) {
				blessingGroup = BlessingGroup.get(groupName);
			}

			String skillName = translator.getNodeValue(blessingId, SKILL);
			SkillDefinition skill = null;
			if (skillName != null) {
				skill = SkillsDefinitionsFactory.getInstance().getElement(skillName, language);
			}

			String characteristicName = translator.getNodeValue(blessingId, CHARACTERISTIC);
			Set<CharacteristicDefinition> characteristics = new HashSet<>();	
			if(characteristicName!=null && characteristicName.contains(",")){
				StringTokenizer characteristicTokenizer = new StringTokenizer(characteristicName, ",");
				while (characteristicTokenizer.hasMoreTokens()) {
					characteristics.add(CharacteristicsDefinitionFactory.getInstance().getElement(characteristicTokenizer.nextToken().trim(), language));
				}
			}else{
				if (characteristicName != null) {
					characteristics.add(CharacteristicsDefinitionFactory.getInstance().getElement(characteristicName, language));
				}
			}

			String situation = translator.getNodeValue(blessingId, SITUATION, language);

			String curseTag = translator.getNodeValue(blessingId, CURSE);
			BlessingClassification blessingClassification = BlessingClassification.BLESSING;

			if (blessingClassification != null) {
				if (Boolean.parseBoolean(curseTag)) {
					blessingClassification = BlessingClassification.CURSE;
				}
			}

			Blessing blessing = new Blessing(blessingId, name, Integer.parseInt(cost), Integer.parseInt(bonification), skill, characteristics, situation,
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
