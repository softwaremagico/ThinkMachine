package com.softwaremagico.tm.character.benefices;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.ExtraBeneficesNumberPreferences;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.HashSet;
import java.util.Set;

public class RandomExtraBeneficeDefinition extends RandomBeneficeDefinition {
    private static final int MAX_COMBAT_STYLES = 2;

    public RandomExtraBeneficeDefinition(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences,
                                         Set<BeneficeDefinition> suggestedBenefices, Set<AvailableBenefice> suggestedAvailableBenefices) throws
            InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        super(characterPlayer, preferences, new HashSet<>(), suggestedBenefices, suggestedAvailableBenefices);
    }

    @Override
    public void assign() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        final int existingCombatStyles = getCharacterPlayer().getMeleeCombatStyles().size()
                + getCharacterPlayer().getRangedCombatStyles().size();

        final IGaussianDistribution beneficesDistribution = ExtraBeneficesNumberPreferences.getSelected(getPreferences());
        // Select a blessing
        final int totalExtraSelectedBenefices = beneficesDistribution.randomGaussian()
                + getCharacterPlayer().getAllBenefices().size();

        // Later, the others.
        while (getCharacterPlayer().getAllBenefices().size() < totalExtraSelectedBenefices
                && !getWeightedElements().isEmpty()) {
            // Select a benefice
            final BeneficeDefinition selectedBenefice = selectElementByWeight();

            // Only a few fighting style by character.
            if (selectedBenefice.getGroup().equals(BeneficeGroup.FIGHTING)) {
                if (existingCombatStyles >= MAX_COMBAT_STYLES) {
                    removeElementWeight(selectedBenefice);
                    continue;
                }
            }

            try {
                assignBenefice(selectedBenefice,
                        FreeStyleCharacterCreation.getFreeAvailablePoints(getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace()));
            } catch (RestrictedElementException e) {
                removeElementWeight(selectedBenefice);
                //Ignore the restricted one.
            }
        }
    }
}
