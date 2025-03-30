package io.github.alathra.charactercards;

/**
 * Implemented in classes that should support being reloaded IE executing the methods during runtime after startup.
 */
public interface Reloadable {
    /**
     * On plugin load.
     */
    void onLoad(CharacterCards plugin);

    /**
     * On plugin enable.
     */
    void onEnable(CharacterCards plugin);

    /**
     * On plugin disable.
     */
    void onDisable(CharacterCards plugin);
}
