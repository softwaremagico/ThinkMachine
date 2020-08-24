package com.softwaremagico.tm.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTrait;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.log.MachineLog;

public class SelectedCyberneticDeviceAdapter extends ElementAdapter<SelectedCyberneticDevice> {
    private static final String CUSTOMIZATIONS = "customizations";

    protected SelectedCyberneticDeviceAdapter(String language, String moduleName) {
        super(language, moduleName);
    }

    private List<CyberneticDeviceTrait> getCustomizations(JsonObject jsonObject) {
        if (jsonObject.has(CUSTOMIZATIONS)) {
            JsonArray customizations = (JsonArray) jsonObject.get(CUSTOMIZATIONS);
            if (customizations != null && !customizations.isJsonNull() && customizations.size() > 0) {
                Gson gsonDeserializer = new GsonBuilder()
                        .registerTypeAdapter(CyberneticDeviceTrait.class, new CyberneticDeviceTraitAdapter(getLanguage(), getModuleName())).create();
                return (List<CyberneticDeviceTrait>) gsonDeserializer.fromJson(jsonObject.get(CUSTOMIZATIONS), CyberneticDeviceTrait.class);
            }
        }
        return null;
    }

    public static List<CyberneticDeviceTrait> parseCustomizations(String name, JsonObject jsonObject, JsonDeserializationContext context) {
        if (jsonObject.has(CUSTOMIZATIONS)) {
            JsonArray customizations = (JsonArray) jsonObject.get(CUSTOMIZATIONS);
            if (customizations != null && !customizations.isJsonNull() && customizations.size() > 0) {
                Type cyberneticDeviceTraitType = new TypeToken<ArrayList<CyberneticDeviceTrait>>(){}.getType();
                return (List<CyberneticDeviceTrait>) context.deserialize(jsonObject.get(name), cyberneticDeviceTraitType);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public JsonElement serialize(SelectedCyberneticDevice selectedCyberneticDevice, Type elementType, JsonSerializationContext jsonSerializationContext) {
        final JsonElement jsonObject = super.serialize(selectedCyberneticDevice, elementType, jsonSerializationContext);
        ((JsonObject) jsonObject).add(CUSTOMIZATIONS, jsonSerializationContext.serialize(selectedCyberneticDevice.getCustomizations()));
        return jsonObject;
    }

    @Override
    public SelectedCyberneticDevice deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        try {
            final SelectedCyberneticDevice selectedCyberneticDevice = new SelectedCyberneticDevice(
                    CyberneticDeviceFactory.getInstance().getElement(super.getElementId(jsonElement), super.getLanguage(), super.getModuleName()));
            //selectedCyberneticDevice.setCustomizations(getCustomizations((JsonObject) jsonElement));
            selectedCyberneticDevice.setCustomizations(parseCustomizations(CUSTOMIZATIONS, (JsonObject) jsonElement, jsonDeserializationContext));
            return selectedCyberneticDevice;
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
            return null;
        }
    }
}
