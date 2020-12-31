package com.softwaremagico.tm.character.equipment.armours;

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

public class ArmourSpecificationFactory extends XmlFactory<ArmourSpecification> {
	private static final String TRANSLATOR_FILE = "armour_specifications.xml";

	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";

	private static class ArmourSpecificationFactoryInit {
		public static final ArmourSpecificationFactory INSTANCE = new ArmourSpecificationFactory();
	}

	public static ArmourSpecificationFactory getInstance() {
		return ArmourSpecificationFactoryInit.INSTANCE;
	}

	@Override
	protected String getTranslatorFile() {
		return TRANSLATOR_FILE;
	}

	@Override
	protected ArmourSpecification createElement(ITranslator translator, String specificationId, String language,
			String moduleName) throws InvalidXmlElementException {
		ArmourSpecification specification = null;
		String name = null;
		try {
			name = translator.getNodeValue(specificationId, NAME, language);
		} catch (Exception e) {
			throw new InvalidArmourException("Invalid name in armour specification '" + specificationId + "'.");
		}

		String description = null;
		try {
			description = translator.getNodeValue(specificationId, DESCRIPTION, language);
		} catch (Exception e) {
			//Description is not mandatory.
		}

		specification = new ArmourSpecification(specificationId, name, description, language, moduleName);

		return specification;
	}
}
