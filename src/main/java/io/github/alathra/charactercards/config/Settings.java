package io.github.alathra.charactercards.config;

import io.github.alathra.charactercards.utility.Cfg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {

    public static int getMinimumAge() {
        return Cfg.get().getOrDefault("CardSettings.minimumAge", 14);
    }

    public static int getMaximumAge() {
        return Cfg.get().getOrDefault("CardSettings.maximumAge", 120);
    }

    public static int getDescriptionCharacterLimit() {
        return Cfg.get().getOrDefault("CardSettings.descriptionCharacterLimit", 512);
    }

    public static Map<String, String> getRawCustomFields() {
        Map<String, String> rawCustomFields = new HashMap<>();
        Map<?, ?> cardSettingsMap = Cfg.get().getMap("CardSettings");
        @SuppressWarnings("unchecked")
        List<Map<?, ?>> fieldsList = (List<Map<?, ?>>) cardSettingsMap.get("CustomFields");
        for (Map<?, ?> fieldEntry : fieldsList) {
            final String label = (String) fieldEntry.get("label");
            final String placeholder = (String) fieldEntry.get("placeholder");
            rawCustomFields.put(label, placeholder);
        }
        return rawCustomFields;
    }

    public static boolean doesShiftRightClickingDisplayCard() {
        return Cfg.get().getOrDefault("InteractSettings.shiftRightClick", false);
    }

    public static boolean doesRightClickingWithPaperDisplayCard() {
        return Cfg.get().getOrDefault("InteractSettings.rightClickPaper", false);
    }

}
