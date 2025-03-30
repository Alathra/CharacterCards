package io.github.alathra.charactercards.utility;

import io.github.milkdrinkers.crate.Config;
import io.github.alathra.charactercards.CharacterCards;
import io.github.alathra.charactercards.config.ConfigHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Convenience class for accessing {@link ConfigHandler#getConfig}
 */
public abstract class Cfg {
    /**
     * Convenience method for {@link ConfigHandler#getConfig} to getConnection {@link Config}
     *
     * @return the config
     */
    @NotNull
    public static Config get() {
        return CharacterCards.getInstance().getConfigHandler().getConfig();
    }
}
