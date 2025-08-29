package com.softwaremagico.tm.random.predefined;

import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.predefined.characters.Npc;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/*-
 * #%L
 * Think Machine (Random Generator)
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

public final class PredefinedMerger {
    private static final String DEFAULT_ID = "merged_profile";

    private PredefinedMerger() {
        super();
    }

    public static IRandomPredefined merge(String language, String moduleName, IRandomPredefined... profiles) {
        if (profiles == null || profiles.length == 0) {
            return null;
        }

        return merge(new HashSet<>(Arrays.asList(profiles)), language, moduleName);
    }

    public static IRandomPredefined merge(Set<IRandomPredefined> profiles, String language, String moduleName) {
        return merge(profiles, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                language, moduleName);
    }

    private static IRandomPredefined getEmptyProfile(String language, String moduleName) {
        return new Npc(DEFAULT_ID, "", null, language, moduleName, null);
    }

    public static IRandomPredefined merge(Set<IRandomPredefined> profiles, Set<IRandomPreference<?>> extraPreferences, Set<AvailableSkill> requiredSkills,
                                          Set<AvailableSkill> suggestedSkills, Set<Blessing> mandatoryBlessings, Set<Blessing> suggestedBlessings,
                                          Set<BeneficeDefinition> mandatoryBenefices, Set<BeneficeDefinition> suggestedBenefices,
                                          Set<AvailableBenefice> mandatoryAvailableBenefices, Set<AvailableBenefice> suggestedAvailableBenefices,
                                          Set<OccultismPath> mandatoryOccultismPaths,
                                          Set<Weapon> mandatoryWeapons, Set<Armour> mandatoryArmours,
                                          Set<Shield> mandatoryShields, String language, String moduleName) {
        if (profiles == null) {
            profiles = new HashSet<>();
        }

        if (extraPreferences == null) {
            extraPreferences = new HashSet<>();
        }

        // Store all information in a new profile.
        final IRandomPredefined finalProfile = getEmptyProfile(language, moduleName);

        // Merge profiles
        for (final IRandomPredefined profile : profiles) {
            // Merge preferences.
            mergePreferences(finalProfile.getPreferences(), removeDuplicates(profile.getPreferences()));

            // Merge characteristics.
            mergeCharacteristics(finalProfile.getCharacteristicsMinimumValues(), profile.getCharacteristicsMinimumValues());

            // Merge Skills.
            mergeSkills(finalProfile.getRequiredSkills(), profile.getRequiredSkills());

            // Merge Skills
            mergeSkills(finalProfile.getSuggestedSkills(), profile.getSuggestedSkills());

            mergeBlessings(finalProfile.getMandatoryBlessings(), profile.getMandatoryBlessings());

            mergeBlessings(finalProfile.getSuggestedBlessings(), profile.getSuggestedBlessings());

            mergeBenefices(finalProfile.getMandatoryBenefices(), profile.getMandatoryBenefices());

            mergeAvailableBenefices(finalProfile.getMandatoryBeneficeSpecializations(), profile.getMandatoryBeneficeSpecializations());

            mergeAvailableBenefices(finalProfile.getSuggestedBeneficeSpecializations(), profile.getSuggestedBeneficeSpecializations());

            mergeBenefices(finalProfile.getSuggestedBenefices(), profile.getSuggestedBenefices());

            mergeWeapons(finalProfile.getMandatoryWeapons(), profile.getMandatoryWeapons());

            mergeArmours(finalProfile.getMandatoryArmours(), profile.getMandatoryArmours());

            mergeShields(finalProfile.getMandatoryShields(), profile.getMandatoryShields());

            mergeFactions(finalProfile, profile);

            mergeRace(finalProfile, profile);

            mergeOccultismPaths(finalProfile.getMandatoryOccultismPaths(), profile.getMandatoryOccultismPaths());

        }

        // Add selected preferences with more priority.
        extraPreferences = mergePreferences(removeDuplicates(extraPreferences), finalProfile.getPreferences());
        finalProfile.getPreferences().clear();
        finalProfile.getPreferences().addAll(extraPreferences);

        mergeSkills(requiredSkills, finalProfile.getRequiredSkills());
        finalProfile.getRequiredSkills().clear();
        finalProfile.getRequiredSkills().addAll(requiredSkills);

        mergeSkills(suggestedSkills, finalProfile.getSuggestedSkills());
        finalProfile.getSuggestedSkills().clear();
        finalProfile.getSuggestedSkills().addAll(suggestedSkills);

        mergeBlessings(mandatoryBlessings, finalProfile.getMandatoryBlessings());
        finalProfile.getMandatoryBlessings().clear();
        finalProfile.getMandatoryBlessings().addAll(mandatoryBlessings);

        mergeBlessings(suggestedBlessings, finalProfile.getSuggestedBlessings());
        finalProfile.getSuggestedBlessings().clear();
        finalProfile.getSuggestedBlessings().addAll(suggestedBlessings);

        mergeBenefices(mandatoryBenefices, finalProfile.getMandatoryBenefices());
        finalProfile.getMandatoryBenefices().clear();
        finalProfile.getMandatoryBenefices().addAll(mandatoryBenefices);

        mergeAvailableBenefices(mandatoryAvailableBenefices, finalProfile.getMandatoryBeneficeSpecializations());
        finalProfile.getMandatoryBeneficeSpecializations().clear();
        finalProfile.getMandatoryBeneficeSpecializations().addAll(mandatoryAvailableBenefices);

        mergeAvailableBenefices(suggestedAvailableBenefices, finalProfile.getSuggestedBeneficeSpecializations());
        finalProfile.getSuggestedBeneficeSpecializations().clear();
        finalProfile.getSuggestedBeneficeSpecializations().addAll(suggestedAvailableBenefices);

        mergeBenefices(suggestedBenefices, finalProfile.getSuggestedBenefices());
        finalProfile.getSuggestedBenefices().clear();
        finalProfile.getSuggestedBenefices().addAll(suggestedBenefices);

        mergeOccultismPaths(mandatoryOccultismPaths, finalProfile.getMandatoryOccultismPaths());
        finalProfile.getMandatoryOccultismPaths().clear();
        finalProfile.getMandatoryOccultismPaths().addAll(mandatoryOccultismPaths);

        mergeWeapons(mandatoryWeapons, finalProfile.getMandatoryWeapons());
        finalProfile.getMandatoryWeapons().clear();
        finalProfile.getMandatoryWeapons().addAll(mandatoryWeapons);

        mergeArmours(mandatoryArmours, finalProfile.getMandatoryArmours());
        finalProfile.getMandatoryArmours().clear();
        finalProfile.getMandatoryArmours().addAll(mandatoryArmours);

        mergeShields(mandatoryShields, finalProfile.getMandatoryShields());
        finalProfile.getMandatoryShields().clear();
        finalProfile.getMandatoryShields().addAll(mandatoryShields);

        return finalProfile;
    }

    private static void mergeCharacteristics(Set<Characteristic> originalCharacteristicsMinimumValues,
                                             Set<Characteristic> preferredCharacteristicsMinimumValues) {
        // Merge Characteristics
        for (final Characteristic newCharacteristic : preferredCharacteristicsMinimumValues) {
            boolean added = false;
            for (final Characteristic characteristic : originalCharacteristicsMinimumValues) {
                if (Objects.equals(characteristic.getCharacteristicDefinition().getCharacteristicName(),
                        newCharacteristic.getCharacteristicDefinition().getCharacteristicName())) {
                    if (characteristic.getValue() < newCharacteristic.getValue()) {
                        characteristic.setValue(newCharacteristic.getValue());
                        added = true;
                        break;
                    }
                }
            }
            if (!added) {
                originalCharacteristicsMinimumValues.add(newCharacteristic);
            }
        }
    }

    private static void mergeOccultismPaths(Set<OccultismPath> originalOccultismPaths, Set<OccultismPath> extraOccultismPaths) {
        originalOccultismPaths.addAll(extraOccultismPaths);
    }

    private static void mergeAvailableBenefices(Set<AvailableBenefice> originalBenefices, Set<AvailableBenefice> extraBenefices) {
        originalBenefices.addAll(extraBenefices);
    }

    private static void mergeBenefices(Set<BeneficeDefinition> originalBenefices, Set<BeneficeDefinition> extraBenefices) {
        originalBenefices.addAll(extraBenefices);
    }

    private static void mergeBlessings(Set<Blessing> originalBlessings, Set<Blessing> extraBlessings) {
        originalBlessings.addAll(extraBlessings);
    }

    private static void mergeSkills(Set<AvailableSkill> originalRequiredSkills, Set<AvailableSkill> requiredSkills) {
        // Merge Characteristics
        originalRequiredSkills.addAll(requiredSkills);
    }

    private static void mergeWeapons(Set<Weapon> originalWeapons, Set<Weapon> weapons) {
        final List<Weapon> sortedWeapons = new ArrayList<>();
        sortedWeapons.addAll(originalWeapons);
        sortedWeapons.addAll(weapons);
        sortedWeapons.sort((weapon0, weapon1) -> Float.compare(weapon1.getCost(), weapon0.getCost()));
        // Keep only the most expensive ones.
        if (!sortedWeapons.isEmpty()) {
            originalWeapons.clear();
            originalWeapons
                    .addAll(sortedWeapons.subList(0, sortedWeapons.size() % 2 == 0 ? sortedWeapons.size() / 2 : (sortedWeapons.size() / 2) + 1));
        }
    }

    private static void mergeArmours(Set<Armour> originalArmour, Set<Armour> armour) {
        final List<Armour> sortedArmour = new ArrayList<>();
        sortedArmour.addAll(originalArmour);
        sortedArmour.addAll(armour);
        sortedArmour.sort((armour0, armour1) -> Float.compare(armour0.getCost(), armour1.getCost()));
        // Keep the most expensive one
        if (!sortedArmour.isEmpty()) {
            originalArmour.clear();
            originalArmour.add(sortedArmour.get(0));
        }
    }

    private static void mergeShields(Set<Shield> originalShields, Set<Shield> shields) {
        final List<Shield> sortedShields = new ArrayList<>();
        sortedShields.addAll(originalShields);
        sortedShields.addAll(shields);
        sortedShields.sort((shield0, shield1) -> Float.compare(shield0.getCost(), shield1.getCost()));
        // Keep the most expensive one
        if (!sortedShields.isEmpty()) {
            originalShields.clear();
            originalShields.add(sortedShields.get(0));
        }
    }

    private static void mergeFactions(IRandomPredefined finalProfile, IRandomPredefined profile) {
        if (finalProfile.getFaction() == null) {
            finalProfile.setFaction(profile.getFaction());
        } else if (profile.getFaction() != null) {
            //Choose randomly one or other.
            if (RandomSelector.RANDOM.nextBoolean()) {
                finalProfile.setFaction(profile.getFaction());
            }
        }
    }

    private static void mergeRace(IRandomPredefined finalProfile, IRandomPredefined profile) {
        if (finalProfile.getRace() == null) {
            finalProfile.setRace(profile.getRace());
        } else if (profile.getRace() != null) {
            //Choose randomly one or other.
            if (RandomSelector.RANDOM.nextBoolean()) {
                finalProfile.setRace(profile.getRace());
            }
        }
    }

    public static Set<IRandomPreference<?>> mergePreferences(Set<IRandomPreference<?>> originalPreferences, Set<IRandomPreference<?>> preferredPreferences) {
        for (final IRandomPreference<?> preferredPreference : new HashSet<>(preferredPreferences)) {
            //Get preference average.
            for (final IRandomPreference<?> randomPreference : new HashSet<>(originalPreferences)) {
                if (randomPreference.getClass().equals(preferredPreference.getClass())) {
                    if (randomPreference.getClass().isEnum()) {
                        final int average = ((((Enum<?>) randomPreference).ordinal() + ((Enum<?>) preferredPreference).ordinal()) + 1) / 2;
                        if (average >= 0 && average < randomPreference.getClass().getEnumConstants().length) {
                            final IRandomPreference<?> averagePreference = randomPreference.getClass().getEnumConstants()[average];
                            originalPreferences.remove(randomPreference);
                            preferredPreferences.remove(preferredPreference);
                            originalPreferences.add(averagePreference);
                        }
                    }
                }
            }
        }
        originalPreferences.addAll(preferredPreferences);
        return originalPreferences;
    }

    public static Set<IRandomPreference<?>> removeDuplicates(Set<IRandomPreference<?>> originalPreferences) {
        final Set<IRandomPreference<?>> filteredPreferences = originalPreferences.stream().filter(java.util.Objects::nonNull).
                collect(Collectors.toSet());
        for (final IRandomPreference<?> preference1 : new HashSet<>(filteredPreferences)) {
            //Get preference average.
            for (final IRandomPreference<?> preference2 : new HashSet<>(filteredPreferences)) {
                if (preference1 != preference2 && Objects.equals(preference1.getClass(), preference2.getClass())) {
                    if (preference1.getClass().isEnum()) {
                        //Select randomly
                        if (RandomSelector.RANDOM.nextBoolean()) {
                            filteredPreferences.remove(preference1);
                        } else {
                            filteredPreferences.remove(preference2);
                        }
                        return removeDuplicates(filteredPreferences);
                    }
                }
            }
        }
        return filteredPreferences;
    }

}
