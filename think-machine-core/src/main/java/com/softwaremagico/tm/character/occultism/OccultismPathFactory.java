package com.softwaremagico.tm.character.occultism;

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
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class OccultismPathFactory extends XmlFactory<OccultismPath> {
	private final static ITranslator translatorBlessing = LanguagePool.getTranslator("occultism.xml");

	private final static String NAME = "name";
	private final static String TYPE = "type";

	private final static String OCCULTISM_POWER = "power";
	private final static String POWER_NAME = "name";
	private final static String POWER_LEVEL = "level";
	private final static String POWER_CHARACTERISTIC = "characteristic";
	private final static String POWER_SKILL = "skill";
	private final static String POWER_RANGE = "range";
	private final static String POWER_DURATION = "duration";
	private final static String POWER_REQUIREMENTS = "requirements";
	private final static String POWER_WYRD = "wyrd";

	private static OccultismPathFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (OccultismPathFactory.class) {
				if (instance == null) {
					instance = new OccultismPathFactory();
				}
			}
		}
	}

	public static OccultismPathFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected OccultismPath createElement(ITranslator translator, String occulstimId, String language)
			throws InvalidXmlElementException {

		try {
			String name = translator.getNodeValue(occulstimId, NAME, language);

			String typeName = translator.getNodeValue(occulstimId, TYPE, language);
			OccultismType occultismType = OccultismType.get(typeName);
			OccultismPath occultismPath = new OccultismPath(occulstimId, name, occultismType);

			for (String powerId : translator.getAllChildrenTags(occulstimId, OCCULTISM_POWER)) {
				String powerName = translator.getNodeValue(powerId, POWER_NAME, language);
				String level = translator.getNodeValue(powerId, POWER_LEVEL, language);
				String characteristicName = translator.getNodeValue(powerId, POWER_CHARACTERISTIC, language);
				String skillName = translator.getNodeValue(powerId, POWER_SKILL, language);
				String range = translator.getNodeValue(powerId, POWER_RANGE, language);
				String duration = translator.getNodeValue(powerId, POWER_DURATION, language);
				String requirements = translator.getNodeValue(powerId, POWER_REQUIREMENTS, language);
				String wyrd = translator.getNodeValue(powerId, POWER_WYRD, language);

				OccultismPower occultismPower = new OccultismPower(powerName, CharacteristicsDefinitionFactory
						.getInstance().get(CharacteristicName.get(characteristicName), language),
						AvailableSkillsFactory.getInstance().getElement(skillName, language), Integer.parseInt(level),
						OccultismRangeFactory.getInstance().getElement(range, language), OccultismDurationFactory
								.getInstance().getElement(duration, language), requirements, Integer.parseInt(wyrd));

				occultismPath.getOccultismPowers().put(Integer.parseInt(level), occultismPower);
			}

			return occultismPath;
		} catch (Exception e) {
			throw new InvalidOccultismPowerException("Invalid structure in occultism path '" + occulstimId + "'.", e);
		}
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorBlessing;
	}
}
