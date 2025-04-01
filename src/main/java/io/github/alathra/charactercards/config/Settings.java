package io.github.alathra.charactercards.config;

import io.github.alathra.charactercards.utility.Cfg;

import java.util.HashMap;
import java.util.Map;

public class Settings {

    public static int getTitleMaxLength() {
        return Cfg.get().getOrDefault("CardSettings.titleMaxLength", 12);
    }

    public static int getFirstNameMaxLength() {
        return Cfg.get().getOrDefault("CardSettings.firstNameMaxLength", 12);
    }

    public static int getLastNameMaxLength() {
        return Cfg.get().getOrDefault("CardSettings.lastNameMaxLength", 12);
    }

    public static int getSuffixMaxLength() {
        return Cfg.get().getOrDefault("CardSettings.suffixMaxLength", 6);
    }

    public static int getGenderMaxLength() {
        return Cfg.get().getOrDefault("CardSettings.genderMaxLength", 18);
    }

    public static int getMinimumAge() {
        return Cfg.get().getOrDefault("CardSettings.minimumAge", 14);
    }

    public static int getMaximumAge() {
        return Cfg.get().getOrDefault("CardSettings.maximumAge", 120);
    }

    public static int getDescriptionCharacterLimit() {
        return Cfg.get().getOrDefault("CardSettings.descriptionCharacterLimit", 512);
    }

    public static boolean getShiftRightClickEnabled() {
        return Cfg.get().getOrDefault("InteractSettings.shiftRightClick", true);
    }

    public static boolean getRightClickPaperEnabled() {
        return Cfg.get().getOrDefault("InteractSettings.rightClickPaper", true);
    }

    public static Map<String, String> getRawCustomFields() {
        Map<String, String> rawCustomFields = new HashMap<>();

        Map<?, ?> cardSettingsMap = Cfg.get().getMap("CardSettings");
        if (cardSettingsMap == null) {
            return rawCustomFields; // Return empty map if CardSettings is missing
        }

        Map<?, Map<?, ?>> fieldsMap = (Map<?, Map<?, ?>>) cardSettingsMap.get("customFields");
        if (fieldsMap == null) {
            return rawCustomFields; // Return empty map if customFields is missing
        }

        for (Map.Entry<?, Map<?, ?>> entry : fieldsMap.entrySet()) {
            Map<?, ?> fieldEntry = entry.getValue();
            if (fieldEntry != null) {
                final String label = (String) fieldEntry.get("label");
                final String placeholder = (String) fieldEntry.get("placeholder");
                if (label != null && placeholder != null) {
                    rawCustomFields.put(label, placeholder);
                }
            }
        }
        return rawCustomFields;
    }
}
