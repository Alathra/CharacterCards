package io.github.alathra.charactercards.translation;

import io.github.alathra.charactercards.CharacterCards;
import io.github.alathra.charactercards.Reloadable;
import io.github.alathra.charactercards.config.ConfigHandler;
import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.wordweaver.Translation;
import io.github.milkdrinkers.wordweaver.config.TranslationConfig;

import java.nio.file.Path;

/**
 * A wrapper handler class for handling WordWeaver lifecycle.
 */
public class TranslationHandler implements Reloadable {
    private final ConfigHandler configHandler;

    public TranslationHandler(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    @Override
    public void onLoad(CharacterCards plugin) {

    }

    @Override
    public void onEnable(CharacterCards plugin) {
        Translation.initialize(TranslationConfig.builder() // Initialize word-weaver
            .translationDirectory(plugin.getDataPath().resolve("lang"))
            .resourcesDirectory(Path.of("lang"))
            .extractLanguages(true)
            .updateLanguages(true)
            .language(configHandler.getConfig().get("language", "en_US.json"))
            .defaultLanguage("en_US.json")
            .componentConverter(s -> ColorParser.of(s).parseLegacy().build()) // Use color parser for components by default
            .build()
        );
    }

    @Override
    public void onDisable(CharacterCards plugin) {
    }
}