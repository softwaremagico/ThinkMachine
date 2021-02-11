package com.softwaremagico.tm.json.factories;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class WeaponsFactoryElements extends FactoryElements<Weapon, WeaponFactory> {

    public WeaponsFactoryElements() {
        super();
        creationTime = new Timestamp(new Date().getTime());
    }

    public WeaponsFactoryElements(String language, String moduleName) throws InvalidXmlElementException {
        this();
        creationTime = new Timestamp(new Date().getTime());
        setElements(WeaponFactory.getInstance().getElements(language, moduleName));
        setVersion(WeaponFactory.getInstance().getVersion(moduleName));
        setTotalElements(WeaponFactory.getInstance().getNumberOfElements(moduleName));
    }

    public WeaponsFactoryElements(int version, int totalElements, List<Weapon> elements) {
        this();
        setElements(elements);
        setVersion(version);
        setTotalElements(totalElements);
    }
}
