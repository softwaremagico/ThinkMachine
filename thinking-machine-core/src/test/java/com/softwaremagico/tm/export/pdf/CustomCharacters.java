package com.softwaremagico.tm.export.pdf;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.itextpdf.text.DocumentException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.CostCalculator;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Race;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.character.equipment.Shield;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.character.equipment.Weapon;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.traits.Benefit;
import com.softwaremagico.tm.character.traits.Blessing;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.CharacterSheet;

@Test(groups = { "customCharacterGeneration" })
public class CustomCharacters {

	@Test
	public void createPaolaCharacter() throws MalformedURLException, DocumentException, IOException {
		CharacterPlayer player = new CharacterPlayer("es");
		player.getInfo().setName("5");
		player.getInfo().setPlayer("Paola");
		player.getInfo().setGender(Gender.FEMALE);
		player.getInfo().setAge(25);
		player.setRace(new Race("Human", 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 0, 0, 0, 0, 0));
		player.getInfo().setPlanet("Ligaheim");
		player.getInfo().setAlliance("Ingeniero");
		player.getInfo().setRank("Aprendiz");

		player.getCharacteristics().getCharacteristic(CharacteristicName.STRENGTH).setValue(3);
		player.getCharacteristics().getCharacteristic(CharacteristicName.DEXTERITY).setValue(7);
		player.getCharacteristics().getCharacteristic(CharacteristicName.ENDURANCE).setValue(5);
		player.getCharacteristics().getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristics().getCharacteristic(CharacteristicName.PERCEPTION).setValue(6);
		player.getCharacteristics().getCharacteristic(CharacteristicName.TECH).setValue(8);
		player.getCharacteristics().getCharacteristic(CharacteristicName.PRESENCE).setValue(3);
		player.getCharacteristics().getCharacteristic(CharacteristicName.WILL).setValue(5);
		player.getCharacteristics().getCharacteristic(CharacteristicName.FAITH).setValue(3);

		player.addSkill("Influenciar", 4);
		player.addSkill("Observar", 5);
		player.addSkill("Sigilo", 6);
		player.addSkill("Vigor", 5);
		player.addSkill("Abrir Cerraduras", 2);
		player.addSkill("Armas de Energía", 5);
		player.addSkill("Ciencia Aplicada", 4);
		player.addSkill("Ciencia de la Física", 1);
		player.addSkill("Controlar Nave Espacial", 2);
		player.addSkill("Controlar Vehíc. Terrestre", 3);
		player.addSkill("Cuerpo a Cuerpo", 1);
		player.addSkill("Empatía", 3);
		player.addSkill("Guerra", 1);
		player.addSkill("Investigar", 2);
		player.addSkill("Mantenimiento de Naves", 2);
		player.addSkill("Máquina Pensante", 4);
		player.addSkill("Recuperación Tecnológic.", 7);
		player.addSkill("Saber [Red de Salto]", 4, true);
		player.addSkill("Saber [Pistol Energía]", 4, true);
		player.addSkill("Saber [Máquina Pensante]", 4, true);

		player.addBlessing(new Blessing("Innovador", 2, 2, "Tecnología", "Inventar"));
		player.addBlessing(new Blessing("Enervado", -2, -2, "Presencia", "Entre sirvientes"));
		player.addBlessing(new Blessing("Hacker", 2, 2, "Máquina Pensante", "--"));
		player.addBlessing(new Blessing("Mecánico", 2, 2, "Recuperación Tecnológic", "Reparando"));
		player.addBlessing(new Blessing("Enemigo Animales", -2, -2, "Todo", "Excepto combate"));
		player.addBlessing(new Blessing("Marca Horrible", -2, -2, "Influencia", "Si es visible"));

		player.addBenefit(new Benefit("Idioma Suprema", 2));
		player.addBenefit(new Benefit("Asociado", 4));
		player.addBenefit(new Benefit("1000 fénix", 4));
		player.addBenefit(new Benefit("Red de Información", 3));

		player.getCybernetics().addElement(
				new Device("Ojo de Ingeniero", 8, 6, "Normal", "Normal", "Automático", "Oculto",
						"Autoalimentado"));
		player.getCybernetics().addElement(
				new Device("Segundo Cerebro", 11, 10, "Normal", "Normal", "Automático", "Oculto",
						"Autoalimentado"));

		player.setShield(new Shield("De Duelo", 5, 10, 15));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Paola.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), 40);
	}

	@Test
	public void characterAnaCharacter() throws MalformedURLException, DocumentException, IOException {
		CharacterPlayer player = new CharacterPlayer("es");
		player.getInfo().setName("");
		player.getInfo().setPlayer("Ana");
		player.getInfo().setGender(Gender.FEMALE);
		player.getInfo().setAge(22);
		player.setRace(new Race("Human", 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 0, 0, 0, 0, 0));
		player.getInfo().setPlanet("Leminkainen");
		player.getInfo().setAlliance("Hawkwood");
		player.getInfo().setRank("Caballero");

		player.getCharacteristics().getCharacteristic(CharacteristicName.STRENGTH).setValue(5);
		player.getCharacteristics().getCharacteristic(CharacteristicName.DEXTERITY).setValue(7);
		player.getCharacteristics().getCharacteristic(CharacteristicName.ENDURANCE).setValue(5);
		player.getCharacteristics().getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristics().getCharacteristic(CharacteristicName.PERCEPTION).setValue(7);
		player.getCharacteristics().getCharacteristic(CharacteristicName.TECH).setValue(5);
		player.getCharacteristics().getCharacteristic(CharacteristicName.PRESENCE).setValue(7);
		player.getCharacteristics().getCharacteristic(CharacteristicName.WILL).setValue(5);
		player.getCharacteristics().getCharacteristic(CharacteristicName.FAITH).setValue(4);

		player.addSkill("Influenciar", 7);
		player.addSkill("Observar", 8);
		player.addSkill("Sigilo", 8);
		player.addSkill("Vigor", 8);
		player.addSkill("Abrir Cerraduras", 4);
		player.addSkill("Arte [Música]", 1);
		player.addSkill("Autocontrol", 2);
		player.addSkill("Callejear", 3);
		player.addSkill("Conocim. del Cuerpo", 1);
		player.addSkill("Controlar Embarcación", 1);
		player.addSkill("Artefacto Cuerpo a C.", 4);
		player.addSkill("Etiqueta", 4);
		player.addSkill("Investigar", 4);
		player.addSkill("Liderazgo", 2);

		player.addBlessing(new Blessing("Inflexible", 2, 2, "Fortaleza", "Honor en juego"));
		player.addBlessing(new Blessing("Orgulloso", -2, -2, "Voluntad", "Insulta"));
		player.addBlessing(new Blessing("Marca Horrible", -2, -2, "Influencia", "Si es visible"));

		player.addBenefit(new Benefit("Rango Caballero", 4));
		player.addBenefit(new Benefit("Idioma Latín", 2));
		player.addBenefit(new Benefit("Decreto Imperial", 4));
		player.addBenefit(new Benefit("Espada de Flujo", 11));
		player.addBenefit(new Benefit("Vendetta", -2));
		
		player.getWeapons().addElement(new Weapon("Espada de Flujo", "Ds+Art.", 1, 7, "3", null, null, 8, Size.L, "Plasma"));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Ana.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), 40);
	}

	@Test
	public void createCarlosCharacter() throws MalformedURLException, DocumentException, IOException {
		CharacterPlayer player = new CharacterPlayer("es");
		player.getInfo().setName("");
		player.getInfo().setPlayer("Carlos");
		player.getInfo().setGender(Gender.MALE);
		player.getInfo().setAge(28);
		player.setRace(new Race("Human", 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 0, 0, 0, 0, 0));
		player.getInfo().setPlanet("Byzantium Sec.");
		player.getInfo().setAlliance("Carroñero");
		player.getInfo().setRank("Genin");

		player.getCharacteristics().getCharacteristic(CharacteristicName.STRENGTH).setValue(7);
		player.getCharacteristics().getCharacteristic(CharacteristicName.DEXTERITY).setValue(8);
		player.getCharacteristics().getCharacteristic(CharacteristicName.ENDURANCE).setValue(6);
		player.getCharacteristics().getCharacteristic(CharacteristicName.WITS).setValue(6);
		player.getCharacteristics().getCharacteristic(CharacteristicName.PERCEPTION).setValue(6);
		player.getCharacteristics().getCharacteristic(CharacteristicName.TECH).setValue(5);
		player.getCharacteristics().getCharacteristic(CharacteristicName.PRESENCE).setValue(4);
		player.getCharacteristics().getCharacteristic(CharacteristicName.WILL).setValue(6);
		player.getCharacteristics().getCharacteristic(CharacteristicName.FAITH).setValue(3);

		player.addSkill("Influenciar", 5);
		player.addSkill("Observar", 4);
		player.addSkill("Vigor", 6);
		player.addSkill("Armas de Fuego", 8);
		player.addSkill("Artería", 4);
		player.addSkill("Autocontrol", 2);
		player.addSkill("Callejear", 3);
		player.addSkill("Conocim. del Cuerpo", 1);
		player.addSkill("Controlar Aeronave", 2);
		player.addSkill("Cuerpo a Cuerpo", 5);
		player.addSkill("Demoliciones", 1);
		player.addSkill("Juego", 1);
		player.addSkill("Liderazgo", 1);
		player.addSkill("Recuperación Tecnológic.", 2);
		player.addSkill("Supervivencia", 2);
		player.addSkill("Torturar", 1);

		player.addBlessing(new Blessing("El Hombre", 2, 2, "Influenciar", "Liderar subalternos"));
		player.addBlessing(new Blessing("Posesivo", -2, -2, "Voluntad", "Excluir acción"));
		player.addBlessing(new Blessing("Marca Horrible", -2, -2, "Influencia", "Si es visible"));

		player.addBenefit(new Benefit("Genin", 8));
		player.addBenefit(new Benefit("Contrato de Pasaje", 3));
		player.addBenefit(new Benefit("1000 fénix", 4));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Carlos.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), 40);
	}
	
	@Test
	public void createNoeliaCharacer() throws MalformedURLException, DocumentException, IOException {
		CharacterPlayer player = new CharacterPlayer("es");
		player.getInfo().setName("");
		player.getInfo().setPlayer("Noelia");
		player.getInfo().setGender(Gender.FEMALE);
		//player.getInfo().setAge(30);
		player.setRace(new Race("Ur-Obun", 3, 4, 3, 3, 3, 3, 3, 3, 3, 6, 1, 0, 0, 0, 2));
		player.getInfo().setPlanet("Obun");
		player.getInfo().setAlliance("Voavenlohjun");
		player.getInfo().setRank("Novicio");

		player.getCharacteristics().getCharacteristic(CharacteristicName.STRENGTH).setValue(3);
		player.getCharacteristics().getCharacteristic(CharacteristicName.DEXTERITY).setValue(6);
		player.getCharacteristics().getCharacteristic(CharacteristicName.ENDURANCE).setValue(3);
		player.getCharacteristics().getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristics().getCharacteristic(CharacteristicName.PERCEPTION).setValue(4);
		player.getCharacteristics().getCharacteristic(CharacteristicName.TECH).setValue(3);
		player.getCharacteristics().getCharacteristic(CharacteristicName.PRESENCE).setValue(7);
		player.getCharacteristics().getCharacteristic(CharacteristicName.WILL).setValue(6);
		player.getCharacteristics().getCharacteristic(CharacteristicName.FAITH).setValue(8);

		player.addSkill("Influenciar", 4);
		player.addSkill("Observar", 6);
		
		player.addSkill("Artería", 2);
		player.addSkill("Autocontrol", 5);
		player.addSkill("Conocim. del Cuerpo", 3);
		player.addSkill("Empatía", 4);
		player.addSkill("Etiqueta", 2);
		player.addSkill("Investigar", 3);
		player.addSkill("Saber [Kelanti]", 0);

		player.getOccultism().setPsiValue(6);

		player.getOccultism().addElement(new OccultismPower("Mano Levitante", "Vol+Autoc.", 1, "Sensorial", "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Mano Lanzadora", "Vol+Autoc.", 2, "Sensorial", "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Mano Aplastante", "Vol+Autoc.", 3, "Sensorial", "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Mano Duelista", "Vol+Autoc.", 4, "Sensorial", "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Intuir", "Vol+Empatía", 1, "Toque", "Instantáneo", "", 1));
		player.getOccultism().addElement(new OccultismPower("Emocionar", "Presencia+Influenciar", 2, "Toque", "Instantáneo", "", 1));
		player.getOccultism().setExtraWyrd(3);
		

		player.addBlessing(new Blessing("Recto", 2, 2, "Fe", "Corregir al errado"));
		player.addBlessing(new Blessing("Condescendiente", -2, -2, "Presencia", "Entre incultos"));
		player.addBlessing(new Blessing("Marca Horrible", -2, -2, "Influencia", "Si es visible"));

		player.addBenefit(new Benefit("Idioma Terráqueo", 2));
		player.addBenefit(new Benefit("Idioma Obun", 0));
		player.addBenefit(new Benefit("Novicio", 4));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Noelia.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), 40);
	}

}
