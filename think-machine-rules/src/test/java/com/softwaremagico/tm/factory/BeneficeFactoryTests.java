package com.softwaremagico.tm.factory;

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
import com.softwaremagico.tm.character.benefices.*;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.file.PathManager;
import junit.framework.Assert;
import org.testng.annotations.Test;

import java.util.Set;

@Test(groups = {"beneficeFactory"})
public class BeneficeFactoryTests {
    private static final String LANGUAGE = "es";

    private static final int DEFINED_BENEFICES = 74;
    private static final int AVAILABLE_BENEFICES = 234;

    @Test
    public void checkTotalElements() {
        org.testng.Assert.assertEquals((int) BeneficeDefinitionFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER),
                DEFINED_BENEFICES);
    }

    @Test
    public void readBenefices() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_BENEFICES, BeneficeDefinitionFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .size());
    }

    @Test
    public void getCalculatedBenefices() throws InvalidXmlElementException {
        Assert.assertEquals(AVAILABLE_BENEFICES,
                AvailableBeneficeFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size());
    }

    @Test
    public void getBeneficesClassification() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_BENEFICES, AvailableBeneficeFactory.getInstance()
                .getAvailableBeneficesByDefinition(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).keySet().size());
        int count = 0;
        for (final Set<AvailableBenefice> benefices : AvailableBeneficeFactory.getInstance()
                .getAvailableBeneficesByDefinition(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).values()) {
            count += benefices.size();
        }
        Assert.assertEquals(AVAILABLE_BENEFICES, count);
    }

    @Test
    public void getBeneficeSpecialization() throws InvalidXmlElementException {
        Assert.assertNotNull(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds250]", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test
    public void getMoneyStandard() {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(250, player.getInitialMoney());
    }

    @Test
    public void getMoneyBenefice() throws InvalidXmlElementException, BeneficeAlreadyAddedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(1000, player.getInitialMoney());
    }

    @Test
    public void getMoneyRemaining() throws InvalidXmlElementException, BeneficeAlreadyAddedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        // Weapon without cost.
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("fluxSword", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        // Weapon with cost.
        player.addWeapon(WeaponFactory.getInstance().getElement("typicalShotgun", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(2, player.getAllWeapons().size());
        Assert.assertEquals(700, player.getMoney());
    }

    @Test
    public void getMoneyAsAfflictionCost() throws InvalidXmlElementException, BeneficeAlreadyAddedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final int remainingCost = CostCalculator.getCost(player);
        final AvailableBenefice cash50 = AvailableBeneficeFactory.getInstance().getElement("cash [firebirds50]",
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(BeneficeClassification.AFFLICTION, cash50.getBeneficeClassification());
        Assert.assertEquals(-2, cash50.getCost());
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds50]", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(remainingCost - 2, CostCalculator.getCost(player));
    }

    @Test
    public void getAssetsBenefice() throws InvalidXmlElementException, BeneficeAlreadyAddedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("assets [assets3000]", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(300, player.getInitialMoney());
    }

    @Test
    public void getAssetsAndMoneyBenefice() throws InvalidXmlElementException, BeneficeAlreadyAddedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("assets [assets3000]", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        Assert.assertEquals(1300, player.getInitialMoney());
    }

    @Test(expectedExceptions = IncompatibleBeneficeException.class)
    public void checkIncompatibilities() throws InvalidXmlElementException, BeneficeAlreadyAddedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("orphan", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("alienUpbringing", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test(expectedExceptions = IncompatibleBeneficeException.class)
    public void checkIncompatibilitiesOpposite() throws InvalidXmlElementException, BeneficeAlreadyAddedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("alienUpbringing", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("orphan", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test(expectedExceptions = IncompatibleBeneficeException.class)
    public void checkIncompatibilitiesSpecializations() throws InvalidXmlElementException, BeneficeAlreadyAddedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds0]", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("assets [assets3000]", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test(expectedExceptions = IncompatibleBeneficeException.class)
    public void checkIncompatibilitiesWithSpecializations() throws InvalidXmlElementException, BeneficeAlreadyAddedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("indebted_4", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("assets [assets5000]", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test
    public void checkDescription() throws InvalidXmlElementException {
        Assert.assertEquals("Debido a una extraña serie de sucesión de eventos, el personaje ha sido criado por miembros de otra raza. Casi nunca se oye que un humano " +
                        "se preocupe por un niño Ur-Ukar, o que un Vorox se preocupe de un infante humano, pero esto ha ocurrido. Un personaje con este Beneficio empieza con los " +
                        "conocimientos del idioma de esa especie (la habilidad de Hablar) en lugar de la suya propia. Cualquiera con un prejuicio hacia las razas no humanas (lo cual " +
                        "incluye prácticamente a todas las personas en los Mundos Conocidos) tenderá a desarrollar una desconfianza especial hacia el personaje.",
                AvailableBeneficeFactory.getInstance().getElement("alienUpbringing", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getDescription());
    }

}
